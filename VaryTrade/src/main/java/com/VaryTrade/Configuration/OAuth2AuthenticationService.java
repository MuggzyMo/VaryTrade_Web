package com.VaryTrade.Configuration;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.OAuth2GoogleUser;
import com.VaryTrade.Model.UserInfo;

@Service
public class OAuth2AuthenticationService extends DefaultOAuth2UserService {
	@Autowired
	private AppUserRepository appUserRepository;
	
	@Override
	public OAuth2GoogleUser loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {	
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String email = oAuth2User.getAttribute("email");
		
		UserInfo userInfo = appUserRepository.retrieveUserInfoByEmail(email);
		if(userInfo == null) {
			userInfo = new UserInfo(email, null, null, null, null, null, null, null, null, null);
			appUserRepository.insertCollectorGoogleUserRegistration(userInfo);
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_GOOGLE_USER"));
			return new OAuth2GoogleUser(email, authorities);
		}
		else if(userInfo.getUserName() != null) {
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			return new OAuth2GoogleUser(email, authorities);
		}
		else {
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_GOOGLE_USER"));
			return new OAuth2GoogleUser(email, authorities);
		}
		
		

		
		
		
	}

}
