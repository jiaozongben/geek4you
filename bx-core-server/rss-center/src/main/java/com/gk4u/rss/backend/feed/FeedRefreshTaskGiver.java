package com.gk4u.rss.backend.feed;


import lombok.extern.slf4j.Slf4j;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Infinite loop fetching feeds from @FeedQueues and queuing them to the {@link FeedRefreshWorker} pool.
 */
@Slf4j
public class FeedRefreshTaskGiver {

    private final FeedQueues queues;
    private final FeedRefreshWorker worker;

    private ExecutorService executor;


    public FeedRefreshTaskGiver(FeedQueues queues, FeedRefreshWorker worker) {
        this.queues = queues;
        this.worker = worker;

        executor = Executors.newFixedThreadPool(1);

    }


    public void stop() {
        log.info("shutting down feedSubscription refresh task giver");
        executor.shutdownNow();
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("interrupted while waiting for threads to finish.");
            }
        }
    }


    public void start() {
        log.info("starting feedSubscription refresh task giver");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (!executor.isShutdown()) {
                    try {
                        FeedRefreshContext context = queues.take();
                        if (context != null) {
                            worker.updateFeed(context);
                        } else {
                            log.debug("nothing to do, sleeping for 15s");
                            try {
                                Thread.sleep(15000);
                            } catch (InterruptedException e) {
                                log.debug("interrupted while sleeping");
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        });
    }
}
