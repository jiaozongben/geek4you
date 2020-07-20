package com.gk4u.rss.backend.urlprovider;

/**
 * Tries to find a feed url given the url and page content
 */
public interface FeedURLProvider {

	String get(String url, String urlContent);

}
