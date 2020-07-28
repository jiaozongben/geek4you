package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.entity.FeedEntryStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.feed.FeedEntryKeyword;
import com.gk4u.rss.frontend.model.UnreadCount;
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
public interface FeedEntryStatusMapper extends BaseMapper<FeedEntryStatus> {

    // 通过用户、目录、feed 查询状态
    public FeedEntryStatus getStatus(User user, FeedSubscription sub, FeedEntry entry);

    //找出星标状态
//    public List<FeedEntryStatus> findStarred(User user, Date newerThan, int offset, int limit, ReadingOrder order, boolean includeContent);

//    public List<FeedEntryStatus> findBySubscriptions(User user, List<FeedSubscription> subs, boolean unreadOnly,
//                                                     List<FeedEntryKeyword> keywords, Date newerThan, int offset, int limit, ReadingOrder order, boolean includeContent,
//                                                     boolean onlyIds, String tag);

    //通过用户、订阅获取未读数量
    public UnreadCount getUnreadCount(User user, FeedSubscription subscription);


    //获取过期状态
    public List<FeedEntryStatus> getOldStatuses(Date olderThan, int limit);
}
