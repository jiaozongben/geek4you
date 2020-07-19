package com.gk4u.rss.frontend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
@ApiModel(description = "Star Request")
@Data
public class StarRequest implements Serializable {

	@ApiModelProperty(value = "id", required = true)
	private String id;

	@ApiModelProperty(value = "feed id", required = true)
	private Long feedId;

	@ApiModelProperty(value = "starred or not", required = true)
	private boolean starred;

}
