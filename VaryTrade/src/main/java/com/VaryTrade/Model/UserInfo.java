package com.VaryTrade.Model;


public class UserInfo {
	private String email;
	private String zipCode;
	private String state;
	private String city;
	private String address;
	private String phoneNum;
	private String firstName;
	private String middleName;
	private String lastName;
	private String userName;
	
	public UserInfo() {}
	
	public UserInfo(String email, String zipCode, String state, String city, String address,
			String phoneNum, String firstName, String middleName, String lastName, String userName) {
		this.email = email;
		this.zipCode = zipCode;
		this.state = state;
		this.city = city;
		this.address = address;
		this.phoneNum = phoneNum;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.userName = userName;
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

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUserName() {
		return userName;
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

	public void setCity(String city) {
		this.city = city;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public WebEditUserInfo mapUserInfoToWebEditUserInfo() {
		WebEditUserInfo webEditUserInfo = new WebEditUserInfo();
		webEditUserInfo.setEmail(this.email);
		webEditUserInfo.setPhoneNum(this.phoneNum);
		webEditUserInfo.setFirstName(this.firstName);
		webEditUserInfo.setMiddleName(this.middleName);
		webEditUserInfo.setLastName(this.lastName);
		webEditUserInfo.setAddress(this.address);
		webEditUserInfo.setCity(this.city);
		webEditUserInfo.setZipCode(this.zipCode);
		webEditUserInfo.setState(this.state);
		return webEditUserInfo;
	}
	
	public WebEditGoogleUserInfo mapUserInfoToWebEditGoogleUserInfo() {
		WebEditGoogleUserInfo webEditGoogleUserInfo = new WebEditGoogleUserInfo();
		webEditGoogleUserInfo.setPhoneNum(this.phoneNum);
		webEditGoogleUserInfo.setFirstName(this.firstName);
		webEditGoogleUserInfo.setMiddleName(this.middleName);
		webEditGoogleUserInfo.setLastName(this.lastName);
		webEditGoogleUserInfo.setAddress(this.address);
		webEditGoogleUserInfo.setCity(this.city);
		webEditGoogleUserInfo.setZipCode(this.zipCode);
		webEditGoogleUserInfo.setState(this.state);
		return webEditGoogleUserInfo;
	}
}

