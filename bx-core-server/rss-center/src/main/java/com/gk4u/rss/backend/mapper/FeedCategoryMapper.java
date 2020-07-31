package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.FeedCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk4u.rss.backend.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-07-28
 */
public interface FeedCategoryMapper extends BaseMapper<FeedCategory> {

    //根据用户查询所有订阅目录
    @Select("select * from feed_category  where user_id = #{user.id}")
    public List<FeedCategory> findAll(@Param("user") User user);

    //通过id查询目录
    @Select("select * from feed_category  where user_id = #{user.id} and id = #{id}")
    public FeedCategory findById(@Param("user") User user, @Param("id") Long id);

    //通过姓名查询目录
    @Select("select * from feed_category where user_id = #{user.id} and name = #{category.name}  ;")
    public FeedCategory findByName(@Param("user") User user, @Param("category") FeedCategory category);

    //通过parent查询目录


//    public List<FeedCategory> findByParent(@Param("user") User user, @Param("parent") FeedCategory parent);

    //查询所有子目录
//    public List<FeedCategory> findAllChildrenCategories(@Param("user") User user, @Param("parent") FeedCategory parent);


}
