package com.gk4u.rss;



import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadTest {

    /**
     * 单线程工作
     */
    @Test
    public void RunnableTest() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        });
        t1.start();
    }

    /**
     * 根据需要可以创建新线程的线程池。
     * 线程池中曾经创建的线程，在完成某个任务后也许会被用来完成另外一项任务。
     */
    @Test
    public void test() {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {//5个任务
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " doing task");
                }
            });
        }
        exec.shutdown();  //关闭线程池
    }

    /**
     * 根据需要可以创建新线程的线程池。
     * 线程池中曾经创建的线程，在完成某个任务后也许会被用来完成另外一项任务。
     */
    @Test
    public void testSingle() {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {//5个任务
            exec.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " doing task");
                }
            });
        }
        exec.shutdown();  //关闭线程池
    }


    @Test
    public void callableTest() {
        ExecutorService exec = Executors.newCachedThreadPool();
        List<Callable<String>> taskList = new ArrayList<Callable<String>>();
        /* 往任务列表中添加5个任务 */
        for (int i = 0; i < 5; i++) {
            taskList.add(new CalcTask());
        }
        /* 结果列表:存放任务完成返回的值 */
        List<Future<String>> resultList = new ArrayList<Future<String>>();
        try {
            /*invokeAll批量运行所有任务, submit提交单个任务*/
            resultList = exec.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            /*从future中输出每个任务的返回值*/
            for (Future<String> future : resultList) {
                System.out.println(future.get());//get方法会阻塞直到结果返回
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void futureTest() throws InterruptedException {
        /*新建一个Callable任务*/
        Callable<Integer> callableTask = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("Calculating 1+1!");
                TimeUnit.SECONDS.sleep(2);//休眠2秒
                return 2;
            }
        };
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Integer> result = executor.submit(callableTask);
        executor.shutdown();
        while (!result.isDone()) {//isDone()方法可以查询子线程是否做完
            System.out.println("子线程正在执行");
            TimeUnit.SECONDS.sleep(1);//休眠1秒
        }
        try {
            System.out.println("子线程执行结果:" + result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }







}
