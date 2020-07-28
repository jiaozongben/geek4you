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
public class FeedCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long userId;

    private Long feedId;

    private String name;

    private String parentId;

    private Boolean collapsed;

    private Integer position;

    //用户
    private User user;
    //父级目录
    private FeedCategory parent;
    //孩子节点
    private Set<FeedCategory> children;

    //订阅目录
    private Set<FeedSubscription> subscriptions;
}
