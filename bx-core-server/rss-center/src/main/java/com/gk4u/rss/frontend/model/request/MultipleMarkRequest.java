package com.gk4u.rss.frontend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@ApiModel(description = "Multiple Mark Request")
@Data
public class MultipleMarkRequest implements Serializable {

	@ApiModelProperty(value = "list of mark requests", required = true)
	private List<MarkRequest> requests;

}
