package com.VaryTrade.Service.Captcha;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.VaryTrade.Model.RecaptchaResponse;

@Service
public class CaptchaServiceImpl implements CaptchaService {
	@Value("${google.recaptcha.key.secret}")
	private String secretKey;	

	@Override
	public boolean isReCaptchaValid(String recaptcha) {
		RestTemplate restTemplate = new RestTemplate();
		URI recaptchaVerify = URI.create("https://www.google.com/recaptcha/api/siteverify?secret=" + secretKey + "&response=" + recaptcha);
		try {
			RecaptchaResponse recaptchaResponse = restTemplate.getForObject(recaptchaVerify, RecaptchaResponse.class);
			if(recaptchaResponse.isSuccess() && recaptchaResponse.getScore() > 0.5) {
				return true;
			}
			else {
				return false;
			}			
		}
		catch(RestClientException e) {
			return false;
		}
	}

}
