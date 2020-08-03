//package com.gk4u.rss.backend.feed;
//
//
//import com.gk4u.rss.backend.CommaFeedConfiguration;
//import com.gk4u.rss.backend.entity.FeedEntry;
//import com.gk4u.rss.backend.entity.FeedSubscription;
//
//import com.gk4u.rss.backend.mapper.FeedSubscriptionMapper;
//import com.gk4u.rss.backend.util.DateUtil;
//import com.google.common.util.concurrent.Striped;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.time.DateUtils;
//
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.Lock;
//
//@Slf4j
//
//public class FeedRefreshUpdater {
//
//
//    private final FeedUpdateService feedUpdateService;
//
//    private final FeedQueues queues;
//    private final CommaFeedConfiguration config;
//    private final FeedSubscriptionMapper feedSubscriptionDAO;
//    private final CacheService cache;
//
//    private FeedRefreshExecutor pool;
//    private Striped<Lock> locks;
//
//
//    public FeedRefreshUpdater(FeedUpdateService feedUpdateService, PubSubService pubSubService,
//                              FeedQueues queues, CommaFeedConfiguration config, FeedSubscriptionMapper feedSubscriptionDAO,
//                              CacheService cache) {
//
//        this.feedUpdateService = feedUpdateService;
//        this.pubSubService = pubSubService;
//        this.queues = queues;
//        this.config = config;
//        this.feedSubscriptionDAO = feedSubscriptionDAO;
//        this.cache = cache;
//
//        int threads = Math.max(config.getDatabaseUpdateThreads(), 1);
//        pool = new FeedRefreshExecutor("feedSubscription-refresh-updater", threads, Math.min(50 * threads, 1000));
//        locks = Striped.lazyWeakLock(threads * 100000);
//
//
//    }
//
//
//    public void start() throws Exception {
//    }
//
//
//    public void stop() throws Exception {
//        log.info("shutting down feedSubscription refresh updater");
//        pool.shutdown();
//    }
//
//    public void updateFeed(FeedRefreshContext context) {
//        pool.execute(new EntryTask(context));
//    }
//
//    private class EntryTask implements FeedRefreshExecutor.Task {
//
//        private FeedRefreshContext context;
//
//        public EntryTask(FeedRefreshContext context) {
//            this.context = context;
//        }
//
//        @Override
//        public void run() {
//            boolean ok = true;
//            final FeedSubscription feedSubscription = context.getFeedSubscription();
//            List<FeedEntry> entries = context.getEntries();
//            if (entries.isEmpty()) {
//                System.out.println("feed is empty.");
//            } else {
//                List<String> lastEntries = cache.getLastEntries(feedSubscription);
//                List<String> currentEntries = new ArrayList<>();
//
//                List<FeedSubscription> subscriptions = null;
//                for (FeedEntry entry : entries) {
//                    String cacheKey = cache.buildUniqueEntryKey(feedSubscription, entry);
//                    if (!lastEntries.contains(cacheKey)) {
//                        log.debug("cache miss for {}", entry.getUrl());
//                        if (subscriptions == null) {
////                            subscriptions = feedSubscriptionDAO.findByFeed(feedSubscription);
//                            subscriptions = null;
//                        }
//                        ok &= addEntry(feedSubscription, entry, subscriptions);
//                    } else {
//                        log.debug("cache hit for {}", entry.getUrl());
//                    }
//
//                    currentEntries.add(cacheKey);
//                }
//                cache.setLastEntries(feedSubscription, currentEntries);
//
//            }
//
//            queues.giveBack(feedSubscription);
//        }
//
//        public boolean isUrgent() {
//            return context.isUrgent();
//        }
//    }
//
//    private boolean addEntry(final Feed feed, final FeedEntry entry, final List<FeedSubscription> subscriptions) {
//        boolean success = false;
//
//        // lock on feedSubscription, make sure we are not updating the same feedSubscription twice at
//        // the same time
//        String key1 = StringUtils.trimToEmpty("" + feed.getId());
//
//        // lock on content, make sure we are not updating the same entry
//        // twice at the same time
////        FeedEntryContent content = entry.getContent();
//        FeedEntryContent content = null;
//        String key2 = DigestUtils.sha1Hex(StringUtils.trimToEmpty(content.getContent() + content.getTitle()));
//
//        Iterator<Lock> iterator = locks.bulkGet(Arrays.asList(key1, key2)).iterator();
//        Lock lock1 = iterator.next();
//        Lock lock2 = iterator.next();
//        boolean locked1 = false;
//        boolean locked2 = false;
//        try {
//            locked1 = lock1.tryLock(1, TimeUnit.MINUTES);
//            locked2 = lock2.tryLock(1, TimeUnit.MINUTES);
//            if (locked1 && locked2) {
//                boolean inserted = feedUpdateService.addEntry(feed, entry, subscriptions);
//                if (inserted) {
//                 }
//                success = true;
//            } else {
//                log.error("lock timeout for " + feed.getUrl() + " - " + key1);
//            }
//        } catch (InterruptedException e) {
//            log.error("interrupted while waiting for lock for " + feed.getUrl() + " : " + e.getMessage(), e);
//        } finally {
//            if (locked1) {
//                lock1.unlock();
//            }
//            if (locked2) {
//                lock2.unlock();
//            }
//        }
//        return success;
//    }
//
//    private void handlePubSub(final Feed feed) {
//        if (feed.getPushHub() != null && feed.getPushTopic() != null) {
//            Date lastPing = DateUtil.localDateTime2Date(feed.getPushLastPing());
//            Date now = new Date();
//            if (lastPing == null || lastPing.before(DateUtils.addDays(now, -3))) {
//                new Thread() {
//                    @Override
//                    public void run() {
//                        pubSubService.subscribe(feed);
//                    }
//                }.start();
//            }
//        }
//    }
//
//}
