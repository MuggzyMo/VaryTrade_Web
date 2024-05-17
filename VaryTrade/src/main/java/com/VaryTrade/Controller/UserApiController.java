package com.VaryTrade.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.VaryTrade.Dao.ResaleDeal.AppResaleDealRepository;
import com.VaryTrade.Dao.TradeDeal.AppTradeDealRepository;
import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.ApiOpenResaleDealList;
import com.VaryTrade.Model.ApiOpenTradeDealList;
import com.VaryTrade.Model.AuthenticationRequest;
import com.VaryTrade.Model.AuthenticationResponse;
import com.VaryTrade.Model.Email;
import com.VaryTrade.Model.ForgotPasswordRequest;
import com.VaryTrade.Model.GoogleRegistrationResponse;
import com.VaryTrade.Model.OAuthGoogleAuthenticationRequest;
import com.VaryTrade.Model.OpenResaleDeal;
import com.VaryTrade.Model.OpenTradeDeal;
import com.VaryTrade.Model.OpenTradeItem;
import com.VaryTrade.Model.Payout;
import com.VaryTrade.Model.UserCredential;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebCreateAccountPassword;
import com.VaryTrade.Model.WebEditGoogleUserInfo;
import com.VaryTrade.Model.WebEditUserInfo;
import com.VaryTrade.Model.WebEditUserPassword;
import com.VaryTrade.Model.WebGoogleRegistration;
import com.VaryTrade.Model.WebHyperwalletRegistration;
import com.VaryTrade.Model.WebUserRegistration;
import com.VaryTrade.Model.WebUserRegistrationApi;
import com.VaryTrade.Model.WebUserSearch;
import com.VaryTrade.Service.Email.AppEmailService;
import com.VaryTrade.Service.GoogleTokenVerifierService.AppGoogleTokenVerifierService;
import com.VaryTrade.Service.Jwt.JwtService;
import com.VaryTrade.Service.TradeDeal.AppTradeDealService;
import com.VaryTrade.Service.User.AppUserService;
import com.VaryTrade.Validator.WebCreateAccountPasswordValidator;
import com.VaryTrade.Validator.WebEditUserInfoValidator;
import com.VaryTrade.Validator.WebEditUserPasswordValidator;
import com.VaryTrade.Validator.WebGoogleRegistrationValidator;
import com.VaryTrade.Validator.WebHyperwalletRegistrationValidator;
import com.VaryTrade.Validator.WebUserRegistrationApiValidator;
import com.hyperwallet.clientsdk.Hyperwallet;
import com.hyperwallet.clientsdk.model.HyperwalletPayment;
import com.hyperwallet.clientsdk.model.HyperwalletUser;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class UserApiController {
	@Autowired
	private AppResaleDealRepository appResaleDealRepository;
	@Autowired
	private AppTradeDealRepository appTradeDealRepository;
	@Autowired
	private AppTradeDealService appTradeDealService;
	@Autowired
	private AppEmailService appEmailService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private WebUserRegistrationApiValidator webUserRegistrationApiValidator;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AppGoogleTokenVerifierService appGoogleTokenVerifierService;
	@Autowired
	private WebGoogleRegistrationValidator webGoogleRegistrationValidator;
	@Autowired
	private WebEditUserInfoValidator webEditUserInfoValidator;
	@Autowired
	private WebCreateAccountPasswordValidator webCreateAccountPasswordValidator;
	@Autowired
	private WebEditUserPasswordValidator webEditUserPasswordValidator;
	@Autowired
	private WebHyperwalletRegistrationValidator webHyperwalletRegistrationValidator;
	@Value("${hyperwallet.username}")
	private String hyperwalletUsername;
	@Value("${hyperwallet.password}")
	private String hyperwalletPassword;

	@GetMapping("/api/trade-deal/profile/open")
	public ResponseEntity<ApiOpenTradeDealList> retrieveCollectorSpecificOpenTradeDeals(
			@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = "type", required = true) int type) {
		return new ResponseEntity<ApiOpenTradeDealList>(createCollectorSpecificApiOpenTradeDealList(username, type),
				HttpStatus.OK);
	}

	@GetMapping("/api/resale-deal/profile/open")
	public ResponseEntity<ApiOpenResaleDealList> retrieveCollectorSpecificOpenResaleDeals(
			@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = "type", required = true) int type) {
		return new ResponseEntity<ApiOpenResaleDealList>(createCollectorSpecificApiOpenResaleDealList(username, type), HttpStatus.OK);
	}

	@PostMapping("/api/profile/search")
	public ResponseEntity<ArrayList<String>> processProfileSearch(@RequestBody @Valid WebUserSearch webUserSearch,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<ArrayList<String>>(createErrorMessageArrayList(result),
					HttpStatus.UNPROCESSABLE_ENTITY);
		}
		UserInfo userInfo = appUserRepository.retrieveUserInfoByUsername(webUserSearch.getUserName());;
		if (userInfo == null) {
			return new ResponseEntity<ArrayList<String>>(
					createResponse("No collector associated with that username has been found."), HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
		}

	}

	@GetMapping("/api/profile/follow-status")
	public ResponseEntity<Boolean> isCollectorFollowingCollector(
			@RequestParam(name = "username", required = true) String username) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		UserInfo userInfo = appUserRepository.retrieveUserInfoByUsername(username);
		return new ResponseEntity<Boolean>(appUserService.doesCollectorFollowCollector(email, userInfo.getEmail()),
				HttpStatus.OK);
	}

	@PostMapping("/api/profile/unfollow")
	public ResponseEntity<ArrayList<String>> processUnfollow(
			@RequestParam(name = "username", required = true) String username) {
		try {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			UserInfo followed = appUserRepository.retrieveUserInfoByUsername(username);
			appUserRepository.deleteFollower(email, followed.getEmail());
			return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed."),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/api/profile/follow")
	public ResponseEntity<ArrayList<String>> processFollow(
			@RequestParam(name = "username", required = true) String username) {
		try {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			UserInfo follower = appUserRepository.retrieveUserInfoByEmail(email);
			UserInfo followed = appUserRepository.retrieveUserInfoByUsername(username);
			appUserRepository.insertFollower(follower.getEmail(), followed.getEmail(), follower.getUserName(),
					followed.getUserName());
			return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed."),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/api/followers")
	public ResponseEntity<ArrayList<String>> retrieveFollowers() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity<ArrayList<String>>(
				appUserRepository.retrieveUserNamesForCollectorsFollowingCollector(email), HttpStatus.OK);
	}

	@GetMapping("/api/following")
	public ResponseEntity<ArrayList<String>> retrieveFollowing() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity<ArrayList<String>>(
				appUserRepository.retrieveUserNamesForCollectorsFollowedByCollector(email), HttpStatus.OK);
	}

	@PostMapping("/api/account/password/edit")
	public ResponseEntity<ArrayList<String>> processEditPassword(
			@RequestBody @Valid WebEditUserPassword webEditUserPassword, BindingResult result) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		webEditUserPassword.setEmail(email);
		webEditUserPassword.trimWebEditUserPasswordFields();
		webEditUserPasswordValidator.validate(webEditUserPassword, result);
		if (result.hasErrors()) {
			return new ResponseEntity<ArrayList<String>>(createErrorMessageArrayList(result),
					HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			try {
				webEditUserPassword.setPassword(passwordEncoder.encode(webEditUserPassword.getPassword()));
				appUserRepository.updateUserPassword(email, webEditUserPassword.getPassword());
				return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed."),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PostMapping("/api/account/password/create")
	public ResponseEntity<ArrayList<String>> processCreatePassword(
			@RequestBody @Valid WebCreateAccountPassword webCreateAccountPassword, BindingResult result) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		webCreateAccountPassword.setEmail(email);
		webCreateAccountPassword.trimWebCreateAccountPasswordFields();
		webCreateAccountPasswordValidator.validate(webCreateAccountPassword, result);
		if (result.hasErrors()) {
			return new ResponseEntity<ArrayList<String>>(createErrorMessageArrayList(result),
					HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			try {
				webCreateAccountPassword.setPassword(passwordEncoder.encode(webCreateAccountPassword.getPassword()));
				appUserRepository.insertCredential(webCreateAccountPassword);
				return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed."),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PostMapping("/api/account/edit")
	public ResponseEntity<ArrayList<String>> processEditAccount(@RequestBody @Valid WebEditUserInfo webEditUserInfo,
			BindingResult result) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		webEditUserInfo.setOriginalEmail(email);
		webEditUserInfo.trimWebEditUserInfoFields();
		webEditUserInfoValidator.validate(webEditUserInfo, result);
		if (result.hasErrors()) {
			return new ResponseEntity<ArrayList<String>>(createErrorMessageArrayList(result),
					HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			try {
				appUserRepository.updateUserInfo(webEditUserInfo);
				return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed."),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PostMapping("/api/account/google/edit")
	public ResponseEntity<ArrayList<String>> processEditGoogleUserAccount(
			@RequestBody @Valid WebEditGoogleUserInfo webEditGoogleUserInfo, BindingResult result) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		webEditGoogleUserInfo.trimWebEditGoogleUserInfoFields();
		if (result.hasErrors()) {
			return new ResponseEntity<ArrayList<String>>(createErrorMessageArrayList(result),
					HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			try {
				appUserRepository.updateGoogleUserInfo(webEditGoogleUserInfo, email);
				return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed."),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@GetMapping("/api/payout/history")
	public ResponseEntity<ArrayList<Payout>> retrievePayouts() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		ArrayList<Payout> payouts = appUserRepository.retrieveAllPayoutsForCollector(email);
		return new ResponseEntity<ArrayList<Payout>>(payouts, HttpStatus.OK);
	}

	@PostMapping("/api/payout")
	public ResponseEntity<ArrayList<String>> processPayout() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		BigDecimal credit = appUserRepository.retrieveUserCredit(email);
		ArrayList<String> response = new ArrayList<String>();
		if (credit.compareTo(BigDecimal.valueOf(10.00)) < 0) {
			response.add("You do not have enough credits to payout.");
			return new ResponseEntity<ArrayList<String>>(createResponse("You do not have enough credits to payout."),
					HttpStatus.FORBIDDEN);
		} else {
			try {
				Hyperwallet hyperwallet = new Hyperwallet(hyperwalletUsername, hyperwalletPassword);
				String userToken = appUserRepository.retrieveUserHyperWalletToken(email);
				HyperwalletPayment payment = appUserService.createHyperwalletPayment(credit, userToken);
				payment = hyperwallet.createPayment(payment);
				appUserRepository.insertPayout(payment, email);
				return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed."),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PostMapping("/api/hyperwallet/registration")
	public ResponseEntity<ArrayList<String>> processHyperwalletRegistration(
			@RequestBody @Valid WebHyperwalletRegistration webHyperwalletRegistration, BindingResult result) {
		webHyperwalletRegistrationValidator.validate(webHyperwalletRegistration, result);
		if (result.hasErrors()) {
			return new ResponseEntity<ArrayList<String>>(createErrorMessageArrayList(result),
					HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			try {
				UserInfo userInfo = appUserRepository
						.retrieveUserInfoByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
				Hyperwallet hyperwallet = new Hyperwallet(hyperwalletUsername, hyperwalletPassword);
				HyperwalletUser hyperwalletUser = appUserService.createHyperwalletUser(userInfo,
						webHyperwalletRegistration.getDateOfBirth());
				hyperwalletUser = hyperwallet.createUser(hyperwalletUser);
				appUserRepository.insertHyperwalletInfo(hyperwalletUser);
				return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed."),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@GetMapping("/api/password/setup/verify")
	public ResponseEntity<Boolean> isUserPasswordSetup() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		UserCredential userCredential = appUserRepository.retrieveUserCredential(email);
		if (userCredential == null) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
	}

	@GetMapping("/api/hyperwallet/verify")
	public ResponseEntity<Boolean> isUserRegisteredWithHyperwallet() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		String hyperwalletToken = appUserRepository.retrieveUserHyperWalletToken(email);
		if (hyperwalletToken == null) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
	}

	@GetMapping("/api/credit")
	public ResponseEntity<String> retrieveUserCredit() {
		try {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			BigDecimal credit = appUserRepository.retrieveUserCredit(email);
			return new ResponseEntity<String>(credit.toPlainString(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("This request could not be processed.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/api/account")
	public ResponseEntity<UserInfo> getUserInfo() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		UserInfo userInfo = appUserRepository.retrieveUserInfoByEmail(email);
		return new ResponseEntity<UserInfo>(userInfo, HttpStatus.OK);
	}

	@PostMapping("/api/password/forgot")
	public ResponseEntity<String> processForgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest,
			BindingResult result, HttpServletResponse response) throws IOException {
		forgotPasswordRequest.trimForgotPasswordRequestFields();
		if (result.hasErrors()) {
			return new ResponseEntity<>("This request could not be processed.", HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			UserCredential userCredential = appUserRepository.retrieveUserCredential(forgotPasswordRequest.getEmail());
			if (userCredential != null) {
				String password = appUserService.generateRandomPassayPassword();
				System.out.println(password);
				Email email = appEmailService.createForgotPasswordEmail(forgotPasswordRequest.getEmail(), password);
				appUserRepository.updateUserPassword(forgotPasswordRequest.getEmail(),
						passwordEncoder.encode(password));
				appEmailService.sendEmail(email);
			}
			return new ResponseEntity<>("Success", HttpStatus.OK);
		}
	}

	@PostMapping("/api/login")
	public ResponseEntity<AuthenticationResponse> processLogin(@RequestBody @Valid AuthenticationRequest authRequest,
			BindingResult result) throws IOException {
		authRequest.trimAuthenticationRequestFields();
		if (result.hasErrors()) {
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(null, null),
					HttpStatus.UNAUTHORIZED);
		} else {
			UserCredential userCredential = appUserRepository.retrieveUserCredential(authRequest.getEmail());
			try {
				Authentication auth = authManager.authenticate(
						new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
				if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
					appUserRepository.updateUserAccountForSuccessfulLoginAttempt(userCredential);
					return new ResponseEntity<AuthenticationResponse>(
							createAuthenticationResponse(authRequest.getEmail()), HttpStatus.OK);
				}
				return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(null, null),
						HttpStatus.UNAUTHORIZED);
			} catch (Exception e) {
				boolean isUserAccountLocked = appUserRepository.updateUserAccountForFailedLoginAttempt(userCredential);
				if (isUserAccountLocked) {
					Email disableAccountEmail = appEmailService.createDisableAccountEmail(userCredential.getEmail());
					appEmailService.sendEmail(disableAccountEmail);
				}
				return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(null, null),
						HttpStatus.UNAUTHORIZED);
			}
		}
	}

	@PostMapping("/api/google/register")
	public ResponseEntity<GoogleRegistrationResponse> proccessGoogleRegistration(
			@RequestBody @Valid WebGoogleRegistration webGoogleRegistration, BindingResult result) {
		webGoogleRegistration.trimWebGoogleRegistrationFields();
		webGoogleRegistrationValidator.validate(webGoogleRegistration, result);
		if (result.hasErrors()) {
			ArrayList<String> errors = createErrorMessageArrayList(result);
			return new ResponseEntity<GoogleRegistrationResponse>(new GoogleRegistrationResponse(null, errors),
					HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			try {
				appUserRepository.updateGoogleUserToUser(webGoogleRegistration);
				return new ResponseEntity<GoogleRegistrationResponse>(
						new GoogleRegistrationResponse(webGoogleRegistration.getUserName(), null), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<GoogleRegistrationResponse>(new GoogleRegistrationResponse(null, null),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@PostMapping("/api/google/login/verify")
	public ResponseEntity<AuthenticationResponse> processGoogleLogin(
			@RequestBody @Valid OAuthGoogleAuthenticationRequest oAuthRequest) throws IOException {
		try {
			if (appGoogleTokenVerifierService.verifyToken(oAuthRequest.getToken())) {
				UserInfo userInfo = appUserRepository.retrieveUserInfoByEmail(oAuthRequest.getEmail());
				if (userInfo == null) {
					userInfo = new UserInfo(oAuthRequest.getEmail(), null, null, null, null, null, null, null, null,
							null);
					appUserRepository.insertCollectorGoogleUserRegistration(userInfo);
				}
				return new ResponseEntity<AuthenticationResponse>(createAuthenticationResponse(oAuthRequest.getEmail()),
						HttpStatus.OK);
			}
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(null, null),
					HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(null, null),
					HttpStatus.UNAUTHORIZED);
		}
	}

	@PostMapping("/api/register")
	public ResponseEntity<ArrayList<String>> processUserRegistration(
			@RequestBody @Valid WebUserRegistrationApi webUserRegistrationApi, BindingResult result) {
		webUserRegistrationApi.trimWebUserRegistrationApiFields();
		webUserRegistrationApiValidator.validate(webUserRegistrationApi, result);
		if (result.hasErrors()) {
			return new ResponseEntity<ArrayList<String>>(createErrorMessageArrayList(result),
					HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			WebUserRegistration webUserRegistration = appUserService
					.mapWebUserRegistrationApiToWebUserRegistration(webUserRegistrationApi);
			webUserRegistration.trimWebUserRegistrationFields();
			webUserRegistration.setPassword(passwordEncoder.encode(webUserRegistration.getPassword()));
			appUserRepository.insertCollectorUserRegistration(webUserRegistration);
			return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
		}
	}

	private ApiOpenResaleDealList createCollectorSpecificApiOpenResaleDealList(String username, int type) {
		UserInfo userInfo = appUserRepository.retrieveUserInfoByUsername(username);
		ArrayList<OpenResaleDeal> openResaleDeals = appResaleDealRepository.retrieveAllOpenResaleDealsByPosterEmail(userInfo.getEmail(), type);
		ArrayList<String> userResaleItemAttrsOne = appTradeDealRepository.retrieveUserTradeItemAttributeOne(type);
		ArrayList<String> userResaleItemAttrsTwo = appTradeDealRepository.retrieveUserTradeItemAttributeTwo(type);
		ArrayList<String> userResaleItemAttrsThree = appTradeDealRepository.retrieveUserTradeItemAttributeThree(type);
		ArrayList<String> userResaleItemAttrs = appTradeDealRepository.retrieveUserTradeItemAttributes(type);
		String collectibleType = appTradeDealRepository.retrieveCollectibleTypeNames().get(type-1);
		return new ApiOpenResaleDealList(null, null, openResaleDeals, userResaleItemAttrsOne, userResaleItemAttrsTwo,
				userResaleItemAttrsThree, userResaleItemAttrs, collectibleType);
	}

	private ApiOpenTradeDealList createCollectorSpecificApiOpenTradeDealList(String username, int type) {
		UserInfo userInfo = appUserRepository.retrieveUserInfoByUsername(username);
		ArrayList<OpenTradeDeal> openTradeDeals = appTradeDealRepository
				.retrieveAllOpenTradeDealsByPosterEmail(userInfo.getEmail(), type);
		ArrayList<ArrayList<OpenTradeItem>> posterTradeItems = appTradeDealService
				.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDeals);
		ArrayList<ArrayList<OpenTradeItem>> accepterTradeItems = appTradeDealService
				.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDeals);
		ArrayList<String> userTradeItemAttrsOne = appTradeDealRepository.retrieveUserTradeItemAttributeOne(type);
		ArrayList<String> userTradeItemAttrsTwo = appTradeDealRepository.retrieveUserTradeItemAttributeTwo(type);
		ArrayList<String> userTradeItemAttrsThree = appTradeDealRepository.retrieveUserTradeItemAttributeThree(type);
		ArrayList<String> userTradeItemAttrs = appTradeDealRepository.retrieveUserTradeItemAttributes(type);
		String collectibleType = appTradeDealRepository.retrieveCollectibleTypeNames().get(type - 1);
		return new ApiOpenTradeDealList(null, null, openTradeDeals, posterTradeItems, accepterTradeItems,
				userTradeItemAttrsOne, userTradeItemAttrsTwo, userTradeItemAttrsThree, userTradeItemAttrs,
				collectibleType);
	}

	private ArrayList<String> createResponse(String message) {
		ArrayList<String> response = new ArrayList<String>();
		response.add(message);
		return response;
	}

	private AuthenticationResponse createAuthenticationResponse(String email) {
		String userName = appUserRepository.retrieveUserName(email);
		String token = jwtService.generateToken(email);
		return new AuthenticationResponse(token, userName);
	}

	private ArrayList<String> createErrorMessageArrayList(BindingResult result) {
		ArrayList<String> errors = new ArrayList<String>();
		for (int i = 0; i < result.getAllErrors().size(); i++) {
			String message = result.getAllErrors().get(i).getDefaultMessage();
			if (message != null) {
				errors.add(result.getAllErrors().get(i).getDefaultMessage());
			} else {
				errors.add(
						messageSource.getMessage(result.getAllErrors().get(i).getCode(), null, null, Locale.ENGLISH));
			}
		}
		return errors;
	}
}
