package com.VaryTrade.Dao.User;

import java.math.BigDecimal;
import java.util.ArrayList;

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
public interface UserDao {
	public Payout insertPayout(HyperwalletPayment payment, String email);
	public String retrievePayoutId(String id);
	public void updateHyperwalletPayPalToken(String paypalToken, String email);
	public String retrieveHyperwalletPayPalToken(String email);
	public void insertHyperwalletInfo(HyperwalletUser hyperwalletUser);
	public String retrieveUserHyperWalletToken(String email);
	public void insertCollectorUserRegistration(WebUserRegistration webUserRegistration);
	public void insertCollectorGoogleUserRegistration(UserInfo userInfo);
	public void insertAdmin(WebUserRegistration webUserRegistration);
	public UserInfo retrieveUserInfoByEmail(String email);
	public UserInfo retrieveUserInfoByUserName(String userName);
	public void updateUserInfo(WebEditUserInfo webEditUserInfo);
	public void updateGoogleUserInfo(WebEditGoogleUserInfo webEditGoogleUserInfo, String email);
	public void updateUserPassword(String email, String password);
	public String retrieveUserPassword(String email);
	public ArrayList<String> retrieveEmailsForCollectorsFollowedByCollector(String email);
	public ArrayList<String> retrieveEmailsForCollectorsFollowingCollector(String email);
	public void insertFollower(String followerEmail, String followedEmail, String followerUserName, String followedUserName);
	public void deleteFollower(String followerEmail, String followedEmail);
	public String retrieveUserRole(String email);
	public ArrayList<String> retrieveUserNamesForCollectorsFollowedByCollector(String email);
	public ArrayList<String> retrieveUserNamesForCollectorsFollowingCollector(String email);
	public BigDecimal retrieveUserCredit(String email);
	public UserCredential retrieveUserCredential(String email);
	public String retrieveUserName(String email);
	public Payout retrievePayout(String id);
	public ArrayList<Payout> retrieveAllPayoutsForCollector(String email);
	public void updateGoogleUserToUser(WebGoogleRegistration webGoogleRegistration);
	public void insertCredential(WebCreateAccountPassword webCreateAccountPassword);
	public boolean updateUserAccountForFailedLoginAttempt(UserCredential userCredential);
	public void updateUserAccountForSuccessfulLoginAttempt(UserCredential userCredential);
}
