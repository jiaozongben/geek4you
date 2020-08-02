package com.gk4u.rss.frontend.model;


//import com.gk4u.rss.backendbak.entity.Feed;
//import com.gk4u.rss.backendbak.entity.FeedSubscription;
//import com.gk4u.rss.backend.feedSubscription.FeedUtils;
//import com.gk4u.rss.backend.util.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")

@Data
public class Subscription implements Serializable {

//    public static Subscription build(FeedSubscription subscription, String publicUrl, UnreadCount unreadCount) {
//        Date now = new Date();
////        FeedCategory category = subscription.getCategory();
////        Feed feedSubscription = subscription.getFeedSubscription();
////        FeedCategory category = subscription.getCategory();
//        Feed feedSubscription = null;
//        Subscription sub = new Subscription();
//        sub.setId(Long.valueOf(subscription.getId()));
//        sub.setName(subscription.getTitle());
//        sub.setPosition(subscription.getPosition());
//        sub.setMessage(feedSubscription.getMessage());
//        sub.setErrorCount(feedSubscription.getErrorCount());
//        sub.setFeedUrl(feedSubscription.getUrl());
//        sub.setFeedLink(feedSubscription.getLink());
//        sub.setIconUrl(FeedUtils.getFaviconUrl(subscription, publicUrl));
//        sub.setLastRefresh(DateUtil.localDateTime2Date(feedSubscription.getLastUpdated()));
//        sub.setNextRefresh((feedSubscription.getDisabledUntil() != null && DateUtil.localDateTime2Date(feedSubscription.getDisabledUntil()).before(now)) ? null : DateUtil.localDateTime2Date(feedSubscription.getDisabledUntil()));
//        sub.setUnread(unreadCount.getUnreadCount());
//        sub.setNewestItemTime(unreadCount.getNewestItemTime());
////        sub.setCategoryId(category == null ? null : String.valueOf(category.getId()));
//
//        sub.setFilter(subscription.getFilter());
//        return sub;
//    }


    private Long id;


    private String name;


    private String message;


    private int errorCount;


    private Date lastRefresh;


    private Date nextRefresh;


    private String feedUrl;


    private String feedLink;


    private String iconUrl;


    private long unread;


    private String categoryId;


    private Integer position;


    private Date newestItemTime;


    private String filter;

}