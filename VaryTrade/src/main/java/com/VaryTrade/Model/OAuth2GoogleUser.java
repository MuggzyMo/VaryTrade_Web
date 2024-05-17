package com.VaryTrade.Model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2GoogleUser implements OAuth2User, Serializable {
	private static final long serialVersionUID = 1L;
	private String email;
	private Collection<GrantedAuthority> authorities;
	
	public OAuth2GoogleUser(String email, Collection<GrantedAuthority> authorities) {
		this.email = email;
		this.authorities = authorities;
	}

	@Override
	public Map<String, Object> getAttributes() {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("email", email);
		attributes.put("authorities", authorities);
		return attributes;
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getName() {
		return email;
	}

}
