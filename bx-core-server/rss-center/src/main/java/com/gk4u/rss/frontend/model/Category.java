package com.gk4u.rss.frontend.model;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")

@Data
public class Category implements Serializable {


	private String id;


	private String parentId;


	private String name;


	private List<Category> children = new ArrayList<>();


	private List<Subscription> feeds = new ArrayList<>();

 	private boolean expanded;

 	private Integer position;
}