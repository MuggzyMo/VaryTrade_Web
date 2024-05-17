package com.VaryTrade.Model;

import java.util.ArrayList;

public class WebTradeDealApi {
	private ArrayList<WebTradeItemApi> posterTradeItems;
	private ArrayList<WebTradeItemApi> accepterTradeItems;
	private int type;
	
	public WebTradeDealApi(ArrayList<WebTradeItemApi> posterTradeItems, ArrayList<WebTradeItemApi> accepterTradeItems, int type) {
		this.posterTradeItems = posterTradeItems;
		this.accepterTradeItems = accepterTradeItems;
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

	public ArrayList<WebTradeItemApi> getPosterTradeItems() {
		return posterTradeItems;
	}

	public ArrayList<WebTradeItemApi> getAccepterTradeItems() {
		return accepterTradeItems;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public void setPosterTradeItems(ArrayList<WebTradeItemApi> posterTradeItems) {
		this.posterTradeItems = posterTradeItems;	
	}

	public void setAccepterTradeItems(ArrayList<WebTradeItemApi> accepterTradeItems) {
		this.accepterTradeItems = accepterTradeItems;
	}
}
