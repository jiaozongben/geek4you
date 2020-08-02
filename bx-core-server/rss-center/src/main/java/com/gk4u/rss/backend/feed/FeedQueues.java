package com.gk4u.rss.backend.feed;


import com.gk4u.rss.backend.CommaFeedConfiguration;

import com.gk4u.rss.backend.entity.FeedSubscription;

import com.gk4u.rss.backend.mapper.FeedSubscriptionMapper;
import com.gk4u.rss.backend.service.impl.FeedSubscriptionServiceImpl;
import com.gk4u.rss.backend.util.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;


public class FeedQueues {

    @Autowired
    private FeedSubscriptionMapper feedSubscriptionMapper;
    @Autowired
    private FeedSubscriptionServiceImpl feedService;
    @Autowired
    private final CommaFeedConfiguration config;

    private Queue<FeedRefreshContext> addQueue = new ConcurrentLinkedQueue<>();
    private Queue<FeedRefreshContext> takeQueue = new ConcurrentLinkedQueue<>();
    private Queue<FeedRefreshContext> giveBackQueue = new ConcurrentLinkedQueue<>();


    public FeedQueues(FeedSubscriptionMapper feedSubscriptionMapper, CommaFeedConfiguration config) {
        this.config = config;
        this.feedSubscriptionMapper = feedSubscriptionMapper;
    }

    /**
     * take a feedSubscription from the refresh queue
     */
    public synchronized FeedRefreshContext take() {
        FeedRefreshContext context = takeQueue.poll();

        if (context == null) {
            refill();
            context = takeQueue.poll();
        }
        return context;
    }

    /**
     * add a feedSubscription to the refresh queue
     */
    public void add(FeedSubscription feedSubscription, boolean urgent) {
        int refreshInterval = config.getRefreshIntervalMinutes();
        boolean alreadyQueued = addQueue.stream().anyMatch(c -> c.getFeedSubscription().getId().equals(feedSubscription.getId()));
        if (!alreadyQueued) {
            addQueue.add(new FeedRefreshContext(feedSubscription, urgent));
        }

    }

    /**
     * refills the refresh queue and empties the giveBack queue while at it
     */
    private void refill() {

        List<FeedRefreshContext> contexts = new ArrayList<>();
        int batchSize = Math.min(100, 3 * config.getBackgroundThreads());

        // add feeds we got from the add() method
        int addQueueSize = addQueue.size();
        for (int i = 0; i < Math.min(batchSize, addQueueSize); i++) {
            contexts.add(addQueue.poll());
        }
//
//        // add feeds that are up to refresh from the database
//        int count = batchSize - contexts.size();
//        if (count > 0) {
//            List<Feed> feeds = feedService.findNextUpdatable(count, getLastLoginThreshold());
//            for (Feed feed : feeds) {
//                contexts.add(new FeedRefreshContext(feed, false));
//            }
//        }
//
//        // set the disabledDate as we use it in feedSubscriptionMapper to decide what to refresh next. We also use a map to remove
//        // duplicates.
//        Map<Integer, FeedRefreshContext> map = new LinkedHashMap<>();
//        for (FeedRefreshContext context : contexts) {
//            Feed feed = context.getFeedSubscription();
//            feed.setDisabledUntil(DateUtil.date2LocalDate(DateUtils.addMinutes(new Date(), config.getRefreshIntervalMinutes())));
//            map.put(feed.getId(), context);
//        }
//
//        // refill the queue
//        takeQueue.addAll(map.values());
//
//        // add feeds from the giveBack queue to the map, overriding duplicates
//        int giveBackQueueSize = giveBackQueue.size();
//        for (int i = 0; i < giveBackQueueSize; i++) {
//            Feed feed = giveBackQueue.poll();
//            map.put(feed.getId(), new FeedRefreshContext(feed, false));
//        }
//
//        // update all feeds in the database
//        List<Feed> feeds = map.values().stream().map(c -> c.getFeedSubscription()).collect(Collectors.toList());
//        feedService.saveBatch(feeds);
    }

    /**
     * give a feedSubscription back, updating it to the database during the next refill()
     */
    public void giveBack(FeedSubscription feedSubscription) {
//        String normalized = FeedUtils.normalizeURL(feedSubscription.getUrl());
//        giveBackQueue.add(feedSubscription);
    }

    private Date getLastLoginThreshold() {
        if (config.getHeavyLoad()) {
            return DateUtils.addDays(new Date(), -30);
        } else {
            return null;
        }
    }

}
