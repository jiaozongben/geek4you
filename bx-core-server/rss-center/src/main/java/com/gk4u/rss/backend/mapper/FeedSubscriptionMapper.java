package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.entity.FeedCategory;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk4u.rss.backend.entity.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-07-28
 */
public interface FeedSubscriptionMapper extends BaseMapper<FeedSubscription> {

    //通过用户，id查询回来订阅目录
    public FeedSubscription findById(User user, Long id);

    //通过feedid 查询回来所有的订阅
    public List<FeedSubscription> findByFeed(Feed feed);

    //通过用户、feedid查询回来订阅
    public FeedSubscription findByFeed(User user, Feed feed);

    //通过用户查询回来所有的订阅
    public List<FeedSubscription> findAll(User user);

    //通过用户 加 目录查询回来所有的订阅
    public List<FeedSubscription> findByCategory(User user, FeedCategory category);

    //通过目录查询回来所有的订阅
    public List<FeedSubscription> findByCategories(User user, List<FeedCategory> categories);

    //
}
