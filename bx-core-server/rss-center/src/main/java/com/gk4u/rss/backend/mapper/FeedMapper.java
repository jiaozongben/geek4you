package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.Feed;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    @Select("SELECT * FROM `feed` WHERE disabledUntil < '#{date}' ORDER BY disabledUntil ASC limit 0,#{count};")
    public List<Feed> findNextUpdatable(@Param("date") Date lastLoginThreshold, @Param("count") int count);

    //通过url查询回feed
    @Select("SELECT * FROM `feed` WHERE normalizedUrl=#normalizedUrl")
    public Feed findByUrl(@Param("normalizedUrl") String normalizedUrl);

    //通过topic 话题查询回来所有的feed
    @Select("SELECT * FROM `feed` WHERE normalizedUrl=#pushTopic")
    public List<Feed> findByTopic(@Param("pushTopic")String topic);
    @Select("SELECT * FROM `feed` WHERE id=#{id}")
    public Feed findById(@Param("id") String fed_id);
    //查询回来所有没有订阅的？？
//    public List<Feed> findWithoutSubscriptions(int max);

}
