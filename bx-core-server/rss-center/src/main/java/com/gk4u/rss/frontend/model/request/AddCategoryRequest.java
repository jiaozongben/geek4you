package com.gk4u.rss.frontend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
@ApiModel(description = "Add Category Request")
@Data
public class AddCategoryRequest implements Serializable {

	@ApiModelProperty(value = "name", required = true)
	private String name;

	@ApiModelProperty(value = "parent category id, if any")
	private String parentId;

}
