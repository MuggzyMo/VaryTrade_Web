package com.VaryTrade.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.VaryTrade.Dao.Misc.AppMiscRepository;

@Controller
public class MiscController {
	@Autowired
	private AppMiscRepository appMiscRepository;
	
	@GetMapping("/contact")
	public String displayCompanyContactInfo(Model model) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		return "Contact";
	}
	
	@GetMapping("/request/denied")
	public String displayRequestDenied(Model model) {
		model.addAttribute("company", appMiscRepository.retrieveCompany());
		return "NotAllowed";
	}
}
