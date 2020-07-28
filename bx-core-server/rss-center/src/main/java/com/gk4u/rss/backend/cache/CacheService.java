package com.gk4u.rss.backend.cache;


import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.frontend.model.Category;
import com.gk4u.rss.frontend.model.UnreadCount;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.Locale;

public abstract class CacheService {

	// feed entries for faster refresh
	public abstract List<String> getLastEntries(Feed feed);

	public abstract void setLastEntries(Feed feed, List<String> entries);

	public String buildUniqueEntryKey(Feed feed, FeedEntry entry) {
		return DigestUtils.sha1Hex(entry.getGuid() + entry.getUrl());
	}

	// user categories
	public abstract Category getUserRootCategory(User user);

	public abstract void setUserRootCategory(User user, Category category);

	public abstract void invalidateUserRootCategory(User... users);

	// unread count
	public abstract UnreadCount getUnreadCount(FeedSubscription sub);

	public abstract void setUnreadCount(FeedSubscription sub, UnreadCount count);

	public abstract void invalidateUnreadCount(FeedSubscription... subs);

}
