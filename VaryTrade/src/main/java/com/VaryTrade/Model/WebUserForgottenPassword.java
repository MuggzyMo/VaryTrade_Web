package com.VaryTrade.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class WebUserForgottenPassword {
	@NotBlank(message = "Email is required.")
	@Email(message = "Proper email is required.")
	@Size(min=1, max=100, message = "Email must be between 1 and 100 characters.")
	private String email;
	@NotBlank
	private String recaptchaToken;
	
	public WebUserForgottenPassword() {}
	
	public WebUserForgottenPassword(String email, String recaptchaToken) {
		this.email = email;
		this.recaptchaToken = recaptchaToken;
	}
	
	public String getRecaptchaToken() {
		return recaptchaToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setRecaptchaToken(String recaptchaToken) {
		this.recaptchaToken = recaptchaToken;
	}
}
