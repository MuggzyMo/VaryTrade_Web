package com.VaryTrade.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebEditUserInfo;

@Component
public class WebEditUserInfoValidator implements Validator {
	@Autowired
	private AppUserRepository appUserRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return WebEditUserInfo.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		WebEditUserInfo webEditUserInfo = (WebEditUserInfo) obj;
		
		UserInfo userInfo = appUserRepository.retrieveUserInfoByEmail(webEditUserInfo.getEmail());
		if(userInfo != null && !webEditUserInfo.getOriginalEmail().equals(userInfo.getEmail())) {
			errors.rejectValue("email", "unique.webEditUserInfo.email");
		}
	}	
}
