package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.entity.FeedEntry;
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
public interface FeedEntryMapper extends BaseMapper<FeedEntry> {
    public List<Feed> findNextUpdatable(int count, Date lastLoginThreshold);

    public Feed findByUrl(String normalizedUrl);

    public List<Feed> findByTopic(String topic);

    public List<Feed> findWithoutSubscriptions(int max);
}
