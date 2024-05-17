package com.VaryTrade.Service.Captcha;

import org.springframework.stereotype.Service;


@Service
public interface CaptchaService {
	public boolean isReCaptchaValid(String recaptcha);
}
