package com.VaryTrade.Model;

public class ClosedTradeItem {
	private int id;
	private int closedTradeDealId;
	private String name;
	private String email;
	private String condition;
	private String userTradeItemAttrOne;
	private String userTradeItemAttrTwo;
	private String userTradeItemAttrThree;
	
	public ClosedTradeItem() {}
	
	public ClosedTradeItem(int id, int closedTradeDealId, String name, String email, String condition,
			String userTradeItemAttrOne, String userTradeItemAttrTwo, String userTradeItemAttrThree) {
		this.id = id;
		this.closedTradeDealId = closedTradeDealId;
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

	public int getClosedTradeDealId() {
		return closedTradeDealId;
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

	public void setClosedTradeDealId(int closedTradeDealId) {
		this.closedTradeDealId = closedTradeDealId;
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

