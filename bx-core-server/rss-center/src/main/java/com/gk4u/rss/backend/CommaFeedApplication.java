//package com.gk4u.rss.backend;
//
//
//import com.gk4u.rss.backend.feed.FeedRefreshUpdater;
//import com.gk4u.rss.backend.feed.FeedRefreshWorker;
//import com.gk4u.rss.backend.model.*;
//import com.gk4u.rss.backend.service.StartupService;
//import com.gk4u.rss.backend.service.UserService;
//import com.google.inject.Guice;
//import com.google.inject.Injector;
//import com.google.inject.Key;
//import com.google.inject.TypeLiteral;
//import io.dropwizard.Application;
//import io.dropwizard.assets.AssetsBundle;
//import io.dropwizard.db.DataSourceFactory;
//import io.dropwizard.forms.MultiPartBundle;
//import io.dropwizard.hibernate.HibernateBundle;
//import io.dropwizard.migrations.MigrationsBundle;
//import io.dropwizard.server.DefaultServerFactory;
//import io.dropwizard.servlets.CacheBustingFilter;
//import io.dropwizard.setup.Bootstrap;
//import io.dropwizard.setup.Environment;
//import org.hibernate.cfg.AvailableSettings;
//import org.springframework.scheduling.config.ScheduledTask;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.util.Date;
//import java.util.EnumSet;
//import java.util.Set;
//import java.util.concurrent.ScheduledExecutorService;
//
//public class CommaFeedApplication extends Application<CommaFeedConfiguration> {
//
//	public static final String USERNAME_ADMIN = "admin";
//	public static final String USERNAME_DEMO = "demo";
//
//	public static final Date STARTUP_TIME = new Date();
//
//	private HibernateBundle<CommaFeedConfiguration> hibernateBundle;
//
//	@Override
//	public String getName() {
//		return "CommaFeed";
//	}
//
//	@Override
//	public void initialize(Bootstrap<CommaFeedConfiguration> bootstrap) {
//		bootstrap.addBundle(hibernateBundle = new HibernateBundle<CommaFeedConfiguration>(AbstractModel.class, Feed.class,
//				FeedCategory.class, FeedEntry.class, FeedEntryContent.class, FeedEntryStatus.class, FeedEntryTag.class,
//				FeedSubscription.class, User.class, UserRole.class, UserSettings.class) {
//			@Override
//			public DataSourceFactory getDataSourceFactory(CommaFeedConfiguration configuration) {
//				DataSourceFactory factory = configuration.getDataSourceFactory();
//
//				// keep using old id generator for backward compatibility
//				factory.getProperties().put(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, "false");
//
//				factory.getProperties().put(AvailableSettings.STATEMENT_BATCH_SIZE, "50");
//				factory.getProperties().put(AvailableSettings.BATCH_VERSIONED_DATA, "true");
//				return factory;
//			}
//		});
//
//		bootstrap.addBundle(new MigrationsBundle<CommaFeedConfiguration>() {
//			@Override
//			public DataSourceFactory getDataSourceFactory(CommaFeedConfiguration configuration) {
//				return configuration.getDataSourceFactory();
//			}
//		});
//
//		bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
//		bootstrap.addBundle(new MultiPartBundle());
//	}
//
//	@Override
//	public void run(CommaFeedConfiguration config, Environment environment) throws Exception {
//		// guice init
//		Injector injector = Guice.createInjector(new CommaFeedModule(hibernateBundle.getSessionFactory(), config, environment.metrics()));
//
//		// session management
//		environment.servlets().setSessionHandler(config.getSessionHandlerFactory().build());
//
//		// support for "@SecurityCheck User user" injection
//		environment.jersey().register(new SecurityCheckFactoryProvider.Binder(injector.getInstance(UserService.class)));
//		// support for "@Context SessionHelper sessionHelper" injection
//		environment.jersey().register(new SessionHelperFactoryProvider.Binder());
//
//		// REST resources
//		environment.jersey().setUrlPattern("/rest/*");
//		((DefaultServerFactory) config.getServerFactory()).setJerseyRootPath("/rest/*");
//		environment.jersey().register(injector.getInstance(AdminREST.class));
//		environment.jersey().register(injector.getInstance(CategoryREST.class));
//		environment.jersey().register(injector.getInstance(EntryREST.class));
//		environment.jersey().register(injector.getInstance(FeedREST.class));
//		environment.jersey().register(injector.getInstance(PubSubHubbubCallbackREST.class));
//		environment.jersey().register(injector.getInstance(ServerREST.class));
//		environment.jersey().register(injector.getInstance(UserREST.class));
//
//		// Servlets
//		environment.servlets().addServlet("next", injector.getInstance(NextUnreadServlet.class)).addMapping("/next");
//		environment.servlets().addServlet("logout", injector.getInstance(LogoutServlet.class)).addMapping("/logout");
//		environment.servlets().addServlet("customCss", injector.getInstance(CustomCssServlet.class)).addMapping("/custom_css.css");
//		environment.servlets().addServlet("analytics.js", injector.getInstance(AnalyticsServlet.class)).addMapping("/analytics.js");
//
//		// Scheduled tasks
//		Set<ScheduledTask> tasks = injector.getInstance(Key.get(new TypeLiteral<Set<ScheduledTask>>() {
//		}));
//		ScheduledExecutorService executor = environment.lifecycle().scheduledExecutorService("task-scheduler", true).threads(tasks.size())
//				.build();
//		for (ScheduledTask task : tasks) {
//			task.register(executor);
//		}
//
//		// database init/changelogs
//		environment.lifecycle().manage(injector.getInstance(StartupService.class));
//
//		// background feed fetching
//		environment.lifecycle().manage(injector.getInstance(FeedRefreshTaskGiver.class));
//		environment.lifecycle().manage(injector.getInstance(FeedRefreshWorker.class));
//		environment.lifecycle().manage(injector.getInstance(FeedRefreshUpdater.class));
//
//		// cache configuration
//		// prevent caching on REST resources, except for favicons
//		environment.servlets().addFilter("cache-filter", new CacheBustingFilter() {
//			@Override
//			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//				String path = ((HttpServletRequest) request).getRequestURI();
//				if (path.contains("/feed/favicon")) {
//					chain.doFilter(request, response);
//				} else {
//					super.doFilter(request, response, chain);
//				}
//			}
//		}).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/rest/*");
//
//	}
//
//	public static void main(String[] args) throws Exception {
//		new CommaFeedApplication().run(args);
//	}
//}
