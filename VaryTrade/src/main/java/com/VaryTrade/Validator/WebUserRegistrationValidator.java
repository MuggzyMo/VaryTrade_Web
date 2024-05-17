package com.VaryTrade.Validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebUserRegistration;
import com.VaryTrade.Service.Captcha.AppCaptchaService;

@Component
public class WebUserRegistrationValidator implements Validator {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppCaptchaService appCaptchaService;
	
	public boolean supports(Class<?> clazz) {
		return WebUserRegistration.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj, Errors errors) {
		WebUserRegistration webUserRegistration = (WebUserRegistration) obj;
		
		String password = webUserRegistration.getPassword();
		String confirmPassword = webUserRegistration.getConfirmPassword();		
		if(!password.equals(confirmPassword)) {
			errors.rejectValue("password", "mismatch.webUser.password");
		}		
		
		UserInfo userInfo = appUserRepository.retrieveUserInfoByUsername(webUserRegistration.getUserName());
		if(userInfo != null) {
			errors.rejectValue("userName", "unique.webUser.userName");
		}
		
		userInfo = appUserRepository.retrieveUserInfoByEmail(webUserRegistration.getEmail());
		if(userInfo != null) {
			errors.rejectValue("email", "unique.webUser.email");
		}
		
		if(!appCaptchaService.isReCaptchaValid(webUserRegistration.getRecaptchaToken())) {
			errors.rejectValue("recaptchaToken", "bot.webUserRegistration.recaptchaToken");
		}
	}
	
	
}
