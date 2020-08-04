//package com.gk4u.rss.backend.feed;
//
//
//import com.gk4u.rss.backend.CommaFeedConfiguration;
//import com.gk4u.rss.backend.entity.FeedEntry;
//import com.gk4u.rss.backend.entity.FeedSubscription;
//import com.gk4u.rss.backend.mapper.FeedEntryMapper;
//import com.gk4u.rss.backend.mapper.FeedSubscriptionMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//
//@Slf4j
//@Service
//public class FeedRefreshUpdater {
//
//
//    private FeedRefreshExecutor pool;
//
//    @Autowired
//    FeedSubscriptionMapper feedSubscriptionMapper;
//
//    @Autowired
//    FeedEntryMapper feedEntryMapper;
//
//    @Autowired
//    CommaFeedConfiguration config;
//
//
//    public FeedRefreshUpdater() {
//        int threads = Math.max(config.getDatabaseUpdateThreads(), 1);
//        pool = new FeedRefreshExecutor("feedSubscription-refresh-updater", threads, Math.min(50 * threads, 1000));
//    }
//
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
//
//            final FeedSubscription feedSubscription = context.getFeedSubscription();
//            List<FeedEntry> entries = context.getEntries();
//            if (entries.isEmpty()) {
//                System.out.println("feed is empty.");
//            } else {
//                List<String> currentEntries = new ArrayList<>();
//
//                List<FeedSubscription> subscriptions = null;
//                for (FeedEntry entry : entries) {
//
//                    //这里去数据库里面查询对应的url是否存在
//
//                    int count = feedEntryMapper.findEntryExists(entry);
//                    if (count > 0) {
//
//                    } else {
//                        feedEntryMapper.insert(entry);
//                    }
//
//
//                }
//
//            }
//
//        }
//
//        public boolean isUrgent() {
//            return context.isUrgent();
//        }
//    }
//
//
//}
