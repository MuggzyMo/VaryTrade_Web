package com.VaryTrade.Dao.Misc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.VaryTrade.Model.Company;

@Repository
public class MiscDaoImpl implements MiscDao {
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}	
	
	@Override
	public Company retrieveCompany() {
		String query = "select * from company";
		return jdbcTemplate.queryForObject(query, new CompanyMapper());	
	}

	@Override
	public ArrayList<String> retrieveStates() {
		String query = "select * from state";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class);
	}
	
	class CompanyMapper implements RowMapper<Company> {

		@Override
		public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
			Company company = new Company();
			company.setName(rs.getString("name"));
			company.setEmail(rs.getString("email"));
			company.setZipCode(rs.getString("zip_code"));
			company.setState(rs.getString("state"));
			company.setCity(rs.getString("city"));
			company.setAddress(rs.getString("address"));
			company.setPhoneNum(rs.getString("phone_number"));
			return company;
		}
		
	}
	
}
