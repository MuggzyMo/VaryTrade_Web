package com.VaryTrade.Validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.VaryTrade.Model.WebCreateAccountPassword;

@Component
public class WebCreateAccountPasswordValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		WebCreateAccountPassword.class.isAssignableFrom(clazz);
		return false;
	}

	@Override
	public void validate(Object obj, Errors errors) {
		WebCreateAccountPassword webCreateAccountPassword = (WebCreateAccountPassword) obj;
		
		String password = webCreateAccountPassword.getPassword();
		String confirmPassword = webCreateAccountPassword.getConfirmPassword();		
		if(!password.equals(confirmPassword)) {
			errors.rejectValue("password", "mismatch.webCreateAccountPassword.password");
		}
		
	}

}
