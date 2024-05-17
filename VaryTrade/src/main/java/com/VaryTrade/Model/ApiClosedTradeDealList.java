package com.VaryTrade.Model;

import java.util.ArrayList;

public class ApiClosedTradeDealList {
	private ArrayList<String> posterUserNames;
	private ArrayList<String> accepterUserNames;
	private ArrayList<ClosedTradeDeal> closedTradeDeals;
	private ArrayList<ArrayList<ClosedTradeItem>> posterTradeItems;
	private ArrayList<ArrayList<ClosedTradeItem>> accepterTradeItems;
	private ArrayList<String> userTradeItemAttrsOne;
	private ArrayList<String> userTradeItemAttrsTwo;
	private ArrayList<String> userTradeItemAttrsThree;
	private ArrayList<String> userTradeItemAttrs;
	private String collectibleType;

	public ApiClosedTradeDealList(ArrayList<String> posterUserNames, ArrayList<String> accepterUserNames, ArrayList<ClosedTradeDeal> closedTradeDeals,
			ArrayList<ArrayList<ClosedTradeItem>> posterTradeItems,
			ArrayList<ArrayList<ClosedTradeItem>> accepterTradeItems, ArrayList<String> userTradeItemAttrsOne,
			ArrayList<String> userTradeItemAttrsTwo, ArrayList<String> userTradeItemAttrsThree,
			ArrayList<String> userTradeItemAttrs, String collectibleType) {
		this.posterUserNames = posterUserNames;
		this.accepterUserNames = accepterUserNames;
		this.closedTradeDeals = closedTradeDeals;
		this.posterTradeItems = posterTradeItems;
		this.accepterTradeItems = accepterTradeItems;
		this.userTradeItemAttrsOne = userTradeItemAttrsOne;
		this.userTradeItemAttrsTwo = userTradeItemAttrsTwo;
		this.userTradeItemAttrsThree = userTradeItemAttrsThree;
		this.userTradeItemAttrs = userTradeItemAttrs;
		this.collectibleType = collectibleType;
	}
	
	public ArrayList<String> getPosterUserNames() {
		return posterUserNames;
	}

	public ArrayList<String> getAccepterUserNames() {
		return accepterUserNames;
	}
	
	public void setPosterUserNames(ArrayList<String> posterUserNames) {
		this.posterUserNames = posterUserNames;
	}

	public void setAccepterUserNames(ArrayList<String> accepterUserNames) {
		this.accepterUserNames = accepterUserNames;
	}

	public String getCollectibleType() {
		return collectibleType;
	}

	public void setCollectibleType(String collectibleType) {
		this.collectibleType = collectibleType;
	}

	public ArrayList<ClosedTradeDeal> getClosedTradeDeals() {
		return closedTradeDeals;
	}

	public ArrayList<ArrayList<ClosedTradeItem>> getPosterTradeItems() {
		return posterTradeItems;
	}

	public ArrayList<ArrayList<ClosedTradeItem>> getAccepterTradeItems() {
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

	public void setClosedTradeDeals(ArrayList<ClosedTradeDeal> closedTradeDeals) {
		this.closedTradeDeals = closedTradeDeals;
	}

	public void setPosterTradeItems(ArrayList<ArrayList<ClosedTradeItem>> posterTradeItems) {
		this.posterTradeItems = posterTradeItems;
	}

	public void setAccepterTradeItems(ArrayList<ArrayList<ClosedTradeItem>> accepterTradeItems) {
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