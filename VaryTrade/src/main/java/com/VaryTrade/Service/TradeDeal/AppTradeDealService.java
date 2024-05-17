package com.VaryTrade.Service.TradeDeal;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VaryTrade.Model.ClosedTradeDeal;
import com.VaryTrade.Model.ClosedTradeItem;
import com.VaryTrade.Model.OpenTradeDeal;
import com.VaryTrade.Model.OpenTradeItem;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebTradeDeal;
import com.VaryTrade.Model.WebTradeItem;

@Service
public class AppTradeDealService {
	@Autowired
	private TradeDealService tradeDealService;
	
	public WebTradeDeal addTradeItem(WebTradeDeal webTradeDeal, WebTradeItem webTradeItem, int type) {
		return tradeDealService.addTradeItem(webTradeDeal, webTradeItem, type);
	}
	
	public boolean doesClosedTradeDealBelongToCollector(ClosedTradeDeal closedTradeDeal, String email) {
		return tradeDealService.doesClosedTradeDealBelongToCollector(closedTradeDeal, email);
	}
	
	public boolean doesPendingTradeDealBelongToCollector(OpenTradeDeal openTradeDeal, String email) {
		return tradeDealService.doesPendingTradeDealBelongToCollector(openTradeDeal, email);
	}
	
	public boolean doesOpenTradeDealBelongToCollector(OpenTradeDeal openTradeDeal, String email) {
		return tradeDealService.doesOpenTradeDealBelongToCollector(openTradeDeal, email);
	}
	
	public ArrayList<WebTradeItem> removeTradeItem(ArrayList<WebTradeItem> tradeItems, int index) {
		return tradeDealService.removeTradeItem(tradeItems, index);
	}
	
	public ArrayList<WebTradeItem> adjustTradeItemIds(ArrayList<WebTradeItem> tradeItems)  {
		return tradeDealService.adjustTradeItemIds(tradeItems);
	}
	
	public ClosedTradeDeal mapPendingTradeDealToFailedClosedTradeDeal(OpenTradeDeal openTradeDeal) {
		return tradeDealService.mapPendingTradeDealToFailedClosedTradeDeal(openTradeDeal);
	}
	
	public ClosedTradeDeal mapPendingTradeDealToPassedClosedTradeDeal(OpenTradeDeal openTradeDeal) {
		return tradeDealService.mapPendingTradeDealToPassedClosedTradeDeal(openTradeDeal);
	}
	
	public ArrayList<UserInfo> retrieveAccepterUserInfoForOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal) {
		return tradeDealService.retrieveAccepterUserInfoForOpenTrades(openTradeDeal);
	}
	
	public ArrayList<UserInfo> retrievePosterUserInfoForOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal) {
		return tradeDealService.retrievePosterUserInfoForOpenTrades(openTradeDeal);
	}
	
	public ArrayList<UserInfo> retrieveAccepterUserInfoForClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal) {
		return tradeDealService.retrieveAccepterUserInfoForClosedTrades(closedTradeDeal);
	}
	
	public ArrayList<UserInfo> retrievePosterUserInfoForClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal) {
		return tradeDealService.retrievePosterUserInfoForClosedTrades(closedTradeDeal);
	}
	
	public ArrayList<ArrayList<ClosedTradeItem>> retrieveAccepterTradeItemsForCollectorClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal) {
		return tradeDealService.retrieveAccepterTradeItemsForCollectorClosedTrades(closedTradeDeal);
	}
	
	public ArrayList<ArrayList<ClosedTradeItem>> retrievePosterTradeItemsForCollectorClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal) {
		return tradeDealService.retrievePosterTradeItemsForCollectorClosedTrades(closedTradeDeal);
	}
	
	public ArrayList<UserInfo> retrieveUserInfoForOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal) {
		return tradeDealService.retrieveUserInfoForOpenTrades(openTradeDeal);
	}
	
	public ArrayList<ArrayList<OpenTradeItem>> retrieveAccepterTradeItemsForCollectorOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal) {
		return tradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDeal);
	}
	
	public ArrayList<ArrayList<OpenTradeItem>> retrievePosterTradeItemsForCollectorOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal) {
		return tradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDeal);
	}
}
