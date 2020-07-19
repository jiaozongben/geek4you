package com.gk4u.rss.frontend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
@ApiModel
@Data
public class IDRequest implements Serializable {

	@ApiModelProperty(required = true)
	private Long id;

}
