package com.VaryTrade.Validator;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.VaryTrade.Model.WebHyperwalletRegistration;

@Component
public class WebHyperwalletRegistrationValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return WebHyperwalletRegistration.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		WebHyperwalletRegistration webHyperwalletRegistration = (WebHyperwalletRegistration) obj;
		
		LocalDate currentDate = LocalDate.now();
		LocalDate birthDate = webHyperwalletRegistration.getDateOfBirth().toLocalDate();
		int age = Period.between(birthDate, currentDate).getYears();
		
		if(age < 18) {
			errors.rejectValue("dateOfBirth", "age.webHyperwalletRegistration.dateOfBirth");
		}
		
	
		
	}

}
