package com.VaryTrade.Model;

import org.springframework.stereotype.Repository;

@Repository
public class Company {
	private String name;
	private String email;
	private String zipCode;
	private String state;
	private String city;
	private String address;
	private String phoneNum;
	
	public Company() {}
	
	public Company(String name, String email, String zipCode, String state,
			String city, String address, String phoneNum) {
		this.name = name;
		this.email = email;
		this.zipCode = zipCode;
		this.state = state;
		this.city = city;
		this.address = address;
		this.phoneNum = phoneNum;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getState() {
		return state;
	}
	
	public String getCity() {
		return city;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
}
