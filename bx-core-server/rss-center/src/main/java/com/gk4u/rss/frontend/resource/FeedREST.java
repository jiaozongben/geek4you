//package com.gk4u.rss.frontend.resource;
//
//import com.codahale.metrics.annotation.Timed;
//import com.commafeed.CommaFeedApplication;
//import com.commafeed.CommaFeedConfiguration;
//import com.commafeed.backend.cache.CacheService;
//import com.commafeed.backend.dao.FeedCategoryDAO;
//import com.commafeed.backend.dao.FeedEntryStatusDAO;
//import com.commafeed.backend.dao.FeedSubscriptionDAO;
//import com.commafeed.backend.favicon.AbstractFaviconFetcher.Favicon;
//import com.commafeed.backend.feed.*;
//import com.commafeed.backend.model.*;
//import com.commafeed.backend.model.UserSettings.ReadingMode;
//import com.commafeed.backend.model.UserSettings.ReadingOrder;
//import com.commafeed.backend.opml.OPMLExporter;
//import com.commafeed.backend.opml.OPMLImporter;
//import com.commafeed.backend.service.FeedEntryFilteringService;
//import com.commafeed.backend.service.FeedEntryFilteringService.FeedEntryFilterException;
//import com.commafeed.backend.service.FeedEntryService;
//import com.commafeed.backend.service.FeedService;
//import com.commafeed.backend.service.FeedSubscriptionService;
//import com.commafeed.frontend.auth.SecurityCheck;
//import com.commafeed.frontend.model.*;
//import com.commafeed.frontend.model.request.*;
//import com.gk4u.rss.backend.model.FeedEntry;
//import com.gk4u.rss.backend.model.FeedEntryContent;
//import com.google.common.base.Preconditions;
//import com.google.common.base.Throwables;
//import com.rometools.opml.feed.opml.Opml;
//import com.rometools.rome.feed.synd.SyndFeed;
//import com.rometools.rome.feed.synd.SyndFeedImpl;
//import com.rometools.rome.io.SyndFeedOutput;
//import com.rometools.rome.io.WireFeedOutput;
//import io.dropwizard.hibernate.UnitOfWork;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.glassfish.jersey.media.multipart.FormDataParam;
//
//import javax.inject.Inject;
//import javax.inject.Singleton;
//import javax.ws.rs.*;
//import javax.ws.rs.core.CacheControl;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.ResponseBuilder;
//import javax.ws.rs.core.Response.Status;
//import java.io.InputStream;
//import java.io.StringWriter;
//import java.net.URI;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Path("/feed")
//@Api(value = "/feed")
//@Slf4j
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
//@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
//@Singleton
//public class FeedREST {
//
//	private static final FeedEntry TEST_ENTRY = initTestEntry();
//
//	private static FeedEntry initTestEntry() {
//		FeedEntry entry = new FeedEntry();
//		entry.setUrl("https://github.com/Athou/commafeed");
//
//		FeedEntryContent content = new FeedEntryContent();
//		content.setAuthor("Athou");
//		content.setTitle("Merge pull request #662 from Athou/dw8");
//		content.setContent("Merge pull request #662 from Athou/dw8");
//		entry.setContent(content);
//		return entry;
//	}
//
//	private final FeedSubscriptionDAO feedSubscriptionDAO;
//	private final FeedCategoryDAO feedCategoryDAO;
//	private final FeedEntryStatusDAO feedEntryStatusDAO;
//	private final FeedFetcher feedFetcher;
//	private final FeedService feedService;
//	private final FeedEntryService feedEntryService;
//	private final FeedSubscriptionService feedSubscriptionService;
//	private final FeedEntryFilteringService feedEntryFilteringService;
//	private final FeedQueues queues;
//	private final OPMLImporter opmlImporter;
//	private final OPMLExporter opmlExporter;
//	private final CacheService cache;
//	private final CommaFeedConfiguration config;
//
//	@Path("/entries")
//	@GET
//	@UnitOfWork
//	@ApiOperation(value = "Get feed entries", notes = "Get a list of feed entries", response = Entries.class)
//	@Timed
//	public Response getFeedEntries(@ApiParam(hidden = true) @SecurityCheck User user,
//                                   @ApiParam(value = "id of the feed", required = true) @QueryParam("id") String id,
//                                   @ApiParam(
//					value = "all entries or only unread ones",
//					allowableValues = "all,unread",
//					required = true) @DefaultValue("unread") @QueryParam("readType") ReadingMode readType,
//                                   @ApiParam(value = "only entries newer than this") @QueryParam("newerThan") Long newerThan,
//                                   @ApiParam(value = "offset for paging") @DefaultValue("0") @QueryParam("offset") int offset,
//                                   @ApiParam(value = "limit for paging, default 20, maximum 1000") @DefaultValue("20") @QueryParam("limit") int limit,
//                                   @ApiParam(
//					value = "ordering",
//					allowableValues = "asc,desc,abc,zyx") @QueryParam("order") @DefaultValue("desc") ReadingOrder order,
//                                   @ApiParam(
//					value = "search for keywords in either the title or the content of the entries, separated by spaces, 3 characters minimum") @QueryParam("keywords") String keywords,
//                                   @ApiParam(value = "return only entry ids") @DefaultValue("false") @QueryParam("onlyIds") boolean onlyIds) {
//
//		Preconditions.checkNotNull(id);
//		Preconditions.checkNotNull(readType);
//
//		keywords = StringUtils.trimToNull(keywords);
//		Preconditions.checkArgument(keywords == null || StringUtils.length(keywords) >= 3);
//		List<FeedEntryKeyword> entryKeywords = FeedEntryKeyword.fromQueryString(keywords);
//
//		limit = Math.min(limit, 1000);
//		limit = Math.max(0, limit);
//
//		Entries entries = new Entries();
//		entries.setOffset(offset);
//		entries.setLimit(limit);
//
//		boolean unreadOnly = readType == ReadingMode.unread;
//
//		Date newerThanDate = newerThan == null ? null : new Date(newerThan);
//
//		FeedSubscription subscription = feedSubscriptionDAO.findById(user, Long.valueOf(id));
//		if (subscription != null) {
//			entries.setName(subscription.getTitle());
//			entries.setMessage(subscription.getFeed().getMessage());
//			entries.setErrorCount(subscription.getFeed().getErrorCount());
//			entries.setFeedLink(subscription.getFeed().getLink());
//
//			List<FeedEntryStatus> list = feedEntryStatusDAO.findBySubscriptions(user, Arrays.asList(subscription), unreadOnly,
//					entryKeywords, newerThanDate, offset, limit + 1, order, true, onlyIds, null);
//
//			for (FeedEntryStatus status : list) {
//				entries.getEntries().add(Entry.build(status, config.getPublicUrl(),
//						config.getImageProxyEnabled()));
//			}
//
//			boolean hasMore = entries.getEntries().size() > limit;
//			if (hasMore) {
//				entries.setHasMore(true);
//				entries.getEntries().remove(entries.getEntries().size() - 1);
//			}
//		} else {
//			return Response.status(Status.NOT_FOUND).entity("<message>feed not found</message>").build();
//		}
//
//		entries.setTimestamp(System.currentTimeMillis());
//		entries.setIgnoredReadStatus(keywords != null);
//		FeedUtils.removeUnwantedFromSearch(entries.getEntries(), entryKeywords);
//		return Response.ok(entries).build();
//	}
//
//	@Path("/entriesAsFeed")
//	@GET
//	@UnitOfWork
//	@ApiOperation(value = "Get feed entries as a feed", notes = "Get a feed of feed entries")
//	@Produces(MediaType.APPLICATION_XML)
//	@Timed
//	public Response getFeedEntriesAsFeed(@ApiParam(hidden = true) @SecurityCheck(apiKeyAllowed = true) User user,
//                                         @ApiParam(value = "id of the feed", required = true) @QueryParam("id") String id,
//                                         @ApiParam(
//					value = "all entries or only unread ones",
//					allowableValues = "all,unread",
//					required = true) @DefaultValue("all") @QueryParam("readType") ReadingMode readType,
//                                         @ApiParam(value = "only entries newer than this") @QueryParam("newerThan") Long newerThan,
//                                         @ApiParam(value = "offset for paging") @DefaultValue("0") @QueryParam("offset") int offset,
//                                         @ApiParam(value = "limit for paging, default 20, maximum 1000") @DefaultValue("20") @QueryParam("limit") int limit,
//                                         @ApiParam(value = "date ordering", allowableValues = "asc,desc") @QueryParam("order") @DefaultValue("desc") ReadingOrder order,
//                                         @ApiParam(
//					value = "search for keywords in either the title or the content of the entries, separated by spaces, 3 characters minimum") @QueryParam("keywords") String keywords,
//                                         @ApiParam(value = "return only entry ids") @DefaultValue("false") @QueryParam("onlyIds") boolean onlyIds) {
//
//		Response response = getFeedEntries(user, id, readType, newerThan, offset, limit, order, keywords, onlyIds);
//		if (response.getStatus() != Status.OK.getStatusCode()) {
//			return response;
//		}
//		Entries entries = (Entries) response.getEntity();
//
//		SyndFeed feed = new SyndFeedImpl();
//		feed.setFeedType("rss_2.0");
//		feed.setTitle("CommaFeed - " + entries.getName());
//		feed.setDescription("CommaFeed - " + entries.getName());
//		feed.setLink(config.getPublicUrl());
//		feed.setEntries(entries.getEntries().stream().map(e -> e.asRss()).collect(Collectors.toList()));
//
//		SyndFeedOutput output = new SyndFeedOutput();
//		StringWriter writer = new StringWriter();
//		try {
//			output.output(feed, writer);
//		} catch (Exception e) {
//			writer.write("Could not get feed information");
//			log.error(e.getMessage(), e);
//		}
//		return Response.ok(writer.toString()).build();
//	}
//
//	private FeedInfo fetchFeedInternal(String url) {
//		FeedInfo info = null;
//		url = StringUtils.trimToEmpty(url);
//		url = prependHttp(url);
//		try {
//			FetchedFeed feed = feedFetcher.fetch(url, true, null, null, null, null);
//			info = new FeedInfo();
//			info.setUrl(feed.getUrlAfterRedirect());
//			info.setTitle(feed.getTitle());
//
//		} catch (Exception e) {
//			log.debug(e.getMessage(), e);
//			throw new WebApplicationException(e, Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
//		}
//		return info;
//	}
//
//	@POST
//	@Path("/fetch")
//	@UnitOfWork
//	@ApiOperation(value = "Fetch a feed", notes = "Fetch a feed by its url", response = FeedInfo.class)
//	@Timed
//	public Response fetchFeed(@ApiParam(hidden = true) @SecurityCheck User user,
//                              @ApiParam(value = "feed url", required = true) FeedInfoRequest req) {
//		Preconditions.checkNotNull(req);
//		Preconditions.checkNotNull(req.getUrl());
//
//		FeedInfo info = null;
//		try {
//			info = fetchFeedInternal(req.getUrl());
//		} catch (Exception e) {
//			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(Throwables.getStackTraceAsString(Throwables.getRootCause(e)))
//					.type(MediaType.TEXT_PLAIN).build();
//		}
//		return Response.ok(info).build();
//	}
//
//	@Path("/refreshAll")
//	@GET
//	@UnitOfWork
//	@ApiOperation(value = "Queue all feeds of the user for refresh", notes = "Manually add all feeds of the user to the refresh queue")
//	@Timed
//	public Response queueAllForRefresh(@ApiParam(hidden = true) @SecurityCheck User user) {
//		feedSubscriptionService.refreshAll(user);
//		return Response.ok().build();
//	}
//
//	@Path("/refresh")
//	@POST
//	@UnitOfWork
//	@ApiOperation(value = "Queue a feed for refresh", notes = "Manually add a feed to the refresh queue")
//	@Timed
//	public Response queueForRefresh(@ApiParam(hidden = true) @SecurityCheck User user,
//                                    @ApiParam(value = "Feed id", required = true) IDRequest req) {
//
//		Preconditions.checkNotNull(req);
//		Preconditions.checkNotNull(req.getId());
//
//		FeedSubscription sub = feedSubscriptionDAO.findById(user, req.getId());
//		if (sub != null) {
//			Feed feed = sub.getFeed();
//			queues.add(feed, true);
//			return Response.ok().build();
//		}
//		return Response.ok(Status.NOT_FOUND).build();
//	}
//
//	@Path("/mark")
//	@POST
//	@UnitOfWork
//	@ApiOperation(value = "Mark feed entries", notes = "Mark feed entries as read (unread is not supported)")
//	@Timed
//	public Response markFeedEntries(@ApiParam(hidden = true) @SecurityCheck User user,
//                                    @ApiParam(value = "Mark request", required = true) MarkRequest req) {
//		Preconditions.checkNotNull(req);
//		Preconditions.checkNotNull(req.getId());
//
//		Date olderThan = req.getOlderThan() == null ? null : new Date(req.getOlderThan());
//		String keywords = req.getKeywords();
//		List<FeedEntryKeyword> entryKeywords = FeedEntryKeyword.fromQueryString(keywords);
//
//		FeedSubscription subscription = feedSubscriptionDAO.findById(user, Long.valueOf(req.getId()));
//		if (subscription != null) {
//			feedEntryService.markSubscriptionEntries(user, Arrays.asList(subscription), olderThan, entryKeywords);
//		}
//		return Response.ok().build();
//	}
//
//	@GET
//	@Path("/get/{id}")
//	@UnitOfWork
//	@ApiOperation(value = "get feed", response = Subscription.class)
//	@Timed
//	public Response getFeed(@ApiParam(hidden = true) @SecurityCheck User user,
//                            @ApiParam(value = "user id", required = true) @PathParam("id") Long id) {
//
//		Preconditions.checkNotNull(id);
//		FeedSubscription sub = feedSubscriptionDAO.findById(user, id);
//		if (sub == null) {
//			return Response.status(Status.NOT_FOUND).build();
//		}
//		UnreadCount unreadCount = feedSubscriptionService.getUnreadCount(user).get(id);
//		return Response.ok(Subscription.build(sub, config.getPublicUrl(), unreadCount)).build();
//	}
//
//	@GET
//	@Path("/favicon/{id}")
//	@UnitOfWork
//	@ApiOperation(value = "Fetch a feed's icon", notes = "Fetch a feed's icon")
//	@Timed
//	public Response getFeedFavicon(@ApiParam(hidden = true) @SecurityCheck User user,
//                                   @ApiParam(value = "subscription id", required = true) @PathParam("id") Long id) {
//
//		Preconditions.checkNotNull(id);
//		FeedSubscription subscription = feedSubscriptionDAO.findById(user, id);
//		if (subscription == null) {
//			return Response.status(Status.NOT_FOUND).build();
//		}
//		Feed feed = subscription.getFeed();
//		Favicon icon = feedService.fetchFavicon(feed);
//
//		ResponseBuilder builder = Response.ok(icon.getIcon(), Optional.ofNullable(icon.getMediaType()).orElse("image/x-icon"));
//
//		CacheControl cacheControl = new CacheControl();
//		cacheControl.setMaxAge(2592000);
//		cacheControl.setPrivate(false);
//		builder.cacheControl(cacheControl);
//
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.MONTH, 1);
//		builder.expires(calendar.getTime());
//		builder.lastModified(CommaFeedApplication.STARTUP_TIME);
//
//		return builder.build();
//	}
//
//	@POST
//	@Path("/subscribe")
//	@UnitOfWork
//	@ApiOperation(value = "Subscribe to a feed", notes = "Subscribe to a feed")
//	@Timed
//	public Response subscribe(@ApiParam(hidden = true) @SecurityCheck User user,
//                              @ApiParam(value = "subscription request", required = true) SubscribeRequest req) {
//		Preconditions.checkNotNull(req);
//		Preconditions.checkNotNull(req.getTitle());
//		Preconditions.checkNotNull(req.getUrl());
//
//		String url = prependHttp(req.getUrl());
//		try {
//			url = fetchFeedInternal(url).getUrl();
//
//			FeedCategory category = null;
//			if (req.getCategoryId() != null && !CategoryREST.ALL.equals(req.getCategoryId())) {
//				category = feedCategoryDAO.findById(Long.valueOf(req.getCategoryId()));
//			}
//			FeedInfo info = fetchFeedInternal(url);
//			feedSubscriptionService.subscribe(user, info.getUrl(), req.getTitle(), category);
//		} catch (Exception e) {
//			log.error("Failed to subscribe to URL {}: {}", url, e.getMessage(), e);
//			return Response.status(Status.SERVICE_UNAVAILABLE).entity("Failed to subscribe to URL " + url + ": " + e.getMessage()).build();
//		}
//		return Response.ok().build();
//	}
//
//	@GET
//	@Path("/subscribe")
//	@UnitOfWork
//	@ApiOperation(value = "Subscribe to a feed", notes = "Subscribe to a feed")
//	@Timed
//	public Response subscribeFromUrl(@ApiParam(hidden = true) @SecurityCheck User user,
//                                     @ApiParam(value = "feed url", required = true) @QueryParam("url") String url) {
//
//		try {
//			Preconditions.checkNotNull(url);
//
//			url = prependHttp(url);
//			url = fetchFeedInternal(url).getUrl();
//
//			FeedInfo info = fetchFeedInternal(url);
//			feedSubscriptionService.subscribe(user, info.getUrl(), info.getTitle());
//		} catch (Exception e) {
//			log.info("Could not subscribe to url {} : {}", url, e.getMessage());
//		}
//		return Response.temporaryRedirect(URI.create(config.getPublicUrl())).build();
//	}
//
//	private String prependHttp(String url) {
//		if (!url.startsWith("http")) {
//			url = "http://" + url;
//		}
//		return url;
//	}
//
//}
