package com.gk4u.rss.backend.model;

import lombok.Getter;
import lombok.Setter;


import java.util.Date;


@Getter
@Setter
public class Feed extends AbstractModel{

    /**
     * The url of the feed
     */
    private String url;

    /**
     * cache the url after potential http 30x redirects
     */
    private String urlAfterRedirect;

    private String normalizedUrl;

    private String normalizedUrlHash;

    /**
     * The url of the website, extracted from the feed
     */
    private String link;

    /**
     * Last time we tried to fetch the feed
     */
    private Date lastUpdated;

    /**
     * Last publishedDate value in the feed
     */
    private Date lastPublishedDate;

    /**
     * date of the last entry of the feed
     */
    private Date lastEntryDate;

    /**
     * error message while retrieving the feed
     */
    private String message;

    /**
     * times we failed to retrieve the feed
     */
    private int errorCount;

    /**
     * feed refresh is disabled until this date
     */
    private Date disabledUntil;

    /**
     * http header returned by the feed
     */
    private String lastModifiedHeader;

    /**
     * http header returned by the feed
     */
    private String etagHeader;

    /**
     * average time between entries in the feed
     */
    private Long averageEntryInterval;

    /**
     * last hash of the content of the feed xml
     */
    private String lastContentHash;

    /**
     * detected hub for pubsubhubbub
     */
    private String pushHub;

    /**
     * detected topic for pubsubhubbub
     */
    private String pushTopic;

    private String pushTopicHash;

    /**
     * last time we subscribed for that topic on that hub
     */
    private Date pushLastPing;

}
