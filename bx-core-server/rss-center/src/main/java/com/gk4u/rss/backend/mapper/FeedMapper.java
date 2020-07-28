package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.Feed;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-07-26
 */
public interface FeedMapper extends BaseMapper<Feed> {



    //找到最近没失效的feed
    public List<Feed> findNextUpdatable(int count, Date lastLoginThreshold);

    //通过url查询回feed
    public Feed findByUrl(String normalizedUrl);

    //通过topic 话题查询回来所有的feed
    public List<Feed> findByTopic(String topic);

    //查询回来所有没有订阅的？？
    public List<Feed> findWithoutSubscriptions(int max);

}
