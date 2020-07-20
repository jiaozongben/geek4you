package com.gk4u.rss.backend.service;


import com.gk4u.rss.backend.CommaFeedConfiguration;
import com.gk4u.rss.backend.model.User;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Properties;

/**
 * Mailing service
 * 
 */
@RequiredArgsConstructor(onConstructor = @__({ @Inject }))
@Singleton
public class MailService {

	private final CommaFeedConfiguration config;

	public void sendMail(User user, String subject, String content) throws Exception {

		CommaFeedConfiguration settings = config;

		final String username = settings.getSmtpUserName();
		final String password = settings.getSmtpPassword();
		final String fromAddress = Optional.ofNullable(settings.getSmtpFromAddress()).orElse(settings.getSmtpUserName());

		String dest = user.getEmail();

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "" + settings.isSmtpTls());
		props.put("mail.smtp.host", settings.getSmtpHost());
		props.put("mail.smtp.port", "" + settings.getSmtpPort());

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(fromAddress, "CommaFeed"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(dest));
		message.setSubject("CommaFeed - " + subject);
		message.setContent(content, "text/html; charset=utf-8");

		Transport.send(message);

	}
}
