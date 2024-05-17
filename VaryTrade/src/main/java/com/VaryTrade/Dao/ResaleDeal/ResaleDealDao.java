package com.VaryTrade.Dao.ResaleDeal;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.VaryTrade.Model.ClosedResaleDeal;
import com.VaryTrade.Model.OpenResaleDeal;

@Repository
public interface ResaleDealDao {
	public ClosedResaleDeal retrieveClosedResaleDeal(int id);
	public ArrayList<ClosedResaleDeal> retrieveAllClosedResaleDealsByEmail(String email, int type);
	public OpenResaleDeal insertOpenResaleDeal(OpenResaleDeal OpenResaleDeal, int itemType);
	public ArrayList<OpenResaleDeal> retrieveOpenResaleDealsNotFromCollector(String email, int type);
	public ArrayList<OpenResaleDeal> retrieveRecentOpenResaleDealsNotFromCollector(String email, int type);
	public OpenResaleDeal retrieveOpenResaleDealById(int id);
	public ArrayList<OpenResaleDeal> retrieveRecentOpenResaleDealsByPosterEmail(String email, int type);
	public ArrayList<OpenResaleDeal> retrieveAllOpenResaleDealsByPosterEmail(String email, int type);
	public ArrayList<OpenResaleDeal> retrieveRecentPendingResaleDealsByEmail(String email, int type);
	public ArrayList<OpenResaleDeal> retrieveAllPendingResaleDealsByEmail(String email, int type);	
	public void deleteOpenResale(int id);
	public void updateOpenResaleDealAsAccepted(int id, String accepterEmail, BigDecimal price, boolean creditUsed);
	public ArrayList<OpenResaleDeal> retrieveAllPendingResaleDeals(int type);
	public void insertClosedResaleDeal(int id, ClosedResaleDeal closedResaleDeal);
	public OpenResaleDeal retrievePendingResaleDealById(int id);
}
