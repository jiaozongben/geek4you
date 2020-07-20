package com.gk4u.rss.backend.service;


import com.gk4u.rss.backend.dao.FeedEntryDAO;
import com.gk4u.rss.backend.dao.FeedEntryTagDAO;
import com.gk4u.rss.backend.model.FeedEntry;
import com.gk4u.rss.backend.model.FeedEntryTag;
import com.gk4u.rss.backend.model.User;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
@Singleton
public class FeedEntryTagService {

	private final FeedEntryDAO feedEntryDAO;
	private final FeedEntryTagDAO feedEntryTagDAO;

	public void updateTags(User user, Long entryId, List<String> tagNames) {
		FeedEntry entry = feedEntryDAO.findById(entryId);
		if (entry == null) {
			return;
		}

		List<FeedEntryTag> existingTags = feedEntryTagDAO.findByEntry(user, entry);
		Set<String> existingTagNames = existingTags.stream().map(t -> t.getName()).collect(Collectors.toSet());

		List<FeedEntryTag> addList = tagNames.stream().filter(name -> !existingTagNames.contains(name))
				.map(name -> new FeedEntryTag(user, entry, name)).collect(Collectors.toList());
		List<FeedEntryTag> removeList = existingTags.stream().filter(tag -> !tagNames.contains(tag.getName())).collect(Collectors.toList());

		feedEntryTagDAO.saveOrUpdate(addList);
		feedEntryTagDAO.delete(removeList);
	}

}
