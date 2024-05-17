package com.VaryTrade.Service.User;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.RandomStringUtils;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.OAuth2GoogleUser;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebUserRegistration;
import com.VaryTrade.Model.WebUserRegistrationApi;
import com.hyperwallet.clientsdk.model.HyperwalletPayment;
import com.hyperwallet.clientsdk.model.HyperwalletUser;
import com.hyperwallet.clientsdk.model.HyperwalletUser.ProfileType;

import jakarta.servlet.http.HttpServletRequest;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private AppUserRepository appUserRepository;
	@Value("${hyperwallet.program.token}")
	private String hyperwalletProgramToken;
	
	@Override
	public WebUserRegistration mapWebUserRegistrationApiToWebUserRegistration(WebUserRegistrationApi webUserRegistrationApi) {
		WebUserRegistration webUserRegistration = new WebUserRegistration(webUserRegistrationApi.getEmail(),
				webUserRegistrationApi.getZipCode(), webUserRegistrationApi.getState(),
				webUserRegistrationApi.getCity(), webUserRegistrationApi.getAddress(),
				webUserRegistrationApi.getPassword(), webUserRegistrationApi.getConfirmPassword(),
				webUserRegistrationApi.getPhoneNum(), webUserRegistrationApi.getFirstName(),
				webUserRegistrationApi.getMiddleName(), webUserRegistrationApi.getLastName(),
				webUserRegistrationApi.getUserName(), null);
		return webUserRegistration;
	}
	
	@Override
	public void setSecurityContextAuthorizationFromGoogleUserToUser(String email, HttpServletRequest request) {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		OAuth2GoogleUser oAuth2GoogleUser = new OAuth2GoogleUser(email, authorities);
		Authentication authentication = new OAuth2AuthenticationToken(oAuth2GoogleUser, authorities, "75331537615-80rrn5099lhe8k5j849v7581mlvdeipk.apps.googleusercontent.com");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
		
	}
	
	@Override
	public boolean isNonprincipalUserAnAdmin(String email) {
		String authority = appUserRepository.retrieveUserRole(email);
		return authority.equals("ROLE_ADMIN");
	}
	
	@Override
	public boolean doesUserHaveRole(String role) {
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().
				getAuthentication().getAuthorities();	
		for(GrantedAuthority authority : authorities) {
			System.out.println(authority);
			if(authority.getAuthority().equals(role)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String generateRandomPassayPassword() {
		PasswordGenerator passwordGenerator = new PasswordGenerator();
		
		CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
		lowerCaseRule.setNumberOfCharacters(2);
		
		CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase);
		upperCaseRule.setNumberOfCharacters(2);
		
		CharacterRule digitCaseRule = new CharacterRule(EnglishCharacterData.Digit);
		digitCaseRule.setNumberOfCharacters(2);
		
		String password = passwordGenerator.generatePassword(10, lowerCaseRule, upperCaseRule, digitCaseRule);
		
		return password;
	}
	
	@Override
	public HyperwalletUser createHyperwalletUser(UserInfo userInfo, Date dateOfBirth) {
		
		HyperwalletUser hyperwalletUser = new HyperwalletUser();
		hyperwalletUser.setClientUserId(userInfo.getUserName());
		hyperwalletUser.setDateOfBirth(dateOfBirth);
		hyperwalletUser.setEmail(userInfo.getEmail());
		hyperwalletUser.setProfileType(ProfileType.INDIVIDUAL);
		hyperwalletUser.setProgramToken(hyperwalletProgramToken);
		return hyperwalletUser;
	}
	
	@Override
	public HyperwalletPayment createHyperwalletPayment(BigDecimal credit, String userToken) {
		HyperwalletPayment payment = new HyperwalletPayment();
		payment.setAmount(credit.doubleValue());
		payment.setClientPaymentId(generateIdForPayout());
		payment.setCurrency("USD");
		payment.setDestinationToken(userToken);
		payment.setProgramToken(hyperwalletProgramToken);
		payment.setPurpose("OTHER");
		return payment;
	}
	
	@Override
	public boolean doesCollectorFollowCollector(String email, String collectorEmail) {
		ArrayList<String> followers = appUserRepository.retrieveEmailsForCollectorsFollowingCollector(collectorEmail);
		for(int i = 0; i < followers.size(); i++) {
			if(followers.get(i).equals(email)) {
				return true;
			}
		}
		return false;
	}
	
	private String generateIdForPayout() {
		String id = RandomStringUtils.randomAlphanumeric(8);	   
		String existingId = appUserRepository.retrievePayoutId(id);
		while(id.equals(existingId)) {
			id = RandomStringUtils.randomAlphanumeric(8);
		}
		return id;
	}
}
