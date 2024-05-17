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
import com.VaryTrade.Dao.TradeDeal.AppTradeDealRepository;
import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.ClosedTradeDeal;
import com.VaryTrade.Model.OpenTradeDeal;
import com.VaryTrade.Model.OpenTradeItem;
import com.VaryTrade.Model.WebTradeDealOne;
import com.VaryTrade.Model.WebTradeDealThree;
import com.VaryTrade.Model.WebTradeDealTwo;
import com.VaryTrade.Model.WebTradeItem;
import com.VaryTrade.Service.PayPalConfirmation.AppPayPalConfirmationService;
import com.VaryTrade.Service.TradeDeal.AppTradeDealService;
import com.VaryTrade.Validator.WebTradeItemValidator;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class TradeController {
	@Autowired
	private AppPayPalConfirmationService appPayPalConfirmationService;
	@Autowired
	private AppMiscRepository appMiscRepository;
	@Autowired
	private AppTradeDealService appTradeDealService;
	@Autowired
	private WebTradeItemValidator webTradeItemValidator;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppTradeDealRepository appTradeDealRepository;
	
	@GetMapping("/your-trade-deals")
	public String displayCollectorTradeDeals(Principal principal, Model model) {
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
		return "CollectorTradeInfo";
	}
	
	@GetMapping("trade-deal/create/type-3")
	public String displayCreateTradeDealThreeForm(WebTradeItem webTradeItem, Model model, HttpSession session) {
		WebTradeDealThree webTradeDealThree = (WebTradeDealThree) session.getAttribute("WEBTRADEDEALTHREE");
		if(webTradeDealThree != null) {
			model.addAttribute("posterTradeItems", webTradeDealThree.getPosterTradeItems());
			model.addAttribute("accepterTradeItems", webTradeDealThree.getAccepterTradeItems());
		}
		else {
			model.addAttribute("posterTradeItems", null);
			model.addAttribute("accepterTradeItems", null);
		}
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(2));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("tradeItems", appTradeDealRepository.retrieveTradeItems(3));
		model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(3));
		model.addAttribute("tradeItemTypes", appTradeDealRepository.retrieveTradeItemTypes());
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		return "CreateTrade2";
	}
	
	@GetMapping("trade-deal/create/type-2")
	public String displayCreateTradeDealTwoForm(WebTradeItem webTradeItem, Model model, HttpSession session) {
		WebTradeDealTwo webTradeDealTwo = (WebTradeDealTwo) session.getAttribute("WEBTRADEDEALTWO");
		if(webTradeDealTwo != null) {
			model.addAttribute("posterTradeItems", webTradeDealTwo.getPosterTradeItems());
			model.addAttribute("accepterTradeItems", webTradeDealTwo.getAccepterTradeItems());
		}
		else {
			model.addAttribute("posterTradeItems", null);
			model.addAttribute("accepterTradeItems", null);
		}
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(1));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("tradeItems", appTradeDealRepository.retrieveTradeItems(2));
		model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(2));
		model.addAttribute("tradeItemTypes", appTradeDealRepository.retrieveTradeItemTypes());
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		return "CreateTrade2";
	}
	
	@GetMapping("/trade-deal/create/type-1")
	public String displayCreateTradeDealOneForm(WebTradeItem webTradeItem, Model model, HttpSession session) {
		WebTradeDealOne webTradeDealOne = (WebTradeDealOne) session.getAttribute("WEBTRADEDEALONE");
		if(webTradeDealOne != null) {
			model.addAttribute("posterTradeItems", webTradeDealOne.getPosterTradeItems());
			model.addAttribute("accepterTradeItems", webTradeDealOne.getAccepterTradeItems());
		}
		else {
			model.addAttribute("posterTradeItems", null);
			model.addAttribute("accepterTradeItems", null);
		}
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(0));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("tradeItems", appTradeDealRepository.retrieveTradeItems(1));
		model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(1));
		model.addAttribute("tradeItemTypes", appTradeDealRepository.retrieveTradeItemTypes());
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		return "CreateTrade1";
	}
	
	@PostMapping("/trade-deal/create/type-3")
	public String processAddTradeItemThreeForm(@Valid WebTradeItem webTradeItem, BindingResult result, Model model, HttpSession session) {
		webTradeItemValidator.validate(webTradeItem, result);
		if(result.hasErrors()) {
			WebTradeDealThree webTradeDealThree = (WebTradeDealThree) session.getAttribute("WEBTRADEDEALTHREE");
			if(webTradeDealThree != null) {
				model.addAttribute("posterTradeItems", webTradeDealThree.getPosterTradeItems());
				model.addAttribute("accepterTradeItems", webTradeDealThree.getAccepterTradeItems());
			}
			else {
				model.addAttribute("posterTradeItems", null);
				model.addAttribute("accepterTradeItems", null);
			}
			model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(3));
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("tradeItems", appTradeDealRepository.retrieveTradeItems(3));
			model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(3));
			model.addAttribute("tradeItemTypes", appTradeDealRepository.retrieveTradeItemTypes());
			model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
			model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
			model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
			model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
			return "CreateTrade3";
		}
		else {
			WebTradeDealThree webTradeDealThree = (WebTradeDealThree) session.getAttribute("WEBTRADEDEALTHREE");
			webTradeDealThree = (WebTradeDealThree) appTradeDealService.addTradeItem(webTradeDealThree, webTradeItem, 3);
			session.setAttribute("WEBTRADEDEALTHREE", webTradeDealThree);
			return "redirect:/trade-deal/create/type-2";
		}
	}
	
	@PostMapping("/trade-deal/create/type-2")
	public String processAddTradeItemTwoForm(@Valid WebTradeItem webTradeItem, BindingResult result, Model model, HttpSession session) {
		webTradeItemValidator.validate(webTradeItem, result);
		if(result.hasErrors()) {
			WebTradeDealTwo webTradeDealTwo = (WebTradeDealTwo) session.getAttribute("WEBTRADEDEALTWO");
			if(webTradeDealTwo != null) {
				model.addAttribute("posterTradeItems", webTradeDealTwo.getPosterTradeItems());
				model.addAttribute("accepterTradeItems", webTradeDealTwo.getAccepterTradeItems());
			}
			else {
				model.addAttribute("posterTradeItems", null);
				model.addAttribute("accepterTradeItems", null);
			}
			model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(1));
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("tradeItems", appTradeDealRepository.retrieveTradeItems(2));
			model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(2));
			model.addAttribute("tradeItemTypes", appTradeDealRepository.retrieveTradeItemTypes());
			model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
			model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
			model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
			model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
			return "CreateTrade2";
		}
		else {
			WebTradeDealTwo webTradeDealTwo = (WebTradeDealTwo) session.getAttribute("WEBTRADEDEALTWO");
			webTradeDealTwo = (WebTradeDealTwo) appTradeDealService.addTradeItem(webTradeDealTwo, webTradeItem, 2);
			session.setAttribute("WEBTRADEDEALTWO", webTradeDealTwo);
			return "redirect:/trade-deal/create/type-2";
		}
	}
	
	@PostMapping("/trade-deal/create/type-1")
	public String processAddTradeItemOneForm(@Valid WebTradeItem webTradeItem, BindingResult result, Model model, HttpSession session) {
		webTradeItemValidator.validate(webTradeItem, result);
		if(result.hasErrors()) {
			WebTradeDealOne webTradeDealOne = (WebTradeDealOne) session.getAttribute("WEBTRADEDEALONE");
			if(webTradeDealOne != null) {
				model.addAttribute("posterTradeItems", webTradeDealOne.getPosterTradeItems());
				model.addAttribute("accepterTradeItems", webTradeDealOne.getAccepterTradeItems());
			}
			else {
				model.addAttribute("posterTradeItems", null);
				model.addAttribute("accepterTradeItems", null);
			}
			model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(0));
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("tradeItems", appTradeDealRepository.retrieveTradeItems(1));
			model.addAttribute("conditions", appTradeDealRepository.retrieveConditions(1));
			model.addAttribute("tradeItemTypes", appTradeDealRepository.retrieveTradeItemTypes());
			model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
			model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
			model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
			model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
			return "CreateTrade1";
		}
		else {
			WebTradeDealOne webTradeDealOne = (WebTradeDealOne) session.getAttribute("WEBTRADEDEALONE");
			webTradeDealOne = (WebTradeDealOne) appTradeDealService.addTradeItem(webTradeDealOne, webTradeItem, 1);
			session.setAttribute("WEBTRADEDEALONE", webTradeDealOne);
			return "redirect:/trade-deal/create/type-1";
		}
	}
	
	@PostMapping("/trade-deal/create/type-3/post")
	public String processPostTradeDealThreeForm(Principal principal, WebTradeItem webTradeItem, Model model, HttpSession session,
			RedirectAttributes redirectAttrs) {
		WebTradeDealThree webTradeDealThree = (WebTradeDealThree) session.getAttribute("WEBTRADEDEALTHREE");		
		if(webTradeDealThree == null || webTradeDealThree.getPosterTradeItems().isEmpty() || webTradeDealThree.getAccepterTradeItems().isEmpty()) {
			redirectAttrs.addAttribute("error", "posting");
			return "redirect:/trade-deal/create/type-3";
		}
		else {		
			OpenTradeDeal openTradeDeal = appTradeDealRepository.insertOpenTradeDeal(webTradeDealThree, principal.getName(), 3);
			session.setAttribute("WEBTRADEDEALTHREE", null);
			return "redirect:/trade-deal/create/post/confirmation?id=" + openTradeDeal.getId();
		}
	}
	
	@PostMapping("/trade-deal/create/type-2/post")
	public String processPostTradeDealTwoForm(Principal principal, WebTradeItem webTradeItem, Model model, HttpSession session,
			RedirectAttributes redirectAttrs) {
		WebTradeDealTwo webTradeDealTwo = (WebTradeDealTwo) session.getAttribute("WEBTRADEDEALTWO");		
		if(webTradeDealTwo == null || webTradeDealTwo.getPosterTradeItems().isEmpty() || webTradeDealTwo.getAccepterTradeItems().isEmpty()) {
			redirectAttrs.addAttribute("error", "posting");
			return "redirect:/trade-deal/create/type-2";
		}
		else {		
			OpenTradeDeal openTradeDeal = appTradeDealRepository.insertOpenTradeDeal(webTradeDealTwo, principal.getName(), 2);
			session.setAttribute("WEBTRADEDEALTWO", null);
			return "redirect:/trade-deal/create/post/confirmation?id=" + openTradeDeal.getId();
		}
	}
	
	@PostMapping("/trade-deal/create/type-1/post")
	public String processPostTradeDealOneForm(Principal principal, WebTradeItem webTradeItem, Model model, HttpSession session,
			RedirectAttributes redirectAttrs) {
		WebTradeDealOne webTradeDealOne = (WebTradeDealOne) session.getAttribute("WEBTRADEDEALONE");		
		if (webTradeDealOne == null || webTradeDealOne.getPosterTradeItems().isEmpty()
				|| webTradeDealOne.getAccepterTradeItems().isEmpty()) {
			redirectAttrs.addAttribute("error", "posting");
			return "redirect:/trade-deal/create/type-1";
		}
		else {		
			OpenTradeDeal openTradeDeal = appTradeDealRepository.insertOpenTradeDeal(webTradeDealOne, principal.getName(), 1);
			session.setAttribute("WEBTRADEDEALONE", null);
			return "redirect:/trade-deal/create/post/confirmation?id=" + openTradeDeal.getId();
		}
	}
	
	@GetMapping("/trade-deal/create/post/confirmation")
	public String displayCreateTradeDealConfirmation(@RequestParam(name="id", required=true) String id, Model model, Principal principal) {
		OpenTradeDeal openTradeDeal = appTradeDealRepository.retrieveOpenTradeDeal(Integer.valueOf(id));
		if(appTradeDealService.doesOpenTradeDealBelongToCollector(openTradeDeal, principal.getName()) && !openTradeDeal.isPendingCompletion()) {
			model.addAttribute("company", appMiscRepository.retrieveCompany());
			model.addAttribute("openTradeDeal", appTradeDealRepository.retrieveOpenTradeDeal(Integer.valueOf(id)));
			model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(openTradeDeal.getItemType()));
			model.addAttribute("posterTradeItems", appTradeDealRepository.retrieveOpenPosterTradeItems(Integer.valueOf(id)));
			model.addAttribute("accepterTradeItems", appTradeDealRepository.retrieveOpenAccepterTradeItems(Integer.valueOf(id)));
			model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(openTradeDeal.getItemType()));
			model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(openTradeDeal.getItemType()));
			model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(openTradeDeal.getItemType()));
			return "CreateTradeConfirmation";
		}
		else {
			return "NotAllowed";
		}	
	}
	
	@PostMapping("/trade-deal/create/type-3/receive/remove")
	public String processDeleteTradeDealThreeReceiveItemForm(@RequestParam(name="id", required=true) String id, Model model, HttpSession session) {
		WebTradeDealThree webTradeDealThree = (WebTradeDealThree) session.getAttribute("WEBTRADEDEALTHREE");
		ArrayList<WebTradeItem> accepterTradeItems = webTradeDealThree.getAccepterTradeItems();
		int index = Integer.valueOf(id);
		accepterTradeItems = appTradeDealService.removeTradeItem(accepterTradeItems, index);
		accepterTradeItems = appTradeDealService.adjustTradeItemIds(accepterTradeItems);
		webTradeDealThree.setAccepterTradeItems(accepterTradeItems);
		session.setAttribute("WEBTRADEDEALTHREE", webTradeDealThree);
		return "redirect:/trade-deal/create/type-1";
	}
	
	@PostMapping("/trade-deal/create/type-3/trade/remove")
	public String processDeleteTradeDealThreeTradeItemForm(@RequestParam(name="id", required=true) String id, Model model, HttpSession session) {
		WebTradeDealThree webTradeDealThree = (WebTradeDealThree) session.getAttribute("WEBTRADEDEALTHREE");
		ArrayList<WebTradeItem> posterTradeItems = webTradeDealThree.getPosterTradeItems();
		int index = Integer.valueOf(id);
		posterTradeItems = appTradeDealService.removeTradeItem(posterTradeItems, index);
		posterTradeItems = appTradeDealService.adjustTradeItemIds(posterTradeItems);
		webTradeDealThree.setPosterTradeItems(posterTradeItems);
		session.setAttribute("WEBTRADEDEALTHREE", webTradeDealThree);
		return "redirect:/trade-deal/create/type-1";
	}
	
	@PostMapping("/trade-deal/create/type-2/receive/remove")
	public String processDeleteTradeDealTwoReceiveItemForm(@RequestParam(name="id", required=true) String id, Model model, HttpSession session) {
		WebTradeDealTwo webTradeDealTwo = (WebTradeDealTwo) session.getAttribute("WEBTRADEDEALTWO");
		ArrayList<WebTradeItem> accepterTradeItems = webTradeDealTwo.getAccepterTradeItems();
		int index = Integer.valueOf(id);
		accepterTradeItems = appTradeDealService.removeTradeItem(accepterTradeItems, index);
		accepterTradeItems = appTradeDealService.adjustTradeItemIds(accepterTradeItems);
		webTradeDealTwo.setAccepterTradeItems(accepterTradeItems);
		session.setAttribute("WEBTRADEDEALTWO", webTradeDealTwo);
		return "redirect:/trade-deal/create/type-2";
	}
	
	@PostMapping("/trade-deal/create/type-2/trade/remove")
	public String processDeleteTradeDealTwoTradeItemForm(@RequestParam(name="id", required=true) String id, Model model, HttpSession session) {
		WebTradeDealTwo webTradeDealTwo = (WebTradeDealTwo) session.getAttribute("WEBTRADEDEALTWO");
		ArrayList<WebTradeItem> posterTradeItems = webTradeDealTwo.getPosterTradeItems();
		int index = Integer.valueOf(id);
		posterTradeItems = appTradeDealService.removeTradeItem(posterTradeItems, index);
		posterTradeItems = appTradeDealService.adjustTradeItemIds(posterTradeItems);
		webTradeDealTwo.setPosterTradeItems(posterTradeItems);
		session.setAttribute("WEBTRADEDEALTWO", webTradeDealTwo);
		return "redirect:/trade-deal/create/type-2";
	}
	
	@PostMapping("/trade-deal/create/type-1/receive/remove")
	public String processDeleteTradeDealOneReceiveItemForm(@RequestParam(name="id", required=true) String id, Model model, HttpSession session) {
		WebTradeDealOne webTradeDealOne = (WebTradeDealOne) session.getAttribute("WEBTRADEDEALONE");
		ArrayList<WebTradeItem> accepterTradeItems = webTradeDealOne.getAccepterTradeItems();
		int index = Integer.valueOf(id);
		accepterTradeItems = appTradeDealService.removeTradeItem(accepterTradeItems, index);
		accepterTradeItems = appTradeDealService.adjustTradeItemIds(accepterTradeItems);
		webTradeDealOne.setAccepterTradeItems(accepterTradeItems);
		session.setAttribute("WEBTRADEDEALONE", webTradeDealOne);
		return "redirect:/trade-deal/create/type-1";
	}
	
	@PostMapping("/trade-deal/create/type-1/trade/remove")
	public String processDeleteTradeDealOneTradeItemForm(@RequestParam(name="id", required=true) String id, Model model, HttpSession session) {
		WebTradeDealOne webTradeDealOne = (WebTradeDealOne) session.getAttribute("WEBTRADEDEALONE");
		ArrayList<WebTradeItem> posterTradeItems = webTradeDealOne.getPosterTradeItems();
		int index = Integer.valueOf(id);
		posterTradeItems = appTradeDealService.removeTradeItem(posterTradeItems, index);
		posterTradeItems = appTradeDealService.adjustTradeItemIds(posterTradeItems);
		webTradeDealOne.setPosterTradeItems(posterTradeItems);
		session.setAttribute("WEBTRADEDEALONE", webTradeDealOne);
		return "redirect:/trade-deal/create/type-1";
	}
	
	@GetMapping("/your-trade-deals/type-1/open")
	public String displayCollectorOpenTradeDealsOne(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDeal  = appTradeDealRepository.retrieveAllOpenTradeDealsByPosterEmail(principal.getName(), 1);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(0));
		model.addAttribute("openTradeDeals",openTradeDeal);
		model.addAttribute("posterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("accepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		return "CollectorOpenTrade";
	}
	
	@GetMapping("/your-trade-deals/type-2/open")
	public String displayCollectorOpenTradeDealsTwo(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDeal  = appTradeDealRepository.retrieveAllOpenTradeDealsByPosterEmail(principal.getName(), 2);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(1));
		model.addAttribute("openTradeDeals",openTradeDeal);
		model.addAttribute("posterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("accepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		return "CollectorOpenTrade";
	}
	
	@GetMapping("/your-trade-deals/type-3/open")
	public String displayCollectorOpenTradeDealsThree(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDeal  = appTradeDealRepository.retrieveAllOpenTradeDealsByPosterEmail(principal.getName(), 3);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(2));
		model.addAttribute("openTradeDeals",openTradeDeal);
		model.addAttribute("posterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("accepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		return "CollectorOpenTrade";
	}
	
	@GetMapping("/your-trade-deals/open/details")
	public String displayCollectorOpenTradeDealDetails(@RequestParam(name="id", required=true) String id, Model model, Principal principal) {
		OpenTradeDeal openTradeDeal = appTradeDealRepository.retrieveOpenTradeDeal(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("openTradeDeal", openTradeDeal);
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(openTradeDeal.getItemType()));
		model.addAttribute("posterTradeItems", appTradeDealRepository.retrieveOpenPosterTradeItems(Integer.valueOf(id)));
		model.addAttribute("accepterTradeItems", appTradeDealRepository.retrieveOpenAccepterTradeItems(Integer.valueOf(id)));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(openTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(openTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(openTradeDeal.getItemType()));
		return "CollectorOpenTradeDetails";
	}
	
	@GetMapping("/your-trade-deals/pending/details")
	public String displayCollectorPendingTradeDealDetails(@RequestParam(name="id", required=true) String id, Model model, Principal principal) {
		OpenTradeDeal pendingTradeDeal = appTradeDealRepository.retrieveOpenTradeDeal(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("pendingTradeDeal", pendingTradeDeal);
		model.addAttribute("posterUserInfo", appUserRepository.retrieveUserInfoByEmail(pendingTradeDeal.getPosterEmail()));
		model.addAttribute("accepterUserInfo", appUserRepository.retrieveUserInfoByEmail(pendingTradeDeal.getAccepterEmail()));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(pendingTradeDeal.getItemType()));
		model.addAttribute("pendingPosterTradeItems", appTradeDealRepository.retrieveOpenPosterTradeItems(Integer.valueOf(id)));
		model.addAttribute("pendingAccepterTradeItems", appTradeDealRepository.retrieveOpenAccepterTradeItems(Integer.valueOf(id)));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(pendingTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(pendingTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(pendingTradeDeal.getItemType()));
		return "CollectorPendingTradeDetails";
	}
	
	@GetMapping("/admin/trade-deal/pending/details")
	public String displayAdminPendingTradeDealDetails(@RequestParam(name="id", required=true) String id, Model model) {
		OpenTradeDeal pendingTradeDeal = appTradeDealRepository.retrieveOpenTradeDeal(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("pendingTradeDeal", pendingTradeDeal);
		model.addAttribute("posterUserInfo", appUserRepository.retrieveUserInfoByEmail(pendingTradeDeal.getPosterEmail()));
		model.addAttribute("accepterUserInfo", appUserRepository.retrieveUserInfoByEmail(pendingTradeDeal.getAccepterEmail()));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(pendingTradeDeal.getItemType()));
		model.addAttribute("pendingPosterTradeItems", appTradeDealRepository.retrieveOpenPosterTradeItems(Integer.valueOf(id)));
		model.addAttribute("pendingAccepterTradeItems", appTradeDealRepository.retrieveOpenAccepterTradeItems(Integer.valueOf(id)));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(pendingTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(pendingTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(pendingTradeDeal.getItemType()));
		return "AdminPendingTradeDetails";
	}
	
	@PostMapping("/admin/trade-deal/pass/details")
	public String processAdminPendingTradeDealPassed(@RequestParam(name="id", required=true) String id, Model model) {
		ClosedTradeDeal closedTradeDeal = appTradeDealService.mapPendingTradeDealToPassedClosedTradeDeal(appTradeDealRepository.retrieveOpenTradeDeal(Integer.valueOf(id)));
		ArrayList<OpenTradeItem> pendingPosterTradeItems = appTradeDealRepository.retrieveOpenPosterTradeItems(Integer.valueOf(id));
		ArrayList<OpenTradeItem> pendingAccepterTradeItems = appTradeDealRepository.retrieveOpenAccepterTradeItems(Integer.valueOf(id));
		appTradeDealRepository.insertClosedTrade(Integer.valueOf(id), closedTradeDeal, pendingPosterTradeItems, pendingAccepterTradeItems);
		return "redirect:/home";
	}
	
	@PostMapping("/admin/trade-deal/fail/details")
	public String processAdminPendingTradeDealFailed(@RequestParam(name="id", required=true) String id, Model model) {
		ClosedTradeDeal closedTradeDeal = appTradeDealService.mapPendingTradeDealToFailedClosedTradeDeal(appTradeDealRepository.retrieveOpenTradeDeal(Integer.valueOf(id)));
		ArrayList<OpenTradeItem> pendingPosterTradeItems = appTradeDealRepository.retrieveOpenPosterTradeItems(Integer.valueOf(id));
		ArrayList<OpenTradeItem> pendingAccepterTradeItems = appTradeDealRepository.retrieveOpenAccepterTradeItems(Integer.valueOf(id));
		appTradeDealRepository.insertClosedTrade(Integer.valueOf(id), closedTradeDeal, pendingPosterTradeItems, pendingAccepterTradeItems);
		return "redirect:/home";
	}
	
	@GetMapping("/your-trade-deals/closed/details")
	public String displayCollectorClosedTradeDealDetails(@RequestParam(name="id", required=true) String id, Model model, Principal principal) {
		ClosedTradeDeal closedTradeDeal = appTradeDealRepository.retrieveClosedTradeDealById(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("closedTradeDeal", closedTradeDeal);
		model.addAttribute("posterUserInfo", appUserRepository.retrieveUserInfoByEmail(closedTradeDeal.getPosterEmail()));
		model.addAttribute("accepterUserInfo", appUserRepository.retrieveUserInfoByEmail(closedTradeDeal.getAccepterEmail()));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(closedTradeDeal.getItemType()));
		model.addAttribute("closedPosterTradeItems", appTradeDealRepository.retrieveClosedPosterTradeItems(Integer.valueOf(id)));
		model.addAttribute("closedAccepterTradeItems", appTradeDealRepository.retrieveClosedAccepterTradeItems(Integer.valueOf(id)));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(closedTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(closedTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(closedTradeDeal.getItemType()));
		return "CollectorClosedTradeDetails";
	}
	
	@GetMapping("/your-trade-deals/type-1/pending")
	public String displayCollectorPendingTradeDealsOne(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> pendingTradeDeal  = appTradeDealRepository.retrieveAllPendingTradeDealsByEmail(principal.getName(), 1);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(0));
		model.addAttribute("pendingTradeDeal",pendingTradeDeal);
		model.addAttribute("pendingPosterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("pendingAccepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		return "CollectorPendingTrade";
	}
	
	@GetMapping("/your-trade-deals/type-2/pending")
	public String displayCollectorPendingTradeDealsTwo(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> pendingTradeDeal  = appTradeDealRepository.retrieveAllPendingTradeDealsByEmail(principal.getName(), 2);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(1));
		model.addAttribute("pendingTradeDeal",pendingTradeDeal);
		model.addAttribute("pendingPosterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("pendingAccepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		return "CollectorPendingTrade";
	}
	
	@GetMapping("/your-trade-deals/type-3/pending")
	public String displayCollectorPendingTradeDealsThree(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> pendingTradeDeal  = appTradeDealRepository.retrieveAllPendingTradeDealsByEmail(principal.getName(), 3);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(2));
		model.addAttribute("pendingTradeDeal",pendingTradeDeal);
		model.addAttribute("pendingPosterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("pendingAccepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(pendingTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		return "CollectorPendingTrade";
	}
	
	@GetMapping("/your-trade-deals/type-1/closed")
	public String displayCollectorClosedTradeDealsOne(Model model, Principal principal) {
		ArrayList<ClosedTradeDeal> closedTradeDeal = appTradeDealRepository.retrieveAllClosedTradeDealsByEmail(principal.getName(), 1);
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(0));
		model.addAttribute("closedTradeDeals", closedTradeDeal);
		model.addAttribute("closedPosterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorClosedTrades(closedTradeDeal));
		model.addAttribute("closedAccepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorClosedTrades(closedTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		return "CollectorClosedTrade";
	}
	
	@GetMapping("/your-trade-deals/type-2/closed")
	public String displayCollectorClosedTradeDealsTwo(Model model, Principal principal) {
		ArrayList<ClosedTradeDeal> closedTradeDeal = appTradeDealRepository.retrieveAllClosedTradeDealsByEmail(principal.getName(), 2);
		System.out.println(closedTradeDeal.size());
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(1));
		model.addAttribute("closedTradeDeals", closedTradeDeal);
		model.addAttribute("closedPosterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorClosedTrades(closedTradeDeal));
		model.addAttribute("closedAccepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorClosedTrades(closedTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		return "CollectorClosedTrade";
	}
	
	@GetMapping("/your-trade-deals/type-3/closed")
	public String displayCollectorClosedTradeDealsThree(Model model, Principal principal) {
		ArrayList<ClosedTradeDeal> closedTradeDeal = appTradeDealRepository.retrieveAllClosedTradeDealsByEmail(principal.getName(), 3);
		System.out.println(closedTradeDeal.size());
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("collectibleNameType", appTradeDealRepository.retrieveCollectibleTypeNames().get(2));
		model.addAttribute("closedTradeDeals", closedTradeDeal);
		model.addAttribute("closedPosterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorClosedTrades(closedTradeDeal));
		model.addAttribute("closedAccepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorClosedTrades(closedTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		return "CollectorClosedTrade";
	}
	
	@GetMapping("/trade-deal/open/type-1")
	public String displayOpenTradeDealsOneToAccept(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDeal = appTradeDealRepository.retrieveOpenTradeDealsNotFromCollector(principal.getName(), 1);
		model.addAttribute("posterInfo", appTradeDealService.retrievePosterUserInfoForOpenTrades(openTradeDeal));
		model.addAttribute("accepterInfo", appTradeDealService.retrieveAccepterUserInfoForOpenTrades(openTradeDeal));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("openTradeDeals", openTradeDeal);
		model.addAttribute("posterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("accepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		return "CollectorAllAcceptableOpenTrades";
	}
	
	@GetMapping("/trade-deal/open/type-2")
	public String displayOpenTradeDealsTwoToAccept(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDeal = appTradeDealRepository.retrieveOpenTradeDealsNotFromCollector(principal.getName(), 2);
		model.addAttribute("posterInfo", appTradeDealService.retrievePosterUserInfoForOpenTrades(openTradeDeal));
		model.addAttribute("accepterInfo", appTradeDealService.retrieveAccepterUserInfoForOpenTrades(openTradeDeal));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("openTradeDeals", openTradeDeal);
		model.addAttribute("posterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("accepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		return "CollectorAllAcceptableOpenTrades";
	}
	
	@GetMapping("/trade-deal/open/type-3")
	public String displayOpenTradeDealsThreeToAccept(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDeal = appTradeDealRepository.retrieveOpenTradeDealsNotFromCollector(principal.getName(), 3);
		model.addAttribute("posterInfo", appTradeDealService.retrievePosterUserInfoForOpenTrades(openTradeDeal));
		model.addAttribute("accepterInfo", appTradeDealService.retrieveAccepterUserInfoForOpenTrades(openTradeDeal));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("openTradeDeals", openTradeDeal);
		model.addAttribute("posterTradeItems", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("accepterTradeItems", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDeal));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		return "CollectorAllAcceptableOpenTrades";	
	}
	
	@PostMapping("/your-trade-deals/open/details/delete")
	public String deleteOpenTradeDeal(@RequestParam(name="id", required=true) String id, Model model, Principal principal) {
		appTradeDealRepository.deleteOpenTrade(Integer.valueOf(id));
		return "redirect:/your-trade-deals";
	}
	
	@GetMapping("/trade-deal/open/details")
	public String displayAcceptOpenTradeDeal(@RequestParam(name="id", required=true) String id, Model model, Principal principal) {
		OpenTradeDeal openTradeDeal = appTradeDealRepository.retrieveOpenTradeDeal(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("posterInfo", appUserRepository.retrieveUserInfoByEmail(openTradeDeal.getPosterEmail()));
		model.addAttribute("openTradeDeal", appTradeDealRepository.retrieveOpenTradeDeal(Integer.valueOf(id)));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(openTradeDeal.getItemType()));
		model.addAttribute("posterTradeItems", appTradeDealRepository.retrieveOpenPosterTradeItems(Integer.valueOf(id)));
		model.addAttribute("accepterTradeItems", appTradeDealRepository.retrieveOpenAccepterTradeItems(Integer.valueOf(id)));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(openTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(openTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(openTradeDeal.getItemType()));
		return "CollectorAcceptOpenTrade";
	}
	
	@PostMapping("/trade-deal/open/details/payment/accept")
	public String processOpenTradeDealPaymentAccepted(@RequestParam(name="id", required=true) String id, 
			@RequestParam(name="order_id", required=true) String orderId, @RequestParam(name="capture_id", required=true) String captureId, Model model, Principal principal, RedirectAttributes redirectAttrs) {
		if(appPayPalConfirmationService.confirmTradePayment(id, orderId, captureId)) {
			appTradeDealRepository.updateOpenTradeDealAsAccepted(Integer.valueOf(id), principal.getName(), false);	
			return "redirect:/trade-deal/accept/confirmation?id=" + id;
		}
		else {
			redirectAttrs.addAttribute("error", "credit");
			return "redirect:/trade-deal/open/details?id=" + id; // Only occurs if payment does not successfully process or if verification fails
		} 
	}
	
	@PostMapping("/trade-deal/open/details/credits/accept")
	public String proccessOpenTradeDealCreditAccepted(@RequestParam(name="id", required=true) String id, Model model, Principal principal,
			RedirectAttributes redirectAttrs) {
		BigDecimal credit = appUserRepository.retrieveUserCredit(principal.getName());
		BigDecimal authenticationFee = BigDecimal.valueOf(15);
		if(credit.compareTo(authenticationFee) > -1) {
			appTradeDealRepository.updateOpenTradeDealAsAccepted(Integer.valueOf(id), principal.getName(), true);
			return "redirect:/trade-deal/accept/confirmation?id=" + id;
		}
		else {
			redirectAttrs.addAttribute("error", "credit");
			return "redirect:/trade-deal/open/details?id=" + id;
		}	
	}
	
	@GetMapping("/trade-deal/accept/confirmation") 
	public String displayPendingTradeDealConfirmation(@RequestParam(name="id", required=true) String id, Model model, Principal principal) {
		OpenTradeDeal openTradeDeal = appTradeDealRepository.retrieveOpenTradeDeal(Integer.valueOf(id));
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		model.addAttribute("pendingTradeDeal", openTradeDeal);
		model.addAttribute("posterUserInfo", appUserRepository.retrieveUserInfoByEmail(openTradeDeal.getPosterEmail()));
		model.addAttribute("accepterUserInfo", appUserRepository.retrieveUserInfoByEmail(openTradeDeal.getAccepterEmail()));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(openTradeDeal.getItemType()));
		model.addAttribute("posterTradeItems", appTradeDealRepository.retrieveOpenPosterTradeItems(Integer.valueOf(id)));
		model.addAttribute("accepterTradeItems", appTradeDealRepository.retrieveOpenAccepterTradeItems(Integer.valueOf(id)));
		model.addAttribute("userTradeItemAttrsOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(openTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(openTradeDeal.getItemType()));
		model.addAttribute("userTradeItemAttrsThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(openTradeDeal.getItemType()));
		return "CollectorAcceptOpenTradeConfirmation";
	}
	
	private Model itemTypeOneAttachedToModel(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDealOne  = appTradeDealRepository.retrieveRecentOpenTradeDealsByPosterEmail(principal.getName(), 1);
		ArrayList<OpenTradeDeal> pendingTradeDealOne = appTradeDealRepository.retrieveRecentPendingTradeDealsByEmail(principal.getName(), 1);
		model.addAttribute("openTradeDealsOne", openTradeDealOne);
		model.addAttribute("openPosterTradeItemsOne", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDealOne));
		model.addAttribute("openAccepterTradeItemsOne", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDealOne));
		model.addAttribute("userTradeItemAttrsOneOne", appTradeDealRepository.retrieveUserTradeItemAttributeOne(1));
		model.addAttribute("userTradeItemAttrsTwoOne", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(1));
		model.addAttribute("userTradeItemAttrsThreeOne", appTradeDealRepository.retrieveUserTradeItemAttributeThree(1));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(1));
		model.addAttribute("pendingTradeDealsOne", pendingTradeDealOne);
		model.addAttribute("pendingAccepterTradeItemsOne", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(pendingTradeDealOne));
		model.addAttribute("pendingPosterTradeItemsOne", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(pendingTradeDealOne));
		return model;
	}
	
	private Model itemTypeTwoAttachedToModel(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDealTwo  = appTradeDealRepository.retrieveRecentOpenTradeDealsByPosterEmail(principal.getName(), 2);
		ArrayList<OpenTradeDeal> pendingTradeDealTwo = appTradeDealRepository.retrieveRecentPendingTradeDealsByEmail(principal.getName(), 2);
		model.addAttribute("openTradeDealsTwo", openTradeDealTwo);
		model.addAttribute("openPosterTradeItemsTwo", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDealTwo));
		model.addAttribute("openAccepterTradeItemsTwo", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDealTwo));
		model.addAttribute("userTradeItemAttrsOneTwo", appTradeDealRepository.retrieveUserTradeItemAttributeOne(2));
		model.addAttribute("userTradeItemAttrsTwoTwo", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(2));
		model.addAttribute("userTradeItemAttrsThreeTwo", appTradeDealRepository.retrieveUserTradeItemAttributeThree(2));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(2));
		model.addAttribute("pendingTradeDealsTwo", pendingTradeDealTwo);
		model.addAttribute("pendingAccepterTradeItemsTwo", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(pendingTradeDealTwo));
		model.addAttribute("pendingPosterTradeItemsTwo", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(pendingTradeDealTwo));
		return model;
	}
	
	private Model itemTypeThreeAttachedToModel(Model model, Principal principal) {
		ArrayList<OpenTradeDeal> openTradeDealThree  = appTradeDealRepository.retrieveRecentOpenTradeDealsByPosterEmail(principal.getName(), 3);
		ArrayList<OpenTradeDeal> pendingTradeDealThree = appTradeDealRepository.retrieveRecentPendingTradeDealsByEmail(principal.getName(), 3);
		model.addAttribute("openTradeDealsThree", openTradeDealThree);
		model.addAttribute("openPosterTradeItemsThree", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(openTradeDealThree));
		model.addAttribute("openAccepterTradeItemsThree", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(openTradeDealThree));
		model.addAttribute("userTradeItemAttrsOneThree", appTradeDealRepository.retrieveUserTradeItemAttributeOne(3));
		model.addAttribute("userTradeItemAttrsTwoThree", appTradeDealRepository.retrieveUserTradeItemAttributeTwo(3));
		model.addAttribute("userTradeItemAttrsThreeThree", appTradeDealRepository.retrieveUserTradeItemAttributeThree(3));
		model.addAttribute("userTradeItemAttr", appTradeDealRepository.retrieveUserTradeItemAttributes(3));
		model.addAttribute("pendingTradeDealsThree", pendingTradeDealThree);
		model.addAttribute("pendingAccepterTradeItemsThree", appTradeDealService.retrieveAccepterTradeItemsForCollectorOpenTrades(pendingTradeDealThree));
		model.addAttribute("pendingPosterTradeItemsThree", appTradeDealService.retrievePosterTradeItemsForCollectorOpenTrades(pendingTradeDealThree));
		return model;
	}
}