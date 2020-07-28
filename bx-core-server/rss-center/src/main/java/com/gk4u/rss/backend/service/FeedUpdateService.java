package com.gk4u.rss.backend.service;


import com.gk4u.rss.backend.entity.*;

import com.gk4u.rss.backend.mapper.FeedEntryMapper;
import com.gk4u.rss.backend.mapper.FeedEntryStatusMapper;
import com.gk4u.rss.backend.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Slf4j

@Service
public class FeedUpdateService {

    @Autowired
    FeedEntryMapper feedEntryDAO;
    @Autowired
    FeedEntryStatusMapper feedEntryStatusDAO;
    @Autowired
    private IFeedEntryService feedEntryService;
    @Autowired
    private IFeedEntryStatusService feedEntryStatusService;
    @Autowired
    private IFeedEntryContentService feedEntryContentService;
//    @Autowired
//    private FeedEntryFilteringService feedEntryFilteringService;

    /**
     * this is NOT thread-safe
     */
    public boolean addEntry(Feed feed, FeedEntry entry, List<FeedSubscription> subscriptions) {

        Long existing = feedEntryDAO.findExisting(entry.getGuid(), feed);
        if (existing != null) {
            return false;
        }

        FeedEntryContent content = feedEntryContentService.findOrCreate(entry.getContent(), feed.getLink());
        entry.setGuidHash(DigestUtils.sha1Hex(entry.getGuid()));
        entry.setContent(content);
        entry.setInserted(DateUtil.date2LocalDate(new Date()));
        entry.setFeed(feed);
        feedEntryService.saveOrUpdate(entry);

        // if filter does not match the entry, mark it as read
//        for (FeedSubscription sub : subscriptions) {
//            boolean matches = true;
//            try {
//                matches = feedEntryFilteringService.filterMatchesEntry(sub.getFilter(), entry);
//            } catch (FeedEntryFilterException e) {
//                log.error("could not evaluate filter {}", sub.getFilter(), e);
//            }
//            if (!matches) {
//                FeedEntryStatus status = new FeedEntryStatus(sub.getUser(), sub, entry);
//                status.setRead(true);
//                feedEntryStatusService.saveOrUpdate(status);
//            }
//        }

        return true;
    }
}
