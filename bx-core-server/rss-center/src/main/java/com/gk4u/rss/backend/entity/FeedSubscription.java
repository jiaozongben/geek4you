package com.gk4u.rss.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author James Bond
 * @since 2020-07-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FeedSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long entryId;

    private String title;

    private Long categoryId;

    private Integer position;

    private String filter;


}
