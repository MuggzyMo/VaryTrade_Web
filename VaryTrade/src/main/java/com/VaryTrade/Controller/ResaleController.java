package com.VaryTrade.Controller;

import java.math.BigDecimal;
import java.security.Principal;

import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
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
import com.VaryTrade.Model.ClosedResaleDeal;
import com.VaryTrade.Model.OpenResaleDeal;
import com.VaryTrade.Model.WebResaleDeal;
import com.VaryTrade.Service.PayPalConfirmation.AppPayPalConfirmationService;
import com.VaryTrade.Service.ResaleDeal.AppResaleDealService;
import com.VaryTrade.Validator.WebResaleDealValidator;

import jakarta.validation.Valid;

@Controller
public class ResaleController {
	@Autowired
	private AppPayPalConfirmationService appPayPalConfirmationService;
	@Autowired
	private AppMiscRepository appMiscRepository;
	@Autowired
	private WebResaleDealValidator webResaleDealValidator;
	@Autowired
	private AppResaleDealService appResaleDealService;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppResaleDealRepository appResaleRepository;
	@Autowired
	private AppTradeDealRepository appTradeDealRepository;
	
	@GetMapping("/resale-deal/create/post/confirmation")
	public String displayCreateResaleDealConfirmation(@RequestParam(name="id", required=true) String id, Model model, Principal principal) {
		OpenResaleDeal openResaleDeal = appResaleRepository.retrieveOpenResaleDealById(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		if(appResaleDealService.doesOpenResaleDealBelongToCollector(openResaleDeal, principal.getName()) && !openResaleDeal.isPendingCompletion()) {
			model.addAttribute("posterInfo", appUserRepository.retrieveUserInfoByEmail(openResaleDeal.getPosterEmail()));
			model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(openResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(openResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(openResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(openResaleDeal.getItemType()));
			model.addAttribute("openResaleDeal", openResaleDeal);
			return "CreateResaleConfirmation";
		}
		return "NotAllowed";
	}
	
	@GetMapping("/your-resale-deals/type-1/closed")
	public String displayCollectorClosedResaleDealsOne(Model model, Principal principal) {
		ArrayList<ClosedResaleDeal> closedResaleDeal = appResaleRepository.retrieveAllClosedResaleDealsByEmail(principal.getName(), 1);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(0));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("closedResaleDeal", closedResaleDeal);
		return "CollectorClosedResale";
	}
	
	@GetMapping("/your-resale-deals/type-2/closed")
	public String displayCollectorClosedResaleDealsTwo(Model model, Principal principal) {
		ArrayList<ClosedResaleDeal> closedResaleDeal = appResaleRepository.retrieveAllClosedResaleDealsByEmail(principal.getName(), 2);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(1));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("closedResaleDeal", closedResaleDeal);
		return "CollectorClosedResale";
	}
	
	@GetMapping("/your-resale-deals/type-3/closed")
	public String displayCollectorClosedResaleDealsThree(Model model, Principal principal) {
		ArrayList<ClosedResaleDeal> closedResaleDeal = appResaleRepository.retrieveAllClosedResaleDealsByEmail(principal.getName(), 3);
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(2));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("closedResaleDeal", closedResaleDeal);
		return "CollectorClosedResale";
	}
	
