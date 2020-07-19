package com.gk4u.rss.frontend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
@ApiModel(description = "Mark Request")
@Data
public class CollapseRequest implements Serializable {

	@ApiModelProperty(value = "category id", required = true)
	private Long id;

	@ApiModelProperty(value = "collapse", required = true)
	private boolean collapse;

}
