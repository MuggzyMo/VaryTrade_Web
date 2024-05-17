package com.VaryTrade.Dao.ResaleDeal;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.VaryTrade.Model.ClosedResaleDeal;
import com.VaryTrade.Model.OpenResaleDeal;

@Repository
public class ResaleDealDaoImpl implements ResaleDealDao {
	@Autowired
	private DataSourceTransactionManager transactionManager;

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public OpenResaleDeal retrievePendingResaleDealById(int id) {
		String query = "select * from open_resale_deal where id = ? and pending_completion = 'f'";
		return jdbcTemplate.queryForObject(query, new OpenResaleDealRowMapper(), new Object[] { id });
	}

	@Override
	public ClosedResaleDeal retrieveClosedResaleDeal(int id) {
		try {
			String query = "select * from closed_resale_deal where id = ?";
			return jdbcTemplate.queryForObject(query, new ClosedResaleDealRowMapper(), id);
		} catch (DataAccessException e) {
			System.out.println("Error in retrieving closed resale deal.");
			return null;
		}
	}

	@Override
	public ArrayList<ClosedResaleDeal> retrieveAllClosedResaleDealsByEmail(String email, int type) {
		String query = "select * from closed_resale_deal where (poster_email = ? or accepter_email = ?) and type = ?";
		return (ArrayList<ClosedResaleDeal>) jdbcTemplate.query(query, new ClosedResaleDealRowMapper(), email, email, type);
	}

