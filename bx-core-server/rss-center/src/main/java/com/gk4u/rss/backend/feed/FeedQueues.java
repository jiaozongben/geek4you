package com.gk4u.rss.backend.feed;


import com.gk4u.rss.backend.CommaFeedConfiguration;

import com.gk4u.rss.backend.entity.FeedSubscription;

import com.gk4u.rss.backend.mapper.FeedSubscriptionMapper;
import com.gk4u.rss.backend.service.impl.FeedSubscriptionServiceImpl;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;


@Component
public class FeedQueues {

    @Autowired
    private FeedSubscriptionServiceImpl feedSubscriptionService;
    @Autowired
    private FeedSubscriptionMapper feedSubscriptionMapper;
    @Autowired
    private CommaFeedConfiguration config;

    private Queue<FeedRefreshContext> addQueue = new ConcurrentLinkedQueue<>();
    private Queue<FeedRefreshContext> takeQueue = new ConcurrentLinkedQueue<>();
    private Queue<FeedRefreshContext> giveBackQueue = new ConcurrentLinkedQueue<>();


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


        List<FeedSubscription> subscriptions = feedSubscriptionMapper.findAll();
        for (FeedSubscription feedSubscription : subscriptions) {
            contexts.add(new FeedRefreshContext(feedSubscription, false));
        }


        // set the disabledDate as we use it in feedSubscriptionMapper to decide what to refresh next. We also use a map to remove
        // duplicates.
        Map<Integer, FeedRefreshContext> map = new LinkedHashMap<>();
        for (FeedRefreshContext context : contexts) {
            FeedSubscription feedSubscription = context.getFeedSubscription();
//            feedSubscription.setDisabledUntil(DateUtil.date2LocalDate(DateUtils.addMinutes(new Date(), config.getRefreshIntervalMinutes())));
            map.put(feedSubscription.getId(), context);
        }

        // refill the queue
        takeQueue.addAll(map.values());

        // add feeds from the giveBack queue to the map, overriding duplicates
//        int giveBackQueueSize = giveBackQueue.size();
//        for (int i = 0; i < giveBackQueueSize; i++) {
//            FeedSubscription feedSubscription = giveBackQueue.poll();
//            map.put(feedSubscription.getId(), new FeedRefreshContext(feedSubscription, false));
//        }

        // update all feeds in the database
        List<FeedSubscription> feedSubscriptions = map.values().stream().map(c -> c.getFeedSubscription()).collect(Collectors.toList());
//        feedSubscriptionService.saveBatch(feedSubscriptions);
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
