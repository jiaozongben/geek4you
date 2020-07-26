package com.gk4u.rss.backend.feed;



import com.gk4u.rss.backend.entity.Feed;
import com.gk4u.rss.backend.entity.FeedEntry;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedRefreshContext {
	private Feed feed;
	private List<FeedEntry> entries;
	private boolean urgent;

	public FeedRefreshContext(Feed feed, boolean isUrgent) {
		this.feed = feed;
		this.urgent = isUrgent;
	}
}
