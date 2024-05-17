package com.VaryTrade.Service.User;

import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebUserRegistration;
import com.VaryTrade.Model.WebUserRegistrationApi;
import com.hyperwallet.clientsdk.model.HyperwalletPayment;
import com.hyperwallet.clientsdk.model.HyperwalletUser;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AppUserService {
	@Autowired
	private UserService userService;
	
	public WebUserRegistration mapWebUserRegistrationApiToWebUserRegistration(WebUserRegistrationApi webUserRegistrationApi) {
		return userService.mapWebUserRegistrationApiToWebUserRegistration(webUserRegistrationApi);
	}
	
	public boolean doesUserHaveRole(String role) {
		return userService.doesUserHaveRole(role);
	}
	
	public String generateRandomPassayPassword() {
		return userService.generateRandomPassayPassword();
	}
	
	public boolean doesCollectorFollowCollector(String email, String collectorEmail) {
		return userService.doesCollectorFollowCollector(email, collectorEmail);
	}
	
	public HyperwalletPayment createHyperwalletPayment(BigDecimal credit, String userToken) {
		return userService.createHyperwalletPayment(credit, userToken);
	}
	
	public HyperwalletUser createHyperwalletUser(UserInfo userInfo, Date dateOfBirth) {
		return userService.createHyperwalletUser(userInfo, dateOfBirth);
	}
	
	public boolean isNonprincipalUserAnAdmin(String email) {
		return userService.isNonprincipalUserAnAdmin(email);
	}
	
	public void setSecurityContextAuthorizationFromGoogleUserToUser(String email, HttpServletRequest request) {
		userService.setSecurityContextAuthorizationFromGoogleUserToUser(email, request);
	}
}
