package com.gk4u.rss.backend.service.impl;

import com.gk4u.rss.backend.entity.FeedCategory;
import com.gk4u.rss.backend.entity.FeedSubscription;
import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.entity.vo.FeedCategoryVO;
import com.gk4u.rss.backend.mapper.FeedCategoryMapper;
import com.gk4u.rss.backend.service.IFeedCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author James Bond
 * @since 2020-07-28
 */
@Service
public class FeedCategoryServiceImpl extends ServiceImpl<FeedCategoryMapper, FeedCategory> implements IFeedCategoryService {

    @Autowired
    FeedSubscriptionServiceImpl feedSubscriptionService;

    //根据用户查询回来 各目录、各目录中的公众号
    public List<FeedCategoryVO> findAll(User user) {
        List<FeedCategory> feedCategoryList =  lambdaQuery().eq(FeedCategory::getUserId, user.getId()).list();
        List<FeedCategoryVO> feedCategoryVOList = new ArrayList<>();
        feedCategoryList.forEach( f ->
        {
            FeedCategoryVO feedCategoryVO = new FeedCategoryVO();

//            Set s = feedSubscriptionService.findByFeedCategory(user,f).stream().collect(Collectors.toSet());
            feedCategoryVO.setCurrent(f);
//            feedCategoryVO.setSubscriptions(s);
            feedCategoryVOList.add(feedCategoryVO);
        });

        return feedCategoryVOList;
    }


    public FeedCategory findById(User user, Long id) {

        return query().eq("user_id", user.getId()).eq("id", id).one();
    }


    public FeedCategory findByName(User user, String name, FeedCategory parent) {
        return query().eq("user_id", user.getId()).eq("name", name).eq("parent_id", parent.getId()).one();
    }


    public List<FeedCategory> findByParent(User user, FeedCategory parent) {

        return lambdaQuery()
                .eq(FeedCategory::getUserId, user.getId())
                .eq(FeedCategory::getParentId, parent.getId())
                .list();
    }


    public List<FeedCategory> findAllChildrenCategories(User user, FeedCategory parent) {

//        return findAll(user).stream().filter(c -> isChild(c.getCurrent(), parent)).collect(Collectors.toList());
        return null;
    }

    private boolean isChild(FeedCategory child, FeedCategory parent) {
        if (parent == null) {
            return true;
        }
        boolean isChild = false;
        while (child != null) {
            if (Objects.equals(child.getId(), parent.getId())) {
                isChild = true;
                break;
            }
//            child = child.getParent();
        }
        return isChild;
    }
}
