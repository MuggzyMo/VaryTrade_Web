package com.VaryTrade.Model;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class WebTradeItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String condition;
	@NotEmpty(message = "Must select whether you want to trade or receive this collectible.")
	private String type;
	private String userTradeItemAttrOne;
	private String userTradeItemAttrTwo;
	private String userTradeItemAttrThree;
	@NotBlank
	private String recaptchaToken;
	
	public WebTradeItem() {}
	
	public WebTradeItem(int id, String name, String condition, String type, String userTradeItemAttrOne,
			String userTradeItemAttrTwo, String userTradeItemAttrThree, String recaptchaToken) {
		this.name = name;
		this.condition = condition;
		this.type = type;
		this.userTradeItemAttrOne = userTradeItemAttrOne;
		this.userTradeItemAttrTwo = userTradeItemAttrTwo;
		this.userTradeItemAttrThree = userTradeItemAttrThree;
		this.recaptchaToken = recaptchaToken;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCondition() {
		return condition;
	}

	public String getType() {
		return type;
	}
	
	public String getUserTradeItemAttrOne() {
		return userTradeItemAttrOne;
	}

	public String getUserTradeItemAttrTwo() {
		return userTradeItemAttrTwo;
	}

	public String getUserTradeItemAttrThree() {
		return userTradeItemAttrThree;
	}
	
	public String getRecaptchaToken() {
		return recaptchaToken;
	}
	
	public void setRecaptchaToken(String recaptchaToken) {
		this.recaptchaToken = recaptchaToken;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public void setUserTradeItemAttrOne(String userTradeItemAttrOne) {
		this.userTradeItemAttrOne = userTradeItemAttrOne;
	}

	public void setUserTradeItemAttrTwo(String userTradeItemAttrTwo) {
		this.userTradeItemAttrTwo = userTradeItemAttrTwo;
	}

	public void setUserTradeItemAttrThree(String userTradeItemAttrThree) {
		this.userTradeItemAttrThree = userTradeItemAttrThree;
	}
	
	
}
