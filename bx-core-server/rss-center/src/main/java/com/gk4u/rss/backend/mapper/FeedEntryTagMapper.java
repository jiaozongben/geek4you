package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.entity.FeedEntryTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk4u.rss.backend.entity.User;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-07-26
 */
public interface FeedEntryTagMapper extends BaseMapper<FeedEntryTag> {

    //通过用户找回来标签tag
    public List<String> findByUser(User user);

    //通过用户、feed找回来内容标签
    public List<FeedEntryTag> findByEntry(User user, FeedEntry entry);
}
