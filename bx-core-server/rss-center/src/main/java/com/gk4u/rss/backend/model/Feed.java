package com.gk4u.rss.backend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
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
 * @since 2020-07-25
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
    private LocalDate lastUpdated;

    @TableField("lastPublishedDate")
    private LocalDate lastPublishedDate;

    @TableField("lastEntryDate")
    private LocalDate lastEntryDate;

    private String message;

    @TableField("errorCount")
    private Integer errorCount;

    @TableField("disabledUntil")
    private LocalDate disabledUntil;

    @TableField("lastModifiedHeader")
    private String lastModifiedHeader;

    @TableField("etagHeader")
    private String etagHeader;

    @TableField("averageEntryInterval")
    private Integer averageEntryInterval;

    @TableField("lastContentHash")
    private String lastContentHash;

    @TableField("pushHub")
    private String pushHub;

    @TableField("pushTopic")
    private String pushTopic;

    @TableField("pushTopicHash")
    private String pushTopicHash;

    @TableField("pushLastPing")
    private LocalDate pushLastPing;


}
