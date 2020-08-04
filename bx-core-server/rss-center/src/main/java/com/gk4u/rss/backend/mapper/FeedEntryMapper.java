package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.FeedEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-08-04
 */
@Component
public interface FeedEntryMapper extends BaseMapper<FeedEntry> {

    @Select("SELECT count(1) FROM `feed_entry` where url = '#{feedEntry.url}';")
    public  int findEntryExists(@Param("feedEntry") FeedEntry feedEntry);
}
