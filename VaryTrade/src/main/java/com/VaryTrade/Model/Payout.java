package com.VaryTrade.Model;

import java.math.BigDecimal;
import java.sql.Date;

public class Payout {
	private String id;
	private String email;
	private BigDecimal amount;
	private Date createdDate;
	
	public Payout() {}
	
	public Payout(String id, String email, BigDecimal amount, Date createdDate) {
		this.id = id;
		this.email = email;
		this.amount = amount;
		this.createdDate = createdDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}
}
