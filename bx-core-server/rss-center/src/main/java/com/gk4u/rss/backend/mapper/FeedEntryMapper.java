package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.entity.FeedEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-08-02
 */
public interface FeedEntryMapper extends BaseMapper<FeedEntry> {
    //查询feed entry 是否存在
    @Select("select * from feed_entry where guidHash = #{guid} and feed_id=#{feed.id}")
    public Long findExisting(@Param("guid") String guid, @Param("feed") Feed feed);

    //查询当前各feed的超出容量的集合
    @Select("select feed_id as id,\n" +
            "count(1) as capacity from feed_entry \n" +
            "group by feed_id\n" +
            "having count(1) > #{maxCapacity}")
    public List<FeedCapacity> findFeedsExceedingCapacity(@Param("maxCapacity") long maxCapacity, long max);

    //删除feed
    @Select("select * from feed_entry where feed_id='#{feed_id}' ")
    public int delete(@Param("feed_id") Long feedId, long max);

    //删除过期feed
    @Select("select * from feed_entry where feed_id='#{feed_id}' order by inserted asc")
    public int deleteOldEntries(Long feedId, long max);

    @AllArgsConstructor
    @Getter
    public static class FeedCapacity {
        private Long id;
        private Long capacity;
    }
}
