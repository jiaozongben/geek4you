package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.FeedSubscription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-08-02
 */
@Component
public interface FeedSubscriptionMapper extends BaseMapper<FeedSubscription> {


    @Select("SELECT * FROM `feed_subscription`;")
    public List<FeedSubscription> findNextUpdatable();
}
