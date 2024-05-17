package com.VaryTrade.Service.Email;

import org.springframework.stereotype.Service;

import com.VaryTrade.Model.Email;

@Service
public interface EmailService {
	public void sendEmail(Email email);
	public Email createForgotPasswordEmail(String recipient, String password);
	public Email createDisableAccountEmail(String recipient);
}
