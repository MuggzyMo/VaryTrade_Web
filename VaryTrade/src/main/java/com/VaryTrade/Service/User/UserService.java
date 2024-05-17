package com.VaryTrade.Service.User;

import java.math.BigDecimal;
import java.sql.Date;

import org.springframework.stereotype.Service;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebUserRegistration;
import com.VaryTrade.Model.WebUserRegistrationApi;
import com.hyperwallet.clientsdk.model.HyperwalletPayment;
import com.hyperwallet.clientsdk.model.HyperwalletUser;

import jakarta.servlet.http.HttpServletRequest;

@Service
public interface UserService {
	public boolean doesUserHaveRole(String role);
	public String generateRandomPassayPassword();
	public HyperwalletUser createHyperwalletUser(UserInfo userInfo, Date dateOfBirth);
	public HyperwalletPayment createHyperwalletPayment(BigDecimal credit, String userToken);
	public boolean doesCollectorFollowCollector(String email, String collectorEmail);
	public boolean isNonprincipalUserAnAdmin(String email);
	public void setSecurityContextAuthorizationFromGoogleUserToUser(String email, HttpServletRequest request);
	public WebUserRegistration mapWebUserRegistrationApiToWebUserRegistration(WebUserRegistrationApi webUserRegistrationApi);
}
