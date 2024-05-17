package com.VaryTrade.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WebCreateAccountPassword {
	private String email;
	@NotBlank(message = "Password is required.")
	@Size(min=10, max=100, message = "Password must be between 10 and 100 characters.")
	@Pattern(regexp = "^[0-9a-zA-Z+!_]+$", message = "Password must be well-formed.")
	private String password;
	@Size(min=10, max=100, message = "Confirm password must be between 10 and 100 characters.")
	private String confirmPassword;
	
	public WebCreateAccountPassword() {}
	
	public WebCreateAccountPassword(String email, String password, String confirmPassword) {
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
	}
	
	public String getEmail() {
		return email;
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
	
	public void setPassword(String password) {
		this.password = password;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	public void trimWebCreateAccountPasswordFields() {
		this.email = this.email.trim();
		this.confirmPassword = this.confirmPassword.trim();
		this.password = this.password.trim();
	}
}
