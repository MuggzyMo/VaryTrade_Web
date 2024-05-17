package com.VaryTrade.Dao.ResaleDeal;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.VaryTrade.Model.ClosedResaleDeal;
import com.VaryTrade.Model.OpenResaleDeal;

@Repository
public class AppResaleDealRepository {
	@Autowired
	private ResaleDealDao resaleDao;
	
	public OpenResaleDeal retrievePendingResaleDealById(int id) {
		return resaleDao.retrievePendingResaleDealById(id);
	}
	
	public ClosedResaleDeal retrieveClosedResaleDeal(int id) {
		return resaleDao.retrieveClosedResaleDeal(id);
	}
	
	public ArrayList<ClosedResaleDeal> retrieveAllClosedResaleDealsByEmail(String email, int type) {
		return resaleDao.retrieveAllClosedResaleDealsByEmail(email, type);
	}
	
	public void insertClosedResaleDeal(int id, ClosedResaleDeal closedResaleDeal) {
		resaleDao.insertClosedResaleDeal(id, closedResaleDeal);
	}
	
	public ArrayList<OpenResaleDeal> retrieveAllPendingResaleDeals(int type) {
		return resaleDao.retrieveAllPendingResaleDeals(type);
	}
	
	public void updateOpenResaleDealAsAccepted(int id, String accepterEmail, BigDecimal price, boolean creditUsed) {
		resaleDao.updateOpenResaleDealAsAccepted(id, accepterEmail, price, creditUsed);
	}
	
	public void deleteOpenResale(int id) {
		resaleDao.deleteOpenResale(id);
	}
	
	public ArrayList<OpenResaleDeal> retrieveRecentOpenResaleDealsByPosterEmail(String email, int type) {
		return resaleDao.retrieveRecentOpenResaleDealsByPosterEmail(email, type);
	}
	
	public ArrayList<OpenResaleDeal> retrieveAllOpenResaleDealsByPosterEmail(String email, int type) {
		return resaleDao.retrieveAllOpenResaleDealsByPosterEmail(email, type);
	}
	
	public ArrayList<OpenResaleDeal> retrieveRecentPendingResaleDealsByEmail(String email, int type) {
		return resaleDao.retrieveRecentPendingResaleDealsByEmail(email, type);
	}
	
	public ArrayList<OpenResaleDeal> retrieveAllPendingResaleDealsByEmail(String email, int type) {
		return resaleDao.retrieveAllPendingResaleDealsByEmail(email, type);
	}
	
	public OpenResaleDeal retrieveOpenResaleDealById(int id) {
		return resaleDao.retrieveOpenResaleDealById(id);
	}
	
	public ArrayList<OpenResaleDeal> retrieveRecentOpenResaleDealsNotFromCollector(String email, int type) {
		return resaleDao.retrieveRecentOpenResaleDealsNotFromCollector(email, type);
	}
	
	public ArrayList<OpenResaleDeal> retrieveOpenResaleDealsNotFromCollector(String email, int type) {
		return resaleDao.retrieveOpenResaleDealsNotFromCollector(email, type);
	}
	
	public OpenResaleDeal insertOpenResaleDeal(OpenResaleDeal openResaleDeal, int type) {
		return resaleDao.insertOpenResaleDeal(openResaleDeal, type);
	}
}
