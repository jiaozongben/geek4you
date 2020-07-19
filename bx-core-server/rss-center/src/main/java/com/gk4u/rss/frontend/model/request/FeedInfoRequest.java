package com.gk4u.rss.frontend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
@ApiModel(description = "Feed information request")
@Data
public class FeedInfoRequest implements Serializable {

	@ApiModelProperty(value = "feed url", required = true)
	private String url;

}
