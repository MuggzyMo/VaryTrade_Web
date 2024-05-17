package com.VaryTrade.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.VaryTrade.Model.WebTradeItem;
import com.VaryTrade.Service.Captcha.AppCaptchaService;

@Component
public class WebTradeItemValidator implements Validator{
	@Autowired
	private AppCaptchaService appCaptchaService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return WebTradeItem.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		WebTradeItem webTradeItem = (WebTradeItem) obj;
		
		System.out.println(webTradeItem.getRecaptchaToken());
		
		if(!appCaptchaService.isReCaptchaValid(webTradeItem.getRecaptchaToken())) {
			errors.rejectValue("recaptchaToken", "bot.webTradeItem.recaptchaToken");
		}
	}
}
