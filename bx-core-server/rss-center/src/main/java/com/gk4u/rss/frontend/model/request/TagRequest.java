package com.gk4u.rss.frontend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@ApiModel(description = "Tag Request")
@Data
public class TagRequest implements Serializable {

	@ApiModelProperty(value = "entry id", required = true)
	private Long entryId;

	@ApiModelProperty(value = "tags", required = true)
	private List<String> tags;

}
