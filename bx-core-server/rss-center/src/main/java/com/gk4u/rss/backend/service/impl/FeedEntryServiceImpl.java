package com.gk4u.rss.backend.service.impl;

import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.mapper.FeedEntryMapper;
import com.gk4u.rss.backend.service.IFeedEntryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author James Bond
 * @since 2020-08-02
 */
@Service
public class FeedEntryServiceImpl extends ServiceImpl<FeedEntryMapper, FeedEntry> implements IFeedEntryService {

}
