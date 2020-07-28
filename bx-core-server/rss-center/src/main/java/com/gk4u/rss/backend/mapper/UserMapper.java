package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-07-28
 */
public interface UserMapper extends BaseMapper<User> {

    //通过用户姓名查询回来所有的姓名
    public User findByName(String name);

    //通过api查询回来用户
    public User findByApiKey(String key);

    //通过email查询回来用户
    public User findByEmail(String email);


}
