package com.VaryTrade.Configuration;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.VaryTrade.Dao.ResaleDeal.AppResaleDealRepository;
import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.ClosedResaleDeal;
import com.VaryTrade.Service.ResaleDeal.AppResaleDealService;

@Component
public class ClosedResaleDealAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppResaleDealRepository appResaleDealRepository;
	@Autowired
	private AppResaleDealService appResaleDealService;
	
	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
		String authority = appUserRepository.retrieveUserRole(authentication.get().getName());
		if(!authority.equals("ROLE_USER")) {
			return new AuthorizationDecision(false);
		}
		else {
			int closedResaleDealId = Integer.valueOf(context.getRequest().getParameter("id"));
			ClosedResaleDeal closedResaleDeal = appResaleDealRepository.retrieveClosedResaleDeal(closedResaleDealId);
			boolean authorize = closedResaleDeal != null && appResaleDealService.doesClosedResaleDealBelongToCollector(closedResaleDeal, authentication.get().getName());
			
			return new AuthorizationDecision(authorize);
		}	
	}

}
