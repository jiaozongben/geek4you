package com.gk4u.rss;

import com.gk4u.rss.backend.feed.FeedQueues;
import com.gk4u.rss.backend.feed.FeedRefreshTaskGiver;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.PostConstruct;

/**
 * rss数据中心
 *
 * @author liugh
 */

@SpringBootApplication
@MapperScan("com.gk4u.rss.backend.mapper")

public class RssApplication {

    public static void main(String[] args) {
        SpringApplication.run(RssApplication.class, args);
    }



}