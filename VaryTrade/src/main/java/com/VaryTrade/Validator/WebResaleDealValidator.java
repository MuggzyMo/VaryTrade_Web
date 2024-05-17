package com.VaryTrade.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


import com.VaryTrade.Model.WebResaleDeal;
import com.VaryTrade.Service.Captcha.AppCaptchaService;

@Component
public class WebResaleDealValidator implements Validator {
	@Autowired
	private AppCaptchaService appCaptchaService;

	@Override
	public boolean supports(Class<?> clazz) {
		return WebResaleDeal.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object obj, Errors errors) {
		WebResaleDeal webResaleDeal = (WebResaleDeal) obj;	
		
		if(!appCaptchaService.isReCaptchaValid(webResaleDeal.getRecaptchaToken())) {
			errors.rejectValue("recaptchaToken", "bot.webResaleDeal.recaptchaToken");
		}
	}
}
