package com.gk4u.rss.backend.mapper;

import com.gk4u.rss.backend.entity.FeedCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gk4u.rss.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author James Bond
 * @since 2020-08-02
 */
@Mapper
public interface FeedCategoryMapper extends BaseMapper<FeedCategory> {

    @Select("SELECT * FROM `feed_category` \n" +
            "where user_id = #{user.id} and name=#{name};")
    FeedCategory findByName(@Param("user") User user, @Param("name") String name);
}
