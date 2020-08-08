package com.gk4u.rss.opml;

import com.alibaba.fastjson.JSONObject;

import com.gk4u.rss.backend.entity.FeedSubscription;
import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.opml.OPMLExporter;
import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OPMLExporterTest {

    @Autowired
    OPMLExporter opmlExporter;

    private FeedSubscription newFeed(String url) {
        FeedSubscription feed = new FeedSubscription();
        feed.setUrl(url);
        return feed;
    }

    private FeedSubscription newFeedSubscription(String title, String url) {
        FeedSubscription feedSubscription = new FeedSubscription();
        feedSubscription.setTitle(title);
        return feedSubscription;
    }

    @Test
    public void generates_OPML_correctly() {
        User user = new User();
        user.setId(1);

        Opml opml = opmlExporter.export(user);

        System.out.println(JSONObject.toJSONString(opml));
    }

    private boolean containsCategory(List<Outline> outlines, String category) {
        for (Outline o : outlines)
            if (!"rss".equals(o.getType()))
                if (category.equals(o.getTitle()))
                    return true;

        return false;
    }

    private boolean containsFeed(List<Outline> outlines, String title, String url) {
        for (Outline o : outlines)
            if ("rss".equals(o.getType()))
                if (title.equals(o.getTitle()) && o.getAttributeValue("xmlUrl").equals(url))
                    return true;

        return false;
    }

    private Outline getCategoryOutline(List<Outline> outlines, String title) {
        for (Outline o : outlines)
            if (o.getTitle().equals(title))
                return o;

        return null;
    }
}