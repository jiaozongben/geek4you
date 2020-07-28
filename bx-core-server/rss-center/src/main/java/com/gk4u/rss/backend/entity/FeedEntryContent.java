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
 * @since 2020-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FeedEntryContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long entryId;

    private String title;

    private String titleHash;

    private String content;

    private String contentHash;

    private String author;

    private String enclosureUrl;

    private String enclosureType;

    private String categories;

    //feed内容
    private Set<FeedEntry> entries;
}
