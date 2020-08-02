package com.gk4u.rss.backend.feed;


import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.gk4u.rss.backend.util.DateUtil;
import com.google.common.collect.Iterables;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;


import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FeedParser {

    private static final String ATOM_10_URI = "http://www.w3.org/2005/Atom";
    private static final Namespace ATOM_10_NS = Namespace.getNamespace(ATOM_10_URI);

    private static final Date START = new Date(86400000);
    private static final Date END = new Date(1000l * Integer.MAX_VALUE - 86400000);

    public FetchedFeed parse(String feed_id,String feedUrl, byte[] xml) throws FeedException {
        FetchedFeed fetchedFeed = new FetchedFeed();
        FeedSubscription feedSubscription = fetchedFeed.getFeedSubscription();
        List<FeedEntry> entries = fetchedFeed.getEntries();

        try {
            Charset encoding = FeedUtils.guessEncoding(xml);
            String xmlString = FeedUtils.trimInvalidXmlCharacters(new String(xml, encoding));
            if (xmlString == null) {
                throw new FeedException("Input string is null for url " + feedUrl);
            }
            xmlString = FeedUtils.replaceHtmlEntitiesWithNumericEntities(xmlString);
            InputSource source = new InputSource(new StringReader(xmlString));
            SyndFeed rss = new SyndFeedInput().build(source);
            handleForeignMarkup(rss);

            fetchedFeed.setTitle(rss.getTitle());
            feedSubscription.setUrl(feedUrl);
            List<SyndEntry> items = rss.getEntries();

            for (SyndEntry item : items) {
                FeedEntry entry = new FeedEntry();

                String guid = item.getUri();
                if (StringUtils.isBlank(guid)) {
                    guid = item.getLink();
                }
                if (StringUtils.isBlank(guid)) {
                    // no guid and no link, skip entry
                    continue;
                }

                entry.setUrl(FeedUtils.truncate(FeedUtils.toAbsoluteUrl(item.getLink(), feedSubscription.getUrl(), feedUrl), 2048));
                entry.setContent(getContent(item));
                entry.setTitle(getTitle(item));
                entry.setInserted(LocalDateTime.now());
                entry.setFeedId(Long.valueOf(feed_id));

                entries.add(entry);
            }
            feedSubscription.setFeedId(Long.valueOf(feed_id));
            feedSubscription.setUrl(feedUrl);

            fetchedFeed.setFeedSubscription(feedSubscription);
        } catch (Exception e) {
            throw new FeedException(String.format("Could not parse feedSubscription from %s : %s", feedUrl, e.getMessage()), e);
        }

        return fetchedFeed;
    }

    /**
     * Adds atom links for rss feeds
     */
    private void handleForeignMarkup(SyndFeed feed) {
        List<Element> foreignMarkup = feed.getForeignMarkup();
        if (foreignMarkup == null) {
            return;
        }
        for (Element element : foreignMarkup) {
            if ("link".equals(element.getName()) && ATOM_10_NS.equals(element.getNamespace())) {
                SyndLink link = new SyndLinkImpl();
                link.setRel(element.getAttributeValue("rel"));
                link.setHref(element.getAttributeValue("href"));
                feed.getLinks().add(link);
            }
        }
    }

    private Date getEntryUpdateDate(SyndEntry item) {
        Date date = item.getUpdatedDate();
        if (date == null) {
            date = item.getPublishedDate();
        }
        if (date == null) {
            date = new Date();
        }
        return date;
    }

    private Date validateDate(Date date, boolean nullToNow) {
        Date now = new Date();
        if (date == null) {
            return nullToNow ? now : null;
        }
        if (date.before(START) || date.after(END)) {
            return now;
        }

        if (date.after(now)) {
            return now;
        }
        return date;
    }

    private String getContent(SyndEntry item) {
        String content = null;
        if (item.getContents().isEmpty()) {
            content = item.getDescription() == null ? null : item.getDescription().getValue();
        } else {
            content = item.getContents().stream().map(c -> c.getValue()).collect(Collectors.joining(System.lineSeparator()));
        }
        return StringUtils.trimToNull(content);
    }

    private String getTitle(SyndEntry item) {
        String title = item.getTitle();
        if (StringUtils.isBlank(title)) {
            Date date = item.getPublishedDate();
            if (date != null) {
                title = DateFormat.getInstance().format(date);
            } else {
                title = "(no title)";
            }
        }
        return StringUtils.trimToNull(title);
    }

    private String findHub(SyndFeed feed) {
        for (SyndLink l : feed.getLinks()) {
            if ("hub".equalsIgnoreCase(l.getRel())) {
                log.debug("found hub {} for feedSubscription {}", l.getHref(), feed.getLink());
                return l.getHref();
            }
        }
        return null;
    }

    private String findSelf(SyndFeed feed) {
        for (SyndLink l : feed.getLinks()) {
            if ("self".equalsIgnoreCase(l.getRel())) {
                log.debug("found self {} for feedSubscription {}", l.getHref(), feed.getLink());
                return l.getHref();
            }
        }
        return null;
    }

}
