//package com.gk4u.rss.backend.feed;
//
//
//import com.gk4u.rss.backend.CommaFeedConfiguration;
//
//import com.gk4u.rss.backendbak.entity.Feed;
//import com.gk4u.rss.backendbak.mapper.FeedMapper;
//
//
//import com.gk4u.rss.backendbak.service.impl.FeedServiceImpl;
//import com.gk4u.rss.backend.util.DateUtil;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.lang3.time.DateUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//
//
//import java.util.*;
//import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.stream.Collectors;
//
//
//public class FeedQueues {
//
//    @Autowired
//    private FeedMapper feedDAO;
//    @Autowired
//    private FeedServiceImpl feedService;
//    @Autowired
//    private final CommaFeedConfiguration config;
//
//    private Queue<FeedRefreshContext> addQueue = new ConcurrentLinkedQueue<>();
//    private Queue<FeedRefreshContext> takeQueue = new ConcurrentLinkedQueue<>();
//    private Queue<Feed> giveBackQueue = new ConcurrentLinkedQueue<>();
//
//
//    public FeedQueues(FeedMapper feedDAO, CommaFeedConfiguration config) {
//        this.config = config;
//        this.feedDAO = feedDAO;
//
//
//    }
//
//    /**
//     * take a feed from the refresh queue
//     */
//    public synchronized FeedRefreshContext take() {
//        FeedRefreshContext context = takeQueue.poll();
//
//        if (context == null) {
//            refill();
//            context = takeQueue.poll();
//        }
//        return context;
//    }
//
//    /**
//     * add a feed to the refresh queue
//     */
//    public void add(Feed feed, boolean urgent) {
//        int refreshInterval = config.getRefreshIntervalMinutes();
//        if (feed.getLastUpdated() == null || DateUtil.localDateTime2Date(feed.getLastUpdated()).before(DateUtils.addMinutes(new Date(), -1 * refreshInterval))) {
//            boolean alreadyQueued = addQueue.stream().anyMatch(c -> c.getFeed().getId().equals(feed.getId()));
//            if (!alreadyQueued) {
//                addQueue.add(new FeedRefreshContext(feed, urgent));
//            }
//        }
//    }
//
//    /**
//     * refills the refresh queue and empties the giveBack queue while at it
//     */
//    private void refill() {
//
//        List<FeedRefreshContext> contexts = new ArrayList<>();
//        int batchSize = Math.min(100, 3 * config.getBackgroundThreads());
//
//        // add feeds we got from the add() method
//        int addQueueSize = addQueue.size();
//        for (int i = 0; i < Math.min(batchSize, addQueueSize); i++) {
//            contexts.add(addQueue.poll());
//        }
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
//        // set the disabledDate as we use it in feedDAO to decide what to refresh next. We also use a map to remove
//        // duplicates.
//        Map<Integer, FeedRefreshContext> map = new LinkedHashMap<>();
//        for (FeedRefreshContext context : contexts) {
//            Feed feed = context.getFeed();
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
//        List<Feed> feeds = map.values().stream().map(c -> c.getFeed()).collect(Collectors.toList());
//        feedService.saveBatch(feeds);
//    }
//
//    /**
//     * give a feed back, updating it to the database during the next refill()
//     */
//    public void giveBack(Feed feed) {
//        String normalized = FeedUtils.normalizeURL(feed.getUrl());
//        feed.setNormalizedUrl(normalized);
//        feed.setNormalizedUrlHash(DigestUtils.sha1Hex(normalized));
//        feed.setLastUpdated(DateUtil.date2LocalDate(new Date(0)));
//        giveBackQueue.add(feed);
//    }
//
//    private Date getLastLoginThreshold() {
//        if (config.getHeavyLoad()) {
//            return DateUtils.addDays(new Date(), -30);
//        } else {
//            return null;
//        }
//    }
//
//}
