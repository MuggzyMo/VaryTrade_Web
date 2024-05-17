package com.VaryTrade.Dao.Misc;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.VaryTrade.Model.Company;

@Repository
public interface MiscDao {
	public Company retrieveCompany();
	public ArrayList<String> retrieveStates();
}
