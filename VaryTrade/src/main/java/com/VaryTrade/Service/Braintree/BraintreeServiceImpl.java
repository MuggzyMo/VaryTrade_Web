package com.VaryTrade.Service.Braintree;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;

@Service
public class BraintreeServiceImpl implements BraintreeService {
	@Value("${braintree.merchant.id}")
	private String merchantId;

	@Value("${braintree.public.key}")
	private String publicKey;

	@Value("${braintree.private.key}")
	private String privateKey;

	@Override
	public String getToken() {
		BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX, merchantId, publicKey, privateKey);
		return gateway.clientToken().generate().toString();
	}
}
