package com.gk4u.rss.backend.dao;


import com.gk4u.rss.backend.model.QUser;
import com.gk4u.rss.backend.model.User;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserDAO extends GenericDAO<User> {

	private QUser user = QUser.user;

	@Inject
	public UserDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public User findByName(String name) {
		return query().selectFrom(user).where(user.name.equalsIgnoreCase(name)).fetchOne();
	}

	public User findByApiKey(String key) {
		return query().selectFrom(user).where(user.apiKey.equalsIgnoreCase(key)).fetchOne();
	}

	public User findByEmail(String email) {
		return query().selectFrom(user).where(user.email.equalsIgnoreCase(email)).fetchOne();
	}

	public long count() {
		return query().selectFrom(user).fetchCount();
	}
}
