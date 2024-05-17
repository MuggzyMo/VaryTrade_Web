package com.VaryTrade.Model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WebEditUserPassword {	
	private String email;
	private String currentPassword;
	@Size(min=10, max=100, message = "Password must be between 10 and 100 characters.")
	@Pattern(regexp = "^[0-9a-zA-Z+!_]+$", message = "Password must be well-formed.")
	private String password;
	@Size(min=10, max=100, message = "Confirmed password must be between 10 and 100 characters.")
	private String confirmPassword;
	
	public WebEditUserPassword() {}
	
	public WebEditUserPassword(String email, String currentPassword, String password, String confirmPassword) {
		this.currentPassword = currentPassword;
		this.password = password;
		this.confirmPassword = confirmPassword;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getCurrentPassword() {
		return currentPassword;
	}
	
	public String getPassword() {
		return password;
	}
		
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public void trimWebEditUserPasswordFields() {
		this.email = this.email.trim();
		this.confirmPassword = this.confirmPassword.trim();
		this.password = this.password.trim();
	}
}
