package com.VaryTrade.Model;

import java.util.ArrayList;

public interface WebTradeDeal {
	public ArrayList<WebTradeItem> getPosterTradeItems();
	public ArrayList<WebTradeItem> getAccepterTradeItems();
	public void setPosterTradeItems(ArrayList<WebTradeItem> posterTradeItems);
	public void setAccepterTradeItems(ArrayList<WebTradeItem> accepterTradeItems);
}
