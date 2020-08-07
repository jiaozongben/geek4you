package com.gk4u.rss.opml;


import com.gk4u.rss.backend.entity.FeedCategory;
import com.gk4u.rss.backend.entity.User;
import com.gk4u.rss.backend.mapper.FeedCategoryMapper;
import com.gk4u.rss.backend.opml.OPMLImporter;
import com.gk4u.rss.backend.service.IFeedSubscriptionService;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OPMLImporterTest {
    @Autowired
    OPMLImporter opmlImporter;

    @Test
    public void testOpmlSubscriptions() throws IOException {
        testOpmlVersion("/opml/Subscriptions.opml");
    }

    @Test
    public void testOpmlV10() throws IOException {
        testOpmlVersion("/opml/opml_v1.0.xml");
    }

    @Test
    public void testOpmlV11() throws IOException {
        testOpmlVersion("/opml/opml_v1.1.xml");
    }

    @Test
    public void testOpmlV20() throws IOException {
        testOpmlVersion("/opml/opml_v2.0.xml");
    }

    @Test
    public void testOpmlNoVersion() throws IOException {
        testOpmlVersion("/opml/opml_noversion.xml");
    }

    private void testOpmlVersion(String fileName) throws IOException {

        User user = new User();
        user.setId(1);
        InputStream input = getClass().getResourceAsStream(fileName);
        String xml = IOUtils.toString(input);
        opmlImporter.importOpml(user, xml);
    }

}
