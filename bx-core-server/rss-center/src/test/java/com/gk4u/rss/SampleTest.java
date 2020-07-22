package com.gk4u.rss;

import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.mapper.UserMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setAge(123);
        user.setName("cy");
        user.setEmail("1131158493@qq.com");
        userMapper.insert(user);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(5L);
        user.setName("cyoking");
        user.setAge(24);
        int i = userMapper.updateById(user);
        System.out.println(i);
    }
    @Test
    public void delete() {
        userMapper.deleteById(1);    //删除一个
        userMapper.deleteBatchIds(Arrays.asList(2,3)); // 接受一个list，删除list内所有id的记录
    }

}