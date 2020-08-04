package com.gk4u.rss.backend.feed;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gk4u.rss.backend.CommaFeedConfiguration;
import com.gk4u.rss.backend.HttpGetter;

import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.entity.FeedSubscription;

import com.gk4u.rss.backend.mapper.FeedEntryMapper;
import com.gk4u.rss.backend.service.impl.FeedEntryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Calls {@link FeedFetcher} and handles its outcome
 */
@Slf4j
@Service
public class FeedRefreshWorker {

    @Autowired
    private FeedFetcher fetcher;

    private final FeedQueues queues;

    @Autowired
    private final CommaFeedConfiguration config;
    @Autowired
    FeedEntryMapper feedEntryMapper;
    private FeedRefreshExecutor pool;


    public FeedRefreshWorker(FeedFetcher fetcher, FeedQueues queues, CommaFeedConfiguration config) {

        this.fetcher = fetcher;
        this.config = config;
        this.queues = queues;
        int threads = config.getBackgroundThreads();
        pool = new FeedRefreshExecutor("feedSubscription-refresh-worker", threads, Math.min(20 * threads, 1000));
    }


    public void start() throws Exception {
    }


    public void stop() throws Exception {
        pool.shutdown();
    }

    public void updateFeed(FeedRefreshContext context) {
        pool.execute(new FeedTask(context));
    }

    private class FeedTask implements FeedRefreshExecutor.Task {

        private FeedRefreshContext context;

        public FeedTask(FeedRefreshContext context) {
            this.context = context;
        }

        @Override
        public void run() {
            update(context);
        }


        public boolean isUrgent() {
            return context.isUrgent();
        }
    }

    private void update(FeedRefreshContext context) {
        FeedSubscription feedSubscription = context.getFeedSubscription();
        int refreshInterval = config.getRefreshIntervalMinutes();
        Date disabledUntil = DateUtils.addMinutes(new Date(), refreshInterval);
        try {
            String url = context.getFeedSubscription().getUrl();
            //查询单个feed消息
            FetchedFeed fetchedFeed = fetcher.fetch(String.valueOf(feedSubscription.getFeedId()), url, false, null, null,
                    null, null);
            // stops here if NotModifiedException or any other exception is thrown
            List<FeedEntry> entries = fetchedFeed.getEntries();

            context.setEntries(entries);
            //这里如果数据库或者缓存里面数据不存在就入库


            for (FeedEntry entry : entries) {

                //这里去数据库里面查询对应的url是否存在
                int count = feedEntryMapper.findEntryExists(entry);
                if (count > 0) {
                    log.info("feed url :{} 已经存在" , entry.getUrl());
                } else {
                    feedEntryMapper.insert(entry);
                }
            }

        } catch (HttpGetter.NotModifiedException e) {
            log.debug("Feed not modified : {} - {}", feedSubscription.getUrl(), e.getMessage());

//            if (config.getHeavyLoad()) {
//                disabledUntil = FeedUtils.buildDisabledUntil(DateUtil.localDateTime2Date(feedSubscription.getLastEntryDate()), feedSubscription.getAverageEntryInterval(), disabledUntil);
//            }
//            feedSubscription.setErrorCount(0);
//            feedSubscription.setMessage(e.getMessage());
//            feedSubscription.setDisabledUntil(DateUtil.date2LocalDate(disabledUntil));

            queues.giveBack(feedSubscription);
        } catch (Exception e) {
            String message = "Unable to refresh feedSubscription " + feedSubscription.getUrl() + " : " + e.getMessage();
            log.debug(e.getClass().getName() + " " + message, e);

//            feedSubscription.setErrorCount(feedSubscription.getErrorCount() + 1);
//            feedSubscription.setMessage(message);
//            feedSubscription.setDisabledUntil(DateUtil.date2LocalDate(FeedUtils.buildDisabledUntil(feedSubscription.getErrorCount())));

            queues.giveBack(feedSubscription);
        }
    }


}
