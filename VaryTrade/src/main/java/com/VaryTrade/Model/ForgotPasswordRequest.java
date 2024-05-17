package com.VaryTrade.Model;

import jakarta.validation.constraints.Email;

public class ForgotPasswordRequest {
	@Email(message = "Proper email is required.")
	private String email;
	
	public ForgotPasswordRequest() {}
	
	public ForgotPasswordRequest(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void trimForgotPasswordRequestFields() {
		email = email.trim();
	}

}
