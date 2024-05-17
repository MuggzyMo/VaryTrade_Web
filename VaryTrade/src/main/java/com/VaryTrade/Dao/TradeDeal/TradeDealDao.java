package com.VaryTrade.Dao.TradeDeal;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.VaryTrade.Model.ClosedTradeDeal;
import com.VaryTrade.Model.ClosedTradeItem;
import com.VaryTrade.Model.OpenTradeDeal;
import com.VaryTrade.Model.OpenTradeItem;
import com.VaryTrade.Model.TradeItem;
import com.VaryTrade.Model.WebTradeDeal;
import com.VaryTrade.Model.WebTradeDealApi;

@Repository
public interface TradeDealDao {
	public ArrayList<String> retrieveCollectibleTypeNames();
	public ArrayList<TradeItem> retrieveTradeItems(int type);
	public ArrayList<String> retrieveUserTradeItemAttributes(int type);
	public ArrayList<String> retrieveUserTradeItemOne(int type);
	public ArrayList<String> retrieveUserTradeItemTwo(int type);
	public ArrayList<String> retrieveUserTradeItemThree(int type);
	public ArrayList<String> retrieveConditions(int type);
	public ArrayList<String> retrieveTradeItemTypes();
	public OpenTradeDeal insertOpenTradeDeal(WebTradeDeal webTradeDeal, String email, int itemType);
	public OpenTradeDeal insertOpenTradeDealFromApiRequest(WebTradeDealApi webTradeDealApi, String email, int itemType);
	public OpenTradeDeal retrieveOpenTradeDealById(int id);
	public ArrayList<OpenTradeItem> retrieveOpenPosterTradeItems(int openTradeDealId);
	public ArrayList<OpenTradeItem> retrieveOpenAccepterTradeItems(int openTradeDealId);
	public ArrayList<OpenTradeDeal> retrieveAllOpenTradeDealsByPosterEmail(String email, int type);
	public ArrayList<OpenTradeDeal> retrieveRecentOpenTradeDealsByPosterEmail(String email, int type);
	public ArrayList<OpenTradeDeal> retrieveRecentOpenTradeDealsNotFromCollector(String email, int type);
	public ArrayList<OpenTradeDeal> retrieveOpenTradeDealsNotFromCollector(String email, int type);
	public void deleteOpenTrade(int id);
	public void updateOpenTradeDealAsAccepted(int id, String accepterEmail, boolean creditUsed);
	public ArrayList<OpenTradeDeal> retrieveAllPendingTradeDealsByEmail(String email, int type);
	public ArrayList<OpenTradeDeal> retrieveRecentPendingTradeDealsByEmail(String email, int type);
	public ArrayList<ClosedTradeDeal> retrieveAllClosedTradeDealsByEmail(String email, int type);
	public ArrayList<ClosedTradeItem> retrieveClosedAccepterTradeItems(int closedTradeDealId);
	public ArrayList<ClosedTradeItem> retrieveClosedPosterTradeItems(int closedTradeDealId);
	public ArrayList<OpenTradeDeal> retrieveAllPendingTradeDeals(int type);
	public void insertClosedTradeDeal(int pendingTradeDealId, ClosedTradeDeal closedTradeDeal, ArrayList<OpenTradeItem> pendingPosterTradeItems,
			ArrayList<OpenTradeItem> pendingAccepterTradeItems);
	public ClosedTradeDeal retrieveClosedTradeDealById(int id);
	public OpenTradeDeal retrievePendingTradeDealById(int id);
	public ArrayList<String> retrieveTradeItemNames(int type);
}
