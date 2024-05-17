package com.VaryTrade.Validator;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.VaryTrade.Model.WebUserForgottenPassword;
import com.VaryTrade.Service.Captcha.AppCaptchaService;


@Component
public class WebUserForgottenPasswordValidator implements Validator{	
	@Autowired
	private AppCaptchaService appCaptchaService;

	@Override
	public boolean supports(Class<?> clazz) {
		return WebUserForgottenPassword.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		WebUserForgottenPassword webUserForgottenPassword = (WebUserForgottenPassword) obj;
		
		if(!appCaptchaService.isReCaptchaValid(webUserForgottenPassword.getRecaptchaToken())) {
			errors.rejectValue("recaptchaToken", "bot.webUserForgottenPassword.recaptchaToken");
		}
		
	}

}
