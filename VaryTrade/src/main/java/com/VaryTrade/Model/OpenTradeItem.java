package com.VaryTrade.Model;

public class OpenTradeItem {
	private int id;
	private int openTradeDealId;
	private String name;
	private String email;
	private String condition;
	private String userTradeItemAttrOne;
	private String userTradeItemAttrTwo;
	private String userTradeItemAttrThree;
	
	public OpenTradeItem() {}
	
	public OpenTradeItem(int id, int openTradeDealId, String name, String email, String condition,
			String userTradeItemAttrOne, String userTradeItemAttrTwo, String userTradeItemAttrThree) {
		this.id = id;
		this.openTradeDealId = openTradeDealId;
		this.name = name;
		this.email = email;
		this.condition = condition;
		this.userTradeItemAttrOne = userTradeItemAttrOne;
		this.userTradeItemAttrTwo = userTradeItemAttrTwo;
		this.userTradeItemAttrThree = userTradeItemAttrThree;
	}

	public int getId() {
		return id;
	}

	public int getOpenTradeDealId() {
		return openTradeDealId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
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

	public void setOpenTradeDealId(int openTradeDealId) {
		this.openTradeDealId = openTradeDealId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
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
