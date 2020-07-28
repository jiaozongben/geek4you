package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.entity.FeedEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

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
@Component
public interface FeedEntryMapper extends BaseMapper<FeedEntry> {

    public Long findExisting(String guid, Feed feed);

    public List<FeedCapacity> findFeedsExceedingCapacity(long maxCapacity, long max);

    public int delete(Long feedId, long max);

    public int deleteOldEntries(Long feedId, long max);

    @AllArgsConstructor
    @Getter
    public static class FeedCapacity {
        private Long id;
        private Long capacity;
    }
}
