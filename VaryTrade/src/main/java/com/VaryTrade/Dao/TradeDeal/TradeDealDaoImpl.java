package com.VaryTrade.Dao.TradeDeal;

import java.sql.ResultSet;
import java.math.BigDecimal;
import java.sql.Date;
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

import com.VaryTrade.Model.ClosedTradeDeal;
import com.VaryTrade.Model.ClosedTradeItem;
import com.VaryTrade.Model.OpenTradeDeal;
import com.VaryTrade.Model.TradeItem;
import com.VaryTrade.Model.WebTradeDeal;
import com.VaryTrade.Model.WebTradeDealApi;
import com.VaryTrade.Model.WebTradeItem;
import com.VaryTrade.Model.WebTradeItemApi;
import com.VaryTrade.Model.OpenTradeItem;

@Repository
public class TradeDealDaoImpl implements TradeDealDao{
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
	public OpenTradeDeal retrievePendingTradeDealById(int id) {
		String query = "select * from open_trade_deal where id = ? and pending_completion = 'f'";
		return jdbcTemplate.queryForObject(query, new OpenTradeDealRowMapper(), new Object[] { id } );
	}
	
	@Override
	public ArrayList<TradeItem> retrieveTradeItems(int type) {
		String query = "select * from trade_item where type = ?";
		return (ArrayList<TradeItem>) jdbcTemplate.query(query, new TradeItemRowMapper(), type);
	}
	