	@GetMapping("/your-resale-deals/closed/details")
	public String displayCollectorClosedResaleDealDetails(@RequestParam(name="id", required=true) String id, Principal principal, Model model) {
		ClosedResaleDeal closedResaleDeal = appResaleRepository.retrieveClosedResaleDeal(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		if(appResaleDealService.doesClosedResaleDealBelongToCollector(closedResaleDeal, principal.getName())) {
			model.addAttribute("posterInfo", appUserRepository.retrieveUserInfoByEmail(closedResaleDeal.getPosterEmail()));
			model.addAttribute("accepterInfo", appUserRepository.retrieveUserInfoByEmail(closedResaleDeal.getAccepterEmail()));
			model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(closedResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(closedResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(closedResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(closedResaleDeal.getItemType()));
			model.addAttribute("closedResaleDeal", closedResaleDeal);
			return "CollectorClosedResaleDetails";
		}
		else {
			return "NotAllowed";
		}
	}
	
	@PostMapping("/admin/resale-deal/pass/details")
	public String processAdminPendingResaleDealPassed(@RequestParam(name="id", required=true) String id, Model model) {
		ClosedResaleDeal closedResaleDeal = appResaleDealService.mapPendingResaleDealToPassedClosedResaleDeal(appResaleRepository.retrieveOpenResaleDealById(Integer.valueOf(id)));
		appResaleRepository.insertClosedResaleDeal(Integer.valueOf(id), closedResaleDeal);
		return "redirect:/home";
	}
	
	@PostMapping("/admin/resale-deal/fail/details")
	public String processAdminPendingResaleDealFailed(@RequestParam(name="id", required=true) String id, Model model) {
		ClosedResaleDeal closedResaleDeal = appResaleDealService.mapPendingResaleDealToFailedClosedResaleDeal(appResaleRepository.retrieveOpenResaleDealById(Integer.valueOf(id)));
		appResaleRepository.insertClosedResaleDeal(Integer.valueOf(id), closedResaleDeal);
		return "redirect:/home";
	}
	
	@GetMapping("/admin/resale-deal/pending/details")
	public String displayAdminPendingTradeDealDetails(@RequestParam(name="id", required=true) String id, Principal principal, Model model) {
		OpenResaleDeal pendingResaleDeal = appResaleRepository.retrieveOpenResaleDealById(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("pendingResaleDeal", pendingResaleDeal);
		model.addAttribute("posterInfo", appUserRepository.retrieveUserInfoByEmail(pendingResaleDeal.getPosterEmail()));
		model.addAttribute("accepterInfo", appUserRepository.retrieveUserInfoByEmail(pendingResaleDeal.getAccepterEmail()));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(pendingResaleDeal.getItemType()));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(pendingResaleDeal.getItemType()));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(pendingResaleDeal.getItemType()));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(pendingResaleDeal.getItemType()));
		return "AdminPendingResaleDetails";
	}
	
	@GetMapping("/your-resale-deals/pending/details")
	public String displayCollectorPendingResaleDealDetails(@RequestParam(name="id", required=true) String id, Principal principal, Model model) {
		OpenResaleDeal pendingResaleDeal = appResaleRepository.retrieveOpenResaleDealById(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		if(appResaleDealService.doesPendingResaleDealBelongToCollector(pendingResaleDeal, principal.getName()) && pendingResaleDeal.isPendingCompletion()) {
			model.addAttribute("posterInfo", appUserRepository.retrieveUserInfoByEmail(pendingResaleDeal.getPosterEmail()));
			model.addAttribute("accepterInfo", appUserRepository.retrieveUserInfoByEmail(pendingResaleDeal.getAccepterEmail()));
			model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(pendingResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(pendingResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(pendingResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(pendingResaleDeal.getItemType()));
			model.addAttribute("pendingResaleDeal", pendingResaleDeal);
			return "CollectorPendingResaleDetails";
		}
		else {
			return "NotAllowed";
		}
	}
	
	@GetMapping("/your-resale-deals/type-1/pending")
	public String displayCollectorPendingResaleDealsOne(Principal principal, Model model) {
		ArrayList<OpenResaleDeal> pendingResaleDeal = appResaleRepository.retrieveAllPendingResaleDealsByEmail(principal.getName(), 1);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(0));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("pendingResaleDeal", pendingResaleDeal);
		return "CollectorPendingResale";
	}
	
	@GetMapping("/your-resale-deals/type-2/pending")
	public String displayCollectorPendingResaleDealsTwo(Principal principal, Model model) {
		ArrayList<OpenResaleDeal> pendingResaleDeal = appResaleRepository.retrieveAllPendingResaleDealsByEmail(principal.getName(), 2);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(1));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("pendingResaleDeal", pendingResaleDeal);
		return "CollectorPendingResale";
	}
	
	@GetMapping("/your-resale-deals/type-3/pending")
	public String displayCollectorPendingResaleDealsThree(Principal principal, Model model) {
		ArrayList<OpenResaleDeal> pendingResaleDeal = appResaleRepository.retrieveAllPendingResaleDealsByEmail(principal.getName(), 3);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(2));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("pendingResaleDeal", pendingResaleDeal);
		return "CollectorPendingResale";
	}
	
