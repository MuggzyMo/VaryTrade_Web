package com.VaryTrade.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.VaryTrade.Dao.ResaleDeal.AppResaleDealRepository;
import com.VaryTrade.Dao.TradeDeal.AppTradeDealRepository;
import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.ApiClosedResaleDealList;
import com.VaryTrade.Model.ApiOpenResaleDealList;
import com.VaryTrade.Model.ClosedResaleDeal;
import com.VaryTrade.Model.OpenResaleDeal;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebResaleDealApi;
import com.VaryTrade.Service.ResaleDeal.AppResaleDealService;

import jakarta.validation.Valid;

@RestController
public class ResaleApiController {
	@Autowired
	private AppResaleDealRepository appResaleDealRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppTradeDealRepository appTradeDealRepository;
	@Autowired
	private AppResaleDealService appResaleDealService;
	@Autowired
	private MessageSource messageSource;

	@PostMapping("/api/resale-deal/open/credit/accept")
	public ResponseEntity<ArrayList<String>> processAcceptResaleDealWithCredit(
			@RequestParam(name = "id", required = true) int id) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		OpenResaleDeal openResaleDeal = appResaleDealRepository.retrieveOpenResaleDealById(Integer.valueOf(id));
		BigDecimal credit = appUserRepository.retrieveUserCredit(email);
		BigDecimal authenticationFee = BigDecimal.valueOf(15);
		BigDecimal price = openResaleDeal.getPrice().add(authenticationFee);
		if (credit.compareTo(price) > -1) {
			appResaleDealRepository.updateOpenResaleDealAsAccepted(Integer.valueOf(id), email, price, true);
			return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
		} else {
			return new ResponseEntity<ArrayList<String>>(createResponse(
					"Must have enough credits to cover the price of the item and the $15.00 authentication fee."),
					HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping("/api/resale-deal/open/payment/accept")
	public ResponseEntity<ArrayList<String>> processAcceptResaleDealWithPayment(
			@RequestParam(name = "id", required = true) int id) {
		try {
			// Within here is where I should make a call to verify the details of the
			// transaction
			// but unfortunately the Flutter Braintree package doesn't provide me with the
			// information
			// that I need to do that. A better payment API would likely do so.
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			appResaleDealRepository.updateOpenResaleDealAsAccepted(Integer.valueOf(id), email, null, false);
			return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed"),
					HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping("/api/your-resale-deals/delete")
	public ResponseEntity<ArrayList<String>> deleteOpenTradeDeal(@RequestParam(name = "id", required = true) int id) {
		try {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			OpenResaleDeal openResaleDeal = appResaleDealRepository.retrieveOpenResaleDealById(id);
			if (email.equals(openResaleDeal.getPosterEmail())) {
				appResaleDealRepository.deleteOpenResale(id);
				return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
			} else {
				return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed"),
						HttpStatus.FORBIDDEN);
			}
		} catch (Exception e) {
			return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed"),
					HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping("/api/resale-deal/create")
	public ResponseEntity<ArrayList<String>> postResaleDeal(@RequestBody @Valid WebResaleDealApi webResaleDealApi,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<ArrayList<String>>(createErrorMessageArrayList(result),
					HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			OpenResaleDeal openResaleDeal = appResaleDealService.mapWebResaleDealApiToOpenResaleDeal(webResaleDealApi,
					SecurityContextHolder.getContext().getAuthentication().getName());
			openResaleDeal = appResaleDealRepository.insertOpenResaleDeal(openResaleDeal, webResaleDealApi.getType());
			return new ResponseEntity<ArrayList<String>>(createResponse(String.valueOf(openResaleDeal.getId())),
					HttpStatus.OK);
		}
	}

	@GetMapping("/api/your-resale-deals/closed")
	public ResponseEntity<ApiClosedResaleDealList> retrieveCollectorClosedResaleList(
			@RequestParam(name = "type", required = true) int type) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity<ApiClosedResaleDealList>(createApiCollectorClosedResaleList(email, type),
				HttpStatus.OK);
	}

	@GetMapping("/api/your-resale-deals/pending")
	public ResponseEntity<ApiOpenResaleDealList> retrieveCollectorPendingResaleList(
			@RequestParam(name = "type", required = true) int type) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity<ApiOpenResaleDealList>(createApiCollectorPendingResaleList(email, type),
				HttpStatus.OK);
	}

	@GetMapping("/api/your-resale-deals/open")
	public ResponseEntity<ApiOpenResaleDealList> retrieveCollectorOpenResaleList(
			@RequestParam(name = "type", required = true) int type) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity<ApiOpenResaleDealList>(createApiCollectorOpenResaleList(email, type), HttpStatus.OK);
	}

	@GetMapping("/api/resale-deal/open")
	public ResponseEntity<ApiOpenResaleDealList> retrieveOpenResaleList(
			@RequestParam(name = "type", required = true) int type) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity<ApiOpenResaleDealList>(createApiOpenResaleList(email, type), HttpStatus.OK);
	}

	private ApiClosedResaleDealList createApiCollectorClosedResaleList(String email, int type) {
		ArrayList<ClosedResaleDeal> closedResaleDeals = appResaleDealRepository
				.retrieveAllClosedResaleDealsByEmail(email, type);
		ArrayList<String> posters = retrievePosterUserNamesFromClosedResaleDeals(closedResaleDeals);
		ArrayList<String> accepters = retrieveAccepterUserNamesFromClosedResaleDeals(closedResaleDeals);
		ArrayList<String> userResaleItemAttrsOne = appTradeDealRepository.retrieveUserTradeItemAttributeOne(type);
		ArrayList<String> userResaleItemAttrsTwo = appTradeDealRepository.retrieveUserTradeItemAttributeTwo(type);
		ArrayList<String> userResaleItemAttrsThree = appTradeDealRepository.retrieveUserTradeItemAttributeThree(type);
		ArrayList<String> userResaleItemAttrs = appTradeDealRepository.retrieveUserTradeItemAttributes(type);
		String collectibleType = appTradeDealRepository.retrieveCollectibleTypeNames().get(type - 1);
		return new ApiClosedResaleDealList(posters, accepters, closedResaleDeals, userResaleItemAttrsOne,
				userResaleItemAttrsTwo, userResaleItemAttrsThree, userResaleItemAttrs, collectibleType);
	}

	private ApiOpenResaleDealList createApiCollectorPendingResaleList(String email, int type) {
		ArrayList<OpenResaleDeal> openResaleDeals = appResaleDealRepository.retrieveAllPendingResaleDealsByEmail(email,
				type);
		ArrayList<String> posters = retrievePosterUserNamesFromOpenResaleDeals(openResaleDeals);
		ArrayList<String> accepters = retrieveAccepterUserNamesFromOpenResaleDeals(openResaleDeals);
		ArrayList<String> userResaleItemAttrsOne = appTradeDealRepository.retrieveUserTradeItemAttributeOne(type);
		ArrayList<String> userResaleItemAttrsTwo = appTradeDealRepository.retrieveUserTradeItemAttributeTwo(type);
		ArrayList<String> userResaleItemAttrsThree = appTradeDealRepository.retrieveUserTradeItemAttributeThree(type);
		ArrayList<String> userResaleItemAttrs = appTradeDealRepository.retrieveUserTradeItemAttributes(type);
		String collectibleType = appTradeDealRepository.retrieveCollectibleTypeNames().get(type - 1);
		return new ApiOpenResaleDealList(posters, accepters, openResaleDeals, userResaleItemAttrsOne,
				userResaleItemAttrsTwo, userResaleItemAttrsThree, userResaleItemAttrs, collectibleType);
	}

	private ApiOpenResaleDealList createApiCollectorOpenResaleList(String email, int type) {
		ArrayList<OpenResaleDeal> openResaleDeals = appResaleDealRepository
				.retrieveAllOpenResaleDealsByPosterEmail(email, type);
		ArrayList<String> userResaleItemAttrsOne = appTradeDealRepository.retrieveUserTradeItemAttributeOne(type);
		ArrayList<String> userResaleItemAttrsTwo = appTradeDealRepository.retrieveUserTradeItemAttributeTwo(type);
		ArrayList<String> userResaleItemAttrsThree = appTradeDealRepository.retrieveUserTradeItemAttributeThree(type);
		ArrayList<String> userResaleItemAttrs = appTradeDealRepository.retrieveUserTradeItemAttributes(type);
		String collectibleType = appTradeDealRepository.retrieveCollectibleTypeNames().get(type - 1);
		return new ApiOpenResaleDealList(null, null, openResaleDeals, userResaleItemAttrsOne, userResaleItemAttrsTwo,
				userResaleItemAttrsThree, userResaleItemAttrs, collectibleType);
	}

	private ApiOpenResaleDealList createApiOpenResaleList(String email, int type) {
		ArrayList<OpenResaleDeal> openResaleDeals = appResaleDealRepository
				.retrieveOpenResaleDealsNotFromCollector(email, type);
		ArrayList<String> posters = retrievePosterUserNamesFromOpenResaleDeals(openResaleDeals);
		ArrayList<String> userResaleItemAttrsOne = appTradeDealRepository.retrieveUserTradeItemAttributeOne(type);
		ArrayList<String> userResaleItemAttrsTwo = appTradeDealRepository.retrieveUserTradeItemAttributeTwo(type);
		ArrayList<String> userResaleItemAttrsThree = appTradeDealRepository.retrieveUserTradeItemAttributeThree(type);
		ArrayList<String> userResaleItemAttrs = appTradeDealRepository.retrieveUserTradeItemAttributes(type);
		String collectibleType = appTradeDealRepository.retrieveCollectibleTypeNames().get(type - 1);
		return new ApiOpenResaleDealList(posters, null, openResaleDeals, userResaleItemAttrsOne, userResaleItemAttrsTwo,
				userResaleItemAttrsThree, userResaleItemAttrs, collectibleType);
	}

	private ArrayList<String> retrievePosterUserNamesFromClosedResaleDeals(
			ArrayList<ClosedResaleDeal> clsoedResaleDeals) {
		ArrayList<String> posterUserNames = new ArrayList<String>();
		ArrayList<UserInfo> posters = appResaleDealService.retrievePosterUserInfoForClosedResales(clsoedResaleDeals);
		for (int i = 0; i < posters.size(); i++) {
			posterUserNames.add(posters.get(i).getUserName());
		}
		return posterUserNames;
	}

	private ArrayList<String> retrieveAccepterUserNamesFromClosedResaleDeals(
			ArrayList<ClosedResaleDeal> closedResaleDeals) {
		ArrayList<String> accepterUserNames = new ArrayList<String>();
		ArrayList<UserInfo> accepters = appResaleDealService
				.retrieveAccepterUserInfoForClosedResales(closedResaleDeals);
		for (int i = 0; i < accepters.size(); i++) {
			accepterUserNames.add(accepters.get(i).getUserName());
		}
		return accepterUserNames;
	}

	private ArrayList<String> retrievePosterUserNamesFromOpenResaleDeals(ArrayList<OpenResaleDeal> openResaleDeals) {
		ArrayList<String> posterUserNames = new ArrayList<String>();
		ArrayList<UserInfo> posters = appResaleDealService.retrievePosterUserInfoForOpenResales(openResaleDeals);
		for (int i = 0; i < posters.size(); i++) {
			posterUserNames.add(posters.get(i).getUserName());
		}
		return posterUserNames;
	}

	private ArrayList<String> retrieveAccepterUserNamesFromOpenResaleDeals(ArrayList<OpenResaleDeal> openResaleDeals) {
		ArrayList<String> accepterUserNames = new ArrayList<String>();
		ArrayList<UserInfo> accepters = appResaleDealService.retrieveAccepterUserInfoForOpenResales(openResaleDeals);
		for (int i = 0; i < accepters.size(); i++) {
			accepterUserNames.add(accepters.get(i).getUserName());
		}
		return accepterUserNames;
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

	private ArrayList<String> createResponse(String message) {
		ArrayList<String> response = new ArrayList<String>();
		response.add(message);
		return response;
	}
}
