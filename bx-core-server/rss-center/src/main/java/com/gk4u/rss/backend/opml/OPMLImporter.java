package com.gk4u.rss.backend.opml;

import com.gk4u.rss.backend.entity.FeedCategory;
import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.feed.FeedUtils;
import com.gk4u.rss.backend.mapper.FeedCategoryMapper;
import com.gk4u.rss.backend.service.impl.FeedSubscriptionServiceImpl;
import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.io.WireFeedInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringReader;
import java.util.List;

import static com.sun.corba.se.impl.util.RepositoryId.cache;


@Slf4j


public class OPMLImporter {

    @Autowired
    private FeedCategoryMapper feedCategoryDAO;
    @Autowired
    private FeedSubscriptionServiceImpl feedSubscriptionService;


    public void importOpml(User user, String xml) {
        xml = xml.substring(xml.indexOf('<'));
        WireFeedInput input = new WireFeedInput();
        try {
            Opml feedSubscription = (Opml) input.build(new StringReader(xml));
            List<Outline> outlines = feedSubscription.getOutlines();
            for (int i = 0; i < outlines.size(); i++) {
                handleOutline(user, outlines.get(i), null, i);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private void handleOutline(User user, Outline outline, FeedCategory parent, int position) {
        List<Outline> children = outline.getChildren();
        if (CollectionUtils.isNotEmpty(children)) {
            String name = FeedUtils.truncate(outline.getText(), 128);
            if (name == null) {
                name = FeedUtils.truncate(outline.getTitle(), 128);
            }
            FeedCategory category = feedCategoryDAO.findByName(user, name);
            if (category == null) {
                if (StringUtils.isBlank(name)) {
                    name = "Unnamed category";
                }

                category = new FeedCategory();
                category.setName(name);

                category.setUserId(Long.valueOf(user.getId()));
                category.setPosition(position);
                feedCategoryDAO.insert(category);
            }

            for (int i = 0; i < children.size(); i++) {
                handleOutline(user, children.get(i), category, i);
            }
        } else {
            String name = FeedUtils.truncate(outline.getText(), 128);
            if (name == null) {
                name = FeedUtils.truncate(outline.getTitle(), 128);
            }
            if (StringUtils.isBlank(name)) {
                name = "Unnamed subscription";
            }
            // make sure we continue with the import process even if a feedSubscription failed
            try {
                feedSubscriptionService.subscribe(user, outline.getXmlUrl(), name, parent, position);
            } catch (Exception e) {
                log.error("error while importing {}: {}", outline.getXmlUrl(), e.getMessage());
            }
        }

    }
}
