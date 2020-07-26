package com.gk4u.rss.backend.service;

import com.gk4u.rss.backend.entity.Feed;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author James Bond
 * @since 2020-07-26
 */
public interface IFeedService extends IService<Feed> {

    public List<Feed> findNextUpdatable(int count, Date lastLoginThreshold);
}
