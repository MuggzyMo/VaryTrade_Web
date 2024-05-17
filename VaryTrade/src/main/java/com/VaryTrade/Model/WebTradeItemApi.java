package com.VaryTrade.Model;

import jakarta.validation.constraints.NotEmpty;

public class WebTradeItemApi {
	private int id;
	private String name;
	private String condition;
	private String userTradeItemAttrOne;
	private String userTradeItemAttrTwo;
	private String userTradeItemAttrThree;

	public WebTradeItemApi() {
	}

	public WebTradeItemApi(int id, String name, String condition, String userTradeItemAttrOne,
			String userTradeItemAttrTwo, String userTradeItemAttrThree, String recaptchaToken) {
		this.name = name;
		this.condition = condition;
		this.userTradeItemAttrOne = userTradeItemAttrOne;
		this.userTradeItemAttrTwo = userTradeItemAttrTwo;
		this.userTradeItemAttrThree = userTradeItemAttrThree;
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

	public String getUserTradeItemAttrOne() {
		return userTradeItemAttrOne;
	}

	public String getUserTradeItemAttrTwo() {
		return userTradeItemAttrTwo;
	}

	public String getUserTradeItemAttrThree() {
		return userTradeItemAttrThree;
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
