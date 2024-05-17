package com.VaryTrade.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {
	@JsonProperty("token")
	private String token;
	
	@JsonProperty("username")
	private String userName;
	
	public AuthenticationResponse() {}
	
	public AuthenticationResponse(String token, String userName) {
		this.token = token;
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}
	
	public String getUsername() {
		return userName;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
