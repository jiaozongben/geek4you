package com.gk4u.rss;

import com.alibaba.fastjson.JSONObject;
import com.gk4u.rss.backend.HttpGetter;


import com.gk4u.rss.backend.feed.FeedFetcher;
import com.gk4u.rss.backend.feed.FetchedFeed;
import com.gk4u.rss.backend.mapper.FeedMapper;
import com.gk4u.rss.backend.model.Feed;
import com.rometools.rome.io.FeedException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Autowired
    private FeedMapper feedsMapper;
    @Autowired
    FeedFetcher feedFetcher;

    //从数据库中查出url并进行遍历查询
    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<Feed> feedsList = feedsMapper.selectList(null);
        feedsList.forEach(System.out::println);

        feedsList.forEach(feeds -> {
            try {
                System.out.println("feeds.getUrl():" + feeds.getUrl() );
                FetchedFeed  fetchedFeed = feedFetcher.fetch(feeds.getUrl(), true, null, null, null, null);
                System.out.println(JSONObject.toJSONString(fetchedFeed));
            } catch (FeedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (HttpGetter.NotModifiedException e) {
                e.printStackTrace();
            }
        });

    }

//    @Test
//    public void testInsert() {
//        User user = new User();
//        user.setAge(123);
//        user.setName("cy");
//        user.setEmail("1131158493@qq.com");
//        feedsMapper.insert(user);
//    }

    //    @Test
//    public void testUpdate() {
//        User user = new User();
//        user.setId(5L);
//        user.setName("cyoking");
//        user.setAge(24);
//        int i = feedsMapper.updateById(user);
//        System.out.println(i);
//    }
    @Test
    public void delete() {
        feedsMapper.deleteById(1);    //删除一个
        feedsMapper.deleteBatchIds(Arrays.asList(2, 3)); // 接受一个list，删除list内所有id的记录
    }

}