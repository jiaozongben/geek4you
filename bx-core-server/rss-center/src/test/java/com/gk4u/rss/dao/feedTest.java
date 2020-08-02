package com.gk4u.rss.dao;

import com.alibaba.fastjson.JSONObject;
import com.gk4u.rss.backend.CommaFeedConfiguration;
import com.gk4u.rss.backend.HttpGetter;

import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.gk4u.rss.backend.feed.FeedFetcher;
import com.gk4u.rss.backend.feed.FeedUtils;
import com.gk4u.rss.backend.feed.FetchedFeed;

import com.gk4u.rss.backend.mapper.FeedEntryMapper;
import com.gk4u.rss.backend.mapper.FeedSubscriptionMapper;
import com.gk4u.rss.backend.service.IFeedEntryService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class feedTest {
    @Autowired
    FeedSubscriptionMapper feedSubscriptionMapper;

    @Autowired
    IFeedEntryService feedEntryService;

    @Autowired
    FeedFetcher fetcher;
    @Autowired
    CommaFeedConfiguration config;
//
//    @Test
//    public void insertTest() throws HttpGetter.NotModifiedException, IOException, FeedException {
//        Feed feedSubscription = new Feed();
//        feedSubscription.setUrl("https://jiaozongben.github.io/feed.xml");
//
//
//        feedMapper.insert(feedSubscription);
//
//    }


    @Test
    public void testFetcher() throws HttpGetter.NotModifiedException, IOException, FeedException {
        FeedSubscription feedSubscription = feedSubscriptionMapper.selectById("1");

        // --------------------
        int refreshInterval = config.getRefreshIntervalMinutes();
        Date disabledUntil = DateUtils.addMinutes(new Date(), refreshInterval);

        String url = feedSubscription.getUrl();
        FetchedFeed fetchedFeed = fetcher.fetch(String.valueOf(feedSubscription.getFeedId()),url, false, null, null,
                null, null);
        // stops here if NotModifiedException or any other exception is thrown
        List<FeedEntry> entries = fetchedFeed.getEntries();

        Integer maxFeedCapacity = config.getMaxFeedCapacity();
        if (maxFeedCapacity > 0) {
            entries = entries.stream().limit(maxFeedCapacity).collect(Collectors.toList());
        }

        System.out.println("resp: " + JSONObject.toJSONString(fetchedFeed));
        feedEntryService.saveBatch(entries);
    }
}
