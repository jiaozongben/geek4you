package com.gk4u.rss.backend.cache;



import com.gk4u.rss.backend.model.Feed;
import com.gk4u.rss.backend.model.FeedSubscription;
import com.gk4u.rss.backend.model.User;
import com.gk4u.rss.frontend.model.Category;
import com.gk4u.rss.frontend.model.UnreadCount;

import java.util.Collections;
import java.util.List;

public class NoopCacheService extends CacheService {

	@Override
	public List<String> getLastEntries(Feed feed) {
		return Collections.emptyList();
	}

	@Override
	public void setLastEntries(Feed feed, List<String> entries) {
	}

	@Override
	public UnreadCount getUnreadCount(FeedSubscription sub) {
		return null;
	}

	@Override
	public void setUnreadCount(FeedSubscription sub, UnreadCount count) {

	}

	@Override
	public void invalidateUnreadCount(FeedSubscription... subs) {

	}

	@Override
	public Category getUserRootCategory(User user) {
		return null;
	}

	@Override
	public void setUserRootCategory(User user, Category category) {

	}

	@Override
	public void invalidateUserRootCategory(User... users) {

	}

}
