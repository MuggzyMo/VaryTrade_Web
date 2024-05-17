package com.VaryTrade.Service.TradeDeal;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.VaryTrade.Model.ClosedTradeDeal;
import com.VaryTrade.Model.ClosedTradeItem;
import com.VaryTrade.Model.OpenTradeDeal;
import com.VaryTrade.Model.OpenTradeItem;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebTradeDeal;
import com.VaryTrade.Model.WebTradeItem;

@Service
public interface TradeDealService {
	public WebTradeDeal addTradeItem(WebTradeDeal webTradeDeal, WebTradeItem webTradeItem, int type);
	public boolean doesOpenTradeDealBelongToCollector(OpenTradeDeal openTradeDeal, String email);
	public boolean doesPendingTradeDealBelongToCollector(OpenTradeDeal openTradeDeal, String email);
	public boolean doesClosedTradeDealBelongToCollector(ClosedTradeDeal closedTradeDeal, String email);
	public ArrayList<WebTradeItem> removeTradeItem(ArrayList<WebTradeItem> tradeItems, int index);
	public ArrayList<WebTradeItem> adjustTradeItemIds(ArrayList<WebTradeItem> tradeItems);
	public ClosedTradeDeal mapPendingTradeDealToFailedClosedTradeDeal(OpenTradeDeal openTradeDeal);
	public ClosedTradeDeal mapPendingTradeDealToPassedClosedTradeDeal(OpenTradeDeal openTradeDeal);
	public ArrayList<ArrayList<OpenTradeItem>> retrievePosterTradeItemsForCollectorOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal);
	public ArrayList<ArrayList<OpenTradeItem>> retrieveAccepterTradeItemsForCollectorOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal);
	public ArrayList<UserInfo> retrieveUserInfoForOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal);
	public ArrayList<UserInfo> retrievePosterUserInfoForClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal);
	public ArrayList<UserInfo> retrieveAccepterUserInfoForClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal);
	public ArrayList<ArrayList<ClosedTradeItem>> retrievePosterTradeItemsForCollectorClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal);
	public ArrayList<ArrayList<ClosedTradeItem>> retrieveAccepterTradeItemsForCollectorClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal);
	public ArrayList<UserInfo> retrievePosterUserInfoForOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal);
	public ArrayList<UserInfo> retrieveAccepterUserInfoForOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal);
}
