package com.gk4u.rss.backend.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;


@Getter
@Setter
public class FeedEntry {

    private String guid;

    private String guidHash;

    private Feed feed;

    private FeedEntryContent content;

    private String url;

    private Date inserted;

    private Date updated;


}
