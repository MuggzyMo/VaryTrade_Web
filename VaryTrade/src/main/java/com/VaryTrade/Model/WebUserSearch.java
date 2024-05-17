package com.VaryTrade.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WebUserSearch {
	@Pattern(regexp = "^[0-9a-zA-Z+!_]+$", message = "Username must be well-formed.")
	@Size(min=5, max=100, message = "Username must be between 5 and 100 characters.")
	@JsonProperty("username")
	private String userName;
	
	public WebUserSearch() {}
	
	public WebUserSearch(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
