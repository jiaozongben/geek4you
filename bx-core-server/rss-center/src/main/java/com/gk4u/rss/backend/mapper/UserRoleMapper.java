package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-07-28
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    //查询所有的角色
    public List<UserRole> findAll();

    //通过用户查询回来所有的角色
    public List<UserRole> findAll(User user);

}
