package com.gk4u.rss.backend.feed;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Wraps a {@link ThreadPoolExecutor} instance. Blocks when queue is full instead of rejecting the task. Allow priority queueing by using
 * {@link Task} instead of {@link Runnable}
 */
@Slf4j
public class FeedRefreshExecutor {

    private String poolName;
    private ThreadPoolExecutor pool;
    private LinkedBlockingDeque<Runnable> queue;

    public FeedRefreshExecutor(final String poolName, int threads, int queueCapacity) {
        log.info("Creating pool {} with {} threads", poolName, threads);
        this.poolName = poolName;
        pool = new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS, queue = new LinkedBlockingDeque<Runnable>(queueCapacity) {
            private static final long serialVersionUID = 1L;
        })
        {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                if (t != null) {
                    log.error("thread from pool {} threw a runtime exception", poolName, t);
                }
            }
        };

    }

    public void execute(Task task) {
        pool.execute(task);
    }

    public static interface Task extends Runnable {
        boolean isUrgent();
    }

    public void shutdown() {
        pool.shutdownNow();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("{} interrupted while waiting for threads to finish.", poolName);
            }
        }
    }
}
