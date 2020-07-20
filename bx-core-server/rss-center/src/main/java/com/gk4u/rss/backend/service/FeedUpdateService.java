package com.gk4u.rss.backend.service;


import com.gk4u.rss.backend.dao.FeedEntryDAO;
import com.gk4u.rss.backend.dao.FeedEntryStatusDAO;
import com.gk4u.rss.backend.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
@Singleton
public class FeedUpdateService {

	private final FeedEntryDAO feedEntryDAO;
	private final FeedEntryStatusDAO feedEntryStatusDAO;
	private final FeedEntryContentService feedEntryContentService;
	private final FeedEntryFilteringService feedEntryFilteringService;

	/**
	 * this is NOT thread-safe
	 */
	public boolean addEntry(Feed feed, FeedEntry entry, List<FeedSubscription> subscriptions) {

		Long existing = feedEntryDAO.findExisting(entry.getGuid(), feed);
		if (existing != null) {
			return false;
		}

		FeedEntryContent content = feedEntryContentService.findOrCreate(entry.getContent(), feed.getLink());
		entry.setGuidHash(DigestUtils.sha1Hex(entry.getGuid()));
		entry.setContent(content);
		entry.setInserted(new Date());
		entry.setFeed(feed);
		feedEntryDAO.saveOrUpdate(entry);

		// if filter does not match the entry, mark it as read
		for (FeedSubscription sub : subscriptions) {
			boolean matches = true;
			try {
				matches = feedEntryFilteringService.filterMatchesEntry(sub.getFilter(), entry);
			} catch (FeedEntryFilteringService.FeedEntryFilterException e) {
				log.error("could not evaluate filter {}", sub.getFilter(), e);
			}
			if (!matches) {
				FeedEntryStatus status = new FeedEntryStatus(sub.getUser(), sub, entry);
				status.setRead(true);
				feedEntryStatusDAO.saveOrUpdate(status);
			}
		}

		return true;
	}
}
