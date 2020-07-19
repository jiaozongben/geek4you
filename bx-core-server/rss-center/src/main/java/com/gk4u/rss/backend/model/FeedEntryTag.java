package com.gk4u.rss.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "FEEDENTRYTAGS")
@SuppressWarnings("serial")
@Getter
@Setter
public class FeedEntryTag extends AbstractModel {

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@JoinColumn(name = "entry_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private FeedEntry entry;

	@Column(name = "name", length = 40)
	private String name;

	public FeedEntryTag() {
	}

	public FeedEntryTag(User user, FeedEntry entry, String name) {
		this.name = name;
		this.entry = entry;
		this.user = user;
	}

}
