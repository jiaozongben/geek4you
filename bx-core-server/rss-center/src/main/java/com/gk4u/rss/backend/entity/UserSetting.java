package com.gk4u.rss.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author James Bond
 * @since 2020-07-28
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserSetting implements Serializable {
    public enum ReadingMode {
        all, unread
    }

    public enum ReadingOrder {
        asc, desc, abc, zyx
    }

    public enum ViewMode {
        title, expanded
    }
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userId;


}
