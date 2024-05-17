package com.VaryTrade.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebUserRegistrationApi;

@Component
public class WebUserRegistrationApiValidator {
	@Autowired
	private AppUserRepository apUserRepository;
	
	public boolean supports(Class<?> clazz) {
		return WebUserRegistrationApi.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object obj, Errors errors) {
		WebUserRegistrationApi webUserRegistrationApi = (WebUserRegistrationApi) obj;
		
		String password = webUserRegistrationApi.getPassword();
		String confirmPassword = webUserRegistrationApi.getConfirmPassword();		
		if(!password.equals(confirmPassword)) {
			errors.rejectValue("password", "mismatch.webUser.password");
		}		
		
		UserInfo userInfo = apUserRepository.retrieveUserInfoByUsername(webUserRegistrationApi.getUserName());
		if(userInfo != null) {
			errors.rejectValue("userName", "unique.webUser.userName");
		}
		
		userInfo = apUserRepository.retrieveUserInfoByEmail(webUserRegistrationApi.getEmail());
		if(userInfo != null) {
			errors.rejectValue("email", "unique.webUser.email");
		}
	}
}
