package com.gk4u.rss.backend;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.ResourceBundle;

@Getter
@Configuration  //证明这是一个配置类
@Component
@PropertySource(value = {"classpath:application.yml"}, ignoreResourceNotFound = true)//可以放多个,{}里面用,分开
public class CommaFeedConfiguration {
    @Value("${app.backgroundThreads}")
    private Integer backgroundThreads;

    @Value("${app.heavyLoad:true}")
    private Boolean heavyLoad;

    @Value("${app.refreshIntervalMinutes}")
    private Integer refreshIntervalMinutes;

    @Value("${app.maxFeedCapacity}")
    private Integer maxFeedCapacity;

    @Value("${app.databaseUpdateThreads}")
    private Integer databaseUpdateThreads;

    @Value("${app.publicUrl}")
    private String publicUrl;


}
