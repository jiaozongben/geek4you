package com.gk4u.rss.backend.service;

import com.commafeed.backend.model.FeedEntry;
import lombok.RequiredArgsConstructor;
import org.apache.commons.jexl2.*;
import org.apache.commons.jexl2.introspection.JexlMethod;
import org.apache.commons.jexl2.introspection.JexlPropertyGet;
import org.apache.commons.jexl2.introspection.Uberspect;
import org.apache.commons.jexl2.introspection.UberspectImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Year;
import java.util.concurrent.*;

@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
@Singleton
public class FeedEntryFilteringService {

	private static final JexlEngine ENGINE = initEngine();

	private static JexlEngine initEngine() {
		// classloader that prevents object creation
		ClassLoader cl = new ClassLoader() {
			@Override
			protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
				return null;
			}
		};

		// uberspect that prevents access to .class and .getClass()
		Uberspect uberspect = new UberspectImpl(LogFactory.getLog(JexlEngine.class)) {
			@Override
			public JexlPropertyGet getPropertyGet(Object obj, Object identifier, JexlInfo info) {
				if ("class".equals(identifier)) {
					return null;
				}
				return super.getPropertyGet(obj, identifier, info);
			}

			@Override
			public JexlMethod getMethod(Object obj, String method, Object[] args, JexlInfo info) {
				if ("getClass".equals(method)) {
					return null;
				}
				return super.getMethod(obj, method, args, info);
			}
		};

		JexlEngine engine = new JexlEngine(uberspect, null, null, null);
		engine.setStrict(true);
		engine.setClassLoader(cl);
		return engine;
	}

	private ExecutorService executor = Executors.newCachedThreadPool();

	public boolean filterMatchesEntry(String filter, FeedEntry entry) throws FeedEntryFilterException {
		if (StringUtils.isBlank(filter)) {
			return true;
		}

		Script script = null;
		try {
			script = ENGINE.createScript(filter);
		} catch (JexlException e) {
			throw new FeedEntryFilterException("Exception while parsing expression " + filter, e);
		}

		JexlContext context = new MapContext();
		context.set("title", entry.getContent().getTitle() == null ? "" : Jsoup.parse(entry.getContent().getTitle()).text().toLowerCase());
		context.set("author", entry.getContent().getAuthor() == null ? "" : entry.getContent().getAuthor().toLowerCase());
		context.set("content", entry.getContent().getContent() == null ? "" : Jsoup.parse(entry.getContent().getContent()).text()
				.toLowerCase());
		context.set("url", entry.getUrl() == null ? "" : entry.getUrl().toLowerCase());
		context.set("categories", entry.getContent().getCategories() == null ? "" : entry.getContent().getCategories().toLowerCase());

		context.set("year", Year.now().getValue());

		Callable<Object> callable = script.callable(context);
		Future<Object> future = executor.submit(callable);
		Object result = null;
		try {
			result = future.get(500, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new FeedEntryFilterException("interrupted while evaluating expression " + filter, e);
		} catch (ExecutionException e) {
			throw new FeedEntryFilterException("Exception while evaluating expression " + filter, e);
		} catch (TimeoutException e) {
			throw new FeedEntryFilterException("Took too long evaluating expression " + filter, e);
		}
		try {
			return (boolean) result;
		} catch (ClassCastException e) {
			throw new FeedEntryFilterException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("serial")
	public static class FeedEntryFilterException extends Exception {
		public FeedEntryFilterException(String message, Throwable t) {
			super(message, t);
		}
	}
}