	@Override
	public ArrayList<String> retrieveTradeItemNames(int type) {
		String query = "select name from trade_item where type = ?";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class, type);
	}
	
	@Override
	public ArrayList<String> retrieveUserTradeItemAttributes(int type) {
		String query = "select attribute from user_trade_item_attribute where type = ?";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class, type);
	}
	
	@Override
	public ArrayList<String> retrieveUserTradeItemOne(int type) {
		String query = "select value from user_trade_item_string_attr_one where type = ?";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class, type);
	}
	
	@Override
	public ArrayList<String> retrieveCollectibleTypeNames() {
		String query = "select name from collectible_type_name";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class);
	}
	
	@Override
	public ArrayList<String> retrieveUserTradeItemTwo(int type) {
		String query = "select value from user_trade_item_string_attr_two where type = ?";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class, type);
	}
	
	@Override
	public ArrayList<String> retrieveUserTradeItemThree(int type) {
		String query = "select value from user_trade_item_string_attr_three where type = ?";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class, type);
	}
	
	@Override
	public ArrayList<String> retrieveConditions(int type) {
		String query = "select condition from condition where type = ?";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class, type);
	}
	
	@Override
	public ArrayList<String> retrieveTradeItemTypes() {
		String query = "select * from trade_item_type";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class);
	}
	
	@Override
	public OpenTradeDeal insertOpenTradeDeal(WebTradeDeal webTradeDeal, String email, int type) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			privInsertOpenTradeDeal(webTradeDeal, email, type);
			OpenTradeDeal openTradeDeal = retrieveLatestOpenTradeDeal();
			insertOpenPosterTradeItems(openTradeDeal, webTradeDeal.getPosterTradeItems());
			insertOpenAccepterTradeItems(openTradeDeal, webTradeDeal.getAccepterTradeItems());
			transactionManager.commit(status);
			return openTradeDeal;
		}
		catch(DataAccessException e) {
			System.out.println("Error in inserting open trade deal, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	@Override
	public OpenTradeDeal insertOpenTradeDealFromApiRequest(WebTradeDealApi webTradeDealApi, String email, int itemType) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			privInsertOpenTradeDealApi(webTradeDealApi, email, itemType);
			OpenTradeDeal openTradeDeal = retrieveLatestOpenTradeDeal();
			insertOpenPosterTradeItemsApi(openTradeDeal, webTradeDealApi.getPosterTradeItems());
			insertOpenAccepterTradeItemsApi(openTradeDeal, webTradeDealApi.getAccepterTradeItems());
			transactionManager.commit(status);
			return openTradeDeal;
		}
		catch(DataAccessException e) {
			System.out.println("Error in inserting open trade deal, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	@Override
	public void insertClosedTradeDeal(int pendingTradeDealId, ClosedTradeDeal closedTradeDeal, ArrayList<OpenTradeItem> pendingPosterTradeItems,
			ArrayList<OpenTradeItem> pendingAccepterTradeItems) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			deleteOpenTrade(pendingTradeDealId);
			insertClosedTradeDeal(closedTradeDeal);
			closedTradeDeal = retrieveLatestClosedTradeDeal();
			if(closedTradeDeal.getAuthenticityStatus().equals("Failed")) {
				addToUserCredits(closedTradeDeal.getAccepterEmail(), BigDecimal.valueOf(15.00));
			}
			insertClosedPosterTradeItems(mapPendingTradeItemsToClosedTradeItems(closedTradeDeal, pendingPosterTradeItems));
			insertClosedAccepterTradeItems(mapPendingTradeItemsToClosedTradeItems(closedTradeDeal, pendingAccepterTradeItems));
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error in inserting closed trade deal, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	@Override
	public void deleteOpenTrade(int id) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			deleteOpenPosterTradeItems(id);
			deleteOpenAccepterTradeItems(id);
			deleteOpenTradeDeal(id);
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error in deleting open trade deal, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	@Override
	public ClosedTradeDeal retrieveClosedTradeDealById(int id) {
		try {
			String query = "select * from closed_trade_deal where id = ?";
			return jdbcTemplate.queryForObject(query, new ClosedTradeDealRowMapper(), id);
		}
		catch(DataAccessException e) {
			System.out.println("Error in retrieving closed trade deal.");
			return null;
		}
	}
	
	@Override
	public OpenTradeDeal retrieveOpenTradeDealById(int id) {
		try {
			String query = "select * from open_trade_deal where id = ?";
			return jdbcTemplate.queryForObject(query, new OpenTradeDealRowMapper(), new Object[] { id } );
		}
		catch(DataAccessException e) {
			System.out.println("Error in retrieving open trade deal");
			return null;
		}
 		
	}
	
	@Override
	public ArrayList<OpenTradeItem> retrieveOpenPosterTradeItems(int openTradeDealId) {
		String query = "select * from open_poster_trade_item where open_trade_deal_id = ?";
		return (ArrayList<OpenTradeItem>) jdbcTemplate.query(query, new OpenTradeItemRowMapper(), new Object[] { openTradeDealId } );
	}
	
	@Override
	public ArrayList<OpenTradeItem> retrieveOpenAccepterTradeItems(int openTradeDealId) {
		String query = "select * from open_accepter_trade_item where open_trade_deal_id = ?";
		return (ArrayList<OpenTradeItem>) jdbcTemplate.query(query, new OpenTradeItemRowMapper(), new Object[] { openTradeDealId } );
	}
	
	@Override
	public ArrayList<OpenTradeDeal> retrieveAllOpenTradeDealsByPosterEmail(String email, int type) {
		String query = "select * from open_trade_deal where poster_email = ? and type = ? and pending_completion = 'f'";
		return (ArrayList<OpenTradeDeal>) jdbcTemplate.query(query, new OpenTradeDealRowMapper(), new Object[] { email, type } );
	}
	
	@Override
	public ArrayList<OpenTradeDeal> retrieveRecentOpenTradeDealsByPosterEmail(String email, int type) {
		String query = "select * from open_trade_deal where poster_email = ? and type = ? and pending_completion = 'f' order by id desc limit 5";
		return (ArrayList<OpenTradeDeal>) jdbcTemplate.query(query, new OpenTradeDealRowMapper(), new Object[] { email, type  } );
	}
	
	@Override
	public ArrayList<OpenTradeDeal> retrieveRecentOpenTradeDealsNotFromCollector(String email, int type) {
		String query = "select * from open_trade_deal where poster_email != ? and type = ? and pending_completion = 'f' order by id desc limit 5";
		return (ArrayList<OpenTradeDeal>) jdbcTemplate.query(query, new OpenTradeDealRowMapper(), new Object[] { email, type } );
	}
	
	@Override
	public ArrayList<OpenTradeDeal> retrieveOpenTradeDealsNotFromCollector(String email, int type) {
		String query = "select * from open_trade_deal where poster_email != ? and type = ? and pending_completion = 'f'";
		return (ArrayList<OpenTradeDeal>) jdbcTemplate.query(query, new OpenTradeDealRowMapper(), new Object[] { email, type } );
	}
	
	@Override
	public ArrayList<OpenTradeDeal> retrieveRecentPendingTradeDealsByEmail(String email, int type) {
		String query = "select * from open_trade_deal where (poster_email = ? or accepter_email = ?) and type = ? and pending_completion = 't' order by id desc limit 5";
		return (ArrayList<OpenTradeDeal>) jdbcTemplate.query(query, new OpenTradeDealRowMapper(), email, email, type);
	}
	
	@Override
	public void updateOpenTradeDealAsAccepted(int id, String accepterEmail, boolean creditUsed) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			updateOpenTradeToPendingTrade(id, accepterEmail);
			updateOpenTradeAccepterEmailForAccepterItems(id, accepterEmail);
			if(creditUsed) {
				subtractFromAccepterUserCredits(accepterEmail);
			}
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error in updating open trade deal as accepted, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	@Override
	public ArrayList<OpenTradeDeal> retrieveAllPendingTradeDealsByEmail(String email, int type) {
		String query = "select * from open_trade_deal where pending_completion = 't' and type = ? and (poster_email = ? or accepter_email = ?)";
		return (ArrayList<OpenTradeDeal>) jdbcTemplate.query(query, new OpenTradeDealRowMapper(), type, email, email);
	}
	
	@Override
	public ArrayList<ClosedTradeDeal> retrieveAllClosedTradeDealsByEmail(String email, int type) {
		String query = "select * from closed_trade_deal where (poster_email = ? or accepter_email = ?) and type = ?";
		return (ArrayList<ClosedTradeDeal>) jdbcTemplate.query(query, new ClosedTradeDealRowMapper(), email, email, type);
	}
	
	@Override
	public ArrayList<ClosedTradeItem> retrieveClosedAccepterTradeItems(int closedTradeDealId) {
		String query = "select * from closed_accepter_trade_item where closed_trade_deal_id = ?";
		return (ArrayList<ClosedTradeItem>) jdbcTemplate.query(query, new ClosedTradeItemRowMapper(), new Object[] { closedTradeDealId } );
	}
	
	@Override
	public ArrayList<ClosedTradeItem> retrieveClosedPosterTradeItems(int closedTradeDealId) {
		String query = "select * from closed_poster_trade_item where closed_trade_deal_id = ?";
		return (ArrayList<ClosedTradeItem>) jdbcTemplate.query(query, new ClosedTradeItemRowMapper(), new Object[] { closedTradeDealId } );
	}
	
	@Override
	public ArrayList<OpenTradeDeal> retrieveAllPendingTradeDeals(int type) {
		String query = "select * from open_trade_deal where type = ? and pending_completion = 't'";
		return (ArrayList<OpenTradeDeal>) jdbcTemplate.query(query, new OpenTradeDealRowMapper(), type);
	}
	
	private ArrayList<ClosedTradeItem> mapPendingTradeItemsToClosedTradeItems(ClosedTradeDeal closedTradeDeal, 
			ArrayList<OpenTradeItem> pendingTradeItems) {
		ArrayList<ClosedTradeItem> closedTradeItems = new ArrayList<>();
		for(int i = 0; i < pendingTradeItems.size(); i++) {
			OpenTradeItem pendingTradeItem = pendingTradeItems.get(i);
			ClosedTradeItem closedTradeItem = new ClosedTradeItem(-1, closedTradeDeal.getId(), pendingTradeItem.getName(), pendingTradeItem.getEmail(),
					pendingTradeItem.getCondition(), pendingTradeItem.getUserTradeItemAttrOne(), pendingTradeItem.getUserTradeItemAttrTwo(), 
					pendingTradeItem.getUserTradeItemAttrThree());
			closedTradeItems.add(closedTradeItem);
		}
		return closedTradeItems;
	}
	
	private void updateOpenTradeAccepterEmailForAccepterItems(int id, String email) {
		String sql = "update open_accepter_trade_item set email = ? where open_trade_deal_id = ?";
		jdbcTemplate.update(sql, email, id);
	}
	
	private void updateOpenTradeToPendingTrade(int id, String accepterEmail) {
		String sql = "update open_trade_deal set accepter_email = ?, pending_completion = 't', accepted_date = ? where id = ?";
		jdbcTemplate.update(sql, accepterEmail, Date.valueOf(LocalDate.now().toString()), id);
	}
	
	private void deleteOpenTradeDeal(int id) {
		String sql = "delete from open_trade_deal where id = ?";
		jdbcTemplate.update(sql, id);
	}
	
	private void deleteOpenPosterTradeItems(int id) {
		String sql = "delete from open_poster_trade_item where open_trade_deal_id = ?";
		jdbcTemplate.update(sql, id);
	}
	
	private void deleteOpenAccepterTradeItems(int id) {
		String sql = "delete from open_accepter_trade_item where open_trade_deal_id = ?";
		jdbcTemplate.update(sql, id);
	}
		
	private void insertOpenPosterTradeItems(OpenTradeDeal openTradeDeal, ArrayList<WebTradeItem> posterTradeItems) {
		for(int i = 0; i < posterTradeItems.size(); i++) {
			String sql = "insert into open_poster_trade_item (open_trade_deal_id, name, email, condition, user_trade_item_string_attr_one,"
					+ " user_trade_item_string_attr_two, user_trade_item_string_attr_three) values (?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, openTradeDeal.getId(), posterTradeItems.get(i).getName(), openTradeDeal.getPosterEmail(),
					posterTradeItems.get(i).getCondition(), posterTradeItems.get(i).getUserTradeItemAttrOne(), 
					posterTradeItems.get(i).getUserTradeItemAttrTwo(), posterTradeItems.get(i).getUserTradeItemAttrThree());
		}
	}
	
	private void insertOpenPosterTradeItemsApi(OpenTradeDeal openTradeDeal, ArrayList<WebTradeItemApi> posterTradeItems) {
		for(int i = 0; i < posterTradeItems.size(); i++) {
			String sql = "insert into open_poster_trade_item (open_trade_deal_id, name, email, condition, user_trade_item_string_attr_one,"
					+ " user_trade_item_string_attr_two, user_trade_item_string_attr_three) values (?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, openTradeDeal.getId(), posterTradeItems.get(i).getName(), openTradeDeal.getPosterEmail(),
					posterTradeItems.get(i).getCondition(), posterTradeItems.get(i).getUserTradeItemAttrOne(), 
					posterTradeItems.get(i).getUserTradeItemAttrTwo(), posterTradeItems.get(i).getUserTradeItemAttrThree());
		}
	}
	
	private void insertClosedPosterTradeItems(ArrayList<ClosedTradeItem> posterTradeItems) {
		for(int i = 0; i < posterTradeItems.size(); i++) {
			String sql = "insert into closed_poster_trade_item (closed_trade_deal_id, name, email, condition, user_trade_item_string_attr_one,"
					+ " user_trade_item_string_attr_two, user_trade_item_string_attr_three) values (?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, posterTradeItems.get(i).getClosedTradeDealId(), posterTradeItems.get(i).getName(), posterTradeItems.get(i).getEmail(),
					posterTradeItems.get(i).getCondition(), posterTradeItems.get(i).getUserTradeItemAttrOne(), 
					posterTradeItems.get(i).getUserTradeItemAttrTwo(), posterTradeItems.get(i).getUserTradeItemAttrThree());
		}
	}
	
	private void insertClosedAccepterTradeItems(ArrayList<ClosedTradeItem> accepterTradeItems) {
		for(int i = 0; i < accepterTradeItems.size(); i++) {
			String sql = "insert into closed_accepter_trade_item (closed_trade_deal_id, name, email, condition, user_trade_item_string_attr_one,"
					+ " user_trade_item_string_attr_two, user_trade_item_string_attr_three) values (?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, accepterTradeItems.get(i).getClosedTradeDealId(), accepterTradeItems.get(i).getName(), accepterTradeItems.get(i).getEmail(),
					accepterTradeItems.get(i).getCondition(), accepterTradeItems.get(i).getUserTradeItemAttrOne(), 
					accepterTradeItems.get(i).getUserTradeItemAttrTwo(), accepterTradeItems.get(i).getUserTradeItemAttrThree());
		}
	}
	
	private void insertOpenAccepterTradeItems(OpenTradeDeal openTradeDeal, ArrayList<WebTradeItem> accepterTradeItems) {
		for(int i = 0; i < accepterTradeItems.size(); i++) {
			String sql = "insert into open_accepter_trade_item (open_trade_deal_id, name, email, condition, user_trade_item_string_attr_one,"
					+ " user_trade_item_string_attr_two, user_trade_item_string_attr_three) values (?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, openTradeDeal.getId(), accepterTradeItems.get(i).getName(), openTradeDeal.getAccepterEmail(),
					accepterTradeItems.get(i).getCondition(), accepterTradeItems.get(i).getUserTradeItemAttrOne(), 
					accepterTradeItems.get(i).getUserTradeItemAttrTwo(), accepterTradeItems.get(i).getUserTradeItemAttrThree());
		}
	}
	
	private void insertOpenAccepterTradeItemsApi(OpenTradeDeal openTradeDeal, ArrayList<WebTradeItemApi> accepterTradeItems) {
		for(int i = 0; i < accepterTradeItems.size(); i++) {
			String sql = "insert into open_accepter_trade_item (open_trade_deal_id, name, email, condition, user_trade_item_string_attr_one,"
					+ " user_trade_item_string_attr_two, user_trade_item_string_attr_three) values (?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, openTradeDeal.getId(), accepterTradeItems.get(i).getName(), openTradeDeal.getAccepterEmail(),
					accepterTradeItems.get(i).getCondition(), accepterTradeItems.get(i).getUserTradeItemAttrOne(), 
					accepterTradeItems.get(i).getUserTradeItemAttrTwo(), accepterTradeItems.get(i).getUserTradeItemAttrThree());
		}
	}
	
	private void privInsertOpenTradeDeal(WebTradeDeal webTradeDeal, String email, int type) {
		String sql = "insert into open_trade_deal(posted_date, poster_email, accepter_email, pending_completion, type)"
				+ " values (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, Date.valueOf(LocalDate.now().toString()), email, null, false, type);
	}
	
	private void privInsertOpenTradeDealApi(WebTradeDealApi webTradeDealApi, String email, int type) {
		String sql = "insert into open_trade_deal(posted_date, poster_email, accepter_email, pending_completion, type)"
				+ " values (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, Date.valueOf(LocalDate.now().toString()), email, null, false, type);
	}
	
	private void insertClosedTradeDeal(ClosedTradeDeal closedTradeDeal) {
		String sql = "insert into closed_trade_deal(poster_email, accepter_email, posted_date, accepted_date,"
				+ " authenticity_status, authenticated_date, type) values(?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, closedTradeDeal.getPosterEmail(), closedTradeDeal.getAccepterEmail(), Date.valueOf(closedTradeDeal.getPostDate()),
				Date.valueOf(closedTradeDeal.getAcceptDate()), closedTradeDeal.getAuthenticityStatus(), Date.valueOf(closedTradeDeal.getAuthenticatedDate()),
				closedTradeDeal.getItemType());
	}
	
	private void subtractFromAccepterUserCredits(String email) {
		String query = "select credit from user_credit where email = ?";
		BigDecimal credit = jdbcTemplate.queryForObject(query, BigDecimal.class, email);
		credit = credit.subtract((BigDecimal.valueOf(15)));
		String insert = "update user_credit set credit = ? where email = ?";
		jdbcTemplate.update(insert, credit, email);
	}
	
	private void addToUserCredits(String email, BigDecimal credit) {
		String query = "select credit from user_credit where email = ?";
		credit = credit.add(jdbcTemplate.queryForObject(query, BigDecimal.class, email));
		String insert = "update user_credit set credit = ? where email = ?";
		jdbcTemplate.update(insert, credit, email);
	}
	
	private OpenTradeDeal retrieveLatestOpenTradeDeal() {
		String query = "select * from open_trade_deal order by id desc limit 1";
		return jdbcTemplate.queryForObject(query, new OpenTradeDealRowMapper());
	}
	
	private ClosedTradeDeal retrieveLatestClosedTradeDeal() {
		String query = "select * from closed_trade_deal order by id desc limit 1";
		return jdbcTemplate.queryForObject(query, new ClosedTradeDealRowMapper());
	}
	
	private class OpenTradeItemRowMapper implements RowMapper<OpenTradeItem> {

		@Override
		public OpenTradeItem mapRow(ResultSet rs, int rowNum) throws SQLException {
			OpenTradeItem openTradeItem = new OpenTradeItem();
			openTradeItem.setId(rs.getInt("id"));
			openTradeItem.setOpenTradeDealId(rs.getInt("open_trade_deal_id"));
			openTradeItem.setName(rs.getString("name"));
			openTradeItem.setEmail(rs.getString("email"));
			openTradeItem.setCondition(rs.getString("condition"));
			openTradeItem.setUserTradeItemAttrOne(rs.getString("user_trade_item_string_attr_one"));
			openTradeItem.setUserTradeItemAttrTwo(rs.getString("user_trade_item_string_attr_two"));
			openTradeItem.setUserTradeItemAttrThree(rs.getString("user_trade_item_string_attr_three"));
			return openTradeItem;
		}		
	}
	
	private class ClosedTradeItemRowMapper implements RowMapper<ClosedTradeItem> {
		
		@Override
		public ClosedTradeItem mapRow(ResultSet rs, int rowNum) throws SQLException {
			ClosedTradeItem closedTradeItem = new ClosedTradeItem();
			closedTradeItem.setId(rs.getInt("id"));
			closedTradeItem.setClosedTradeDealId(rs.getInt("closed_trade_deal_id"));
			closedTradeItem.setName(rs.getString("name"));
			closedTradeItem.setEmail(rs.getString("email"));
			closedTradeItem.setCondition(rs.getString("condition"));
			closedTradeItem.setUserTradeItemAttrOne(rs.getString("user_trade_item_string_attr_one"));
			closedTradeItem.setUserTradeItemAttrTwo(rs.getString("user_trade_item_string_attr_two"));
			closedTradeItem.setUserTradeItemAttrThree(rs.getString("user_trade_item_string_attr_three"));
			return closedTradeItem;
		}
	}
	
	private class TradeItemRowMapper implements RowMapper<TradeItem> {

		@Override
		public TradeItem mapRow(ResultSet rs, int rowNum) throws SQLException {
			TradeItem tradeItem = new TradeItem();
			tradeItem.setName(rs.getString("name"));
			tradeItem.setItemType(rs.getInt("type"));;
			return tradeItem;
		}
	}
	
	class OpenTradeDealRowMapper implements RowMapper<OpenTradeDeal> {

		@Override
		public OpenTradeDeal mapRow(ResultSet rs, int rowNum) throws SQLException {
			OpenTradeDeal openTradeDeal = new OpenTradeDeal();
			openTradeDeal.setId(rs.getInt("id"));
			openTradeDeal.setPosterEmail(rs.getString("poster_email"));
			openTradeDeal.setAccepterEmail(rs.getString("accepter_email"));
			openTradeDeal.setPostDate(String.valueOf(rs.getDate("posted_date")));
			openTradeDeal.setAcceptDate(String.valueOf(rs.getDate("accepted_date")));
			openTradeDeal.setPendingCompletion(rs.getBoolean("pending_completion"));
			openTradeDeal.setItemType(rs.getInt("type"));
			return openTradeDeal;
		}		
	}
	
	private class ClosedTradeDealRowMapper implements RowMapper<ClosedTradeDeal> {
		
		@Override
		public ClosedTradeDeal mapRow(ResultSet rs, int rowNum) throws SQLException {
			ClosedTradeDeal closedTradeDeal = new ClosedTradeDeal();
			closedTradeDeal.setId(rs.getInt("id"));
			closedTradeDeal.setPosterEmail(rs.getString("poster_email"));
			closedTradeDeal.setAccepterEmail(rs.getString("accepter_email"));
			closedTradeDeal.setPostDate(String.valueOf(rs.getDate("posted_date")));
			closedTradeDeal.setAcceptDate(String.valueOf(rs.getDate("accepted_date")));
			closedTradeDeal.setAuthenticityStatus(rs.getString("authenticity_status"));
			closedTradeDeal.setAuthenticatedDate(String.valueOf(rs.getDate("authenticated_date")));
			closedTradeDeal.setItemType(rs.getInt("type"));
			return closedTradeDeal;
		}
	}
}
