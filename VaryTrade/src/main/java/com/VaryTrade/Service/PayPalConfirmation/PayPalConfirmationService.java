package com.VaryTrade.Service.PayPalConfirmation;

import org.springframework.stereotype.Service;

@Service
public interface PayPalConfirmationService {
	public boolean confirmResaleDealPayment(String id, String paymentId, String captureId);
	public boolean confirmTradeDealPayment(String id, String paymentId, String captureId);
}
