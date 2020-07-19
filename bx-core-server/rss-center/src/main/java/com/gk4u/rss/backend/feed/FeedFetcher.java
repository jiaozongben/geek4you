package com.gk4u.rss.backend.feed;

import com.gk4u.rss.backend.CommaFeedConfiguration;
import com.gk4u.rss.backend.HttpGetter;
import com.gk4u.rss.backend.model.Feed;
import com.gk4u.rss.backend.urlprovider.FeedURLProvider;
import com.gk4u.rss.frontend.model.FeedInfo;
import com.rometools.rome.io.FeedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

@Slf4j
public class FeedFetcher {


    private static FeedParser parser;
    private static HttpGetter getter;
    private static Set<FeedURLProvider> urlProviders;

    public static FetchedFeed fetch(String feedUrl, boolean extractFeedUrlFromHtml, String lastModified, String eTag, Date lastPublishedDate,
                                    String lastContentHash) throws FeedException, IOException, HttpGetter.NotModifiedException {
        log.debug("Fetching feed {}", feedUrl);
        FetchedFeed fetchedFeed = null;
        CommaFeedConfiguration commaFeedConfiguration = new CommaFeedConfiguration();
        getter = new HttpGetter(commaFeedConfiguration);
        parser = new  FeedParser();

        int timeout = 20000;

        HttpGetter.HttpResult result = getter.getBinary(feedUrl, lastModified, eTag, timeout);
        byte[] content = result.getContent();

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
                && lastPublishedDate.getTime() == fetchedFeed.getFeed().getLastPublishedDate().getTime()) {
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

    public static void main(String[] args) throws IOException, HttpGetter.NotModifiedException, FeedException {


        String url = "https://jiaozongben.github.io/feed.xml";
        FeedInfo info = null;
        url = org.apache.commons.lang3.StringUtils.trimToEmpty(url);
//        url = prependHttp(url);
        try {
            FetchedFeed feed =  fetch(url, true, null, null, null, null);
            info = new FeedInfo();
            info.setUrl(feed.getUrlAfterRedirect());
            info.setTitle(feed.getTitle());

        } catch (Exception e) {
            log.debug(e.getMessage(), e);
            throw new WebApplicationException(e, Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
        }

        System.out.println(info);
     }
    private String prependHttp(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }
}
