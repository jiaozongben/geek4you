package com.gk4u.rss.backend.feed;


import com.gk4u.rss.backend.HttpGetter;

import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.urlprovider.FeedURLProvider;
import com.gk4u.rss.backend.util.DateUtil;
import com.rometools.rome.io.FeedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

@Slf4j
@Component
public class FeedFetcher {

    @Autowired
    private FeedParser parser;
    @Autowired
    private HttpGetter getter;
    private Set<FeedURLProvider> urlProviders;

    public FetchedFeed fetch(String feedUrl, boolean extractFeedUrlFromHtml, String lastModified, String eTag, Date lastPublishedDate,
                             String lastContentHash) throws FeedException, IOException, HttpGetter.NotModifiedException {
        log.debug("Fetching feed {}", feedUrl);
        FetchedFeed fetchedFeed = null;

        int timeout = 20000;

        HttpGetter.HttpResult result = getter.getBinary(feedUrl, lastModified, eTag, timeout);
        byte[] content = result.getContent();
        String str = new String(content, StandardCharsets.UTF_8);
        System.out.println("resp bytes:" + str);

//        log.debug("http.getter返回报文:" + str);
        try {
            fetchedFeed = parser.parse(result.getUrlAfterRedirect(), content);
        } catch (FeedException e) {
            if (extractFeedUrlFromHtml) {
                String extractedUrl = extractFeedUrl(urlProviders, feedUrl, StringUtils.newStringUtf8(result.getContent()));
                if (org.apache.commons.lang3.StringUtils.isNotBlank(extractedUrl)) {
                    feedUrl = extractedUrl;

                    result = getter.getBinary(extractedUrl, lastModified, eTag, timeout);
                    content = result.getContent();
                    fetchedFeed = parser.parse(result.getUrlAfterRedirect(), content);
                } else {
                    throw e;
                }
            } else {
                throw e;
            }
        }

        if (content == null) {
            throw new IOException("Feed content is empty.");
        }

        String hash = DigestUtils.sha1Hex(content);
        if (lastContentHash != null && hash != null && lastContentHash.equals(hash)) {
            log.debug("content hash not modified: {}", feedUrl);
            throw new HttpGetter.NotModifiedException("content hash not modified");
        }

        if (lastPublishedDate != null && fetchedFeed.getFeed().getLastPublishedDate() != null
                && lastPublishedDate.getTime() == DateUtil.localDateTime2Date(fetchedFeed.getFeed().getLastPublishedDate()).getTime()) {
            log.debug("publishedDate not modified: {}", feedUrl);
            throw new HttpGetter.NotModifiedException("publishedDate not modified");
        }

        Feed feed = fetchedFeed.getFeed();
        feed.setLastModifiedHeader(result.getLastModifiedSince());
        feed.setEtagHeader(FeedUtils.truncate(result.getETag(), 255));
        feed.setLastContentHash(hash);
        fetchedFeed.setFetchDuration(result.getDuration());
        fetchedFeed.setUrlAfterRedirect(result.getUrlAfterRedirect());
        return fetchedFeed;
    }

    private static String extractFeedUrl(Set<FeedURLProvider> urlProviders, String url, String urlContent) {
        for (FeedURLProvider urlProvider : urlProviders) {
            String feedUrl = urlProvider.get(url, urlContent);
            if (feedUrl != null)
                return feedUrl;
        }

        return null;
    }
}
