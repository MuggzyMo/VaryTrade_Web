package com.VaryTrade.Dao.TradeDeal;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.VaryTrade.Model.ClosedTradeDeal;
import com.VaryTrade.Model.ClosedTradeItem;
import com.VaryTrade.Model.OpenTradeDeal;
import com.VaryTrade.Model.OpenTradeItem;
import com.VaryTrade.Model.TradeItem;
import com.VaryTrade.Model.WebTradeDeal;
import com.VaryTrade.Model.WebTradeDealApi;

@Repository
public class AppTradeDealRepository {
	@Autowired
	private TradeDealDao tradeDao;
	
	public ArrayList<String> retrieveTradeItemNames(int type) {
		return tradeDao.retrieveTradeItemNames(type);
	}
	
	public OpenTradeDeal insertOpenTradeDealApi(WebTradeDealApi webTradeDealApi, String email, int type) {
		return tradeDao.insertOpenTradeDealFromApiRequest(webTradeDealApi, email, type);
	}
	
	public ArrayList<String> retrieveCollectibleTypeNames() {
		return tradeDao.retrieveCollectibleTypeNames();
	}
	
	public OpenTradeDeal retrievePendingTradeDealById(int id) {
		return tradeDao.retrievePendingTradeDealById(id);
	}
	
	public ArrayList<TradeItem> retrieveTradeItems(int type) {
		return tradeDao.retrieveTradeItems(type);
	}
	
	public ArrayList<String> retrieveUserTradeItemAttributes(int type) {
		return tradeDao.retrieveUserTradeItemAttributes(type);
	}
	
	public ArrayList<String> retrieveUserTradeItemAttributeOne(int type) {
		return tradeDao.retrieveUserTradeItemOne(type);
	}
	
	public ArrayList<String> retrieveUserTradeItemAttributeTwo(int type) {
		return tradeDao.retrieveUserTradeItemTwo(type);
	}
	
	public ArrayList<String> retrieveUserTradeItemAttributeThree(int type) {
		return tradeDao.retrieveUserTradeItemThree(type);
	}
	
	public ArrayList<String> retrieveConditions(int type) {
		return tradeDao.retrieveConditions(type);
	}
	
	public ArrayList<String> retrieveTradeItemTypes() {
		return tradeDao.retrieveTradeItemTypes();
	}
	
	public OpenTradeDeal insertOpenTradeDeal(WebTradeDeal webTradeDeal, String email, int itemType) {
		return tradeDao.insertOpenTradeDeal(webTradeDeal, email, itemType);
	}
	
	public ArrayList<OpenTradeItem> retrieveOpenPosterTradeItems(int openTradeDealId) {
		return tradeDao.retrieveOpenPosterTradeItems(openTradeDealId);
	}
	
	public ArrayList<OpenTradeItem> retrieveOpenAccepterTradeItems(int openTradeDealId) {
		return tradeDao.retrieveOpenAccepterTradeItems(openTradeDealId);
	}
	
	public OpenTradeDeal retrieveOpenTradeDeal(int id) {
		return tradeDao.retrieveOpenTradeDealById(id);
	}
	
	public ArrayList<OpenTradeDeal> retrieveAllOpenTradeDealsByPosterEmail(String email, int type) {
		return tradeDao.retrieveAllOpenTradeDealsByPosterEmail(email, type);
	}
	
	public ArrayList<OpenTradeDeal> retrieveRecentOpenTradeDealsByPosterEmail(String email, int type) {
		return tradeDao.retrieveRecentOpenTradeDealsByPosterEmail(email, type);
	}
	
	public ArrayList<OpenTradeDeal> retrieveRecentOpenTradeDealsNotFromCollector(String email, int type) {
		return tradeDao.retrieveRecentOpenTradeDealsNotFromCollector(email, type);
	}
	
	public ArrayList<OpenTradeDeal> retrieveOpenTradeDealsNotFromCollector(String email, int type) {
		return tradeDao.retrieveOpenTradeDealsNotFromCollector(email, type);
	}
	
	public void deleteOpenTrade(int id) {
		tradeDao.deleteOpenTrade(id);
	}
	
	public void insertClosedTrade(int pendingTradeDealId, ClosedTradeDeal closedTradeDeal, ArrayList<OpenTradeItem> pendingPosterTradeItems,
			ArrayList<OpenTradeItem> pendingAccepterTradeItems) {
		tradeDao.insertClosedTradeDeal(pendingTradeDealId, closedTradeDeal, pendingPosterTradeItems, pendingAccepterTradeItems);
	}
		
	public void updateOpenTradeDealAsAccepted(int id, String accepterEmail, boolean creditUsed) {
		tradeDao.updateOpenTradeDealAsAccepted(id, accepterEmail, creditUsed);
	}
	
	public ArrayList<OpenTradeDeal> retrieveAllPendingTradeDealsByEmail(String email, int type) {
		return tradeDao.retrieveAllPendingTradeDealsByEmail(email, type);
	}
	
	public ClosedTradeDeal retrieveClosedTradeDealById(int id) {
		return tradeDao.retrieveClosedTradeDealById(id);
	}
	
	public ArrayList<ClosedTradeDeal> retrieveAllClosedTradeDealsByEmail(String email, int type) {
		return tradeDao.retrieveAllClosedTradeDealsByEmail(email, type);
	}
	
	public ArrayList<ClosedTradeItem> retrieveClosedPosterTradeItems(int closedTradeDealId) {
		return tradeDao.retrieveClosedPosterTradeItems(closedTradeDealId);
	}
	
	public ArrayList<ClosedTradeItem> retrieveClosedAccepterTradeItems(int closedTradeDealId) {
		return tradeDao.retrieveClosedAccepterTradeItems(closedTradeDealId);
	}
	
	public ArrayList<OpenTradeDeal> retrieveRecentPendingTradeDealsByEmail(String email, int type) {
		return tradeDao.retrieveRecentPendingTradeDealsByEmail(email, type);
	}
	
	public ArrayList<OpenTradeDeal> retrieveAllPendingTradeDeals(int type) {
		return tradeDao.retrieveAllPendingTradeDeals(type);
	}
}
