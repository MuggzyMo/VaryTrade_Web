package com.VaryTrade.Dao.Misc;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.VaryTrade.Model.Company;

@Repository
public class AppMiscRepository {
	@Autowired
	private MiscDao miscDao;
	
	public Company retrieveCompany() {
		return miscDao.retrieveCompany();
	}
	
	public ArrayList<String> retrieveStates() {
		return miscDao.retrieveStates();
	}
	
}
