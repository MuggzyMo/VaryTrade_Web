package com.VaryTrade.Service.Mapper;

import org.springframework.stereotype.Service;

import com.VaryTrade.Model.WebUserRegistration;
import com.VaryTrade.Model.WebUserRegistrationApi;

@Service
public interface MapperService {
	
	public WebUserRegistration mapWebUserRegistrationApiToWebUserRegistration(WebUserRegistrationApi webUserRegistrationApi);
}
