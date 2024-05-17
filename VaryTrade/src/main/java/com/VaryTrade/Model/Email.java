package com.VaryTrade.Model;

public class Email {
	private String recipient;
	private String message;
	private String subject;
	
	public Email() {}
	
	public Email(String recipient, String message, String subject) {
		this.recipient = recipient;
		this.message = message;
		this.subject = subject;
	}

	public String getRecipient() {
		return recipient;
	}

	public String getMessage() {
		return message;
	}

	public String getSubject() {
		return subject;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
