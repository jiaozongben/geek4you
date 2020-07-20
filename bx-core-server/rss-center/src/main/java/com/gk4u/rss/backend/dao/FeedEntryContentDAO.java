package com.gk4u.rss.backend.dao;


import com.gk4u.rss.backend.model.FeedEntryContent;
import com.gk4u.rss.backend.model.QFeedEntry;
import com.gk4u.rss.backend.model.QFeedEntryContent;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class FeedEntryContentDAO extends GenericDAO<FeedEntryContent> {

	private QFeedEntryContent content = QFeedEntryContent.feedEntryContent;
	private QFeedEntry entry = QFeedEntry.feedEntry;

	@Inject
	public FeedEntryContentDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Long findExisting(String contentHash, String titleHash) {
		return query().select(content.id).from(content).where(content.contentHash.eq(contentHash), content.titleHash.eq(titleHash))
				.fetchFirst();
	}

	public int deleteWithoutEntries(int max) {

		JPQLQuery<Integer> subQuery = JPAExpressions.selectOne().from(entry).where(entry.content.id.eq(content.id));
		List<FeedEntryContent> list = query().selectFrom(content).where(subQuery.notExists()).limit(max).fetch();

		int deleted = list.size();
		delete(list);
		return deleted;
	}
}
