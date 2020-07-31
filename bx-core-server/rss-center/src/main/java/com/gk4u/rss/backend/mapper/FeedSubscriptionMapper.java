package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.entity.FeedCategory;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk4u.rss.backend.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-07-31
 */
public interface FeedSubscriptionMapper extends BaseMapper<FeedSubscription> {

    @Select("select * from feed_subscription where user_id = #{user.id} and id = #{id} ")
    public FeedSubscription findById(@Param("user") User user, @Param("id") Long id);

    public List<FeedSubscription> findByFeed(Feed feed);

    public FeedSubscription findByFeed(User user, Feed feed);

    public List<FeedSubscription> findAll(User user);

    public List<FeedSubscription> findByCategory(User user, FeedCategory category);

    public List<FeedSubscription> findByCategories(User user, List<FeedCategory> categories);

}
