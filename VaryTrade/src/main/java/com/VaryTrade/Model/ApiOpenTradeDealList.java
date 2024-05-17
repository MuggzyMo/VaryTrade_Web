package com.VaryTrade.Model;

import java.util.ArrayList;

public class ApiOpenTradeDealList {
	private ArrayList<String> posterUserNames;
	private ArrayList<String> accepterUserNames;
	private ArrayList<OpenTradeDeal> openTradeDeals;
	private ArrayList<ArrayList<OpenTradeItem>> posterTradeItems;
	private ArrayList<ArrayList<OpenTradeItem>> accepterTradeItems;
	private ArrayList<String> userTradeItemAttrsOne;
	private ArrayList<String> userTradeItemAttrsTwo;
	private ArrayList<String> userTradeItemAttrsThree;
	private ArrayList<String> userTradeItemAttrs;
	private String collectibleType;

	public ApiOpenTradeDealList(ArrayList<String> posterUserNames, ArrayList<String> accepterUserNames, ArrayList<OpenTradeDeal> openTradeDeals,
			ArrayList<ArrayList<OpenTradeItem>> posterTradeItems, ArrayList<ArrayList<OpenTradeItem>> accepterTradeItems,
			ArrayList<String> userTradeItemAttrsOne, ArrayList<String> userTradeItemAttrsTwo, ArrayList<String> userTradeItemAttrsThree,
			ArrayList<String> userTradeItemAttrs, String collectibleType) {
		this.posterUserNames = posterUserNames;
		this.accepterUserNames = accepterUserNames;
		this.openTradeDeals = openTradeDeals;
		this.posterTradeItems = posterTradeItems;
		this.accepterTradeItems = accepterTradeItems;
		this.userTradeItemAttrsOne = userTradeItemAttrsOne;
		this.userTradeItemAttrsTwo = userTradeItemAttrsTwo;
		this.userTradeItemAttrsThree = userTradeItemAttrsThree;
		this.userTradeItemAttrs = userTradeItemAttrs;
		this.collectibleType = collectibleType;
	}
	
	public String getCollectibleType() {
		return collectibleType;
	}
	
	public void setCollectibleType(String collectibleType) {
		this.collectibleType = collectibleType;
	}

	public ArrayList<String> getPosterUserNames() {
		return posterUserNames;
	}

	public ArrayList<String> getAccepterUserNames() {
		return accepterUserNames;
	}

	public ArrayList<OpenTradeDeal> getOpenTradeDeals() {
		return openTradeDeals;
	}

	public ArrayList<ArrayList<OpenTradeItem>> getPosterTradeItems() {
		return posterTradeItems;
	}

	public ArrayList<ArrayList<OpenTradeItem>> getAccepterTradeItems() {
		return accepterTradeItems;
	}

	public ArrayList<String> getUserTradeItemAttrsOne() {
		return userTradeItemAttrsOne;
	}

	public ArrayList<String> getUserTradeItemAttrsTwo() {
		return userTradeItemAttrsTwo;
	}

	public ArrayList<String> getUserTradeItemAttrsThree() {
		return userTradeItemAttrsThree;
	}

	public ArrayList<String> getUserTradeItemAttrs() {
		return userTradeItemAttrs;
	}

	public void setPosterUserNames(ArrayList<String> posterUserNames) {
		this.posterUserNames = posterUserNames;
	}

	public void setAccepterUserNames(ArrayList<String> accepterUserNames) {
		this.accepterUserNames = accepterUserNames;
	}

	public void setOpenTradeDeals(ArrayList<OpenTradeDeal> openTradeDeals) {
		this.openTradeDeals = openTradeDeals;
	}

	public void setPosterTradeItems(ArrayList<ArrayList<OpenTradeItem>> posterTradeItems) {
		this.posterTradeItems = posterTradeItems;
	}

	public void setAccepterTradeItems(ArrayList<ArrayList<OpenTradeItem>> accepterTradeItems) {
		this.accepterTradeItems = accepterTradeItems;
	}

	public void setUserTradeItemAttrsOne(ArrayList<String> userTradeItemAttrsOne) {
		this.userTradeItemAttrsOne = userTradeItemAttrsOne;
	}

	public void setUserTradeItemAttrsTwo(ArrayList<String> userTradeItemAttrsTwo) {
		this.userTradeItemAttrsTwo = userTradeItemAttrsTwo;
	}

	public void setUserTradeItemAttrsThree(ArrayList<String> userTradeItemAttrsThree) {
		this.userTradeItemAttrsThree = userTradeItemAttrsThree;
	}

	public void setUserTradeItemAttrs(ArrayList<String> userTradeItemAttrs) {
		this.userTradeItemAttrs = userTradeItemAttrs;
	}
}
