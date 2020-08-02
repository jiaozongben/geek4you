package com.gk4u.rss.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author James Bond
 * @since 2020-07-26
 */
@Data
@Getter
@Setter
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

    //订阅目录
    private FeedSubscription subscription;

    //feed订阅内容
    private FeedEntry entry;

    //标签
    private List<FeedEntryTag> tags = new ArrayList<>();

    //用户
    private User user;

    public FeedEntryStatus(User user, FeedSubscription subscription, FeedEntry entry) {
        setUser(user);
        setSubscription(subscription);
        setEntry(entry);
        setInserted(entry.getInserted());
        setUpdated(entry.getUpdated());
    }

}
