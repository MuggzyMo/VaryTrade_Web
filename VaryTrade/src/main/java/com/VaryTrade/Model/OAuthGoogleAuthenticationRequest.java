package com.VaryTrade.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class OAuthGoogleAuthenticationRequest {
	@Email
	private String email;
	@NotBlank
	private String token;
	
	public OAuthGoogleAuthenticationRequest(String email, String token) {
		this.email = email;
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public String getToken() {
		return token;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
