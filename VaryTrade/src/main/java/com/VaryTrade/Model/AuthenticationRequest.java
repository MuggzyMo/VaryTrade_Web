package com.VaryTrade.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public class AuthenticationRequest {
	@Email(message = "Proper email is required.")
	private String email;
	@Pattern(regexp = "^[0-9a-zA-Z+!_]+$", message = "Password must be well-formed.")
	private String password;
	
	public AuthenticationRequest() {}
	
	public AuthenticationRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void trimAuthenticationRequestFields() {
		email = email.trim();
		password = password.trim();
	}
}
