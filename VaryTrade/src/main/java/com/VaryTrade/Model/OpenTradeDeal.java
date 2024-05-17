package com.VaryTrade.Model;

public class OpenTradeDeal {
	private int id;
	private String postDate;
	private String acceptDate;
	private String posterEmail;
	private String accepterEmail;
	private boolean pendingCompletion;
	private int itemType;
	
	public OpenTradeDeal() {}
	
	public OpenTradeDeal(int id, String date, String posterEmail, String accepterEmail, boolean pendingCompletion, int itemType) {
		this.id = id;
		this.postDate = date;
		this.posterEmail = posterEmail;
		this.accepterEmail = accepterEmail;
		this.pendingCompletion = pendingCompletion;
		this.itemType = itemType;
	}
	
	public int getItemType() {
		return itemType;
	}

	public int getId() {
		return id;
	}

	public String getPostDate() {
		return postDate;
	}
	
	public String getAcceptDate() {
		return acceptDate;
	}

	public String getPosterEmail() {
		return posterEmail;
	}

	public String getAccepterEmail() {
		return accepterEmail;
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

	public void setPostDate(String date) {
		this.postDate = date;
	}
	
	public void setAcceptDate(String date) {
		this.acceptDate = date;
	}

	public void setPosterEmail(String posterEmail) {
		this.posterEmail = posterEmail;
	}

	public void setAccepterEmail(String accepterEmail) {
		this.accepterEmail = accepterEmail;
	}

	public void setPendingCompletion(boolean pendingCompletion) {
		this.pendingCompletion = pendingCompletion;
	}
}
