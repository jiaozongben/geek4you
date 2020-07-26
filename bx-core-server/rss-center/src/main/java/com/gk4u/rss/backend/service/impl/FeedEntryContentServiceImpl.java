package com.gk4u.rss.backend.service.impl;

import com.gk4u.rss.backend.entity.FeedEntryContent;
import com.gk4u.rss.backend.feed.FeedUtils;
import com.gk4u.rss.backend.mapper.FeedEntryContentMapper;
import com.gk4u.rss.backend.service.IFeedEntryContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author James Bond
 * @since 2020-07-26
 */
@Service
public class FeedEntryContentServiceImpl extends ServiceImpl<FeedEntryContentMapper, FeedEntryContent> implements IFeedEntryContentService {

    @Autowired
    FeedEntryContentMapper feedEntryContentDAO;


    @Override
    public FeedEntryContent findOrCreate(FeedEntryContent content, String baseUrl) {
        String contentHash = DigestUtils.sha1Hex(StringUtils.trimToEmpty(content.getContent()));
        String titleHash = DigestUtils.sha1Hex(StringUtils.trimToEmpty(content.getTitle()));
        Long existingId = feedEntryContentDAO.findExisting(contentHash, titleHash);

        FeedEntryContent result = null;
        if (existingId == null) {
            content.setContentHash(contentHash);
            content.setTitleHash(titleHash);

            content.setAuthor(FeedUtils.truncate(FeedUtils.handleContent(content.getAuthor(), baseUrl, true), 128));
            content.setTitle(FeedUtils.truncate(FeedUtils.handleContent(content.getTitle(), baseUrl, true), 2048));
            content.setContent(FeedUtils.handleContent(content.getContent(), baseUrl, false));
            result = content;

            saveOrUpdate(result);

        } else {
            result = new FeedEntryContent();
            result.setId(Math.toIntExact(existingId));
        }
        return result;
    }
}
