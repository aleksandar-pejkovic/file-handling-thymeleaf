package com.practice.mailweb.feature.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender emailSender;

	public void send(String subject) {
		var message = new SimpleMailMessage();

		message.setFrom("pejko89.ap@gmail.com");
		message.setTo("pejko1989@live.com");
		message.setSubject(subject);
		message.setText("Event triggered!");
		emailSender.send(message);
		System.out.println("Mail sent!");
	}
}
