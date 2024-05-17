package com.VaryTrade.Model;

import java.util.ArrayList;

public class ApiOpenResaleDealList {
	private ArrayList<String> posterUserNames;
	private ArrayList<String> accepterUserNames;
	private ArrayList<OpenResaleDeal> openResaleDeals;
	private ArrayList<String> userResaleItemAttrsOne;
	private ArrayList<String> userResaleItemAttrsTwo;
	private ArrayList<String> userResaleItemAttrsThree;
	private ArrayList<String> userResaleItemAttrs;
	private String collectibleType;

	public ApiOpenResaleDealList(ArrayList<String> posterUserNames, ArrayList<String> accepterUserNames, ArrayList<OpenResaleDeal> openResaleDeals,
			ArrayList<String> userResaleItemAttrsOne, ArrayList<String> userResaleItemAttrsTwo, ArrayList<String> userResaleItemAttrsThree,
			ArrayList<String> userResaleItemAttrs, String collectibleType) {
		this.posterUserNames = posterUserNames;
		this.accepterUserNames = accepterUserNames;
		this.openResaleDeals = openResaleDeals;
		this.userResaleItemAttrsOne = userResaleItemAttrsOne;
		this.userResaleItemAttrsTwo = userResaleItemAttrsTwo;
		this.userResaleItemAttrsThree = userResaleItemAttrsThree;
		this.userResaleItemAttrs = userResaleItemAttrs;
		this.collectibleType = collectibleType;
	}

	public ArrayList<String> getPosterUserNames() {
		return posterUserNames;
	}

	public ArrayList<String> getAccepterUserNames() {
		return accepterUserNames;
	}

	public ArrayList<OpenResaleDeal> getOpenResaleDeals() {
		return openResaleDeals;
	}

	public ArrayList<String> getUserResaleItemAttrsOne() {
		return userResaleItemAttrsOne;
	}

	public ArrayList<String> getUserResaleItemAttrsTwo() {
		return userResaleItemAttrsTwo;
	}

	public ArrayList<String> getUserResaleItemAttrsThree() {
		return userResaleItemAttrsThree;
	}

	public ArrayList<String> getUserResaleItemAttrs() {
		return userResaleItemAttrs;
	}

	public String getCollectibleType() {
		return collectibleType;
	}

	public void setPosterUserNames(ArrayList<String> posterUserNames) {
		this.posterUserNames = posterUserNames;
	}

	public void setAccepterUserNames(ArrayList<String> accepterUserNames) {
		this.accepterUserNames = accepterUserNames;
	}

	public void setOpenResaleDeals(ArrayList<OpenResaleDeal> openResaleDeals) {
		this.openResaleDeals = openResaleDeals;
	}

	public void setUserResaleItemAttrsOne(ArrayList<String> userResaleItemAttrsOne) {
		this.userResaleItemAttrsOne = userResaleItemAttrsOne;
	}

	public void setUserResaleItemAttrsTwo(ArrayList<String> userResaleItemAttrsTwo) {
		this.userResaleItemAttrsTwo = userResaleItemAttrsTwo;
	}

	public void setUserResaleItemAttrsThree(ArrayList<String> userResaleItemAttrsThree) {
		this.userResaleItemAttrsThree = userResaleItemAttrsThree;
	}

	public void setUserResaleItemAttrs(ArrayList<String> userResaleItemAttrs) {
		this.userResaleItemAttrs = userResaleItemAttrs;
	}

	public void setCollectibleType(String collectibleType) {
		this.collectibleType = collectibleType;
	}

}
