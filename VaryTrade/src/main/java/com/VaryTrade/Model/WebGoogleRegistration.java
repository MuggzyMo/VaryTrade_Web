package com.VaryTrade.Model;

import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class WebGoogleRegistration {
	@JsonProperty("email")
	private String email;
	@Pattern(regexp = "^\\d{5}$", message = "Zip Code is required.")
	@JsonProperty("zipCode")
	private String zipCode;
	@NotNull(message = "State is required.")
	@JsonProperty("state")
	private String state;
	@NotBlank(message = "City is required.")
	@JsonProperty("city")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "City must be well-formed")
	@Size(min=1, max=100, message = "City must be between 1 and 100 characters.")
	private String city;
	@NotBlank(message = "Address is required.")
	@JsonProperty("address")
	@Pattern(regexp = "^[0-9a-zA-Z ]+$", message = "Address must be well-formed")
	@Size(min=1, max=100, message = "Address must be between 1 and 100 characters.")
	private String address;
	@NotBlank(message = "Phone number is required.")
	@Pattern(regexp = "[0-9]{3}-[0-9]{3}-[0-9]{4}")
	@JsonProperty("phoneNum")
	private String phoneNum;
	@NotBlank(message = "First name is required.")
	@JsonProperty("firstName")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "First name must be well-formed.")
	@Size(min=1, max=100, message = "First name must be between 1 and 100 characters.")
	private String firstName;
	@JsonProperty("middleName")
	@Pattern(regexp = "^[a-zA-Z ]*$", message = "Middle name must be well-formed.")
	private String middleName;
	@NotBlank(message = "Last name is required.")
	@JsonProperty("lastName")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Last name must be well-formed.")
	@Size(min=1, max=100, message = "Last name must be between 1 and 100 characters.")
	private String lastName;
	@NotBlank(message = "Username is required.")
	@Size(min=5, max=100, message = "Username must be between 5 and 100 characters.")
	@JsonProperty("userName")
	@Pattern(regexp = "^[0-9a-zA-Z+!_]+$", message = "Username must be well-formed.")
	private String userName;
	
	public WebGoogleRegistration() {}
	
	public WebGoogleRegistration(String email, String zipCode, String state, String city, String address, String phoneNum,
			String firstName, String middleName, String lastName, String userName) {
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
	
	public void trimWebGoogleRegistrationFields() {
		this.address = this.address.trim();
		this.city = this.city.trim();
		this.state = this.state.trim();
		this.email = this.email.trim();
		this.firstName = this.firstName.trim();
		this.lastName = this.lastName.trim();
		this.middleName = this.middleName.trim();
		this.phoneNum = this.phoneNum.trim();
		this.userName = this.userName.trim();
	}
}
