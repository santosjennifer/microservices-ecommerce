package com.github.ecommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.github.ecommerce.model.EmailMessage;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@PropertySource("classpath:env/mail.properties")
public class EmailService {
	
    @Value("${mail.smtp.username}")
    private String emailFrom;

	private JavaMailSender javaMailSender;
	
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void send(EmailMessage emailMessage) throws MessagingException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

		helper.setFrom(emailFrom);
		helper.setSubject(emailMessage.getSubject());
		helper.setText(emailMessage.getMessage(), true);
		helper.setTo(emailMessage.getTo());

		javaMailSender.send(mimeMessage);
	}
	
}
