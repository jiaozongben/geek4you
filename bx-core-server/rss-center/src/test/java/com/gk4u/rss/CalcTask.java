package com.gk4u.rss;

import java.util.concurrent.Callable;

public class CalcTask implements Callable<String> {
    @Override
    public String call() throws Exception {
        return Thread.currentThread().getName();
    }
}
