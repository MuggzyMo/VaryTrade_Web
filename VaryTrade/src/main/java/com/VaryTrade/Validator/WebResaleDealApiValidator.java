package com.VaryTrade.Validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.VaryTrade.Model.WebResaleDealApi;

@Component
public class WebResaleDealApiValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return WebResaleDealApi.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		WebResaleDealApi webResaleDealApi = (WebResaleDealApi) obj;
		
		BigDecimal price = webResaleDealApi.getPrice();
		String stringPrice = price.toPlainString();
		int indexOfDecimal = stringPrice.indexOf('.');
		if(indexOfDecimal != -1) {
			if(stringPrice.length() - 1 - 2 != indexOfDecimal) {
				errors.rejectValue("price", "invalid.webResaleDealApi.price");
			}
		}
	}

}
