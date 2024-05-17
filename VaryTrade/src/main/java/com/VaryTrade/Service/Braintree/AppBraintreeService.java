package com.VaryTrade.Service.Braintree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppBraintreeService {
	@Autowired
	BraintreeService braintreeService;

	public String getToken() {
		return braintreeService.getToken();
	}
}
