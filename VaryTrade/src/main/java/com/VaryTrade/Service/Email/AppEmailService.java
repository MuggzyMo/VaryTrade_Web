package com.VaryTrade.Service.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VaryTrade.Model.Email;

@Service
public class AppEmailService {
	@Autowired
	private EmailService emailService;
	
	public void sendEmail(Email email) {
		emailService.sendEmail(email);
	}
	
	public Email createForgotPasswordEmail(String recipient, String password) {
		return emailService.createForgotPasswordEmail(recipient, password);
	}
	
	public Email createDisableAccountEmail(String recipient) {
		return emailService.createDisableAccountEmail(recipient);
	}
	
}
