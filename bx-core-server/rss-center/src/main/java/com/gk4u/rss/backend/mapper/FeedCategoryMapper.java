package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.FeedCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk4u.rss.backend.entity.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-07-28
 */
public interface FeedCategoryMapper extends BaseMapper<FeedCategory> {

    //根据用户查询所有订阅目录
    public List<FeedCategory> findAll(User user);

    //通过id查询目录
    public FeedCategory findById(User user, Long id);

    //通过姓名查询目录
    public FeedCategory findByName(User user, String name, FeedCategory parent);

    //通过parent查询目录
    public List<FeedCategory> findByParent(User user, FeedCategory parent);

    //查询所有子目录
    public List<FeedCategory> findAllChildrenCategories(User user, FeedCategory parent);



}
