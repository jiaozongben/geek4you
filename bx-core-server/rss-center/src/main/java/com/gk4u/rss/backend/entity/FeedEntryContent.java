package com.gk4u.rss.backend.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class FeedEntryContent  {

	
	private String title;

	
	private String titleHash;

	
	
	
	private String content;

	
	private String contentHash;

	
	private String author;

	
	private String enclosureUrl;

	
	private String enclosureType;

	
	private String categories;

	
	private Set<FeedEntry> entries;

}
