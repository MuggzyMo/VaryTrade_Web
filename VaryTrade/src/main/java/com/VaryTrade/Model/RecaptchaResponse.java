package com.VaryTrade.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecaptchaResponse {
	@JsonProperty("success")
	private boolean success;
	@JsonProperty
	private float score;
	@JsonProperty
	private String action;
	
	public RecaptchaResponse() {}
	
	public RecaptchaResponse(boolean success, float score, String action) {
		this.success = success;
		this.score = score;
		this.action = action;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isSuccess() {
		return success;
	}

	public float getScore() {
		return score;
	}

	public String getAction() {
		return action;
	}
}
