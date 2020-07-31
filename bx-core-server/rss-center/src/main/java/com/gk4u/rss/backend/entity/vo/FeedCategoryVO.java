package com.gk4u.rss.backend.entity.vo;

import com.gk4u.rss.backend.entity.FeedCategory;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.gk4u.rss.backend.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class FeedCategoryVO {


        //用户
    private User user;
    private FeedCategory current;
    //父级目录
    private FeedCategory parent;
    //孩子节点
    private Set<FeedCategory> children;

    //订阅目录
    private Set<FeedSubscription> subscriptions;
}
