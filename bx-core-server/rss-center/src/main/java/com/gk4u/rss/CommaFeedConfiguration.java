package com.gk4u.rss;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.ResourceBundle;

@Getter
public class CommaFeedConfiguration {

	public static enum CacheType {
		NOOP, REDIS
	}

	private ResourceBundle bundle;

	public CommaFeedConfiguration() {
		bundle = ResourceBundle.getBundle("application");
	}


	@Valid
	@NotNull
	@JsonProperty("app")
	private ApplicationSettings applicationSettings;

	public String getVersion() {
		return bundle.getString("version");
	}

	public String getGitCommit() {
		return bundle.getString("git.commit");
	}

	@Getter
	public static class ApplicationSettings {
		@NotNull
		@NotBlank
		@Valid
		private String publicUrl;

		@NotNull
		@Valid
		private Boolean allowRegistrations;

		@NotNull
		@Valid
		private Boolean createDemoAccount;

		private String googleAnalyticsTrackingCode;

		private String googleAuthKey;

		@NotNull
		@Min(1)
		@Valid
		private Integer backgroundThreads;

		@NotNull
		@Min(1)
		@Valid
		private Integer databaseUpdateThreads;

		private String smtpHost;
		private int smtpPort;
		private boolean smtpTls;
		private String smtpUserName;
		private String smtpPassword;
		private String smtpFromAddress;

		private boolean graphiteEnabled;
		private String graphitePrefix;
		private String graphiteHost;
		private int graphitePort;
		private int graphiteInterval;

		@NotNull
		@Valid
		private Boolean heavyLoad;

		@NotNull
		@Valid
		private Boolean pubsubhubbub;

		@NotNull
		@Valid
		private Boolean imageProxyEnabled;

		@NotNull
		@Min(0)
		@Valid
		private Integer queryTimeout;

		@NotNull
		@Min(0)
		@Valid
		private Integer keepStatusDays;

		@NotNull
		@Min(0)
		@Valid
		private Integer maxFeedCapacity;

		@NotNull
		@Min(0)
		@Valid
		private Integer refreshIntervalMinutes;

		@NotNull
		@Valid
		private CacheType cache;

		@Valid
		private String announcement;

		private String userAgent;

		public Date getUnreadThreshold() {
			int keepStatusDays = getKeepStatusDays();
			return keepStatusDays > 0 ? DateUtils.addDays(new Date(), -1 * keepStatusDays) : null;
		}

	}

}
