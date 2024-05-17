package com.VaryTrade.Model;

public class ClosedTradeDeal {
	private int id;
	private String posterEmail;
	private String accepterEmail;
	private String postDate;
	private String acceptDate;
	private String authenticatedDate;
	private String authenticityStatus;
	private int itemType;
	
	public ClosedTradeDeal() {}
	
	public ClosedTradeDeal(int id, String posterEmail, String accepterEmail, String postDate, String acceptDate,
			String authenticityStatus, String authenticatedDate, int itemType) {
		this.id = id;
		this.posterEmail = posterEmail;
		this.accepterEmail = accepterEmail;
		this.postDate = postDate;
		this.acceptDate = acceptDate;
		this.authenticatedDate = authenticatedDate;
		this.authenticityStatus = authenticityStatus;
		this.itemType = itemType;
	}

	public int getId() {
		return id;
	}
	
	public int getItemType() {
		return itemType;
	}

	public String getPosterEmail() {
		return posterEmail;
	}

	public String getAccepterEmail() {
		return accepterEmail;
	}

	public String getPostDate() {
		return postDate;
	}

	public String getAcceptDate() {
		return acceptDate;
	}

	public String getAuthenticatedDate() {
		return authenticatedDate;
	}
	
	public String getAuthenticityStatus() {
		return authenticityStatus;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}

	public void setPosterEmail(String posterEmail) {
		this.posterEmail = posterEmail;
	}

	public void setAccepterEmail(String accepterEmail) {
		this.accepterEmail = accepterEmail;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public void setAcceptDate(String acceptDate) {
		this.acceptDate = acceptDate;
	}
	
	public void setAuthenticatedDate(String authenticatedDate) {
		this.authenticatedDate = authenticatedDate;
	}

	public void setAuthenticityStatus(String authenticityStatus) {
		this.authenticityStatus = authenticityStatus;
	}
}
