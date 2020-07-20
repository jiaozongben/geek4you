package com.gk4u.rss.backend.service.internal;


import com.gk4u.rss.backend.CommaFeedConfiguration;
import com.gk4u.rss.backend.dao.UserDAO;
import com.gk4u.rss.backend.model.User;
import com.gk4u.rss.backend.service.FeedSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;

@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
@Singleton
public class PostLoginActivities {

	private final UserDAO userDAO;
	private final FeedSubscriptionService feedSubscriptionService;
	private final CommaFeedConfiguration config;

	public void executeFor(User user) {
		Date lastLogin = user.getLastLogin();
		Date now = new Date();

		boolean saveUser = false;
		// only update lastLogin field every hour in order to not
		// invalidate the cache every time someone logs in
		if (lastLogin == null || lastLogin.before(DateUtils.addHours(now, -1))) {
			user.setLastLogin(now);
			saveUser = true;
		}
		if (config.getHeavyLoad() && user.shouldRefreshFeedsAt(now)) {
			feedSubscriptionService.refreshAll(user);
			user.setLastFullRefresh(now);
			saveUser = true;
		}
		if (saveUser) {
			userDAO.saveOrUpdate(user);
		}
	}

}
