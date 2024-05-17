package com.VaryTrade.Model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleRegistrationResponse {
	@JsonProperty("username")
	private String userName;
	
	@JsonProperty("errorMessage")
	private ArrayList<String> errorMessage;
	
	public GoogleRegistrationResponse() {}
	
	public GoogleRegistrationResponse(String userName, ArrayList<String> errorMessage) {
		this.userName = userName;
		this.errorMessage = errorMessage;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public ArrayList<String> getErrorMessage() {
		return errorMessage;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setErrorMessage(ArrayList<String> errorMessage) {
		this.errorMessage = errorMessage;	
	}	
}
