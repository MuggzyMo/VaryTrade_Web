package com.VaryTrade.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public class WebEditUserInfo {
	@Email
	@NotBlank
	@Size(min=1, max=100, message = "Email must be between 1 and 100 characters.")
	private String email;
	@NotBlank
	@Pattern(regexp = "^\\d{5}$")
	private String zipCode;
	@NotNull
	private String state;
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "City must be well-formed.")
	@Size(min=1, max=100, message = "City must be between 1 and 100 characters.")
	private String city;
	@NotBlank
	@Pattern(regexp = "^[0-9a-zA-Z ]+$", message = "Address must be well-formed.")
	@Size(min=1, max=100, message = "Address must be between 1 and 100 characters.")
	private String address;
	@NotBlank
	@Pattern(regexp = "[0-9]{3}-[0-9]{3}-[0-9]{4}")
	private String phoneNum;
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "First name must be well-formed.")
	@Size(min=1, max=100, message = "First name must be between 1 and 100 characters.")
	private String firstName;
	@Pattern(regexp = "^[a-zA-Z ]*$", message = "Middle name must be well-formed.")
	private String middleName;
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Last name must be well-formed.")
	@Size(min=1, max=100, message = "Last name must be between 1 and 100 characters.")
	private String lastName;
	private String originalEmail;
	
	public WebEditUserInfo() {}
	
	public WebEditUserInfo(String email, String zipCode, String state, String city, String address,
			String phoneNum, String firstName, String middleName, String lastName, String originalEmail) {
		this.email = email.toLowerCase();
		this.zipCode = zipCode;
		this.state = state;
		this.city = city;
		this.address = address;
		this.phoneNum = phoneNum;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.originalEmail = originalEmail;
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
	
	public String getOriginalEmail() {
		return originalEmail;
	}

	public void setEmail(String email) {
		this.email = email.toLowerCase();
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
	
	public void setOriginalEmail(String originalEmail) {
		this.originalEmail = originalEmail;
	}
	
	public void trimWebEditUserInfoFields() {
		this.email = this.email.trim();
		this.address = this.address.trim();
		this.city = this.city.trim();
		this.state = this.state.trim();
		this.firstName = this.firstName.trim();
		this.lastName = this.lastName.trim();
		this.middleName = this.middleName.trim();
		this.phoneNum = this.phoneNum.trim();
	}
}
