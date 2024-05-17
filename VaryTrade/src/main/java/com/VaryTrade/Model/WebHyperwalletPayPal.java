package com.VaryTrade.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class WebHyperwalletPayPal {
	@NotBlank(message = "Email is required.")
	@Email(message = "Proper email is required.")
	private String email;
	
	public WebHyperwalletPayPal() {}
	
	public WebHyperwalletPayPal(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}