	@Override
	public void insertClosedResaleDeal(int id, ClosedResaleDeal closedResaleDeal) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			privInsertClosedResaleDeal(closedResaleDeal);
			deletePendingResaleDeal(id);
			if (closedResaleDeal.getAuthenticityStatus().equals("Passed")) {
				addToUserCredits(closedResaleDeal.getPosterEmail(), closedResaleDeal.getPrice());
			} else {
				addToUserCredits(closedResaleDeal.getAccepterEmail(), closedResaleDeal.getPrice());
			}
			transactionManager.commit(status);
		} catch (DataAccessException e) {
			System.out.println("Error in inserting closed resale deal, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public ArrayList<OpenResaleDeal> retrieveAllPendingResaleDeals(int type) {
		String query = "select * from open_resale_deal where pending_completion = 't' and type = ?";
		return (ArrayList<OpenResaleDeal>) jdbcTemplate.query(query, new OpenResaleDealRowMapper(), type);
	}

	@Override
	public void updateOpenResaleDealAsAccepted(int id, String accepterEmail, BigDecimal price, boolean creditUsed) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			String sql = "update open_resale_deal set accepter_email = ?, pending_completion = 't', accepted_date = ? where id = ?";
			jdbcTemplate.update(sql, accepterEmail, Date.valueOf(LocalDate.now().toString()), id);
			if (creditUsed) {
				subtractFromAccepterUserCredits(accepterEmail, price);
			}
			transactionManager.commit(status);

		} catch (DataAccessException e) {
			System.out.println("Error in updating open resale deal, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public OpenResaleDeal insertOpenResaleDeal(OpenResaleDeal openResaleDeal, int type) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			String sql = "insert into open_resale_deal(posted_date, poster_email, price, name, condition, "
					+ "pending_completion, user_resale_item_attr_one, user_resale_item_attr_two, user_resale_item_attr_three, type) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, Date.valueOf(openResaleDeal.getPostDate()), openResaleDeal.getPosterEmail(),
					openResaleDeal.getPrice(), openResaleDeal.getName(), openResaleDeal.getCondition(),
					openResaleDeal.isPendingCompletion(), openResaleDeal.getUserResaleItemAttrOne(),
					openResaleDeal.getUserResaleItemAttrTwo(), openResaleDeal.getUserResaleItemAttrThree(), type);

			String query = "select * from open_resale_deal order by id desc limit 1";
			openResaleDeal = jdbcTemplate.queryForObject(query, new OpenResaleDealRowMapper());
			transactionManager.commit(status);
			return openResaleDeal;
		} catch (DataAccessException e) {
			System.out.println("Error in inserting open resale deal, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public void deleteOpenResale(int id) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			String sql = "delete from open_resale_deal where id = ?";
			jdbcTemplate.update(sql, id);
			transactionManager.commit(status);
		} catch (DataAccessException e) {
			System.out.println("Error in deleting open resale deal, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public ArrayList<OpenResaleDeal> retrieveRecentOpenResaleDealsByPosterEmail(String email, int type) {
		String query = "select * from open_resale_deal where poster_email = ? and type = ? and pending_completion = 'f' order by id desc limit 5";
		return (ArrayList<OpenResaleDeal>) jdbcTemplate.query(query, new OpenResaleDealRowMapper(),
				new Object[] { email, type });
	}

	@Override
	public ArrayList<OpenResaleDeal> retrieveAllOpenResaleDealsByPosterEmail(String email, int type) {
		String query = "select * from open_resale_deal where poster_email = ? and type = ? and pending_completion = 'f'";
		return (ArrayList<OpenResaleDeal>) jdbcTemplate.query(query, new OpenResaleDealRowMapper(),
				new Object[] { email, type });
	}

	@Override
	public ArrayList<OpenResaleDeal> retrieveRecentPendingResaleDealsByEmail(String email, int type) {
		String query = "select * from open_resale_deal where (poster_email = ? or accepter_email = ?) and type = ? and pending_completion = 't' order by id desc limit 5";
		return (ArrayList<OpenResaleDeal>) jdbcTemplate.query(query, new OpenResaleDealRowMapper(), email, email, type);
	}

	@Override
	public ArrayList<OpenResaleDeal> retrieveAllPendingResaleDealsByEmail(String email, int type) {
		String query = "select * from open_resale_deal where (poster_email = ? or accepter_email = ?) and type = ? and pending_completion = 't'";
		return (ArrayList<OpenResaleDeal>) jdbcTemplate.query(query, new OpenResaleDealRowMapper(), email, email, type);
	}

	@Override
	public OpenResaleDeal retrieveOpenResaleDealById(int id) {
		try {
			String query = "select * from open_resale_deal where id = ?";
			return jdbcTemplate.queryForObject(query, new OpenResaleDealRowMapper(), id);
		} catch (DataAccessException e) {
			System.out.println("Error retrieving open resale deal.");
			return null;
		}
	}

	@Override
	public ArrayList<OpenResaleDeal> retrieveRecentOpenResaleDealsNotFromCollector(String email, int type) {
		String query = "select * from open_resale_deal where poster_email != ? and type = ? and pending_completion = 'f' order by id desc limit 5";
		return (ArrayList<OpenResaleDeal>) jdbcTemplate.query(query, new OpenResaleDealRowMapper(), email, type);
	}

	@Override
	public ArrayList<OpenResaleDeal> retrieveOpenResaleDealsNotFromCollector(String email, int type) {
		String query = "select * from open_resale_deal where poster_email != ? and type = ? and pending_completion = 'f'";
		return (ArrayList<OpenResaleDeal>) jdbcTemplate.query(query, new OpenResaleDealRowMapper(), email, type);
	}

	private void addToUserCredits(String email, BigDecimal credit) {
		String query = "select credit from user_credit where email = ?";
		credit = credit.add(jdbcTemplate.queryForObject(query, BigDecimal.class, email));
		String insert = "update user_credit set credit = ? where email = ?";
		jdbcTemplate.update(insert, credit, email);

	}

	private void privInsertClosedResaleDeal(ClosedResaleDeal closedResaleDeal) {
		String sql = "insert into closed_resale_deal(posted_date, accepted_date, poster_email, accepter_email, price, name, "
				+ "condition, authenticity_status, authenticated_date, user_resale_item_attr_one, user_resale_item_attr_two,"
				+ "user_resale_item_attr_three, type) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, Date.valueOf(closedResaleDeal.getPostDate()),
				Date.valueOf(closedResaleDeal.getAcceptDate()), closedResaleDeal.getPosterEmail(),
				closedResaleDeal.getAccepterEmail(), closedResaleDeal.getPrice(), closedResaleDeal.getName(),
				closedResaleDeal.getCondition(), closedResaleDeal.getAuthenticityStatus(),
				Date.valueOf(closedResaleDeal.getAuthenticatedDate()), closedResaleDeal.getUserResaleItemAttrOne(),
				closedResaleDeal.getUserResaleItemAttrTwo(), closedResaleDeal.getUserResaleItemAttrThree(), closedResaleDeal.getItemType());
	}

	private void deletePendingResaleDeal(int id) {
		String sql = "delete from open_resale_deal where id = ?";
		jdbcTemplate.update(sql, id);
	}

	private void subtractFromAccepterUserCredits(String email, BigDecimal price) {
		String query = "select credit from user_credit where email = ?";
		BigDecimal credit = jdbcTemplate.queryForObject(query, BigDecimal.class, email);
		credit = credit.subtract(price);
		String insert = "update user_credit set credit = ? where email = ?";
		jdbcTemplate.update(insert, credit, email);
	}

	class OpenResaleDealRowMapper implements RowMapper<OpenResaleDeal> {

		@Override
		public OpenResaleDeal mapRow(ResultSet rs, int rowNum) throws SQLException {
			OpenResaleDeal openResaleDeal = new OpenResaleDeal();
			openResaleDeal.setId(rs.getInt("id"));
			openResaleDeal.setPosterEmail(rs.getString("poster_email"));
			openResaleDeal.setAccepterEmail(rs.getString("accepter_email"));
			openResaleDeal.setAcceptDate(String.valueOf(rs.getDate("accepted_date")));
			openResaleDeal.setCondition(rs.getString("condition"));
			openResaleDeal.setName(rs.getString("name"));
			openResaleDeal.setPendingCompletion(rs.getBoolean("pending_completion"));
			openResaleDeal.setPostDate(String.valueOf(rs.getDate("posted_date")));
			openResaleDeal.setPrice(rs.getBigDecimal("price"));
			openResaleDeal.setUserResaleItemAttrOne(rs.getString("user_resale_item_attr_one"));
			openResaleDeal.setUserResaleItemAttrTwo(rs.getString("user_resale_item_attr_two"));
			openResaleDeal.setUserResaleItemAttrThree(rs.getString("user_resale_item_attr_three"));
			openResaleDeal.setItemType(rs.getInt("type"));
			return openResaleDeal;
		}
	}

	class ClosedResaleDealRowMapper implements RowMapper<ClosedResaleDeal> {

		@Override
		public ClosedResaleDeal mapRow(ResultSet rs, int rowNum) throws SQLException {
			ClosedResaleDeal closedResaleDeal = new ClosedResaleDeal();
			closedResaleDeal.setId(rs.getInt("id"));
			closedResaleDeal.setName(rs.getString("name"));
			closedResaleDeal.setCondition(rs.getString("condition"));
			closedResaleDeal.setAuthenticityStatus(rs.getString("authenticity_status"));
			closedResaleDeal.setAccepterEmail(rs.getString("accepter_email"));
			closedResaleDeal.setPosterEmail(rs.getString("poster_email"));
			closedResaleDeal.setAuthenticatedDate(String.valueOf(rs.getDate("authenticated_date")));
			closedResaleDeal.setAcceptDate(String.valueOf(rs.getDate("accepted_date")));
			closedResaleDeal.setPostDate(String.valueOf(rs.getDate("posted_date")));
			closedResaleDeal.setPrice(rs.getBigDecimal("price"));
			closedResaleDeal.setUserResaleItemAttrOne(rs.getString("user_resale_item_attr_one"));
			closedResaleDeal.setUserResaleItemAttrTwo(rs.getString("user_resale_item_attr_two"));
			closedResaleDeal.setUserResaleItemAttrThree(rs.getString("user_resale_item_attr_three"));
			closedResaleDeal.setItemType(rs.getInt("type"));
			return closedResaleDeal;
		}
	}
}
