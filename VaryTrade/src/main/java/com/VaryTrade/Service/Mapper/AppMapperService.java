package com.VaryTrade.Service.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VaryTrade.Model.WebUserRegistration;
import com.VaryTrade.Model.WebUserRegistrationApi;

@Service
public class AppMapperService {
	@Autowired
	private MapperService mapperService;
	
	public WebUserRegistration mapWebUserRegistrationApiToWebUserRegistration(WebUserRegistrationApi webUserRegistrationApi) {
		return mapperService.mapWebUserRegistrationApiToWebUserRegistration(webUserRegistrationApi);
	}
}
