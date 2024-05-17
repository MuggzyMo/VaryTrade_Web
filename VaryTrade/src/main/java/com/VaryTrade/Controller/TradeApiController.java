package com.VaryTrade.Controller;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.VaryTrade.Dao.TradeDeal.AppTradeDealRepository;
import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.ApiClosedTradeDealList;
import com.VaryTrade.Model.ApiOpenTradeDealList;
import com.VaryTrade.Model.ClosedTradeDeal;
import com.VaryTrade.Model.ClosedTradeItem;
import com.VaryTrade.Model.OpenTradeDeal;
import com.VaryTrade.Model.OpenTradeItem;
import com.VaryTrade.Model.UserInfo;
import com.VaryTrade.Model.WebTradeDealApi;
import com.VaryTrade.Service.TradeDeal.AppTradeDealService;

import jakarta.validation.Valid;

@RestController
public class TradeApiController {
	@Autowired
	private AppTradeDealRepository appTradeDealRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppTradeDealService appTradeDealService;
	
	@PostMapping("/api/trade-deal/open/credit/accept")
	public ResponseEntity<ArrayList<String>> processAcceptTradeDealWithCredit(
			@RequestParam(name = "id", required = true) int id) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		BigDecimal credit = appUserRepository.retrieveUserCredit(email);
		BigDecimal authenticationFee = BigDecimal.valueOf(15);
		if(credit.compareTo(authenticationFee) > -1) {
			appTradeDealRepository.updateOpenTradeDealAsAccepted(Integer.valueOf(id), email, true);
			return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<ArrayList<String>>(createResponse(
					"Must have enough credits to cover the price of the $15.00 authentication fee."),
					HttpStatus.FORBIDDEN);
		}	
	}

	@PostMapping("/api/trade-deal/open/payment/accept")
	public ResponseEntity<ArrayList<String>> processAcceptTradeDealWithPayment(
			@RequestParam(name = "id", required = true) int id) {
		try {
			// Within here is where I should make a call to verify the details of the
			// transaction
			// but unfortunately the Flutter Braintree package doesn't provide me with the
			// information
			// that I need to do that. A better payment API would likely do so.
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			appTradeDealRepository.updateOpenTradeDealAsAccepted(Integer.valueOf(id), email, false);	
			return new ResponseEntity<ArrayList<String>>(createResponse("Success"), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<ArrayList<String>>(createResponse("This request could not be processed"),
					HttpStatus.FORBIDDEN);
		}
	}

	@PostMapping("/api/your-trade-deals/delete")
	public ResponseEntity<ArrayList<String>> deleteOpenTradeDeal(@RequestParam(name = "id", required = true) int id) {
		try {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			OpenTradeDeal openTradeDeal = appTradeDealRepository.retrieveOpenTradeDeal(id);
			if (email.equals(openTradeDeal.getPosterEmail())) {
				appTradeDealRepository.deleteOpenTrade(Integer.valueOf(id));
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

	@GetMapping("/api/attributes/names")
	public ResponseEntity<ArrayList<String>> retrieveTradeItemAttributeNames(
			@RequestParam(name = "type", required = true) int type) {
		return new ResponseEntity<ArrayList<String>>(appTradeDealRepository.retrieveUserTradeItemAttributes(type),
				HttpStatus.OK);
	}

	@GetMapping("/api/attributes-three")
	public ResponseEntity<ArrayList<String>> retrieveTradeItemAttributeOneValues(
			@RequestParam(name = "type", required = true) int type) {
		return new ResponseEntity<ArrayList<String>>(appTradeDealRepository.retrieveUserTradeItemAttributeThree(type),
				HttpStatus.OK);
	}

	@GetMapping("/api/attributes-two")
	public ResponseEntity<ArrayList<String>> retrieveTradeItemAttributeTwoValues(
			@RequestParam(name = "type", required = true) int type) {
		return new ResponseEntity<ArrayList<String>>(appTradeDealRepository.retrieveUserTradeItemAttributeTwo(type),
				HttpStatus.OK);
	}

	@GetMapping("/api/attributes-one")
	public ResponseEntity<ArrayList<String>> retrieveTradeItemAttributeThreeValues(
			@RequestParam(name = "type", required = true) int type) {
		return new ResponseEntity<ArrayList<String>>(appTradeDealRepository.retrieveUserTradeItemAttributeOne(type),
				HttpStatus.OK);
	}

	@GetMapping("/api/collectible/conditions")
	public ResponseEntity<ArrayList<String>> retrieveConditions(
			@RequestParam(name = "type", required = true) int type) {
		return new ResponseEntity<ArrayList<String>>(appTradeDealRepository.retrieveConditions(type), HttpStatus.OK);
	}

	@GetMapping("/api/trade-deal/collectibles")
	public ResponseEntity<ArrayList<String>> retrieveTradeItems(
			@RequestParam(name = "type", required = true) int type) {
		return new ResponseEntity<ArrayList<String>>(appTradeDealRepository.retrieveTradeItemNames(type),
				HttpStatus.OK);
	}

	@PostMapping("/api/trade-deal/create")
	public ResponseEntity<ArrayList<String>> postTradeDeal(@RequestBody @Valid WebTradeDealApi webTradeDealApi) {
		if (webTradeDealApi == null || webTradeDealApi.getPosterTradeItems().isEmpty()
				|| webTradeDealApi.getAccepterTradeItems().isEmpty()) {
			return new ResponseEntity<ArrayList<String>>(
					createResponse("Must have items you wish to trade and items you wish to receive."),
					HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			OpenTradeDeal openTradeDeal = appTradeDealRepository.insertOpenTradeDealApi(webTradeDealApi,
					SecurityContextHolder.getContext().getAuthentication().getName(), webTradeDealApi.getType());
			return new ResponseEntity<ArrayList<String>>(createResponse(String.valueOf(openTradeDeal.getId())),
					HttpStatus.OK);
		}
	}

	@GetMapping("/api/your-trade-deals/closed")
	public ResponseEntity<ApiClosedTradeDealList> retrieveCollectorClosedTradeDeals(
			@RequestParam(name = "type", required = true) int type) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity<ApiClosedTradeDealList>(createApiCollectorClosedDealTradeList(email, type),
				HttpStatus.OK);
	}

	@GetMapping("/api/your-trade-deals/open")
	public ResponseEntity<ApiOpenTradeDealList> retrieveCollectorOpenTradeDeals(
			@RequestParam(name = "type", required = true) int type) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity<ApiOpenTradeDealList>(createApiCollectorOpenTradeDealList(email, type),
				HttpStatus.OK);
	}

	@GetMapping("/api/your-trade-deals/pending")
	public ResponseEntity<ApiOpenTradeDealList> retrieveCollectorPendingTradeDeals(
			@RequestParam(name = "type", required = true) int type) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity<ApiOpenTradeDealList>(createApiCollectorPendingTradeDealList(email, type),
				HttpStatus.OK);
	}

	@GetMapping("/api/collectible/types")
	public ResponseEntity<ArrayList<String>> retrieveTradeItemTypes() {
		return new ResponseEntity<ArrayList<String>>(appTradeDealRepository.retrieveCollectibleTypeNames(),
				HttpStatus.OK);
	}

	@GetMapping("/api/trade-deal/open")
	public ResponseEntity<ApiOpenTradeDealList> retrieveOpenTradeDealList(
			@RequestParam(name = "type", required = true) int type) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity<ApiOpenTradeDealList>(createApiOpenTradeDealList(email, type), HttpStatus.OK);
	}

	private ArrayList<String> createResponse(String message) {
		ArrayList<String> response = new ArrayList<String>();
		response.add(message);
		return response;
	}

	private ApiClosedTradeDealList createApiCollectorClosedDealTradeList(String email, int type) {
		ArrayList<ClosedTradeDeal> closedTradeDeals = appTradeDealRepository.retrieveAllClosedTradeDealsByEmail(email,
				type);
		ArrayList<String> posters = retrievePosterUserNamesFromClosedTradeDeals(closedTradeDeals);
		ArrayList<String> accepters = retrieveAccepterUserNamesFromClosedTradeDeals(closedTradeDeals);
		ArrayList<ArrayList<ClosedTradeItem>> posterTradeItems = appTradeDealService
				.retrievePosterTradeItemsForCollectorClosedTrades(closedTradeDeals);
		ArrayList<ArrayList<ClosedTradeItem>> accepterTradeItems = appTradeDealService
				.retrieveAccepterTradeItemsForCollectorClosedTrades(closedTradeDeals);
		ArrayList<String> userTradeItemAttrsOne = appTradeDealRepository.retrieveUserTradeItemAttributeOne(type);
		ArrayList<String> userTradeItemAttrsTwo = appTradeDealRepository.retrieveUserTradeItemAttributeTwo(type);
		ArrayList<String> userTradeItemAttrsThree = appTradeDealRepository.retrieveUserTradeItemAttributeThree(type);
		ArrayList<String> userTradeItemAttrs = appTradeDealRepository.retrieveUserTradeItemAttributes(type);
		String collectibleType = appTradeDealRepository.retrieveCollectibleTypeNames().get(type - 1);
		return new ApiClosedTradeDealList(posters, accepters, closedTradeDeals, posterTradeItems, accepterTradeItems,
				userTradeItemAttrsOne, userTradeItemAttrsTwo, userTradeItemAttrsThree, userTradeItemAttrs,
				collectibleType);
	}

	private ApiOpenTradeDealList createApiCollectorOpenTradeDealList(String email, int type) {
		ArrayList<OpenTradeDeal> openTradeDeals = appTradeDealRepository.retrieveAllOpenTradeDealsByPosterEmail(email,
				type);
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

	private ApiOpenTradeDealList createApiCollectorPendingTradeDealList(String email, int type) {
		ArrayList<OpenTradeDeal> pendingTradeDeals = appTradeDealRepository.retrieveAllPendingTradeDealsByEmail(email,
				type);
		ArrayList<String> posters = retrievePosterUserNamesFromOpenTradeDeals(pendingTradeDeals);
		ArrayList<String> accepters = retrieveAccepterUserNamesFromOpenTradeDeals(pendingTradeDeals);
		ArrayList<ArrayList<OpenTradeItem>> posterTradeItems = appTradeDealService
				.retrievePosterTradeItemsForCollectorOpenTrades(pendingTradeDeals);
		ArrayList<ArrayList<OpenTradeItem>> accepterTradeItems = appTradeDealService
				.retrieveAccepterTradeItemsForCollectorOpenTrades(pendingTradeDeals);
		ArrayList<String> userTradeItemAttrsOne = appTradeDealRepository.retrieveUserTradeItemAttributeOne(type);
		ArrayList<String> userTradeItemAttrsTwo = appTradeDealRepository.retrieveUserTradeItemAttributeTwo(type);
		ArrayList<String> userTradeItemAttrsThree = appTradeDealRepository.retrieveUserTradeItemAttributeThree(type);
		ArrayList<String> userTradeItemAttrs = appTradeDealRepository.retrieveUserTradeItemAttributes(type);
		String collectibleType = appTradeDealRepository.retrieveCollectibleTypeNames().get(type - 1);
		return new ApiOpenTradeDealList(posters, accepters, pendingTradeDeals, posterTradeItems, accepterTradeItems,
				userTradeItemAttrsOne, userTradeItemAttrsTwo, userTradeItemAttrsThree, userTradeItemAttrs,
				collectibleType);
	}

	private ApiOpenTradeDealList createApiOpenTradeDealList(String email, int type) {
		ArrayList<OpenTradeDeal> openTradeDeals = appTradeDealRepository.retrieveOpenTradeDealsNotFromCollector(email,
				type);
		ArrayList<String> posters = retrievePosterUserNamesFromOpenTradeDeals(openTradeDeals);
		// ArrayList<String> accepters =
		// retrieveAccepterUserNamesFromOpenTradeDeals(openTradeDeals);
		ArrayList<ArrayList<OpenTradeItem>> posterTradeItems = appTradeDealService
				.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDeals);
		ArrayList<ArrayList<OpenTradeItem>> accepterTradeItems = appTradeDealService
				.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDeals);
		ArrayList<String> userTradeItemAttrsOne = appTradeDealRepository.retrieveUserTradeItemAttributeOne(type);
		ArrayList<String> userTradeItemAttrsTwo = appTradeDealRepository.retrieveUserTradeItemAttributeTwo(type);
		ArrayList<String> userTradeItemAttrsThree = appTradeDealRepository.retrieveUserTradeItemAttributeThree(type);
		ArrayList<String> userTradeItemAttrs = appTradeDealRepository.retrieveUserTradeItemAttributes(type);
		String collectibleType = appTradeDealRepository.retrieveCollectibleTypeNames().get(type - 1);
		return new ApiOpenTradeDealList(posters, null, openTradeDeals, posterTradeItems, accepterTradeItems,
				userTradeItemAttrsOne, userTradeItemAttrsTwo, userTradeItemAttrsThree, userTradeItemAttrs,
				collectibleType);
	}

	private ArrayList<String> retrievePosterUserNamesFromOpenTradeDeals(ArrayList<OpenTradeDeal> openTradeDeals) {
		ArrayList<String> posterUserNames = new ArrayList<String>();
		ArrayList<UserInfo> posters = appTradeDealService.retrievePosterUserInfoForOpenTrades(openTradeDeals);
		for (int i = 0; i < posters.size(); i++) {
			posterUserNames.add(posters.get(i).getUserName());
		}
		return posterUserNames;
	}

	private ArrayList<String> retrieveAccepterUserNamesFromOpenTradeDeals(ArrayList<OpenTradeDeal> openTradeDeals) {
		ArrayList<String> accepterUserNames = new ArrayList<String>();
		ArrayList<UserInfo> accepters = appTradeDealService.retrieveAccepterUserInfoForOpenTrades(openTradeDeals);
		for (int i = 0; i < accepters.size(); i++) {
			accepterUserNames.add(accepters.get(i).getUserName());
		}
		return accepterUserNames;
	}

	private ArrayList<String> retrievePosterUserNamesFromClosedTradeDeals(ArrayList<ClosedTradeDeal> closedTradeDeals) {
		ArrayList<String> posterUserNames = new ArrayList<String>();
		ArrayList<UserInfo> posters = appTradeDealService.retrievePosterUserInfoForClosedTrades(closedTradeDeals);
		for (int i = 0; i < posters.size(); i++) {
			posterUserNames.add(posters.get(i).getUserName());
		}
		return posterUserNames;
	}

	private ArrayList<String> retrieveAccepterUserNamesFromClosedTradeDeals(
			ArrayList<ClosedTradeDeal> closedTradeDeals) {
		ArrayList<String> accepterUserNames = new ArrayList<String>();
		ArrayList<UserInfo> accepters = appTradeDealService.retrieveAccepterUserInfoForClosedTrades(closedTradeDeals);
		for (int i = 0; i < accepters.size(); i++) {
			accepterUserNames.add(accepters.get(i).getUserName());
		}
		return accepterUserNames;
	}
}
