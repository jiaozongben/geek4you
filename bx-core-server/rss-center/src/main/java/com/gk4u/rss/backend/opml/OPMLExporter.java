package com.gk4u.rss.backend.opml;


import com.gk4u.rss.backend.entity.FeedCategory;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.mapper.FeedCategoryMapper;
import com.gk4u.rss.backend.mapper.FeedSubscriptionMapper;
import com.rometools.opml.feed.opml.Attribute;
import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OPMLExporter {

    @Autowired
	private   FeedCategoryMapper feedCategoryDAO;
    @Autowired
	private FeedSubscriptionMapper feedSubscriptionDAO;


	public Opml export(User user) {
		Opml opml = new Opml();
		opml.setFeedType("opml_1.1");
		opml.setTitle(String.format("%s subscriptions in CommaFeed", user.getName()));
		opml.setCreated(new Date());

		List<FeedCategory> categories = feedCategoryDAO.findAll(user);
		Collections.sort(categories,
				(e1, e2) -> ObjectUtils.firstNonNull(e1.getPosition(), 0) - ObjectUtils.firstNonNull(e2.getPosition(), 0));

		List<FeedSubscription> subscriptions = feedSubscriptionDAO.findAllByUser(user);
		Collections.sort(subscriptions,
				(e1, e2) -> ObjectUtils.firstNonNull(e1.getPosition(), 0) - ObjectUtils.firstNonNull(e2.getPosition(), 0));

		// export root categories
		for (FeedCategory cat : categories) {
			opml.getOutlines().add(buildCategoryOutline(cat));
		}

		// export root subscriptions
		for (FeedSubscription sub : subscriptions) {
			opml.getOutlines().add(buildSubscriptionOutline(sub));
		}

		return opml;

	}

	private Outline buildCategoryOutline(FeedCategory cat) {
		Outline outline = new Outline();
		outline.setText(cat.getName());
		outline.setTitle(cat.getName());
		return outline;
	}

	private Outline buildSubscriptionOutline(FeedSubscription sub) {
		Outline outline = new Outline();
		outline.setText(sub.getTitle());
		outline.setTitle(sub.getTitle());
		outline.setType("rss");
		outline.getAttributes().add(new Attribute("xmlUrl", sub.getUrl()));
		return outline;
	}
}
