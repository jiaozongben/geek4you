package com.gk4u.rss.frontend.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UnreadCount implements Serializable {


	private long feedId;


	private long unreadCount;


	private Date newestItemTime;

	public UnreadCount() {
	}

	public UnreadCount(long feedId, long unreadCount, Date newestItemTime) {
		this.feedId = feedId;
		this.unreadCount = unreadCount;
		this.newestItemTime = newestItemTime;
	}

}