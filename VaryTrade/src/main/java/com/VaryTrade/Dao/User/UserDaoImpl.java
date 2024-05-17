package com.VaryTrade.Dao.User;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

import com.VaryTrade.Model.Payout;
import com.VaryTrade.Model.UserCredential;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebCreateAccountPassword;
import com.VaryTrade.Model.WebEditGoogleUserInfo;
import com.VaryTrade.Model.WebEditUserInfo;
import com.VaryTrade.Model.WebGoogleRegistration;
import com.VaryTrade.Model.WebUserRegistration;
import com.hyperwallet.clientsdk.model.HyperwalletPayment;
import com.hyperwallet.clientsdk.model.HyperwalletUser;

@Repository
public class UserDaoImpl implements UserDao {
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
	public void updateUserAccountForSuccessfulLoginAttempt(UserCredential userCredential) {
		if(!userCredential.isEnabled()) {
			userCredential.setEnabled(true);
			userCredential.setDisableTime(null);
			userCredential.setFailedLoginAttempt(0);
			updateAccountAccess(userCredential);
		}
		else if (userCredential.getFailedLoginAttempt() > 0) {
			updateFailedLoginAttempts(userCredential);
		}
	}
	
	@Override
	public boolean updateUserAccountForFailedLoginAttempt(UserCredential userCredential) {
		if(userCredential != null) {
			if(userCredential.isEnabled() && userCredential.getFailedLoginAttempt() < 3) {
				userCredential.setFailedLoginAttempt(userCredential.getFailedLoginAttempt() + 1);
				updateFailedLoginAttempts(userCredential);
				if(userCredential.getFailedLoginAttempt() == 3) {
					userCredential.setEnabled(false);
					userCredential.setDisableTime(Timestamp.from(Instant.now().plus(24, ChronoUnit.SECONDS)));
					updateAccountAccess(userCredential);
					return true;
				}		
			}
		}
		return false;
	}
	
