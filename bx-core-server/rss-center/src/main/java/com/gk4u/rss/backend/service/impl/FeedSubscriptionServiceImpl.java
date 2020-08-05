package com.gk4u.rss.backend.service.impl;

import com.gk4u.rss.backend.CommaFeedConfiguration;
import com.gk4u.rss.backend.entity.FeedCategory;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.feed.FeedUtils;
import com.gk4u.rss.backend.mapper.FeedSubscriptionMapper;
import com.gk4u.rss.backend.service.IFeedSubscriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author James Bond
 * @since 2020-08-02
 */
@Service
public class FeedSubscriptionServiceImpl extends ServiceImpl<FeedSubscriptionMapper, FeedSubscription> implements IFeedSubscriptionService {

    @Autowired
    private   CommaFeedConfiguration config;
    @Autowired
    private FeedSubscriptionMapper feedSubscriptionMapper;


    public FeedSubscription subscribe(User user, String url, String title, FeedCategory category, int position) {

        final String pubUrl = config.getPublicUrl();

        FeedSubscription feedSubscription = feedSubscriptionMapper.findOrCreate(url);

        FeedSubscription sub = feedSubscriptionDAO.findByFeed(user, feedSubscription);
        if (sub == null) {
            sub = new FeedSubscription();
            sub.setFeed(feedSubscription);
            sub.setUser(user);
        }
        sub.setCategory(category);
        sub.setPosition(position);
        sub.setTitle(FeedUtils.truncate(title, 128));
        feedSubscriptionDAO.saveOrUpdate(sub);

        queues.add(feedSubscription, false);

        return feedSubscription;
    }
    public FeedSubscription findOrCreate(String url);

}
