package com.gk4u.rss.backend.feed;




import com.gk4u.rss.backend.entity.FeedEntry;
import com.gk4u.rss.backend.entity.FeedSubscription;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FetchedFeed {

	private FeedSubscription feedSubscription = new FeedSubscription();
	private List<FeedEntry> entries = new ArrayList<>();

	private String title;
	private String urlAfterRedirect;
	private long fetchDuration;

}
