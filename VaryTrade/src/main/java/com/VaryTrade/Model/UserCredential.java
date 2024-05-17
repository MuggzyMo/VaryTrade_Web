package com.VaryTrade.Model;

import java.sql.Timestamp;

public class UserCredential {
	private String email;
	private String password;
	private boolean enabled;
	private int failedLoginAttempt;
	private Timestamp disableTime;
	
	public UserCredential() {}
	
	public UserCredential(String email, String password, boolean enabled, int failedLoginAttempt, Timestamp disableTime) {
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.failedLoginAttempt = failedLoginAttempt;
		this.disableTime = disableTime;
	}
	
	public int getFailedLoginAttempt() {
		return failedLoginAttempt;
	}
	
	public Timestamp getDisableTime() {
		return disableTime;
	}

	public String getEmail() {
		return email;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public String getPassword() {
		return password;
	}
	
	public void setFailedLoginAttempt(int failedLoginAttempt) {
		this.failedLoginAttempt = failedLoginAttempt;
	}
	
	public void setDisableTime(Timestamp disableTime) {
		this.disableTime = disableTime;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
