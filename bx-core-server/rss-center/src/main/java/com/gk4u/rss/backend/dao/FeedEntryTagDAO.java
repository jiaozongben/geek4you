package com.gk4u.rss.backend.dao;


import com.gk4u.rss.backend.model.FeedEntry;
import com.gk4u.rss.backend.model.FeedEntryTag;
import com.gk4u.rss.backend.model.QFeedEntryTag;
import com.gk4u.rss.backend.model.User;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class FeedEntryTagDAO extends GenericDAO<FeedEntryTag> {

	private QFeedEntryTag tag = QFeedEntryTag.feedEntryTag;

	@Inject
	public FeedEntryTagDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public List<String> findByUser(User user) {
		return query().selectDistinct(tag.name).from(tag).where(tag.user.eq(user)).fetch();
	}

	public List<FeedEntryTag> findByEntry(User user, FeedEntry entry) {
		return query().selectFrom(tag).where(tag.user.eq(user), tag.entry.eq(entry)).fetch();
	}
}
