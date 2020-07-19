package com.gk4u.rss;

import com.gk4u.rss.backend.CommaFeedConfiguration;
import com.gk4u.rss.backend.HttpGetter;

import java.io.IOException;
import java.util.Date;

public class FeedFetcher {
    public static void main(String[] args) throws IOException, HttpGetter.NotModifiedException {


        String feedUrl = "https://jiaozongben.github.io/feed.xml";
        boolean extractFeedUrlFromHtml = false;
        String lastModified = null;
        String eTag = null;
        Date lastPublishedDate = null;
        String lastContentHash = null;
        CommaFeedConfiguration commaFeedConfiguration = new CommaFeedConfiguration();
        HttpGetter getter = new HttpGetter(commaFeedConfiguration);
        int timeout = 20000;
        HttpGetter.HttpResult result = getter.getBinary(feedUrl, lastModified, eTag, timeout);
        byte[] content = result.getContent();

        System.out.println(content);

    }
}
