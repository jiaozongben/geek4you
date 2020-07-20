package com.gk4u.rss.backend.dao;


import com.gk4u.rss.backend.model.*;
import com.google.common.collect.Iterables;
import com.querydsl.jpa.JPQLQuery;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class FeedSubscriptionDAO extends GenericDAO<FeedSubscription> {

	private QFeedSubscription sub = QFeedSubscription.feedSubscription;

	@Inject
	public FeedSubscriptionDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public FeedSubscription findById(User user, Long id) {
		List<FeedSubscription> subs = query().selectFrom(sub).where(sub.user.eq(user), sub.id.eq(id)).leftJoin(sub.feed).fetchJoin()
				.leftJoin(sub.category).fetchJoin().fetch();
		return initRelations(Iterables.getFirst(subs, null));
	}

	public List<FeedSubscription> findByFeed(Feed feed) {
		return query().selectFrom(sub).where(sub.feed.eq(feed)).fetch();
	}

	public FeedSubscription findByFeed(User user, Feed feed) {
		List<FeedSubscription> subs = query().selectFrom(sub).where(sub.user.eq(user), sub.feed.eq(feed)).fetch();
		return initRelations(Iterables.getFirst(subs, null));
	}

	public List<FeedSubscription> findAll(User user) {
		List<FeedSubscription> subs = query().selectFrom(sub).where(sub.user.eq(user)).leftJoin(sub.feed).fetchJoin().leftJoin(sub.category)
				.fetchJoin().fetch();
		return initRelations(subs);
	}

	public List<FeedSubscription> findByCategory(User user, FeedCategory category) {
		JPQLQuery<FeedSubscription> query = query().selectFrom(sub).where(sub.user.eq(user));
		if (category == null) {
			query.where(sub.category.isNull());
		} else {
			query.where(sub.category.eq(category));
		}
		return initRelations(query.fetch());
	}

	public List<FeedSubscription> findByCategories(User user, List<FeedCategory> categories) {
		Set<Long> categoryIds = categories.stream().map(c -> c.getId()).collect(Collectors.toSet());
		return findAll(user).stream().filter(s -> s.getCategory() != null && categoryIds.contains(s.getCategory().getId()))
				.collect(Collectors.toList());
	}

	private List<FeedSubscription> initRelations(List<FeedSubscription> list) {
		list.forEach(s -> initRelations(s));
		return list;
	}

	private FeedSubscription initRelations(FeedSubscription sub) {
		if (sub != null) {
			Models.initialize(sub.getFeed());
			Models.initialize(sub.getCategory());
		}
		return sub;
	}
}
