package com.gk4u.rss.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author James Bond
 * @since 2020-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FeedEntryTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Long entryId;

    //feed对象
    private FeedEntry entry;

    //feed订阅的用户
    private User user;

    public FeedEntryTag(User user, FeedEntry entry, String name) {
        this.name = name;
        this.entry = entry;
        this.user = user;
    }


}
