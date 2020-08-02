package com.gk4u.rss.backend.feed;




import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.entity.FeedSubscription;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedRefreshContext {
	private FeedSubscription feedSubscription;
	private List<FeedEntry> entries;
	private boolean urgent;

	public FeedRefreshContext(FeedSubscription feedSubscription, boolean isUrgent) {
		this.feedSubscription = feedSubscription;
		this.urgent = isUrgent;
	}
}