	@GetMapping("/resale-deal/accept/confirmation") 
	public String displayPendingTradeDealConfirmation(@RequestParam(name="id", required=true) String id, Model model, Principal principal) {
		OpenResaleDeal openResaleDeal = appResaleRepository.retrieveOpenResaleDealById(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		if(appResaleDealService.doesPendingResaleDealBelongToCollector(openResaleDeal, principal.getName())) {
			model.addAttribute("pendingResaleDeal", openResaleDeal);
			model.addAttribute("posterInfo", appUserRepository.retrieveUserInfoByEmail(openResaleDeal.getPosterEmail()));
			model.addAttribute("accepterInfo", appUserRepository.retrieveUserInfoByEmail(openResaleDeal.getAccepterEmail()));
			model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(openResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(openResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(openResaleDeal.getItemType()));
			model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(openResaleDeal.getItemType()));
			return "CollectorAcceptOpenResaleConfirmation";
		}
		else {
			return "NotAllowed";
		}
	}
	
	@PostMapping("/resale-deal/open/details/payment/accept")
	public String processOpenResaleDealPaymentAccepted(@RequestParam(name="id", required=true) String id, 
			@RequestParam(name="order_id", required=true) String orderId, Principal principal, @RequestParam(name="capture_id", required=true) String captureId, Model model, RedirectAttributes redirectAttrs) {
		if(appPayPalConfirmationService.confirmResalePayment(id, orderId, captureId)) {
			appResaleRepository.updateOpenResaleDealAsAccepted(Integer.valueOf(id), principal.getName(), null, false);
			return "redirect:/resale-deal/accept/confirmation?id=" + id;
		}
		else {
			redirectAttrs.addAttribute("error", "payment");
			return "redirect:/resale-deal/open/details?id=" + id; // Only occurs if payment does not successfully process or if verification fails
		}
	}
	
	@PostMapping("/resale-deal/open/details/credit/accept")
	public String processOpenResaleDealCreditAccepted(@RequestParam(name="id", required=true) String id, Principal principal, Model model,
			RedirectAttributes redirectAttrs) {
		OpenResaleDeal openResaleDeal = appResaleRepository.retrieveOpenResaleDealById(Integer.valueOf(id));
		BigDecimal credit = appUserRepository.retrieveUserCredit(principal.getName());
		BigDecimal authenticationFee = BigDecimal.valueOf(15);
		BigDecimal price = openResaleDeal.getPrice().add(authenticationFee);
		if(credit.compareTo(price) > -1) {
			appResaleRepository.updateOpenResaleDealAsAccepted(Integer.valueOf(id), principal.getName(), price, true);
			return "redirect:/resale-deal/accept/confirmation?id=" + id;
		}
		else {
			redirectAttrs.addAttribute("error", "credit");
			return "redirect:/resale-deal/open/details?id=" + id;
		}
	}
	
	@GetMapping("/resale-deal/open/details")
	public String displayAcceptOpenResaleDeal(@RequestParam(name="id", required=true) String id, Principal principal, Model model) {
		OpenResaleDeal openResaleDeal = appResaleRepository.retrieveOpenResaleDealById(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("posterInfo", appUserRepository.retrieveUserInfoByEmail(openResaleDeal.getPosterEmail()));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(openResaleDeal.getItemType()));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(openResaleDeal.getItemType()));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(openResaleDeal.getItemType()));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(openResaleDeal.getItemType()));
		model.addAttribute("openResaleDeal", openResaleDeal);
		return "CollectorAcceptOpenResale";
	}
	
	@PostMapping("/your-resale-deals/open/details/delete")
	public String deleteOpenResaleDeal(@RequestParam(name="id", required=true) String id, Principal principal, Model model) {
		appResaleRepository.deleteOpenResale(Integer.valueOf(id));
		return "redirect:/your-resale-deals";
	}
	
	@GetMapping("/your-resale-deals")
	public String displayCollectorResaleDeals(Principal principal, Model model) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		ArrayList<String> collectibleTypeName = appTradeDealRepository.retrieveCollectibleTypeNames();
		model.addAttribute("collectibleTypeName", collectibleTypeName);
		
		model = itemTypeOneAttachedToModel(model, principal);
	
		if(collectibleTypeName.size() == 2) {
			model = itemTypeTwoAttachedToModel(model, principal);
		}
		else if(collectibleTypeName.size() == 3) {
			model = itemTypeTwoAttachedToModel(model, principal);
			model = itemTypeThreeAttachedToModel(model, principal);
		}
			
		return "CollectorResaleInfo";
	}
	
	@GetMapping("/your-resale-deals/type-1/open")
	public String displayCollectorOpenResaleDealsOne(Principal principal, Model model) {
		ArrayList<OpenResaleDeal> openResaleDeal = appResaleRepository.retrieveAllOpenResaleDealsByPosterEmail(principal.getName(), 1);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(0));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("openResaleDeal", openResaleDeal);
		return "CollectorOpenResale";
	}
	
	@GetMapping("/your-resale-deals/type-2/open")
	public String displayCollectorOpenResaleDealsTwo(Principal principal, Model model) {
		ArrayList<OpenResaleDeal> openResaleDeal = appResaleRepository.retrieveAllOpenResaleDealsByPosterEmail(principal.getName(), 2);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(1));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("openResaleDeal", openResaleDeal);
		return "CollectorOpenResale";
	}
	
	@GetMapping("/your-resale-deals/type-3/open")
	public String displayCollectorOpenResaleDealsThree(Principal principal, Model model) {
		ArrayList<OpenResaleDeal> openResaleDeal = appResaleRepository.retrieveAllOpenResaleDealsByPosterEmail(principal.getName(), 3);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(2));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("openResaleDeal", openResaleDeal);
		return "CollectorOpenResale";
	}
	
	@GetMapping("/resale-deal/create/type-3")
	public String displayCreateResaleDealFormThree(WebResaleDeal webResaleDeal, Model model) {
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames());
		model.addAttribute("type", "3");
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("resaleItems", appTradeDealRepository.retrieveTradeItems(3));
		model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(3));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		return "CreateResale";
	}
	
	@GetMapping("resale-deal/create/type-2")
	public String displayCreateResaleDealFormTwo(WebResaleDeal webResaleDeal, Model model) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames());
		model.addAttribute("type", "2");
		model.addAttribute("resaleItems", appTradeDealRepository.retrieveTradeItems(2));
		model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(2));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		return "CreateResale";
	}
	
	@GetMapping("/resale-deal/create/type-1")
	public String displayCreateResaleDealFormOne(WebResaleDeal webResaleDeal, Model model) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames());
		model.addAttribute("type", "1");
		model.addAttribute("resaleItems", appTradeDealRepository.retrieveTradeItems(1));
		model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(1));
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		return "CreateResale";
	}
	
	@PostMapping("/resale-deal/create/type-1")
	public String processCreateResaleDealOneForm(@Valid WebResaleDeal webResaleDeal, BindingResult result, Model model, Principal principal) {
		webResaleDealValidator.validate(webResaleDeal, result);
		if(result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("type", "1");
			model.addAttribute("resaleItems", appTradeDealRepository.retrieveTradeItems(1));
			model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(1));
			model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
			model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
			model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
			model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
			return "CreateResale";
		}
		else {
			OpenResaleDeal openResaleDeal = appResaleDealService.mapWebResaleDealToOpenResaleDeal(webResaleDeal, principal.getName());
			openResaleDeal = appResaleRepository.insertOpenResaleDeal(openResaleDeal, 1);
			return "redirect:/resale-deal/create/post/confirmation?id=" + openResaleDeal.getId();
		}
	}
	
	@PostMapping("/resale-deal/create/type-2")
	public String processCreateResaleDealTwoForm(@Valid WebResaleDeal webResaleDeal, BindingResult result, Model model, Principal principal) {
		webResaleDealValidator.validate(webResaleDeal, result);
		if(result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("type", "2");
			model.addAttribute("resaleItems", appTradeDealRepository.retrieveTradeItems(2));
			model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(2));
			model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
			model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
			model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
			model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
			return "CreateResale";
		}
		else {
			OpenResaleDeal openResaleDeal = appResaleDealService.mapWebResaleDealToOpenResaleDeal(webResaleDeal, principal.getName());
			openResaleDeal = appResaleRepository.insertOpenResaleDeal(openResaleDeal, 2);
			return "redirect:/resale-deal/create/post/confirmation?id=" + openResaleDeal.getId();
		}
	}
	
	@PostMapping("/resale-deal/create/type-3")
	public String processCreateResaleDealThreeForm(@Valid WebResaleDeal webResaleDeal, BindingResult result, Model model, Principal principal) {
		webResaleDealValidator.validate(webResaleDeal, result);
		if(result.hasErrors()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("type", "3");
			model.addAttribute("resaleItems", appTradeDealRepository.retrieveTradeItems(3));
			model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(3));
			model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
			model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
			model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
			model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
			return "CreateResale";
		}
		else {
			OpenResaleDeal openResaleDeal = appResaleDealService.mapWebResaleDealToOpenResaleDeal(webResaleDeal, principal.getName());
			openResaleDeal = appResaleRepository.insertOpenResaleDeal(openResaleDeal, 1);
			return "redirect:/resale-deal/create/post/confirmation?id=" + openResaleDeal.getId();
		}
	}
	
	@GetMapping("/resale-deal/open/type-1")
	public String displayOpenResaleDealsOneToAccept(Model model, Principal principal) {
		ArrayList<OpenResaleDeal> openResaleDeal = appResaleRepository.retrieveOpenResaleDealsNotFromCollector(principal.getName(), 1);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("openResaleDeals", openResaleDeal);
		model.addAttribute("posterInfo", appResaleDealService.retrievePosterUserInfoForOpenResales(openResaleDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		return "CollectorAllAcceptableOpenResales";
	}
	
	@GetMapping("/resale-deal/open/type-2")
	public String displayOpenResaleDealsTwoToAccept(Model model, Principal principal) {
		ArrayList<OpenResaleDeal> openResaleDeal = appResaleRepository.retrieveOpenResaleDealsNotFromCollector(principal.getName(), 2);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("openResaleDeals", openResaleDeal);
		model.addAttribute("posterInfo", appResaleDealService.retrievePosterUserInfoForOpenResales(openResaleDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		return "CollectorAllAcceptableOpenResales";
	}
	
	@GetMapping("/resale-deal/open/type-3")
	public String displayOpenResaleDealsThreeToAccept(Model model, Principal principal) {
		ArrayList<OpenResaleDeal> openResaleDeal = appResaleRepository.retrieveOpenResaleDealsNotFromCollector(principal.getName(), 3);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("openResaleDeals", openResaleDeal);
		model.addAttribute("posterInfo", appResaleDealService.retrievePosterUserInfoForOpenResales(openResaleDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		return "CollectorAllAcceptableOpenResales";
	}
	
	@GetMapping("/your-resale-deals/open/details")
	public String displayOpenResaleDealDetails(@RequestParam(name="id", required=true) String id, Model model, Principal principal) {
		OpenResaleDeal openResaleDeal = appResaleRepository.retrieveOpenResaleDealById(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("openResaleDeal", openResaleDeal);
		model.addAttribute("userResaleItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(openResaleDeal.getItemType()));
		model.addAttribute("userResaleItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(openResaleDeal.getItemType()));
		model.addAttribute("userResaleItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(openResaleDeal.getItemType()));
		model.addAttribute("userResaleItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(openResaleDeal.getItemType()));
		return "CollectorOpenResaleDetails";
	}
	
	private Model itemTypeOneAttachedToModel(Model model, Principal principal) {
		ArrayList<OpenResaleDeal> openResaleDealOne = appResaleRepository.retrieveRecentOpenResaleDealsByPosterEmail(principal.getName(), 1);
		ArrayList<OpenResaleDeal> pendingResaleDealOne = appResaleRepository.retrieveRecentPendingResaleDealsByEmail(principal.getName(), 1);
		model.addAttribute("userResaleItemAttrOne", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		System.out.println(appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		model.addAttribute("userResaleItemAttrsOneOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userResaleItemAttrsTwoOne", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userResaleItemAttrsThreeOne", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("openResaleDealOne", openResaleDealOne);
		model.addAttribute("pendingResaleDealOne", pendingResaleDealOne);
		return model;
	}
	
	private Model itemTypeTwoAttachedToModel(Model model, Principal principal) {
		ArrayList<OpenResaleDeal> openResaleDealTwo = appResaleRepository.retrieveRecentOpenResaleDealsByPosterEmail(principal.getName(),2);
		ArrayList<OpenResaleDeal> pendingResaleDealTwo = appResaleRepository.retrieveRecentPendingResaleDealsByEmail(principal.getName(), 2);
		model.addAttribute("userResaleItemAttrTwo", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		model.addAttribute("userResaleItemAttrsOneTwo", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userResaleItemAttrsTwoTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userResaleItemAttrsThreeTwo", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("openResaleDealTwo", openResaleDealTwo);
		model.addAttribute("pendingResaleDealTwo", pendingResaleDealTwo);
		return model;
	}
	
	private Model itemTypeThreeAttachedToModel(Model model, Principal principal) {
		ArrayList<OpenResaleDeal> openResaleDealThree = appResaleRepository.retrieveRecentOpenResaleDealsByPosterEmail(principal.getName(),3);
		ArrayList<OpenResaleDeal> pendingResaleDealThree = appResaleRepository.retrieveRecentPendingResaleDealsByEmail(principal.getName(), 3);
		model.addAttribute("userResaleItemAttrThree", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		model.addAttribute("userResaleItemAttrsOneThree", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userResaleItemAttrsTwoThree", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userResaleItemAttrsThreeThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("openResaleDealThree", openResaleDealThree);
		model.addAttribute("pendingResaleDealThree", pendingResaleDealThree);
		return model;
	}
}
