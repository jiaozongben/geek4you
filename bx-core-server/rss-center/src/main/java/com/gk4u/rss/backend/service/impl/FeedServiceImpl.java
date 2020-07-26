package com.gk4u.rss.backend.service.impl;

import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.mapper.FeedMapper;
import com.gk4u.rss.backend.service.IFeedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author James Bond
 * @since 2020-07-26
 */
@Service
public class FeedServiceImpl extends ServiceImpl<FeedMapper, Feed> implements IFeedService {

    @Override
    public List<Feed> findNextUpdatable(int count, Date lastLoginThreshold) {
        return null;
    }
}
