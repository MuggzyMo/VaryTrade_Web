package com.VaryTrade.Service.PayPalConfirmation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppPayPalConfirmationService {
	@Autowired
	private PayPalConfirmationService payPalConfirmationService;
	
	public boolean confirmResalePayment(String id, String paymentId, String captureId) {
		return payPalConfirmationService.confirmResaleDealPayment(id, paymentId, captureId);
	}
	
	public boolean confirmTradePayment(String id, String paymentId, String captureId) {
		return payPalConfirmationService.confirmTradeDealPayment(id, paymentId, captureId);
	}

}
