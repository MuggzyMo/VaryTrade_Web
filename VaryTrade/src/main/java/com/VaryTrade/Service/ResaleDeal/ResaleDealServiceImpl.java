package com.VaryTrade.Service.ResaleDeal;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.ClosedResaleDeal;
import com.VaryTrade.Model.OpenResaleDeal;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebResaleDeal;
import com.VaryTrade.Model.WebResaleDealApi;

@Service
public class ResaleDealServiceImpl implements ResaleDealService {
	@Autowired
	private AppUserRepository appUseRepository;
	
	@Override
	public ArrayList<UserInfo> retrieveAccepterUserInfoForClosedResales(ArrayList<ClosedResaleDeal> closedResaleDeals) {
		ArrayList<UserInfo> accepterInfo = new ArrayList<>();
		for(int i = 0; i < closedResaleDeals.size(); i++) {
			accepterInfo.add(appUseRepository.retrieveUserInfoByEmail(closedResaleDeals.get(i).getAccepterEmail()));
		}
		return accepterInfo;
	}
	
	@Override
	public ArrayList<UserInfo> retrievePosterUserInfoForClosedResales(ArrayList<ClosedResaleDeal> closedResaleDeals) {
		ArrayList<UserInfo> posterInfo = new ArrayList<>();
		for(int i = 0; i < closedResaleDeals.size(); i++) {
			posterInfo.add(appUseRepository.retrieveUserInfoByEmail(closedResaleDeals.get(i).getPosterEmail()));
		}
		return posterInfo;
	}
	
	@Override
	public OpenResaleDeal mapWebResaleDealApiToOpenResaleDeal(WebResaleDealApi webResaleDealApi, String email) {
		OpenResaleDeal openResaleDeal = new OpenResaleDeal();
		openResaleDeal.setPostDate(LocalDate.now().toString());
		openResaleDeal.setName(webResaleDealApi.getName());
		openResaleDeal.setCondition(webResaleDealApi.getCondition());
		openResaleDeal.setPosterEmail(email);
		openResaleDeal.setPendingCompletion(false);
		openResaleDeal.setPrice(webResaleDealApi.getPrice());
		openResaleDeal.setUserResaleItemAttrOne(webResaleDealApi.getUserResaleItemAttrOne());
		openResaleDeal.setUserResaleItemAttrTwo(webResaleDealApi.getUserResaleItemAttrTwo());
		openResaleDeal.setUserResaleItemAttrThree(webResaleDealApi.getUserResaleItemAttrThree());
		return openResaleDeal;
	}
	
	@Override
	public ArrayList<UserInfo> retrievePosterUserInfoForOpenResales(ArrayList<OpenResaleDeal> openResaleDeals) {
		ArrayList<UserInfo> posterInfo = new ArrayList<>();
		for(int i = 0; i < openResaleDeals.size(); i++) {
			posterInfo.add(appUseRepository.retrieveUserInfoByEmail(openResaleDeals.get(i).getPosterEmail()));
		}
		return posterInfo;
	}
	
	@Override
	public ArrayList<UserInfo> retrieveAccepterUserInfoForOpenResales(ArrayList<OpenResaleDeal> openResaleDeals) {
		ArrayList<UserInfo> accepterInfo = new ArrayList<>();
		for(int i = 0; i < openResaleDeals.size(); i++) {
			accepterInfo.add(appUseRepository.retrieveUserInfoByEmail(openResaleDeals.get(i).getAccepterEmail()));
		}
		return accepterInfo;
	}
	
	@Override
	public OpenResaleDeal mapWebResaleDealToOpenResaleDeal(WebResaleDeal webResaleDeal, String email) {
		OpenResaleDeal openResaleDeal = new OpenResaleDeal();
		openResaleDeal.setPostDate(LocalDate.now().toString());
		openResaleDeal.setName(webResaleDeal.getName());
		openResaleDeal.setCondition(webResaleDeal.getCondition());
		openResaleDeal.setPosterEmail(email);
		openResaleDeal.setPendingCompletion(false);
		openResaleDeal.setPrice(webResaleDeal.getPrice());
		openResaleDeal.setUserResaleItemAttrOne(webResaleDeal.getUserResaleItemAttrOne());
		openResaleDeal.setUserResaleItemAttrTwo(webResaleDeal.getUserResaleItemAttrTwo());
		openResaleDeal.setUserResaleItemAttrThree(webResaleDeal.getUserResaleItemAttrThree());
		return openResaleDeal;
	}
	
	@Override
	public ClosedResaleDeal mapPendingResaleDealToPassedClosedResaleDeal(OpenResaleDeal pendingResaleDeal) {
		System.out.println(pendingResaleDeal.getCondition());
		return new ClosedResaleDeal(-1, pendingResaleDeal.getPostDate(), pendingResaleDeal.getAcceptDate(), pendingResaleDeal.getAccepterEmail(),
				pendingResaleDeal.getPosterEmail(), pendingResaleDeal.getPrice(), pendingResaleDeal.getName(), pendingResaleDeal.getCondition(),
				pendingResaleDeal.getUserResaleItemAttrOne(), pendingResaleDeal.getUserResaleItemAttrTwo(), pendingResaleDeal.getUserResaleItemAttrThree(), 
				"Passed", LocalDate.now().toString(), pendingResaleDeal.getItemType());
	}
	
	@Override
	public ClosedResaleDeal mapPendingResaleDealToFailedClosedResaleDeal(OpenResaleDeal pendingResaleDeal) {
		return new ClosedResaleDeal(-1, pendingResaleDeal.getPostDate(), pendingResaleDeal.getAcceptDate(), pendingResaleDeal.getAccepterEmail(),
				pendingResaleDeal.getPosterEmail(), pendingResaleDeal.getPrice(), pendingResaleDeal.getName(), pendingResaleDeal.getCondition(),
				 pendingResaleDeal.getUserResaleItemAttrOne(), pendingResaleDeal.getUserResaleItemAttrTwo(), pendingResaleDeal.getUserResaleItemAttrThree(), 
				 "Failed", LocalDate.now().toString(), pendingResaleDeal.getItemType());
	}
	
	@Override
	public boolean doesPendingResaleDealBelongToCollector(OpenResaleDeal openResaleDeal, String email) {
		return !openResaleDeal.getAccepterEmail().isEmpty() && (email.equals(openResaleDeal.getPosterEmail()) || email.equals(openResaleDeal.getAccepterEmail()));
	}
	
	@Override
	public boolean doesClosedResaleDealBelongToCollector(ClosedResaleDeal closedResaleDeal, String email) {
		return closedResaleDeal.getAccepterEmail().equals(email) || closedResaleDeal.getPosterEmail().equals(email);
	}
	
	@Override
	public boolean doesOpenResaleDealBelongToCollector(OpenResaleDeal openResaleDeal, String email) {
		return email.equals(openResaleDeal.getPosterEmail());
	}
}
