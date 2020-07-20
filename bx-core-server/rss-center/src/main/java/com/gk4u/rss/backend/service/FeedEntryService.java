package com.gk4u.rss.backend.service;


import com.gk4u.rss.backend.cache.CacheService;
import com.gk4u.rss.backend.dao.FeedEntryDAO;
import com.gk4u.rss.backend.dao.FeedEntryStatusDAO;
import com.gk4u.rss.backend.dao.FeedSubscriptionDAO;
import com.gk4u.rss.backend.feed.FeedEntryKeyword;
import com.gk4u.rss.backend.model.FeedEntry;
import com.gk4u.rss.backend.model.FeedEntryStatus;
import com.gk4u.rss.backend.model.FeedSubscription;
import com.gk4u.rss.backend.model.User;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
@Singleton
public class FeedEntryService {

	private final FeedSubscriptionDAO feedSubscriptionDAO;
	private final FeedEntryDAO feedEntryDAO;
	private final FeedEntryStatusDAO feedEntryStatusDAO;
	private final CacheService cache;

	public void markEntry(User user, Long entryId, boolean read) {

		FeedEntry entry = feedEntryDAO.findById(entryId);
		if (entry == null) {
			return;
		}

		FeedSubscription sub = feedSubscriptionDAO.findByFeed(user, entry.getFeed());
		if (sub == null) {
			return;
		}

		FeedEntryStatus status = feedEntryStatusDAO.getStatus(user, sub, entry);
		if (status.isMarkable()) {
			status.setRead(read);
			feedEntryStatusDAO.saveOrUpdate(status);
			cache.invalidateUnreadCount(sub);
			cache.invalidateUserRootCategory(user);
		}
	}

	public void starEntry(User user, Long entryId, Long subscriptionId, boolean starred) {

		FeedSubscription sub = feedSubscriptionDAO.findById(user, subscriptionId);
		if (sub == null) {
			return;
		}

		FeedEntry entry = feedEntryDAO.findById(entryId);
		if (entry == null) {
			return;
		}

		FeedEntryStatus status = feedEntryStatusDAO.getStatus(user, sub, entry);
		status.setStarred(starred);
		feedEntryStatusDAO.saveOrUpdate(status);
	}

	public void markSubscriptionEntries(User user, List<FeedSubscription> subscriptions, Date olderThan, List<FeedEntryKeyword> keywords) {
		List<FeedEntryStatus> statuses = feedEntryStatusDAO.findBySubscriptions(user, subscriptions, true, keywords, null, -1, -1, null,
				false, false, null);
		markList(statuses, olderThan);
		cache.invalidateUnreadCount(subscriptions.toArray(new FeedSubscription[0]));
		cache.invalidateUserRootCategory(user);
	}

	public void markStarredEntries(User user, Date olderThan) {
		List<FeedEntryStatus> statuses = feedEntryStatusDAO.findStarred(user, null, -1, -1, null, false);
		markList(statuses, olderThan);
	}

	private void markList(List<FeedEntryStatus> statuses, Date olderThan) {
		List<FeedEntryStatus> list = new ArrayList<>();
		for (FeedEntryStatus status : statuses) {
			if (!status.isRead()) {
				Date inserted = status.getEntry().getInserted();
				if (olderThan == null || inserted == null || olderThan.after(inserted)) {
					status.setRead(true);
					list.add(status);
				}
			}
		}
		feedEntryStatusDAO.saveOrUpdate(list);
	}
}
