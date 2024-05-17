package com.VaryTrade.Service.Captcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppCaptchaService {
	@Autowired
	private CaptchaService captchaService;
	
	public boolean isReCaptchaValid(String recaptcha) {
		return captchaService.isReCaptchaValid(recaptcha);
	}
	
}
