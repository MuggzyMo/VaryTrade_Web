package com.VaryTrade.Model;

public class TradeItem {
	private String name;
	private int itemType;
	
	public TradeItem() {}
	
	public TradeItem(String name, int itemType) {
		this.name = name;
		this.itemType = itemType;
	}
	
	public String getName() {
		return name;
	}

	public int getItemType() {
		return itemType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setItemType(int itemType) {
		this.itemType = itemType;
	}
}
