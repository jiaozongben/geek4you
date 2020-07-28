package com.gk4u.rss.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author James Bond
 * @since 2020-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FeedSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long entryId;

    private String title;

    private Integer position;

    private String filter;


    //用户
    private User user;

    // feed
    private Feed feed;
    //获取目录
    private FeedCategory category;

    //状态
    private Set<FeedEntryStatus> statuses;
}
