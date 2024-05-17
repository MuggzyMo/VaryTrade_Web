package com.VaryTrade.Controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.VaryTrade.Dao.Misc.AppMiscRepository;
import com.VaryTrade.Dao.ResaleDeal.AppResaleDealRepository;
import com.VaryTrade.Dao.TradeDeal.AppTradeDealRepository;
import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.Email;

import com.VaryTrade.Model.OpenResaleDeal;
import com.VaryTrade.Model.OpenTradeDeal;
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
import com.VaryTrade.Model.WebUserForgottenPassword;
import com.VaryTrade.Model.WebUserSearch;
import com.VaryTrade.Service.Email.AppEmailService;
import com.VaryTrade.Service.ResaleDeal.AppResaleDealService;
import com.VaryTrade.Service.TradeDeal.AppTradeDealService;
import com.VaryTrade.Service.User.AppUserService;
import com.VaryTrade.Validator.WebCreateAccountPasswordValidator;
import com.VaryTrade.Validator.WebEditUserInfoValidator;
import com.VaryTrade.Validator.WebEditUserPasswordValidator;
import com.VaryTrade.Validator.WebGoogleRegistrationValidator;
import com.VaryTrade.Validator.WebHyperwalletRegistrationValidator;
import com.VaryTrade.Validator.WebUserForgottenPasswordValidator;
import com.VaryTrade.Validator.WebUserRegistrationValidator;
import com.hyperwallet.clientsdk.Hyperwallet;
import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.HyperwalletPayment;
import com.hyperwallet.clientsdk.model.HyperwalletUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserController {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private SecurityContextRepository securityContextRepository;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AppEmailService appEmailService;
	@Autowired
	private AppTradeDealService appTradeDealService;
	@Autowired
	private AppResaleDealService appResaleDealService;
	@Autowired
	private AppMiscRepository appMiscRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppResaleDealRepository appResaleDealRepository;
	@Autowired
	private AppTradeDealRepository appTradeDealRepository;
	@Autowired
	private WebUserRegistrationValidator webUserRegistrationValidator;
	@Autowired
	private WebEditUserInfoValidator webEditUserInfoValidator;
	@Autowired
	private WebEditUserPasswordValidator webEditUserPasswordValidator;
	@Autowired
	private WebHyperwalletRegistrationValidator webHyperwalletRegistrationValidator;
	@Autowired
	private WebUserForgottenPasswordValidator webUserForgottenPasswordValidator;
	@Autowired
	private WebGoogleRegistrationValidator webGoogleRegistrationValidator;
	@Autowired
	private WebCreateAccountPasswordValidator webCreateUserPasswordValidator;
	@Value("${hyperwallet.username}")
	private String hyperwalletUsername;
	@Value("${hyperwallet.password}")
	private String hyperwalletPassword;

	@GetMapping("/account/password/create")
	public String displayCreatePasswordForm(WebCreateAccountPassword webCreateAccountPassword, Model model,
			Principal principal) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		if (appUserRepository.retrieveUserCredential(principal.getName()) == null) {
			return "CreateAccountPassword";
		} else {
			return "NotAllowed";
		}
	}

	@PostMapping("/account/password/create")
	public String processCreatePasswordForm(@Valid WebCreateAccountPassword webCreateAccountPassword,
			BindingResult result, Model model, Principal principal) {
		webCreateAccountPassword.setEmail(principal.getName());
		webCreateAccountPassword.trimWebCreateAccountPasswordFields();
		webCreateUserPasswordValidator.validate(webCreateAccountPassword, result);
		if (result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			return "CreateAccountPassword";
		} else {
			webCreateAccountPassword.setPassword(passwordEncoder.encode(webCreateAccountPassword.getPassword()));
			appUserRepository.insertCredential(webCreateAccountPassword);
			return "redirect:/account";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	@PostMapping("/account/payout")
	public String processPayout(Model model, Principal principal, RedirectAttributes redirectAttrs) {
		BigDecimal credit = appUserRepository.retrieveUserCredit(principal.getName());
		if (credit.compareTo(BigDecimal.valueOf(10.00)) < 0) {
			redirectAttrs.addAttribute("error", "credit");
			return "redirect:/account";
		} else {
			Hyperwallet hyperwallet = new Hyperwallet(hyperwalletUsername, hyperwalletPassword);
			String userToken = appUserRepository.retrieveUserHyperWalletToken(principal.getName());
			HyperwalletPayment payment;

			try {
				payment = appUserService.createHyperwalletPayment(credit, userToken);
				payment = hyperwallet.createPayment(payment);
				Payout payout = appUserRepository.insertPayout(payment, principal.getName());
				return "redirect:/account/payout/confirmation?id=" + payout.getId();
			} catch (HyperwalletException e) {
				return "redirect:/error";
			}
		}
	}

	@GetMapping("/account/payout/all")
	public String displayPayoutList(Model model, Principal principal) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("allPayout", appUserRepository.retrieveAllPayoutsForCollector(principal.getName()));
		return "CollectorPayoutList";
	}

	@GetMapping("/account/payout/confirmation")
	public String displayPayoutConfirmation(@RequestParam(name = "id", required = true) String id, Model model,
			Principal principal) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());

		Payout payout = appUserRepository.retrievePayout(id);

		if (!payout.getEmail().equals(principal.getName())) {
			return "Not Allowed";
		} else {
			model.addAttribute("payout", payout);
			return "CollectorPayoutConfirmation";
		}
	}

	@GetMapping("/account/payout/setup")
	public String displayHyperwalletRegistration(WebHyperwalletRegistration webHyperwalletRegistration, Model model,
			Principal principal) {
		String hyperwalletToken = appUserRepository.retrieveUserHyperWalletToken(principal.getName());
		if (hyperwalletToken != null) {
			return "NotAllowed";
		} else {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			return "HyperwalletRegistration";
		}
	}

	@PostMapping("/account/payout/setup/submit")
	public String processHyperwalletRegistration(@Valid WebHyperwalletRegistration webHyperwalletRegistration,
			BindingResult result, Model model, Principal principal) throws ParseException {
		webHyperwalletRegistrationValidator.validate(webHyperwalletRegistration, result);
		if (result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			return "HyperwalletRegistration";
		}

		UserInfo userInfo = appUserRepository.retrieveUserInfoByEmail(principal.getName());
		Hyperwallet hyperwallet = new Hyperwallet(hyperwalletUsername, hyperwalletPassword);

		HyperwalletUser hyperwalletUser;
		try {
			hyperwalletUser = appUserService.createHyperwalletUser(userInfo,
					webHyperwalletRegistration.getDateOfBirth());
			hyperwalletUser = hyperwallet.createUser(hyperwalletUser);
			appUserRepository.insertHyperwalletInfo(hyperwalletUser);
			return "redirect:/account";
		} catch (HyperwalletException e) {
			return "redirect:/error";
		}

	}

	@GetMapping("/password/forgot")
	public String displayForgotPassword(WebUserForgottenPassword webUserForgottenPassword, Model model) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		return "ForgotPassword";
	}

	@PostMapping("/password/forgot")
	public String processForgotPassword(@Valid WebUserForgottenPassword webUserForgottenPassword, BindingResult result,
			Model model) {
		webUserForgottenPasswordValidator.validate(webUserForgottenPassword, result);
		if (result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			return "ForgotPassword";
		} else {
			UserCredential userCredential = appUserRepository
					.retrieveUserCredential(webUserForgottenPassword.getEmail());
			if (userCredential != null) {
				String password = appUserService.generateRandomPassayPassword();
				System.out.println(password);
				Email email = appEmailService.createForgotPasswordEmail(webUserForgottenPassword.getEmail().trim(),
						password);
				appUserRepository.updateUserPassword(webUserForgottenPassword.getEmail().trim(),
						passwordEncoder.encode(password));
				appEmailService.sendEmail(email);
			}
			return "redirect:/login";
		}
	}

	@GetMapping("/profile/search")
	public String displayCollectorPublicProfileSearch(WebUserSearch webUserSearch, Model model) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("userFound", true);
		return "ProfileSearch";
	}

	@PostMapping("/profile/search")
	public String processCollectorPublicProfileSearch(@Valid WebUserSearch webUserSearch, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("userFound", true);
			return "ProfileSearch";
		} else if (appUserRepository.retrieveUserInfoByUsername(webUserSearch.getUserName()) == null) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("userFound", false);
			return "ProfileSearch";
		} else {
			return "redirect:/profile?username=" + webUserSearch.getUserName();
		}
	}

	@GetMapping("/profile")
	public String displayCollectorPublicProfile(@RequestParam(name = "username", required = true) String username,
			Model model, Principal principal) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		UserInfo userInfo = appUserRepository.retrieveUserInfoByUsername(username);
		if (userInfo == null || userInfo.getEmail().equals(principal.getName())
				|| appUserService.isNonprincipalUserAnAdmin(userInfo.getEmail())) {
			return "NotAllowed";
		} else {
			model.addAttribute("user", userInfo);
			ArrayList<String> collectibleTypeName = appTradeDealRepository.retrieveCollectibleTypeNames();
			model.addAttribute("collectibleTypeName", collectibleTypeName);
			model.addAttribute("followStatus",
					appUserService.doesCollectorFollowCollector(principal.getName(), userInfo.getEmail()));

			model = itemTypeOneAttachedToModelCollectorProfilePage(model, userInfo.getEmail());

			if (collectibleTypeName.size() == 2) {
				model = itemTypeTwoAttachedToModelCollectorProfilePage(model, userInfo.getEmail());
			} else if (collectibleTypeName.size() == 3) {
				model = itemTypeTwoAttachedToModelCollectorProfilePage(model, userInfo.getEmail());
				model = itemTypeThreeAttachedToModelCollectorProfilePage(model, userInfo.getEmail());
			}
			return "CollectorPublicProfile";
		}
	}

	@PostMapping("/profile/follow")
	public String processCollectorPublicProfileFollow(@RequestParam(name = "username", required = true) String username,
			Model model, Principal principal) {
		UserInfo follower = appUserRepository.retrieveUserInfoByEmail(principal.getName());
		UserInfo followed = appUserRepository.retrieveUserInfoByUsername(username);
		appUserRepository.insertFollower(follower.getEmail(), followed.getEmail(), follower.getUserName(),
				followed.getUserName());
		return "redirect:/profile?username=" + username;
	}

	@PostMapping("/profile/unfollow")
	public String processCollectorPublicProfileUnfollow(
			@RequestParam(name = "username", required = true) String username, Model model, Principal principal) {
		UserInfo followed = appUserRepository.retrieveUserInfoByUsername(username);
		appUserRepository.deleteFollower(principal.getName(), followed.getEmail());
		return "redirect:/profile?username=" + username;
	}

	@GetMapping("/followers")
	public String displayFollowers(Model model, Principal principal) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("followers",
				appUserRepository.retrieveUserNamesForCollectorsFollowingCollector(principal.getName()));
		return "Followers";
	}

	@GetMapping("/following")
	public String displayFollowing(Model model, Principal principal) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("following",
				appUserRepository.retrieveUserNamesForCollectorsFollowedByCollector(principal.getName()));
		return "Following";
	}

	@GetMapping("/admin/register/confirmation")
	public String displayAdminRegistrationConfirmation(Model model) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		return "AdminRegistrationConfirmation";
	}

	@GetMapping("/register")
	public String displayCollectorRegistrationForm(Model model, WebUserRegistration webUserRegistration) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("states", appMiscRepository.retrieveStates());
		return "CollectorRegistration";
	}

	@GetMapping("/admin/register")
	public String displayAdminRegistrationForm(Model model, WebUserRegistration webUserRegistration) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("states", appMiscRepository.retrieveStates());
		return "AdminRegistration";
	}

	@PostMapping("/admin/register")
	public String processAdminRegistrationForm(@Valid WebUserRegistration webUserRegistration, BindingResult result,
			Model model) {
		webUserRegistrationValidator.validate(webUserRegistration, result);
		if (result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("states", appMiscRepository.retrieveStates());
			return "AdminRegistration";
		} else {
			webUserRegistration.trimWebUserRegistrationFields();
			webUserRegistration.setPassword(passwordEncoder.encode(webUserRegistration.getPassword()));
			appUserRepository.insertAdmin(webUserRegistration);
			return "redirect:/admin/register/confirmation";
		}
	}

	@PostMapping("/register")
	public String processCollectorRegistrationForm(@Valid WebUserRegistration webUserRegistration, BindingResult result,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		webUserRegistration.trimWebUserRegistrationFields();
		webUserRegistrationValidator.validate(webUserRegistration, result);
		if (result.hasErrors()) {
			model.addAttribute("webUser", webUserRegistration);
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("states", appMiscRepository.retrieveStates());
			return "CollectorRegistration";
		} else {
			webUserRegistration.setPassword(passwordEncoder.encode(webUserRegistration.getPassword()));
			appUserRepository.insertCollectorUserRegistration(webUserRegistration);
			autoLogin(webUserRegistration, "ROLE_USER", request, response);
			return "redirect:/home";
		}
	}

	@GetMapping("/google/register")
	public String displayGoogleRegistrationForm(WebGoogleRegistration webGoogleRegistration, Model model) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("states", appMiscRepository.retrieveStates());
		return "GoogleRegistration";
	}

	@PostMapping("/google/register")
	public String processGoogleRegistrationForm(@Valid WebGoogleRegistration webGoogleRegistration,
			BindingResult result, Model model, Principal principal, HttpServletRequest request) {
		webGoogleRegistration.setEmail(principal.getName());
		webGoogleRegistration.trimWebGoogleRegistrationFields();
		webGoogleRegistrationValidator.validate(webGoogleRegistration, result);
		if (result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("states", appMiscRepository.retrieveStates());
			return "GoogleRegistration";
		} else {
			appUserRepository.updateGoogleUserToUser(webGoogleRegistration);
			appUserService.setSecurityContextAuthorizationFromGoogleUserToUser(webGoogleRegistration.getEmail(),
					request);
			return "redirect:/home";
		}

	}

	@GetMapping({ "/", "/home" })
	public String displayUserHome(Principal principal, Model model) {
		ArrayList<String> collectibleTypeName = appTradeDealRepository.retrieveCollectibleTypeNames();
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleTypeName", collectibleTypeName);
		if (appUserService.doesUserHaveRole("ROLE_ADMIN")) {
			model = itemTypeOneAttachedToModelAdminHomePage(model);
			if (collectibleTypeName.size() == 2) {
				model = itemTypeTwoAttachedToModelAdminHomePage(model);
			} else if (collectibleTypeName.size() == 3) {
				model = itemTypeTwoAttachedToModelAdminHomePage(model);
				model = itemTypeThreeAttachedToModelAdminHomePage(model);
			}
			return "AdminHome";
		} else {
			itemTypeOneAttachedToModelCollectorHomePage(model, principal);

			if (collectibleTypeName.size() == 2) {
				model = itemTypeTwoAttachedToModelCollectorHomePage(model, principal);
			} else if (collectibleTypeName.size() == 3) {
				model = itemTypeTwoAttachedToModelCollectorHomePage(model, principal);
				model = itemTypeThreeAttachedToModelCollectorHomePage(model, principal);
			}

			return "CollectorHome";
		}
	}

	@GetMapping("/login")
	public String displayLoginForm(Model model) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		return "Login";
	}

	@GetMapping("/account")
	public String displayUserInfo(Principal principal, Model model) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("user", appUserRepository.retrieveUserInfoByEmail(principal.getName()));
		if (!appUserService.doesUserHaveRole("ROLE_ADMIN")) {
			model.addAttribute("credit", appUserRepository.retrieveUserCredit(principal.getName()));

			UserCredential userCredential = appUserRepository.retrieveUserCredential(principal.getName());
			if (userCredential != null) {
				model.addAttribute("passwordSetup", false);
			} else {
				model.addAttribute("passwordSetup", true);
			}

			String hyperwalletToken = appUserRepository.retrieveUserHyperWalletToken(principal.getName());
			if (hyperwalletToken == null) {
				model.addAttribute("hyperwallet", true);
			} else {
				model.addAttribute("hyperwallet", false);
			}
		}
		return "UserInfo";
	}

	@GetMapping("/account/edit")
	public String displayEditUserInfoForm(Principal principal, Model model) {
		UserInfo userInfo = appUserRepository.retrieveUserInfoByEmail(principal.getName());
		UserCredential userCredential = appUserRepository.retrieveUserCredential(principal.getName());

		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("states", appMiscRepository.retrieveStates());
		if (userCredential == null) {
			WebEditGoogleUserInfo webEditGoogleUserInfo = userInfo.mapUserInfoToWebEditGoogleUserInfo();
			model.addAttribute("webEditGoogleUserInfo", webEditGoogleUserInfo);
			return "EditGoogleUserInfo";
		} else {
			WebEditUserInfo webEditUserInfo = userInfo.mapUserInfoToWebEditUserInfo();
			model.addAttribute("webEditUserInfo", webEditUserInfo);
			return "EditUserInfo";
		}
	}

	@PostMapping("/account/google/edit")
	public String processEditGoogleUserInfoForm(@Valid WebEditGoogleUserInfo webEditGoogleUserInfo,
			BindingResult result, Principal principal, Model model) {
		webEditGoogleUserInfo.trimWebEditGoogleUserInfoFields();
		if (result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("states", appMiscRepository.retrieveStates());
			return "EditGoogleUserInfo";
		} else {
			appUserRepository.updateGoogleUserInfo(webEditGoogleUserInfo, principal.getName());
			return "redirect:/account";
		}
	}

	@PostMapping("/account/edit")
	public String processEditUserInfoForm(@Valid WebEditUserInfo webEditUserInfo, BindingResult result,
			Principal principal, Model model) {
		webEditUserInfo.setOriginalEmail(principal.getName());
		webEditUserInfo.trimWebEditUserInfoFields();
		webEditUserInfoValidator.validate(webEditUserInfo, result);
		if (result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("states", appMiscRepository.retrieveStates());
			return "EditUserInfo";
		} else {
			appUserRepository.updateUserInfo(webEditUserInfo);
			UserInfo userInfo = appUserRepository.retrieveUserInfoByEmail(webEditUserInfo.getEmail());
			if (!userInfo.getEmail().equals(principal.getName())) {
				return "redirect:/logout";
			} else {
				return "redirect:/account";
			}
		}
	}

	@GetMapping("/account/password/edit")
	public String displayEditPasswordForm(WebEditUserPassword webEditUserPassword, Model model, Principal principal) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		return "EditUserPassword";
	}

	@PostMapping("/account/password/edit")
	public String processEditPasswordForm(@Valid WebEditUserPassword webEditUserPassword, BindingResult result,
			Principal principal, Model model) {
		webEditUserPassword.setEmail(principal.getName());
		webEditUserPassword.trimWebEditUserPasswordFields();
		webEditUserPasswordValidator.validate(webEditUserPassword, result);
		if (result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			return "EditUserPassword";
		} else {
			webEditUserPassword.setPassword(passwordEncoder.encode(webEditUserPassword.getPassword()));
			appUserRepository.updateUserPassword(principal.getName(), webEditUserPassword.getPassword());
			return "redirect:/account";
		}
	}

	private Model itemTypeOneAttachedToModelAdminHomePage(Model model) {
		ArrayList<OpenTradeDeal> pendingTradeDeal = appTradeDealRepository.retrieveAllPendingTradeDeals(1);
		ArrayList<OpenResaleDeal> pendingResaleDeal = appResaleDealRepository.retrieveAllPendingResaleDeals(1);
		model.addAttribute("pendingTradeDealsOne", pendingTradeDeal);
		model.addAttribute("pendingResaleDealOne", pendingResaleDeal);
		model.addAttribute("posterTradeItemsOne",
				appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("accepterTradeItemsOne",
				appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("userTradeItemAttrsOneOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userTradeItemAttrsTwoOne", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userTradeItemAttrsThreeOne", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("userTradeItemAttrOne", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		return model;
	}

	private Model itemTypeTwoAttachedToModelAdminHomePage(Model model) {
		ArrayList<OpenTradeDeal> pendingTradeDeal = appTradeDealRepository.retrieveAllPendingTradeDeals(2);
		ArrayList<OpenResaleDeal> pendingResaleDeal = appResaleDealRepository.retrieveAllPendingResaleDeals(2);
		model.addAttribute("pendingTradeDealsTwo", pendingTradeDeal);
		model.addAttribute("pendingResaleDealTwo", pendingResaleDeal);
		model.addAttribute("posterTradeItemsTwo",
				appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("accepterTradeItemsTwo",
				appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("userTradeItemAttrsOneTwo", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userTradeItemAttrsTwoTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userTradeItemAttrsThreeTwo", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("userTradeItemAttrTwo", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		return model;
	}

	private Model itemTypeThreeAttachedToModelAdminHomePage(Model model) {
		ArrayList<OpenTradeDeal> pendingTradeDeal = appTradeDealRepository.retrieveAllPendingTradeDeals(3);
		ArrayList<OpenResaleDeal> pendingResaleDeal = appResaleDealRepository.retrieveAllPendingResaleDeals(3);
		model.addAttribute("pendingTradeDealsThree", pendingTradeDeal);
		model.addAttribute("pendingResaleDealThree", pendingResaleDeal);
		model.addAttribute("posterTradeItemsThree",
				appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("accepterTradeItemsThree",
				appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("userTradeItemAttrsOneThree", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userTradeItemAttrsTwoThree", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userTradeItemAttrsThreeThree",
				appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("userTradeItemAttrThree", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		return model;
	}

	private Model itemTypeOneAttachedToModelCollectorProfilePage(Model model, String email) {
		ArrayList<OpenTradeDeal> openTradeDealOne = appTradeDealRepository.retrieveAllOpenTradeDealsByPosterEmail(email,
				1);
		ArrayList<OpenResaleDeal> openResaleDealOne = appResaleDealRepository
				.retrieveAllOpenResaleDealsByPosterEmail(email, 1);
		System.out.println("Size " + openTradeDealOne.size());
		model.addAttribute("openTradeDealsOne", openTradeDealOne);
		model.addAttribute("openResaleDealsOne", openResaleDealOne);
		model.addAttribute("openPosterTradeItemsOne",
				appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDealOne));
		model.addAttribute("openAccepterTradeItemsOne",
				appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDealOne));
		model.addAttribute("userTradeItemAttrsOneOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userTradeItemAttrsTwoOne", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userTradeItemAttrsThreeOne", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("userTradeItemAttrOne", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		return model;
	}

	private Model itemTypeTwoAttachedToModelCollectorProfilePage(Model model, String email) {
		ArrayList<OpenTradeDeal> openTradeDealTwo = appTradeDealRepository.retrieveAllOpenTradeDealsByPosterEmail(email,
				2);
		ArrayList<OpenResaleDeal> openResaleDealTwo = appResaleDealRepository
				.retrieveAllOpenResaleDealsByPosterEmail(email, 2);
		model.addAttribute("openTradeDealsTwo", openTradeDealTwo);
		model.addAttribute("openResaleDealsTwo", openResaleDealTwo);
		model.addAttribute("openPosterTradeItemsTwo",
				appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDealTwo));
		model.addAttribute("openAccepterTradeItemsTwo",
				appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDealTwo));
		model.addAttribute("userTradeItemAttrsOneTwo", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userTradeItemAttrsTwoTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userTradeItemAttrsThreeTwo", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("userTradeItemAttrTwo", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		return model;
	}

	private Model itemTypeThreeAttachedToModelCollectorProfilePage(Model model, String email) {
		ArrayList<OpenTradeDeal> openTradeDealThree = appTradeDealRepository
				.retrieveAllOpenTradeDealsByPosterEmail(email, 3);
		ArrayList<OpenResaleDeal> openResaleDealThree = appResaleDealRepository
				.retrieveAllOpenResaleDealsByPosterEmail(email, 3);
		model.addAttribute("openTradeDealsThree", openTradeDealThree);
		model.addAttribute("openResaleDealsThree", openResaleDealThree);
		model.addAttribute("openPosterTradeItemsThree",
				appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDealThree));
		model.addAttribute("openAccepterTradeItemsThree",
				appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDealThree));
		model.addAttribute("userTradeItemAttrsOneThree", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userTradeItemAttrsTwoThree", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userTradeItemAttrsThreeThree",
				appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("userTradeItemAttrThree", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		return model;
	}

	private Model itemTypeOneAttachedToModelCollectorHomePage(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDealOne = appTradeDealRepository
				.retrieveRecentOpenTradeDealsNotFromCollector(principal.getName(), 1);
		ArrayList<OpenResaleDeal> openResaleDealOne = appResaleDealRepository
				.retrieveRecentOpenResaleDealsNotFromCollector(principal.getName(), 1);
		model.addAttribute("posterInfoOne", appTradeDealService.retrieveUserInfoForOpenTrades(openTradeDealOne));
		model.addAttribute("openTradeDealsOne", openTradeDealOne);
		model.addAttribute("openResaleDealsOne", openResaleDealOne);
		model.addAttribute("posterTradeItemsOne",
				appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDealOne));
		model.addAttribute("accepterTradeItemsOne",
				appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDealOne));
		model.addAttribute("userTradeItemAttrsOneOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userTradeItemAttrsTwoOne", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userTradeItemAttrsThreeOne", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("userTradeItemAttrOne", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		model.addAttribute("posterInfoResaleOne",
				appResaleDealService.retrievePosterUserInfoForOpenResales(openResaleDealOne));
		return model;
	}

	private Model itemTypeTwoAttachedToModelCollectorHomePage(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDealTwo = appTradeDealRepository
				.retrieveRecentOpenTradeDealsNotFromCollector(principal.getName(), 2);
		ArrayList<OpenResaleDeal> openResaleDealTwo = appResaleDealRepository
				.retrieveRecentOpenResaleDealsNotFromCollector(principal.getName(), 2);
		model.addAttribute("posterInfoTwo", appTradeDealService.retrieveUserInfoForOpenTrades(openTradeDealTwo));
		model.addAttribute("openTradeDealsTwo", openTradeDealTwo);
		model.addAttribute("openResaleDealsTwo", openResaleDealTwo);
		model.addAttribute("posterTradeItemsTwo",
				appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDealTwo));
		model.addAttribute("accepterTradeItemsTwo",
				appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDealTwo));
		model.addAttribute("userTradeItemAttrsOneTwo", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userTradeItemAttrsTwoTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userTradeItemAttrsThreeTwo", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("userTradeItemAttrTwo", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		model.addAttribute("posterInfoResaleTwo",
				appResaleDealService.retrievePosterUserInfoForOpenResales(openResaleDealTwo));
		return model;
	}

	private Model itemTypeThreeAttachedToModelCollectorHomePage(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDealThree = appTradeDealRepository
				.retrieveRecentOpenTradeDealsNotFromCollector(principal.getName(), 3);
		ArrayList<OpenResaleDeal> openResaleDealThree = appResaleDealRepository
				.retrieveRecentOpenResaleDealsNotFromCollector(principal.getName(), 3);
		model.addAttribute("posterInfoThree", appTradeDealService.retrieveUserInfoForOpenTrades(openTradeDealThree));
		model.addAttribute("openTradeDealsThree", openTradeDealThree);
		model.addAttribute("openResaleDealsThree", openResaleDealThree);
		model.addAttribute("posterTradeItemsThree",
				appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDealThree));
		model.addAttribute("accepterTradeItemsThree",
				appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDealThree));
		model.addAttribute("userTradeItemAttrsOneThree", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userTradeItemAttrsTwoThree", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userTradeItemAttrsThreeThree",
				appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("userTradeItemAttrThree", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		model.addAttribute("posterInfoResaleThree",
				appResaleDealService.retrievePosterUserInfoForOpenResales(openResaleDealThree));
		return model;
	}

	private void autoLogin(WebUserRegistration webUserRegistration, String role, HttpServletRequest request,
			HttpServletResponse response) {
		Collection<GrantedAuthority> authorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(String.join(", ", role));
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				webUserRegistration.getEmail(), null, authorities);
		SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
		SecurityContext context = securityContextHolderStrategy.createEmptyContext();
		context.setAuthentication(auth);
		securityContextHolderStrategy.setContext(context);
		securityContextRepository.saveContext(context, request, response);

	}
}
