package com.VaryTrade.Service.TradeDeal;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VaryTrade.Dao.TradeDeal.AppTradeDealRepository;
import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.ClosedTradeDeal;
import com.VaryTrade.Model.ClosedTradeItem;
import com.VaryTrade.Model.OpenTradeDeal;
import com.VaryTrade.Model.OpenTradeItem;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebTradeDeal;
import com.VaryTrade.Model.WebTradeDealOne;
import com.VaryTrade.Model.WebTradeDealThree;
import com.VaryTrade.Model.WebTradeDealTwo;
import com.VaryTrade.Model.WebTradeItem;

@Service
public class TradeDealServiceImpl implements TradeDealService {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppTradeDealRepository appTradeDealRepository;
	
	@Override
	public ArrayList<ArrayList<ClosedTradeItem>> retrievePosterTradeItemsForCollectorClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal) {
		ArrayList<ArrayList<ClosedTradeItem>> posterTradeItems = new ArrayList<>();
		for(int i = 0; i < closedTradeDeal.size(); i++) {
			int id = closedTradeDeal.get(i).getId();
			posterTradeItems.add(appTradeDealRepository.retrieveClosedPosterTradeItems(id));
		}
		return posterTradeItems;
	}
	
	@Override
	public ArrayList<ArrayList<ClosedTradeItem>> retrieveAccepterTradeItemsForCollectorClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal) {
		ArrayList<ArrayList<ClosedTradeItem>> accepterTradeItems = new ArrayList<>();
		for(int i = 0; i < closedTradeDeal.size(); i++) {
			int id = closedTradeDeal.get(i).getId();
			accepterTradeItems.add(appTradeDealRepository.retrieveClosedAccepterTradeItems(id));
		}
		return accepterTradeItems;
	}
	
	@Override
	public ArrayList<UserInfo> retrievePosterUserInfoForClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal) {
		ArrayList<UserInfo> posterInfo = new ArrayList<>();
		for(int i = 0; i < closedTradeDeal.size(); i++) {
			String email = closedTradeDeal.get(i).getPosterEmail();
			posterInfo.add(appUserRepository.retrieveUserInfoByEmail(email));
		}
		return posterInfo;
	}
	
	@Override
	public ArrayList<UserInfo> retrieveAccepterUserInfoForClosedTrades(ArrayList<ClosedTradeDeal> closedTradeDeal) {
		ArrayList<UserInfo> accepterInfo = new ArrayList<>();
		for(int i = 0; i < closedTradeDeal.size(); i++) {
			String email = closedTradeDeal.get(i).getAccepterEmail();
			accepterInfo.add(appUserRepository.retrieveUserInfoByEmail(email));
		}
		return accepterInfo;
	}
	
	@Override
	public ArrayList<UserInfo> retrievePosterUserInfoForOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal) {
		ArrayList<UserInfo> posterInfo = new ArrayList<>();
		for(int i = 0; i < openTradeDeal.size(); i++) {
			String email = openTradeDeal.get(i).getPosterEmail();
			posterInfo.add(appUserRepository.retrieveUserInfoByEmail(email));
		}
		return posterInfo;
	}
	
	@Override
	public ArrayList<UserInfo> retrieveAccepterUserInfoForOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal) {
		ArrayList<UserInfo> accepterInfo = new ArrayList<>();
		for(int i = 0; i < openTradeDeal.size(); i++) {
			String email = openTradeDeal.get(i).getAccepterEmail();
			accepterInfo.add(appUserRepository.retrieveUserInfoByEmail(email));
		}
		return accepterInfo;
	}
	
	@Override
	public ArrayList<ArrayList<OpenTradeItem>> retrievePosterTradeItemsForCollectorOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal) {
		ArrayList<ArrayList<OpenTradeItem>> posterTradeItems = new ArrayList<>();
		for(int i = 0; i < openTradeDeal.size(); i++) {
			int id = openTradeDeal.get(i).getId();
			posterTradeItems.add(appTradeDealRepository.retrieveOpenPosterTradeItems(id));
		}
		return posterTradeItems;
	}
	
	@Override
	public ArrayList<ArrayList<OpenTradeItem>> retrieveAccepterTradeItemsForCollectorOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal) {
		ArrayList<ArrayList<OpenTradeItem>> accepterTradeItems = new ArrayList<>();
		for(int i = 0; i < openTradeDeal.size(); i++) {
			int id = openTradeDeal.get(i).getId();
			accepterTradeItems.add(appTradeDealRepository.retrieveOpenAccepterTradeItems(id));
		}
		return accepterTradeItems;
	}
	
	@Override
	public ArrayList<UserInfo> retrieveUserInfoForOpenTrades(ArrayList<OpenTradeDeal> openTradeDeal) {
		ArrayList<UserInfo> posterInfo = new ArrayList<>();
		for(int i = 0; i < openTradeDeal.size(); i++) {
			String email = openTradeDeal.get(i).getPosterEmail();
			posterInfo.add(appUserRepository.retrieveUserInfoByEmail(email));
		}
		return posterInfo;
	}
	
	@Override
	public WebTradeDeal addTradeItem(WebTradeDeal webTradeDeal, WebTradeItem webTradeItem, int type) {
		if(webTradeDeal == null) {
			if(type == 1) {
				webTradeDeal = new WebTradeDealOne();
			}
			else if(type == 2){
				webTradeDeal = new WebTradeDealTwo();	
			}
			else {
				webTradeDeal = new WebTradeDealThree();
			}
		}		
		if(webTradeItem.getType().equals("Trade")) {
			webTradeItem.setId(webTradeDeal.getPosterTradeItems().size());
			webTradeDeal.getPosterTradeItems().add(webTradeItem);
		}
		else {
			webTradeItem.setId(webTradeDeal.getAccepterTradeItems().size());
			webTradeDeal.getAccepterTradeItems().add(webTradeItem);
		}
		return webTradeDeal;
	}
	
	@Override
	public boolean doesOpenTradeDealBelongToCollector(OpenTradeDeal openTradeDeal, String email) {
		return email.equals(openTradeDeal.getPosterEmail());
	}
	
	@Override
	public boolean doesPendingTradeDealBelongToCollector(OpenTradeDeal openTradeDeal, String email) {
		return !openTradeDeal.getAccepterEmail().isEmpty() && (email.equals(openTradeDeal.getPosterEmail()) || email.equals(openTradeDeal.getAccepterEmail()));
	}
	
	@Override
	public boolean doesClosedTradeDealBelongToCollector(ClosedTradeDeal closedTradeDeal, String email) {
		return email.equals(closedTradeDeal.getAccepterEmail()) || email.equals(closedTradeDeal.getPosterEmail());
	}
	
	@Override
	public ArrayList<WebTradeItem> removeTradeItem(ArrayList<WebTradeItem> tradeItems, int index) {
		tradeItems.remove(index);
		return tradeItems;
	}
	
	@Override
	public ArrayList<WebTradeItem> adjustTradeItemIds(ArrayList<WebTradeItem> tradeItems)  {
		for(int i = 0; i < tradeItems.size(); i++) {
			tradeItems.get(i).setId(i);
		}
		return tradeItems;
	}
	
	@Override
	public ClosedTradeDeal mapPendingTradeDealToFailedClosedTradeDeal(OpenTradeDeal openTradeDeal) {
		return new ClosedTradeDeal(-1, openTradeDeal.getPosterEmail(), openTradeDeal.getAccepterEmail(),
				openTradeDeal.getPostDate(), openTradeDeal.getAcceptDate(), "Failed", LocalDate.now().toString(), openTradeDeal.getItemType());
	}
	
	@Override
	public ClosedTradeDeal mapPendingTradeDealToPassedClosedTradeDeal(OpenTradeDeal openTradeDeal) {
		return new ClosedTradeDeal(-1, openTradeDeal.getPosterEmail(), openTradeDeal.getAccepterEmail(),
				openTradeDeal.getPostDate(), openTradeDeal.getAcceptDate(), "Passed", LocalDate.now().toString(), openTradeDeal.getItemType());
	}
}
