package com.VaryTrade.Service.Mapper;

import org.springframework.stereotype.Service;

import com.VaryTrade.Model.WebUserRegistration;
import com.VaryTrade.Model.WebUserRegistrationApi;

@Service
public class MapperServiceImpl implements MapperService {
	
	@Override
	public WebUserRegistration mapWebUserRegistrationApiToWebUserRegistration(WebUserRegistrationApi webUserRegistrationApi) {
		WebUserRegistration webUserRegistration = new WebUserRegistration(webUserRegistrationApi.getEmail(), webUserRegistrationApi.getZipCode(), 
				webUserRegistrationApi.getState(), webUserRegistrationApi.getCity(), webUserRegistrationApi.getAddress(), webUserRegistrationApi.getPassword(), 
				webUserRegistrationApi.getConfirmPassword(), webUserRegistrationApi.getPhoneNum(), webUserRegistrationApi.getFirstName(),
				webUserRegistrationApi.getMiddleName(), webUserRegistrationApi.getLastName(), webUserRegistrationApi.getUserName(), null);
		return webUserRegistration;
	}
}
