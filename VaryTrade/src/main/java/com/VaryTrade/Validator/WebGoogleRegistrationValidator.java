package com.VaryTrade.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebGoogleRegistration;

@Component
public class WebGoogleRegistrationValidator implements Validator {
	
	@Autowired
	private AppUserRepository appUserRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return WebGoogleRegistration.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		WebGoogleRegistration webGoogleRegistration = (WebGoogleRegistration) obj;
		
		UserInfo userInfo = appUserRepository.retrieveUserInfoByUsername(webGoogleRegistration.getUserName());
		if(userInfo != null) {
			errors.rejectValue("userName", "unique.webGoogleRegistration.userName");
		}
		
	}

}
