package com.VaryTrade.Model;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

public class WebResaleDealApi {
	@DecimalMin(value="0.01", message = "Must sell item for minimum of $0.01")
	@DecimalMax(value="100000000.00", message = "Must sell for lower than $100,000,000.00")
	@Digits(integer=9, fraction=2)
	private BigDecimal price;
	private String name;
	private String condition;
	private String userResaleItemAttrOne;
	private String userResaleItemAttrTwo;
	private String userResaleItemAttrThree;
	private int type;
	
	public WebResaleDealApi() {}
	
	public WebResaleDealApi(String posterEmail, BigDecimal price, String name, String condition, String userResaleItemAttrOne, 
			String userResaleItemAttrTwo, String userResaleItemAttrThree, int type) {
		this.price = price;
		this.name = name;
		this.condition = name;
		this.userResaleItemAttrOne = userResaleItemAttrOne;
		this.userResaleItemAttrTwo = userResaleItemAttrTwo;
		this.userResaleItemAttrThree = userResaleItemAttrThree;
	}
	
	public int getType() {
		return type;
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

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(int type) {
		this.type = type;
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
}
