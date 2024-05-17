package com.VaryTrade.Service.ResaleDeal;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.VaryTrade.Model.ClosedResaleDeal;
import com.VaryTrade.Model.OpenResaleDeal;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebResaleDeal;
import com.VaryTrade.Model.WebResaleDealApi;

@Service
public class AppResaleDealService {
	@Autowired
	private ResaleDealService resaleDealService;
	
	public ArrayList<UserInfo> retrievePosterUserInfoForClosedResales(ArrayList<ClosedResaleDeal> closedResaleDeals) {
		return resaleDealService.retrievePosterUserInfoForClosedResales(closedResaleDeals);
	}
	
	public ArrayList<UserInfo> retrieveAccepterUserInfoForClosedResales(ArrayList<ClosedResaleDeal> closedResaleDeals) {
		return resaleDealService.retrieveAccepterUserInfoForClosedResales(closedResaleDeals);
	}
	
	public ArrayList<UserInfo> retrieveAccepterUserInfoForOpenResales(ArrayList<OpenResaleDeal> openResaleDeals) {
		return resaleDealService.retrieveAccepterUserInfoForOpenResales(openResaleDeals);
	}
	
	public OpenResaleDeal mapWebResaleDealApiToOpenResaleDeal(WebResaleDealApi webResaleDealApi, String email) {
		return resaleDealService.mapWebResaleDealApiToOpenResaleDeal(webResaleDealApi, email);
	}
	
	public ArrayList<UserInfo> retrievePosterUserInfoForOpenResales(ArrayList<OpenResaleDeal> openResaleDeals) {
		return resaleDealService.retrievePosterUserInfoForOpenResales(openResaleDeals);
	}
	
	public ClosedResaleDeal mapPendingResaleDealToPassedClosedResaleDeal(OpenResaleDeal pendingResaleDeal) {
		return resaleDealService.mapPendingResaleDealToPassedClosedResaleDeal(pendingResaleDeal);
	}
	
	public ClosedResaleDeal mapPendingResaleDealToFailedClosedResaleDeal(OpenResaleDeal pendingResaleDeal) {
		return resaleDealService.mapPendingResaleDealToFailedClosedResaleDeal(pendingResaleDeal);
	}
	
	public boolean doesPendingResaleDealBelongToCollector(OpenResaleDeal openResaleDeal, String email) {
		return resaleDealService.doesPendingResaleDealBelongToCollector(openResaleDeal, email);
	}
	
	public boolean doesClosedResaleDealBelongToCollector(ClosedResaleDeal closedResaleDeal, String email) {
		return resaleDealService.doesClosedResaleDealBelongToCollector(closedResaleDeal, email);
	}
	
	public boolean doesOpenResaleDealBelongToCollector(OpenResaleDeal openResaleDeal, String email) {
		return resaleDealService.doesOpenResaleDealBelongToCollector(openResaleDeal, email);
	}
	
	public OpenResaleDeal mapWebResaleDealToOpenResaleDeal(WebResaleDeal webResaleDeal, String email) {
		return resaleDealService.mapWebResaleDealToOpenResaleDeal(webResaleDeal, email);
	}
}
