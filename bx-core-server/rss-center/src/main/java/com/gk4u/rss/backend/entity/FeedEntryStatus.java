package com.gk4u.rss.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
public class FeedEntryStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long userid;

    private Long entryId;

    private Boolean readStatus;

    private Boolean starred;

    private Boolean markable;

    private LocalDateTime inserted;

    private LocalDateTime updated;


}
