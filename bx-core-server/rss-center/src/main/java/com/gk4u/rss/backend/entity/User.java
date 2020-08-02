package com.gk4u.rss.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String email;

    private String password;

    @TableField("apiKey")
    private String apiKey;

    private Integer salt;

    private Boolean disabled;

    @TableField("lastLogin")
    private LocalDateTime lastLogin;

    private LocalDateTime created;

    @TableField("recoverPasswordToken")
    private String recoverPasswordToken;

    @TableField("recoverPasswordTokenDate")
    private LocalDateTime recoverPasswordTokenDate;

    @TableField("lastFullRefresh")
    private LocalDateTime lastFullRefresh;


}
