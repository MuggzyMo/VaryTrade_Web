package com.VaryTrade.Model;


import java.sql.Date;

import jakarta.validation.constraints.NotNull;


public class WebHyperwalletRegistration {
	@NotNull(message = "DOB is required.")
	private Date dateOfBirth;
	
	public WebHyperwalletRegistration() {}
	
	public WebHyperwalletRegistration(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}
