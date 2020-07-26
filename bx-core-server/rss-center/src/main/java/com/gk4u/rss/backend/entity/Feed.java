package com.gk4u.rss.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
public class Feed implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String url;

    @TableField("urlAfterRedirect")
    private String urlAfterRedirect;

    @TableField("normalizedUrl")
    private String normalizedUrl;

    @TableField("normalizedUrlHash")
    private String normalizedUrlHash;

    private String link;

    @TableField("lastUpdated")
    private LocalDateTime lastUpdated;

    @TableField("lastPublishedDate")
    private LocalDateTime lastPublishedDate;

    @TableField("lastEntryDate")
    private LocalDateTime lastEntryDate;

    private String message;

    @TableField("errorCount")
    private Integer errorCount;

    @TableField("disabledUntil")
    private LocalDateTime disabledUntil;

    @TableField("lastModifiedHeader")
    private String lastModifiedHeader;

    @TableField("etagHeader")
    private String etagHeader;

    @TableField("averageEntryInterval")
    private Long averageEntryInterval;

    @TableField("lastContentHash")
    private String lastContentHash;

    @TableField("pushHub")
    private String pushHub;

    @TableField("pushTopic")
    private String pushTopic;

    @TableField("pushTopicHash")
    private String pushTopicHash;

    @TableField("pushLastPing")
    private LocalDateTime pushLastPing;


}
