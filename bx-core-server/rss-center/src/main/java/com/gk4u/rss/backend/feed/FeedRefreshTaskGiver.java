package com.gk4u.rss.backend.feed;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Infinite loop fetching feeds from @FeedQueues and queuing them to the {@link FeedRefreshWorker} pool.
 */
@Slf4j
@Service
public class FeedRefreshTaskGiver {

    @Autowired
    private FeedQueues queues;
    @Autowired
    private FeedRefreshWorker worker;

    private ExecutorService executor;

    @PostConstruct
    public void helloWorld() {
        System.out.println("helloWorld");

        start();
    }

    public FeedRefreshTaskGiver() {
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
