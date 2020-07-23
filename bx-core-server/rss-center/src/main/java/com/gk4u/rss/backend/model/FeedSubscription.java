package com.gk4u.rss.backend.model;

import com.gk4u.rss.backend.entity.User;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class FeedSubscription extends AbstractModel {


    private User user;


    private Feed feed;


    private String title;


    private Integer position;


    private String filter;

}
