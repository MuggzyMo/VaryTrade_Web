package com.VaryTrade.Model;

import java.math.BigDecimal;

public class OpenResaleDeal {
	private int id;
	private String postDate;
	private String acceptDate;
	private String accepterEmail;
	private String posterEmail;
	private BigDecimal price;
	private String name;
	private String condition;
	private String userResaleItemAttrOne;
	private String userResaleItemAttrTwo;
	private String userResaleItemAttrThree;
	private boolean pendingCompletion;
	private int itemType;
	
	public OpenResaleDeal() {}
	
	public OpenResaleDeal(int id, String postDate, String acceptDate, String accepterEmail, String posterEmail, BigDecimal price,
			String name, String condition, String userResaleItemAttrOne, String userResaleItemAttrTwo, String userResaleItemAttrThree,
			boolean pendingCompletion, int itemType) {
		this.id = id;
		this.postDate = postDate;
		this.acceptDate = acceptDate;
		this.accepterEmail = accepterEmail;
		this.posterEmail = posterEmail;
		this.price = price;
		this.name = name;
		this.condition = name;
		this.userResaleItemAttrOne = userResaleItemAttrOne;
		this.userResaleItemAttrTwo = userResaleItemAttrTwo;
		this.userResaleItemAttrThree = userResaleItemAttrThree;
		this.pendingCompletion = pendingCompletion;
		this.itemType = itemType;
	}
	
	public int getId() {
		return id;
	}
	
	public int getItemType() {
		return itemType;
	}

	public String getPostDate() {
		return postDate;
	}

	public String getAcceptDate() {
		return acceptDate;
	}

	public String getAccepterEmail() {
		return accepterEmail;
	}

	public String getPosterEmail() {
		return posterEmail;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}
	
	public String getCondition() {
		return condition;
	}

	public String getUserResaleItemAttrOne() {
		return userResaleItemAttrOne;
	}

	public String getUserResaleItemAttrTwo() {
		return userResaleItemAttrTwo;
	}

	public String getUserResaleItemAttrThree() {
		return userResaleItemAttrThree;
	}
	
	public boolean isPendingCompletion() {
		return pendingCompletion;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public void setAcceptDate(String acceptDate) {
		this.acceptDate = acceptDate;
	}

	public void setAccepterEmail(String accepterEmail) {
		this.accepterEmail = accepterEmail;
	}

	public void setPosterEmail(String posterEmail) {
		this.posterEmail = posterEmail;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setUserResaleItemAttrOne(String userResaleItemAttrOne) {
		this.userResaleItemAttrOne = userResaleItemAttrOne;
	}

	public void setUserResaleItemAttrTwo(String userResaleItemAttrTwo) {
		this.userResaleItemAttrTwo = userResaleItemAttrTwo;
	}

	public void setUserResaleItemAttrThree(String userResaleItemAttrThree) {
		this.userResaleItemAttrThree = userResaleItemAttrThree;
	}
	
	public void setPendingCompletion(boolean pendingCompletion) {
		this.pendingCompletion = pendingCompletion;
	}
}	
