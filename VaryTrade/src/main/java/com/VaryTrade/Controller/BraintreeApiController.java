package com.VaryTrade.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.VaryTrade.Service.Braintree.AppBraintreeService;

@RestController
public class BraintreeApiController {
	@Autowired 
	AppBraintreeService appBraintreeService;
	
	@GetMapping("/api/braintree/token")
	public ResponseEntity<String> retrieveClientToken() {
		try {
			return new ResponseEntity<String>(appBraintreeService.getToken(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
