package com.gk4u.rss.backend.controller;


//import com.gk4u.rss.backendbak.entity.FeedEntry;
//import com.gk4u.rss.backendbak.entity.FeedSubscription;
//import com.gk4u.rss.backendbak.entity.User;
//import com.gk4u.rss.backendbak.entity.UserSetting;
//import com.gk4u.rss.backendbak.mapper.FeedEntryStatusMapper;
//import com.gk4u.rss.backendbak.mapper.FeedMapper;
//import com.gk4u.rss.backendbak.mapper.FeedSubscriptionMapper;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author James Bond
 * @since 2020-08-02
 */
@RestController
@RequestMapping("/feed-subscription")
public class FeedSubscriptionController {
//    @Autowired
//    FeedMapper feedMapper;
//
//    @Autowired
//    FeedSubscriptionMapper feedSubscriptionMapper;
//
//    @Autowired
//    FeedEntryStatusMapper feedEntryStatusMapper;
//
//    @RequestMapping(value = "/entries", method = RequestMethod.GET, name = "Get feed entries")
//    public List<FeedEntry> findByUserName(
//            @RequestParam(value = "user", name = "userid", required = true) int userid,
//            @RequestParam(value = "id", name = "feedid", required = true) Long feed_id,
//            @RequestParam(value = "readType", name = "all,unread", required = true) UserSetting.ReadingMode readType,
//            @RequestParam(value = "keywords", name = "search for keywords in either the title or the content of the entries, separated by spaces, 3 characters minimum", required = true) String keywords) {
//        System.out.println("开始查询...");
//        User user = new User();
//        user.setId(userid);
//
//
//        FeedSubscription subscription = feedSubscriptionMapper.findById(user, feed_id);
//
//
//        feedMapper.findById(String.valueOf(subscription.getFeedId()));
//        return null;
//    }
}
