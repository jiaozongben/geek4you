package com.gk4u.rss.backend.service.impl;

import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.mapper.UserMapper;
import com.gk4u.rss.backend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author James Bond
 * @since 2020-08-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
