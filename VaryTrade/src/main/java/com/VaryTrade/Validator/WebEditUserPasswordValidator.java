package com.VaryTrade.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.WebEditUserPassword;

@Component
public class WebEditUserPasswordValidator implements Validator {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public boolean supports(Class<?> clazz) {
		return WebEditUserPassword.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		WebEditUserPassword webEditUserPassword = (WebEditUserPassword) obj;
		
		String password = webEditUserPassword.getPassword();
		String confirmPassword = webEditUserPassword.getConfirmPassword();		
		if(!password.equals(confirmPassword)) {
			errors.rejectValue("password", "mismatch.webEditUserPassword.password");
		}
		
		String webCurrentPassword = webEditUserPassword.getCurrentPassword();
		String currentPassword = appUserRepository.retrieveUserPassword(webEditUserPassword.getEmail());
		if(!passwordEncoder.matches(webCurrentPassword, currentPassword)) {
			errors.rejectValue("currentPassword", "incorrect.webEditUserPassword.currentPassword");
		}
	}

}
