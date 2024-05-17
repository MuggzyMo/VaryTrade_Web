package com.VaryTrade.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class WebTradeDealOne implements Serializable, WebTradeDeal {
	private static final long serialVersionUID = 1L;
	private ArrayList<WebTradeItem> posterTradeItems;
	private ArrayList<WebTradeItem> accepterTradeItems;
	
	public WebTradeDealOne() {
		posterTradeItems = new ArrayList<WebTradeItem>();
		accepterTradeItems = new ArrayList<WebTradeItem>();
	}
	
	public WebTradeDealOne(ArrayList<WebTradeItem> posterTradeItems, ArrayList<WebTradeItem> accepterTradeItems) {
		this.posterTradeItems = posterTradeItems;
		this.accepterTradeItems = accepterTradeItems;
	}

	@Override
	public ArrayList<WebTradeItem> getPosterTradeItems() {
		return posterTradeItems;
	}
	
	@Override
	public ArrayList<WebTradeItem> getAccepterTradeItems() {
		return accepterTradeItems;
	}

	@Override
	public void setPosterTradeItems(ArrayList<WebTradeItem> posterTradeItems) {
		this.posterTradeItems = posterTradeItems;
	}
	
	@Override
	public void setAccepterTradeItems(ArrayList<WebTradeItem> accepterTradeItems) {
		this.accepterTradeItems = accepterTradeItems;
	}
}
