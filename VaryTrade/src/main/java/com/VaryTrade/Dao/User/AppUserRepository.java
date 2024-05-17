package com.VaryTrade.Dao.User;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
public class AppUserRepository {
	@Autowired
	private UserDao userDao;
	
	public void updateGoogleUserInfo(WebEditGoogleUserInfo webEditGoogleUserInfo, String email) {
		userDao.updateGoogleUserInfo(webEditGoogleUserInfo, email);
	}
	
	public void updateUserAccountForSuccessfulLoginAttempt(UserCredential userCredential) {
		userDao.updateUserAccountForSuccessfulLoginAttempt(userCredential);
	}
	
	public boolean updateUserAccountForFailedLoginAttempt(UserCredential userCredential) {
		return userDao.updateUserAccountForFailedLoginAttempt(userCredential);
	}
	
	public void insertCredential(WebCreateAccountPassword webCreateAccountPassword) {
		userDao.insertCredential(webCreateAccountPassword);
	}
	
	public void updateGoogleUserToUser(WebGoogleRegistration webGoogleRegistration) {
		userDao.updateGoogleUserToUser(webGoogleRegistration);
	}
	
	public ArrayList<Payout> retrieveAllPayoutsForCollector(String email) {
		return userDao.retrieveAllPayoutsForCollector(email);
	}
	
	public Payout retrievePayout(String id) {
		return userDao.retrievePayout(id);
	}
	
	public String retrieveUserName(String email) {
		return userDao.retrieveUserName(email);
	}
	
	public UserCredential retrieveUserCredential(String email) {
		return userDao.retrieveUserCredential(email);
	}
	
	public Payout insertPayout(HyperwalletPayment payout, String payeeEmail) {
		return userDao.insertPayout(payout, payeeEmail);
	}
	
	public String retrievePayoutId(String id) {
		return userDao.retrievePayoutId(id);
	}
	
	public void updateHyperwalletPayPalToken(String paypalToken, String email) {
		userDao.updateHyperwalletPayPalToken(paypalToken, email);
	}
	
	public void insertHyperwalletInfo(HyperwalletUser hyperwalletUser) {
		userDao.insertHyperwalletInfo(hyperwalletUser);
	}
	
	public String retrieveHyperwalletPayPalToken(String email) {
		return userDao.retrieveHyperwalletPayPalToken(email);
	}
	
	public String retrieveUserHyperWalletToken(String email) {
		return userDao.retrieveUserHyperWalletToken(email);
	}
	
	public BigDecimal retrieveUserCredit(String email) {
		return userDao.retrieveUserCredit(email);
	}
	
	public String retrieveUserRole(String email) {
		return userDao.retrieveUserRole(email);
	}
	
	public String retrieveUserPassword(String email) {
		return userDao.retrieveUserPassword(email);
	}
	
	public void insertCollectorUserRegistration(WebUserRegistration webUserRegistration) {
		userDao.insertCollectorUserRegistration(webUserRegistration);
	}
	
	public void insertCollectorGoogleUserRegistration(UserInfo userInfo) {
		userDao.insertCollectorGoogleUserRegistration(userInfo);
	}
	
	public void insertAdmin(WebUserRegistration webUserRegistration) {
		userDao.insertAdmin(webUserRegistration);
	}
	
	public UserInfo retrieveUserInfoByEmail(String email) {
		return userDao.retrieveUserInfoByEmail(email);
	}
	
	public UserInfo retrieveUserInfoByUsername(String username) {
		return userDao.retrieveUserInfoByUserName(username);
	}
	
	public void updateUserInfo(WebEditUserInfo webEditInfoUser) {
	    userDao.updateUserInfo(webEditInfoUser);
	}
	
	public void updateUserPassword(String email, String password) {
		userDao.updateUserPassword(email, password);
	}
	
	public ArrayList<String> retrieveEmailsForCollectorsFollowedByCollector(String email) {
		return userDao.retrieveEmailsForCollectorsFollowedByCollector(email);
	}
	
	public ArrayList<String> retrieveEmailsForCollectorsFollowingCollector(String email) {
		return userDao.retrieveEmailsForCollectorsFollowingCollector(email);
	}
	
	public ArrayList<String> retrieveUserNamesForCollectorsFollowedByCollector(String email) {
		return userDao.retrieveUserNamesForCollectorsFollowedByCollector(email);
	}
	
	public ArrayList<String> retrieveUserNamesForCollectorsFollowingCollector(String email) {
		return userDao.retrieveUserNamesForCollectorsFollowingCollector(email);
	}
	
	public void insertFollower(String followerEmail, String followedEmail, String followerUserName, String followedUserName) {
		userDao.insertFollower(followerEmail, followedEmail, followerUserName, followedUserName);
	}
	
	public void deleteFollower(String followerEmail, String followedEmail) {
		userDao.deleteFollower(followerEmail, followedEmail);
	}
}
