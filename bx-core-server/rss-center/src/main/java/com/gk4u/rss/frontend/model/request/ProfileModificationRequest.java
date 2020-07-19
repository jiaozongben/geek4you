package com.gk4u.rss.frontend.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
@ApiModel(description = "Profile modification request")
@Data
public class ProfileModificationRequest implements Serializable {

	@ApiModelProperty(value = "changes email of the user, if specified")
	private String email;

	@ApiModelProperty(value = "changes password of the user, if specified")
	private String password;

	@ApiModelProperty(value = "generate a new api key")
	private boolean newApiKey;

}
