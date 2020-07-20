package com.gk4u.rss.backend.dao;


import com.gk4u.rss.backend.model.QUserSettings;
import com.gk4u.rss.backend.model.User;
import com.gk4u.rss.backend.model.UserSettings;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserSettingsDAO extends GenericDAO<UserSettings> {

	private QUserSettings settings = QUserSettings.userSettings;

	@Inject
	public UserSettingsDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public UserSettings findByUser(User user) {
		return query().selectFrom(settings).where(settings.user.eq(user)).fetchFirst();
	}
}
