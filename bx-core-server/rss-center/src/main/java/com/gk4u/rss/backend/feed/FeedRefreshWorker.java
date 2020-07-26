package com.gk4u.rss.backend.feed;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.gk4u.rss.CommaFeedConfiguration;
import com.gk4u.rss.backend.HttpGetter;

import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * Calls {@link FeedFetcher} and handles its outcome
 */
@Slf4j

public class FeedRefreshWorker {

    private final FeedFetcher fetcher;
    private final FeedQueues queues;
    private final CommaFeedConfiguration config;
    private FeedRefreshExecutor pool;


    public FeedRefreshWorker(FeedFetcher fetcher, FeedQueues queues, CommaFeedConfiguration config) {

        this.fetcher = fetcher;
        this.config = config;
        this.queues = queues;
//		int threads = config.getApplicationSettings().getBackgroundThreads();
//		pool = new FeedRefreshExecutor("feed-refresh-worker", threads, Math.min(20 * threads, 1000) );
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

        @Override
        public boolean isUrgent() {
            return context.isUrgent();
        }
    }

    private void update(FeedRefreshContext context) {
        Feed feed = context.getFeed();
        int refreshInterval = config.getRefreshIntervalMinutes();
        Date disabledUntil = DateUtils.addMinutes(new Date(), refreshInterval);
        try {
            String url = Optional.ofNullable(feed.getUrlAfterRedirect()).orElse(feed.getUrl());
            FetchedFeed fetchedFeed = fetcher.fetch(url, false, feed.getLastModifiedHeader(), feed.getEtagHeader(),
                    DateUtil.localDateTime2Date(feed.getLastPublishedDate()), feed.getLastContentHash());
            // stops here if NotModifiedException or any other exception is thrown
            List<FeedEntry> entries = fetchedFeed.getEntries();

            Integer maxFeedCapacity = config.getMaxFeedCapacity();
            if (maxFeedCapacity > 0) {
                entries = entries.stream().limit(maxFeedCapacity).collect(Collectors.toList());
            }

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

            handlePubSub(feed, fetchedFeed.getFeed());
            context.setEntries(entries);
//            feedRefreshUpdater.updateFeed(context);

        } catch (HttpGetter.NotModifiedException e) {
            log.debug("Feed not modified : {} - {}", feed.getUrl(), e.getMessage());

            if (config.getHeavyLoad()) {
                disabledUntil = FeedUtils.buildDisabledUntil(DateUtil.localDateTime2Date(feed.getLastEntryDate()), feed.getAverageEntryInterval(), disabledUntil);
            }
            feed.setErrorCount(0);
            feed.setMessage(e.getMessage());
            feed.setDisabledUntil(DateUtil.date2LocalDate(disabledUntil));

            queues.giveBack(feed);
        } catch (Exception e) {
            String message = "Unable to refresh feed " + feed.getUrl() + " : " + e.getMessage();
            log.debug(e.getClass().getName() + " " + message, e);

            feed.setErrorCount(feed.getErrorCount() + 1);
            feed.setMessage(message);
            feed.setDisabledUntil(DateUtil.date2LocalDate(FeedUtils.buildDisabledUntil(feed.getErrorCount())));

            queues.giveBack(feed);
        }
    }

    private void handlePubSub(Feed feed, Feed fetchedFeed) {
        String hub = fetchedFeed.getPushHub();
        String topic = fetchedFeed.getPushTopic();
        if (hub != null && topic != null) {
            if (hub.contains("hubbub.api.typepad.com")) {
                // that hub does not exist anymore
                return;
            }
            if (topic.startsWith("www.")) {
                topic = "http://" + topic;
            } else if (topic.startsWith("feed://")) {
                topic = "http://" + topic.substring(7);
            } else if (topic.startsWith("http") == false) {
                topic = "http://" + topic;
            }
            log.debug("feed {} has pubsub info: {}", feed.getUrl(), topic);
            feed.setPushHub(hub);
            feed.setPushTopic(topic);
            feed.setPushTopicHash(DigestUtils.sha1Hex(topic));
        }
    }
}
