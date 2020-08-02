package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.entity.FeedEntryTag;
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
 * @since 2020-08-01
 */
public interface FeedEntryTagMapper extends BaseMapper<FeedEntryTag> {


    @Select("SELECT DISTINCT name  FROM `feed_entry_tag` where user_id=#{user.id};")
    public List<String> findByUser(@Param("user") User user);

    @Select("SELECT DISTINCT *  FROM `feed_entry_tag` where user_id=#{user.id} and entry_id = #{entry.id};")
    public List<FeedEntryTag> findByEntry(@Param("user") User user, @Param("entry") FeedEntry entry);

}
