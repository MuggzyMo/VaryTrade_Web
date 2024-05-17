package com.VaryTrade.Configuration;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import com.VaryTrade.Dao.TradeDeal.AppTradeDealRepository;
import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.OpenTradeDeal;
import com.VaryTrade.Service.TradeDeal.AppTradeDealService;

@Component
public class CollectorOpenTradeDealAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppTradeDealRepository appTradeDealRepository;
	@Autowired
	private AppTradeDealService appTradeDealService;

	@Override
	public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
		String authority = appUserRepository.retrieveUserRole(authentication.get().getName());
		if(!authority.equals("ROLE_USER")) {
			return new AuthorizationDecision(false);
		}
		else {
			int openTradeDealId = Integer.valueOf(context.getRequest().getParameter("id"));
			OpenTradeDeal openTradeDeal = appTradeDealRepository.retrieveOpenTradeDeal(openTradeDealId);
			boolean authorize = openTradeDeal != null && !openTradeDeal.isPendingCompletion() && 
					appTradeDealService.doesOpenTradeDealBelongToCollector(openTradeDeal, authentication.get().getName());
				
			return new AuthorizationDecision(authorize);
		}
	}
}
