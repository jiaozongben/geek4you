package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.entity.FeedEntryStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.feed.FeedEntryKeyword;
import com.gk4u.rss.frontend.model.UnreadCount;

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
public interface FeedEntryStatusMapper extends BaseMapper<FeedEntryStatus> {
    public FeedEntryStatus getStatus(User user, FeedSubscription sub, FeedEntry entry);


//    public List<FeedEntryStatus> findStarred(User user, Date newerThan, int offset, int limit, ReadingOrder order, boolean includeContent);

//    public List<FeedEntryStatus> findBySubscriptions(User user, List<FeedSubscription> subs, boolean unreadOnly,
//                                                     List<FeedEntryKeyword> keywords, Date newerThan, int offset, int limit, ReadingOrder order, boolean includeContent,
//                                                     boolean onlyIds, String tag);

    public UnreadCount getUnreadCount(User user, FeedSubscription subscription);

    public List<FeedEntryStatus> getOldStatuses(Date olderThan, int limit);
}
