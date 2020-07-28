package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.entity.UserSetting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-07-28
 */
public interface UserSettingMapper extends BaseMapper<UserSetting> {

    //通过用户查询回来所有的用户信息配置
    public UserSetting findByUser(User user);
}
