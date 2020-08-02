package com.gk4u.rss.dao;

import com.alibaba.fastjson.JSONObject;
import com.gk4u.rss.CommaFeedConfiguration;
import com.gk4u.rss.backend.HttpGetter;
import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.feed.FeedFetcher;
import com.gk4u.rss.backend.feed.FeedUtils;
import com.gk4u.rss.backend.feed.FetchedFeed;
import com.gk4u.rss.backend.mapper.FeedMapper;

import com.gk4u.rss.backend.service.IFeedService;
import com.gk4u.rss.backend.util.DateUtil;
import com.rometools.rome.io.FeedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class feedTest {
    @Autowired
    FeedMapper feedMapper;
    @Autowired
    IFeedService feedService;

    @Autowired
    FeedFetcher fetcher;
    @Autowired
    CommaFeedConfiguration config;

    @Test
    public void insertTest() throws HttpGetter.NotModifiedException, IOException, FeedException {
        Feed feed = new Feed();
        feed.setUrl("https://jiaozongben.github.io/feed.xml");


        feedMapper.insert(feed);

    }


    @Test
    public void testFetcher() throws HttpGetter.NotModifiedException, IOException, FeedException {
        Feed feed = feedMapper.selectById("1");

        // --------------------
        int refreshInterval = config.getRefreshIntervalMinutes();
        Date disabledUntil = DateUtils.addMinutes(new Date(), refreshInterval);

        String url = Optional.ofNullable(feed.getUrlAfterRedirect()).orElse(feed.getUrl());
        FetchedFeed fetchedFeed = fetcher.fetch(url, false, null, null,
                null, null);
//        FetchedFeed fetchedFeed = fetcher.fetch(url, false, feed.getLastModifiedHeader(), feed.getEtagHeader(),
//                DateUtil.localDateTime2Date(feed.getLastPublishedDate()), feed.getLastContentHash());
        // stops here if NotModifiedException or any other exception is thrown
        List<FeedEntry> entries = fetchedFeed.getEntries();

        Integer maxFeedCapacity = config.getMaxFeedCapacity();
        if (maxFeedCapacity > 0) {
            entries = entries.stream().limit(maxFeedCapacity).collect(Collectors.toList());
        }
        System.out.println("resp: " + JSONObject.toJSONString(fetchedFeed));

        if (config.getHeavyLoad()) {

            disabledUntil = FeedUtils.buildDisabledUntil(DateUtil.localDateTime2Date(fetchedFeed.getFeed().getLastEntryDate()), fetchedFeed.getFeed()
                    .getAverageEntryInterval(), disabledUntil);
        }
        String urlAfterRedirect = fetchedFeed.getUrlAfterRedirect();
        if (StringUtils.equals(url, urlAfterRedirect)) {
            urlAfterRedirect = null;
        }
        feed.setUrlAfterRedirect(urlAfterRedirect);
        feed.setLink(fetchedFeed.getFeed().getLink());
        feed.setLastModifiedHeader(fetchedFeed.getFeed().getLastModifiedHeader());
        feed.setEtagHeader(fetchedFeed.getFeed().getEtagHeader());
        feed.setLastContentHash(fetchedFeed.getFeed().getLastContentHash());
        feed.setLastPublishedDate(fetchedFeed.getFeed().getLastPublishedDate());
        feed.setAverageEntryInterval(fetchedFeed.getFeed().getAverageEntryInterval());
        feed.setLastEntryDate(fetchedFeed.getFeed().getLastEntryDate());

        feed.setErrorCount(0);
        feed.setMessage(null);
        feed.setDisabledUntil(DateUtil.date2LocalDate(disabledUntil));

        System.out.println(JSONObject.toJSONString(feed));
    }
}
