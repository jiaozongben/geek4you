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


}
