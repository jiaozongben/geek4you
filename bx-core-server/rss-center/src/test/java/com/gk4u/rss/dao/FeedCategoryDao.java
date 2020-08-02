//package com.gk4u.rss.dao;
//
//
//import com.gk4u.rss.backendbak.entity.FeedCategory;
//import com.gk4u.rss.backendbak.entity.User;
//import com.gk4u.rss.backendbak.mapper.FeedCategoryMapper;
//import com.gk4u.rss.backendbak.service.IFeedCategoryService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class FeedCategoryDao {
//
//
//    @Autowired
//    FeedCategoryMapper feedCategoryMapper;
//    @Autowired
//    IFeedCategoryService feedCategoryService;
//
//    @Test
//    public void findAllchaindaoTest() {
//        User user = new User();
//        user.setId(124);
////        List<FeedCategory> feedCategories = feedCategoryService.findAll(user);
////        feedCategories.forEach(e -> System.out.println(e.toString()));
//    }
//
//    @Test
//    public void findAlldaoTest() {
//        User user = new User();
//        user.setId(123);
//        List<FeedCategory> feedCategories = feedCategoryMapper.findAll(user);
//        feedCategories.forEach(e -> System.out.println(e.toString()));
//    }
//
//
//    @Test
//    public void findByIdTest() {
//        User user = new User();
//        user.setId(123);
//        Long id = 123L;
//
////        FeedCategory feedCategorie = feedCategoryMapper.findById(user, id);
////
////        System.out.println(feedCategorie.toString());
//    }
//
//    @Test
//    public void findByNameTest() {
//        User user = new User();
//        user.setId(124);
//
//        String categoryName = "可爱的你";
//        FeedCategory parent = new FeedCategory();
//        parent.setName("可爱的你");
//        FeedCategory feedCategory = feedCategoryMapper.findByName(user, parent);
//        System.out.println(feedCategory.toString());
//    }
//
//}