	@Override
	public void updateGoogleUserToUser(WebGoogleRegistration webGoogleRegistration) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			updateUserInfoForGoogleUserToUser(webGoogleRegistration);
			updateAuthorityFromGoogleUserToUser(webGoogleRegistration);
			transactionManager.commit(status);
			
		}
		catch(DataAccessException e) {
			System.out.println("Update Google registered user failed, rolling back.");
			transactionManager.rollback(status);
		}
	}
	
	@Override
	public ArrayList<Payout> retrieveAllPayoutsForCollector(String email) {
		String query = "select * from payout where email = ?";
		return (ArrayList<Payout>) jdbcTemplate.query(query, new PayoutRowMapper(), email);
	}
	
	@Override
	public String retrieveUserName(String email) {
		String query = "select username from user_info where email = ?";
		return jdbcTemplate.queryForObject(query, String.class, email);
	}
	
	@Override
	public UserCredential retrieveUserCredential(String email) {
		try {
			String query = "select * from credential where email = ?";
			return jdbcTemplate.queryForObject(query, new UserCredentialMapper(), email);
		}
		catch(DataAccessException e) {
			System.out.println("Error retrieving user credentials.");
			return null;
		}		
	}
	
	@Override
	public Payout insertPayout(HyperwalletPayment payment, String email) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try  {
			insertPayoutPriv(payment, email);
			clearCollectorCredit(email);
			Payout payout = retrievePayout(payment.getClientPaymentId());
			transactionManager.commit(status);
			return payout;
		}
		catch(DataAccessException e) {
			System.out.println("Error inserting payout and updating user credit, rolling back");
			transactionManager.rollback(status);
			return null;
		}
	}
	
	@Override
	public Payout retrievePayout(String id) {
		try {
			String query = "select * from payout where id = ?";
			return jdbcTemplate.queryForObject(query, new PayoutRowMapper(), id);
		}
		catch(DataAccessException e) {
			System.out.println(e.getMessage());
			return null;
		}

	}
	
	@Override
	public void insertCredential(WebCreateAccountPassword webCreateAccountPassword) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		System.out.println("Reached");
		try {
			String sql = "insert into credential(email, password, enabled, failed_login_attempt) values(?, ?, ?, ?)";
			jdbcTemplate.update(sql, webCreateAccountPassword.getEmail(), webCreateAccountPassword.getPassword(), true, 0);
			
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error inserting user credential, rolling back");
			transactionManager.rollback(status);
		}
	}
	
	@Override
	public String retrievePayoutId(String id) {
		try {
			String sql = "select id from payout where id = ?";
			return jdbcTemplate.queryForObject(sql, String.class, id);
		}
		catch(DataAccessException e) {
			System.out.println("Error retrieving payout id");
			return null;
		}
	}
	
	@Override
	public void updateHyperwalletPayPalToken(String paypalToken, String email) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String sql = "update user_hyperwallet_info set paypal_token = ? where email = ?";
			jdbcTemplate.update(sql, paypalToken, email);
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error inserting paypal token, rolling back");
			transactionManager.rollback(status);
		}
	}
	
	@Override
	public String retrieveHyperwalletPayPalToken(String email) {
		try {
			String query = "select paypal_token from user_hyperwallet_info where email = ?";
			return jdbcTemplate.queryForObject(query, String.class, email);
		}
		catch(DataAccessException e) {
			return null;
		}
	}
	
	@Override
	public void insertHyperwalletInfo(HyperwalletUser hyperwalletUser) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String sql = "insert into user_hyperwallet_info(email, user_token) values (?, ?)";
			jdbcTemplate.update(sql, hyperwalletUser.getEmail(), hyperwalletUser.getToken());
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error inserting hyperwallet user info, rolling back");
			transactionManager.rollback(status);
		}
	}
	
	@Override
	public String retrieveUserHyperWalletToken(String email) {
		try {
			String query = "select user_token from user_hyperwallet_info where email = ?";
			return jdbcTemplate.queryForObject(query, String.class, email);
		}
		catch(DataAccessException e) {
			return null;
		}
	}
	
	@Override
	public BigDecimal retrieveUserCredit(String email) {
		String query = "select credit from user_credit where email = ?";
		return jdbcTemplate.queryForObject(query, BigDecimal.class, email);
	}
	
	@Override
	public String retrieveUserRole(String email) {
		String query = "select authority from user_authority where email = ?";
		return jdbcTemplate.queryForObject(query, String.class, email);
	}
	
	@Override
	public String retrieveUserPassword(String email) {
		String query = "select password from credential where email = ?";
		return jdbcTemplate.queryForObject(query, String.class, email);
	}

	@Override
	public void insertCollectorUserRegistration(WebUserRegistration webUserRegistration) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			insertUserInfo(webUserRegistration);
			insertCredentialPriv(webUserRegistration);
			insertCollectorUserAuthority(webUserRegistration);
			insertCollectorCredit(webUserRegistration.getEmail());
			// Potential use of triggers
			
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error in inserting user from basic registration, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	@Override
	public void insertCollectorGoogleUserRegistration(UserInfo userInfo) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			insertCollectorGoogleUserAuthority(userInfo);
			insertGoogleUserInfo(userInfo);
			insertCollectorCredit(userInfo.getEmail());
			// Potential use of triggers
			
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error in inserting user from Google registration, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	@Override
	public void insertAdmin(WebUserRegistration webUserRegistration) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			insertUserInfo(webUserRegistration);
			insertCredentialPriv(webUserRegistration);
			insertAdminAuthority(webUserRegistration);
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error in inserting user, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public UserInfo retrieveUserInfoByEmail(String email) {
		System.out.println(email);
		try {
			String query = "select * from user_info where email = ?";
			return jdbcTemplate.queryForObject(query, new UserInfoRowMapper(), new Object[] { email }); 
		}
		catch(DataAccessException e) {
			System.out.println("Error in retrieving user by email");
			return null;
		}
	}
	
	@Override
	public UserInfo retrieveUserInfoByUserName(String username) {
		try {
			String query = "select * from user_info where username = ?";
			return jdbcTemplate.queryForObject(query, new UserInfoRowMapper(), new Object[] { username });
		}
		catch(DataAccessException e) {
			System.out.println("Error in retrieving user by username");
			return null;
		}
	}
	
	@Override
	public void updateGoogleUserInfo(WebEditGoogleUserInfo webEditGoogleUserInfo, String email) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			updateGoogleUserInfoPriv(webEditGoogleUserInfo, email);
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error updating Google user info, rolling back");
			transactionManager.rollback(status);
		}
	}
	
	@Override
	public void updateUserInfo(WebEditUserInfo webEditUserInfo) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			updateUserInfoPriv(webEditUserInfo);
			updateCredentialEmail(webEditUserInfo);
			updateUserAuthorityEmail(webEditUserInfo);
			updateOpenTradeDealPosterEmail(webEditUserInfo);
			updateOpenTradeDealAccepterEmail(webEditUserInfo);
			updateOpenPosterTradeItemEmail(webEditUserInfo);
			updateOpenAccepterTradeItemEmail(webEditUserInfo);
			updateClosedTradeDealPosterEmail(webEditUserInfo);
			updateClosedTradeDealAccepterEmail(webEditUserInfo);
			updateClosedPosterTradeItemEmail(webEditUserInfo);
			updateClosedAccepterTradeItemEmail(webEditUserInfo);
			updateFollowsFollowerEmail(webEditUserInfo);
			updateFollowsFollowedEmail(webEditUserInfo);
			updateOpenResaleDealPosterEmail(webEditUserInfo);
			updateOpenResaleDealAccepterEmail(webEditUserInfo);
			updateClosedResaleDealPosterEmail(webEditUserInfo);
			updateClosedResaleDealAccepterEmail(webEditUserInfo);
			updateUserCreditEmail(webEditUserInfo);
			updateUserHyperwalletInfoEmail(webEditUserInfo);
			updatePayoutEmail(webEditUserInfo);
			// Potential use of triggers
			
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error updating user info, rolling back");
			transactionManager.rollback(status);
		}
	}
	
	@Override
	public ArrayList<String> retrieveEmailsForCollectorsFollowedByCollector(String email) {
		String query = "select followed_email from follows where follower_email = ?";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class, email);
	}
	
	@Override
	public ArrayList<String> retrieveEmailsForCollectorsFollowingCollector(String email) {
		String query = "select follower_email from follows where followed_email = ?";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class, email);
	}
	
	@Override
	public ArrayList<String> retrieveUserNamesForCollectorsFollowedByCollector(String email) {
		String query = "select followed_username from follows where follower_email = ?";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class, email);
	}
	
	@Override
	public ArrayList<String> retrieveUserNamesForCollectorsFollowingCollector(String email) {
		String query = "select follower_username from follows where followed_email = ?";
		return (ArrayList<String>) jdbcTemplate.queryForList(query, String.class, email);
	}
	
	@Override
	public void updateUserPassword(String email, String password) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String sql = "update credential set password = ? where email = ?";
			jdbcTemplate.update(sql, password, email);
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Error updating user password, rolling back");
			transactionManager.rollback(status);
		}
	}
	
	@Override
	public void insertFollower(String followerEmail, String followedEmail, String followerUserName, String followedUserName) {
		String sql = "insert into follows(follower_email, followed_email, follower_username, followed_username) values(?, ?, ?, ?)";
		jdbcTemplate.update(sql, followerEmail, followedEmail, followerUserName, followedUserName);
	}
	
	@Override
	public void deleteFollower(String followerEmail, String followedEmail) {
		String sql = "delete from follows where follower_email = ? and followed_email = ?";
		jdbcTemplate.update(sql, followerEmail, followedEmail);
	}
	
	// Private ---------------------------------------------------------------------------------------------
	
	private void updateUserInfoForGoogleUserToUser(WebGoogleRegistration webGoogleRegistration) {
		String sql = "update user_info set zip_code = ?, state = ?, city = ?, address = ?, phone_number = ?, first_name = ?, middle_name = ?,"
				+ " last_name = ?, username = ? where email = ?";
		jdbcTemplate.update(sql, webGoogleRegistration.getZipCode(), webGoogleRegistration.getState(), webGoogleRegistration.getCity(), webGoogleRegistration.getAddress(),
				webGoogleRegistration.getPhoneNum(), webGoogleRegistration.getFirstName(), webGoogleRegistration.getMiddleName(), webGoogleRegistration.getLastName(),
				webGoogleRegistration.getUserName(), webGoogleRegistration.getEmail());
	}
	
	private void updateAuthorityFromGoogleUserToUser(WebGoogleRegistration webGoogleRegistration) {
		String sql = "update user_authority set authority = 'ROLE_USER' where email = ?";
		jdbcTemplate.update(sql, webGoogleRegistration.getEmail());
	}
	
	private void insertPayoutPriv(HyperwalletPayment payout, String payeeEmail) {
		String sql = "insert into payout(id, email, amount, created_date) values (?, ?, ?, ?)";
		jdbcTemplate.update(sql, payout.getClientPaymentId(), payeeEmail, payout.getAmount(), payout.getCreatedOn());
	}
	
	private void clearCollectorCredit(String email) {
		String sql = "update user_credit set credit = 0.00 where email = ?";
		jdbcTemplate.update(sql, email);
	}
	
	private void insertCredentialPriv(WebUserRegistration webUserRegistration) {
		String sql = "insert into credential(email, password, enabled, failed_login_attempt) values (?, ?, ?, ?)";
		jdbcTemplate.update(sql, webUserRegistration.getEmail(), webUserRegistration.getPassword(), true, 0);
	}
	
	private void insertUserInfo(WebUserRegistration webUserRegistration) {
		String sql = "insert into user_info(email, zip_code, state, city, address, phone_number, first_name,"
				+ " middle_name, last_name, username) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, webUserRegistration.getEmail(), webUserRegistration.getZipCode(), webUserRegistration.getState(), webUserRegistration.getCity(),
				webUserRegistration.getAddress(), webUserRegistration.getPhoneNum(), webUserRegistration.getFirstName(), webUserRegistration.getMiddleName(),
				webUserRegistration.getLastName(), webUserRegistration.getUserName());
	}
	
	private void insertGoogleUserInfo(UserInfo userInfo) {
		String sql = "insert into user_info(email, zip_code, state, city, address, phone_number, first_name,"
				+ " middle_name, last_name, username) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, userInfo.getEmail(), userInfo.getZipCode(), userInfo.getState(), userInfo.getCity(),
				userInfo.getAddress(), userInfo.getPhoneNum(), userInfo.getFirstName(), userInfo.getMiddleName(),
				userInfo.getLastName(), userInfo.getUserName());
	}
	
	private void insertCollectorGoogleUserAuthority(UserInfo userInfo) {
		String sql = "insert into user_authority(email, authority) values(?, ?)";
		jdbcTemplate.update(sql, userInfo.getEmail(), "ROLE_GOOGLE_USER");
	}
	
	private void insertCollectorUserAuthority(WebUserRegistration webUserRegistration) {
		String sql = "insert into user_authority(email, authority) values(?, ?)";
		jdbcTemplate.update(sql, webUserRegistration.getEmail(), "ROLE_USER");
	}
	
	private void insertCollectorCredit(String email) {
		String sql = "insert into user_credit(email, credit) values(?, ?)";
		jdbcTemplate.update(sql, email, 0.00);
	}
	
	private void insertAdminAuthority(WebUserRegistration webUserRegistration) {
		String sql = "insert into user_authority(email, authority) values(?, ?)";
		jdbcTemplate.update(sql, webUserRegistration.getEmail(), "ROLE_ADMIN");
	}
	
	private void updatePayoutEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update payout set email = ? where email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());

	}
	
	private void updateUserHyperwalletInfoEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update user_hyperwallet_info set email = ? where email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());

	}
	
	private void updateUserInfoPriv(WebEditUserInfo webEditUserInfo) {
		String sql = "update user_info set email = ?, address = ?, zip_code = ?, city = ?, state = ?, first_name = ?,"
				+ " last_name = ?, middle_name = ?, phone_number = ? where email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getAddress(), webEditUserInfo.getZipCode(),
				webEditUserInfo.getCity(), webEditUserInfo.getState(), webEditUserInfo.getFirstName(), webEditUserInfo.getLastName(),
				webEditUserInfo.getMiddleName(), webEditUserInfo.getPhoneNum(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateGoogleUserInfoPriv(WebEditGoogleUserInfo webEditGoogleUserInfo, String email) {
		String sql = "update user_info set address = ?, zip_code = ?, city = ?, state = ?, first_name = ?,"
				+ " last_name = ?, middle_name = ?, phone_number = ? where email = ?";
		jdbcTemplate.update(sql, webEditGoogleUserInfo.getAddress(), webEditGoogleUserInfo.getZipCode(),
				webEditGoogleUserInfo.getCity(), webEditGoogleUserInfo.getState(), webEditGoogleUserInfo.getFirstName(), webEditGoogleUserInfo.getLastName(),
				webEditGoogleUserInfo.getMiddleName(), webEditGoogleUserInfo.getPhoneNum(), email);
	}
	
	private void updateCredentialEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update credential set email = ? where email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateUserAuthorityEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update user_authority set email = ? where email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateOpenTradeDealPosterEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update open_trade_deal set poster_email = ? where poster_email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateOpenResaleDealPosterEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update open_resale_deal set poster_email = ? where poster_email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateOpenResaleDealAccepterEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update open_resale_deal set accepter_email = ? where accepter_email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateClosedResaleDealPosterEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update closed_resale_deal set poster_email = ? where poster_email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateClosedResaleDealAccepterEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update closed_resale_deal set accepter_email = ? where accepter_email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateOpenTradeDealAccepterEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update open_trade_deal set accepter_email = ? where accepter_email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateOpenPosterTradeItemEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update open_poster_trade_item set email = ? where email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateOpenAccepterTradeItemEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update open_accepter_trade_item set email = ? where email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateClosedTradeDealPosterEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update closed_trade_deal set poster_email = ? where poster_email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateUserCreditEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update user_credit set email = ? where email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateClosedTradeDealAccepterEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update closed_trade_deal set accepter_email = ? where accepter_email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateClosedPosterTradeItemEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update closed_poster_trade_item set email = ? where email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateClosedAccepterTradeItemEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update closed_accepter_trade_item set email = ? where email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateFollowsFollowerEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update follows set follower_email = ? where follower_email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateFollowsFollowedEmail(WebEditUserInfo webEditUserInfo) {
		String sql = "update follows set followed_email = ? where followed_email = ?";
		jdbcTemplate.update(sql, webEditUserInfo.getEmail(), webEditUserInfo.getOriginalEmail());
	}
	
	private void updateAccountAccess(UserCredential userCredential) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try  {
			String sql = "update credential set failed_login_attempt = ?, enabled = ?, disable_time = ? where email = ?";
			jdbcTemplate.update(sql, userCredential.getFailedLoginAttempt(), userCredential.isEnabled(), userCredential.getDisableTime(), userCredential.getEmail());
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Enable status update failed, rolling back.");
			transactionManager.rollback(status);
		}
	}
	
	private void updateFailedLoginAttempts(UserCredential userCredential) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try  {
			String sql = "update credential set failed_login_attempt = ? where email = ?";
			jdbcTemplate.update(sql, userCredential.getFailedLoginAttempt(), userCredential.getEmail());
			transactionManager.commit(status);
		}
		catch(DataAccessException e) {
			System.out.println("Failed attempt login update failed, rolling back.");
			transactionManager.rollback(status);
		}
	}
	
	class UserCredentialMapper implements RowMapper<UserCredential> {

		@Override
		public UserCredential mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserCredential userCredential = new UserCredential();
			userCredential.setEmail(rs.getString("email"));
			userCredential.setPassword(rs.getString("password"));
			userCredential.setEnabled(rs.getBoolean("enabled"));
			userCredential.setDisableTime(rs.getTimestamp("disable_time"));
			userCredential.setFailedLoginAttempt(rs.getInt("failed_login_attempt"));
			return userCredential;
		}
	}
	
	class PayoutRowMapper implements RowMapper<Payout> {

		@Override
		public Payout mapRow(ResultSet rs, int rowNum) throws SQLException {
			Payout payout = new Payout();
			payout.setId(rs.getString("id"));
			payout.setEmail(rs.getString("email"));
			payout.setAmount(rs.getBigDecimal("amount"));
			payout.setCreatedDate(rs.getDate("created_date"));
			return payout;
		}
		
	}
	
	class UserInfoRowMapper implements RowMapper<UserInfo> {

		@Override
		public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserInfo userInfo = new UserInfo();
			userInfo.setEmail(rs.getString("email"));
			userInfo.setAddress(rs.getString("address"));
			userInfo.setCity(rs.getString("city"));
			userInfo.setFirstName(rs.getString("first_name"));
			userInfo.setLastName(rs.getString("last_name"));
			userInfo.setMiddleName(rs.getString("middle_name"));
			userInfo.setPhoneNum(rs.getString("phone_number"));
			userInfo.setState(rs.getString("state"));
			userInfo.setZipCode(rs.getString("zip_code"));
			userInfo.setUserName(rs.getString("username"));
			return userInfo;
		}	
	}
}
