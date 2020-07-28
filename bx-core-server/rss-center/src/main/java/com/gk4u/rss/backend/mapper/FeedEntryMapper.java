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

    //查询feed entry 是否存在
    public Long findExisting(String guid, Feed feed);

    //查询当前各feed的容量，分组查询数量
    public List<FeedCapacity> findFeedsExceedingCapacity(long maxCapacity, long max);

    //删除feed
    public int delete(Long feedId, long max);

    //删除过期feed
    public int deleteOldEntries(Long feedId, long max);

    @AllArgsConstructor
    @Getter
    public static class FeedCapacity {
        private Long id;
        private Long capacity;
    }
}
