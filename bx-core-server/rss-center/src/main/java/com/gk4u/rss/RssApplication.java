package com.gk4u.rss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * rss数据中心
 * 
 * @author liugh
 *
 */

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RssApplication {

	public static void main(String[] args) {
		SpringApplication.run(RssApplication.class, args);
	}

}