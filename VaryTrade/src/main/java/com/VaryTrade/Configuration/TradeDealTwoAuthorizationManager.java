package com.VaryTrade.Configuration;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.VaryTrade.Dao.TradeDeal.AppTradeDealRepository;
import com.VaryTrade.Dao.User.AppUserRepository;

@Component
public class TradeDealTwoAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppTradeDealRepository appTradeDealRepository;
	
	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
		ArrayList<String> collectibleTypes = appTradeDealRepository.retrieveCollectibleTypeNames();
		String authority = appUserRepository.retrieveUserRole(authentication.get().getName());
		if(!authority.equals("ROLE_USER")) {
			return new AuthorizationDecision(false);
		}
		else if(collectibleTypes.size() < 2) {
			return new AuthorizationDecision(false);	
		}
		else {
			return new AuthorizationDecision(true);
		}
	}

}
