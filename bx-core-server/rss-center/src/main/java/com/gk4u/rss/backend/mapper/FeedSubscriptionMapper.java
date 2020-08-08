package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.FeedSubscription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk4u.rss.backend.entity.User;
import com.rometools.rome.feed.atom.Feed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-08-02
 */

@Mapper
public interface FeedSubscriptionMapper extends BaseMapper<FeedSubscription> {


    @Select("SELECT * FROM `feed_subscription` ;")
    public List<FeedSubscription> findAll();

    @Select("SELECT * FROM `feed_subscription` where user_id=#{user.id};")
    public List<FeedSubscription> findAllByUser(@Param("user") User user);

    @Select("select * from feed_subscription where user_id=#{user.id} and url = #{url}")
    public FeedSubscription findByUrl(@Param("user") User user, @Param("url") String url);

}
