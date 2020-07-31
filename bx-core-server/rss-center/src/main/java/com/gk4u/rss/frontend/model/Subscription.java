package com.gk4u.rss.frontend.model;


import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.entity.FeedCategory;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.gk4u.rss.backend.feed.FeedUtils;
import com.gk4u.rss.backend.util.DateUtil;
import lombok.Data;
import org.jsoup.helper.DataUtil;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")

@Data
public class Subscription implements Serializable {

    public static Subscription build(FeedSubscription subscription, String publicUrl, UnreadCount unreadCount) {
        Date now = new Date();
//        FeedCategory category = subscription.getCategory();
//        Feed feed = subscription.getFeed();
//        FeedCategory category = subscription.getCategory();
        Feed feed = null;
        Subscription sub = new Subscription();
        sub.setId(Long.valueOf(subscription.getId()));
        sub.setName(subscription.getTitle());
        sub.setPosition(subscription.getPosition());
        sub.setMessage(feed.getMessage());
        sub.setErrorCount(feed.getErrorCount());
        sub.setFeedUrl(feed.getUrl());
        sub.setFeedLink(feed.getLink());
        sub.setIconUrl(FeedUtils.getFaviconUrl(subscription, publicUrl));
        sub.setLastRefresh(DateUtil.localDateTime2Date(feed.getLastUpdated()));
        sub.setNextRefresh((feed.getDisabledUntil() != null && DateUtil.localDateTime2Date(feed.getDisabledUntil()).before(now)) ? null : DateUtil.localDateTime2Date(feed.getDisabledUntil()));
        sub.setUnread(unreadCount.getUnreadCount());
        sub.setNewestItemTime(unreadCount.getNewestItemTime());
//        sub.setCategoryId(category == null ? null : String.valueOf(category.getId()));

        sub.setFilter(subscription.getFilter());
        return sub;
    }


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