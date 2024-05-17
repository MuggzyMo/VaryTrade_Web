package com.VaryTrade.Service.ResaleDeal;

import java.util.ArrayList;

import com.VaryTrade.Model.ClosedResaleDeal;
import com.VaryTrade.Model.OpenResaleDeal;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebResaleDeal;
import com.VaryTrade.Model.WebResaleDealApi;

public interface ResaleDealService {
	public OpenResaleDeal mapWebResaleDealToOpenResaleDeal(WebResaleDeal webResaleDeal, String email);
	public OpenResaleDeal mapWebResaleDealApiToOpenResaleDeal(WebResaleDealApi webResaleDealApi, String email);
	public ClosedResaleDeal mapPendingResaleDealToPassedClosedResaleDeal(OpenResaleDeal pendingResaleDeal);
	public ClosedResaleDeal mapPendingResaleDealToFailedClosedResaleDeal(OpenResaleDeal pendingResaleDeal);
	public boolean doesPendingResaleDealBelongToCollector(OpenResaleDeal openResaleDeal, String email);
	public boolean doesClosedResaleDealBelongToCollector(ClosedResaleDeal closedResaleDeal, String email);
	public boolean doesOpenResaleDealBelongToCollector(OpenResaleDeal openResaleDeal, String email);
	public ArrayList<UserInfo> retrievePosterUserInfoForOpenResales(ArrayList<OpenResaleDeal> openResaleDeals);
	public ArrayList<UserInfo> retrieveAccepterUserInfoForOpenResales(ArrayList<OpenResaleDeal> openResaleDeals);
	public ArrayList<UserInfo> retrieveAccepterUserInfoForClosedResales(ArrayList<ClosedResaleDeal> closedResaleDeals);
	public ArrayList<UserInfo> retrievePosterUserInfoForClosedResales(ArrayList<ClosedResaleDeal> closedResaleDeals);
}
