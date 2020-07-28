package com.gk4u.rss.backend.service.impl;

import com.gk4u.rss.backend.entity.UserSetting;
import com.gk4u.rss.backend.mapper.UserSettingMapper;
import com.gk4u.rss.backend.service.IUserSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author James Bond
 * @since 2020-07-28
 */
@Service
public class UserSettingServiceImpl extends ServiceImpl<UserSettingMapper, UserSetting> implements IUserSettingService {

}
