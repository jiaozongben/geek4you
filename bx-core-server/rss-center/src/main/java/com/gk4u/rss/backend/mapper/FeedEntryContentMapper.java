package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.FeedEntryContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-07-26
 */
@Component
public interface FeedEntryContentMapper extends BaseMapper<FeedEntryContent> {


    public Long findExisting(String contentHash, String titleHash);

    public int deleteWithoutEntries(int max);

}
