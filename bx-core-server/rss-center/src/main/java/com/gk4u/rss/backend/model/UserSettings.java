package com.gk4u.rss.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "USERSETTINGS")
@SuppressWarnings("serial")
@Getter
@Setter
public class UserSettings extends AbstractModel {

	public enum ReadingMode {
		all, unread
	}

	public enum ReadingOrder {
		asc, desc, abc, zyx
	}

	public enum ViewMode {
		title, expanded
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReadingMode readingMode;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReadingOrder readingOrder;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ViewMode viewMode;

	@Column(name = "user_lang", length = 4)
	private String language;

	private boolean showRead;
	private boolean scrollMarks;

	@Column(length = 32)
	private String theme;

	@Lob
	@Column(length = Integer.MAX_VALUE)
	@Type(type = "org.hibernate.type.TextType")
	private String customCss;

	@Column(name = "scroll_speed")
	private int scrollSpeed;

	private boolean email;
	private boolean gmail;
	private boolean facebook;
	private boolean twitter;
	private boolean googleplus;
	private boolean tumblr;
	private boolean pocket;
	private boolean instapaper;
	private boolean buffer;
	private boolean readability;

}
