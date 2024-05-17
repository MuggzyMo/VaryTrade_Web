package com.VaryTrade.Service.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.VaryTrade.Dao.Misc.AppMiscRepository;
import com.VaryTrade.Model.Company;
import com.VaryTrade.Model.Email;

@Service
public class EmailServiceImpl implements EmailService {
	@Autowired
	AppMiscRepository appMiscRepository;
	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public void sendEmail(Email email) {
		SimpleMailMessage emailContent = new SimpleMailMessage();
		emailContent.setFrom(sender);
		emailContent.setTo(email.getRecipient());
		emailContent.setText(email.getMessage());
		emailContent.setSubject(email.getSubject());
		javaMailSender.send(emailContent);
	}
	
	@Override
	public Email createForgotPasswordEmail(String recipient, String password) {
		Email email = new Email();
		Company company = appMiscRepository.retrieveCompany();
		email.setRecipient(recipient);
		email.setMessage("Your new password is " + password + ". If you wish to change this password when you are signed in, you can update it from the account page.");
		email.setSubject("Forgotten Password for " + company.getName() + " application.");
		return email;
	}
	
	@Override
	public Email createDisableAccountEmail(String recipient) {
		Email email = new Email(recipient, "Your account has been locked due to too many failed login attempts. "
				+ "It will unlock in 24 hours. If these attempts were not you, contact us.", "Locked Account");
		return email;
	}
}
