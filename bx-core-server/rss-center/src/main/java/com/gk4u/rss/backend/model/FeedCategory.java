package com.gk4u.rss.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "FEEDCATEGORIES")
@SuppressWarnings("serial")
@Getter
@Setter
public class FeedCategory extends AbstractModel {

	@Column(length = 128, nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	private FeedCategory parent;

	@OneToMany(mappedBy = "parent")
	private Set<FeedCategory> children;

	@OneToMany(mappedBy = "category")
	private Set<FeedSubscription> subscriptions;

	private boolean collapsed;

	private Integer position;

}
