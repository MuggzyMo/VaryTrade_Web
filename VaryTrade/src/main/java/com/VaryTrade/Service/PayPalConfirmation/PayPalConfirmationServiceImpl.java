package com.VaryTrade.Service.PayPalConfirmation;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.VaryTrade.Dao.ResaleDeal.AppResaleDealRepository;
import com.VaryTrade.Model.OpenResaleDeal;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class PayPalConfirmationServiceImpl implements PayPalConfirmationService {
	@Autowired
	private AppResaleDealRepository appResaleDealRepository;
	@Value("${paypal.secret}")
	private String secret;
	@Value("${paypal.client.id}")
	private String clientId;

	@Override
	public boolean confirmTradeDealPayment(String id, String paymentId, String captureId) {
		//String clientId = "Aats_uiGNaz44XBspUT1ARSCB0KCZySjSo9QTgESEdIFrNSNPBCdCYwmnE4VvYsHzdRps9WkDx3hoI6t";
		//String secret = "EO6UuRg2SDN9pFsC7c_R5at8rHn_YE9gjoUGHbXPABJMkTw13ggQbYYXMkd7lu_gkvjXiw0NynXqEqSe";
		String credentials = clientId + ":" + secret;
		String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

		try {
			String sanitizedPaymentId = paymentId.replace("\"", "");
			String urlString = UriComponentsBuilder
					.fromUriString("https://api-m.sandbox.paypal.com/v2/checkout/orders/{sanitizedPaymentId}")
					.buildAndExpand(sanitizedPaymentId).toUriString();
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.setRequestProperty("Authorization", "Basic " + base64Credentials);
			httpConn.setRequestProperty("Accept", "application/json");
			httpConn.setRequestProperty("Accept-Language", "en_US");

			InputStream responseStream = httpConn.getResponseCode() / 100 == 2 ? httpConn.getInputStream()
					: httpConn.getErrorStream();
			Scanner s = new Scanner(responseStream).useDelimiter("\\A");
			String response = s.hasNext() ? s.next() : "";

			PayPalPaymentDetails payPalPaymentDetails = parseJson(response);

			if (payPalPaymentDetails.getValue().equals(BigDecimal.valueOf(Double.valueOf(15.00)))
					&& payPalPaymentDetails.getCurrencyCode().equals("USD")) {
				return true;
			} else {
				refundPayment(captureId);
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean confirmResaleDealPayment(String id, String paymentId, String captureId) {
		//String clientId = "Aats_uiGNaz44XBspUT1ARSCB0KCZySjSo9QTgESEdIFrNSNPBCdCYwmnE4VvYsHzdRps9WkDx3hoI6t";
		//String secret = "EO6UuRg2SDN9pFsC7c_R5at8rHn_YE9gjoUGHbXPABJMkTw13ggQbYYXMkd7lu_gkvjXiw0NynXqEqSe";
		String credentials = clientId + ":" + secret;
		String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
		OpenResaleDeal openResaleDeal = appResaleDealRepository.retrievePendingResaleDealById(Integer.valueOf(id));

		try {
			String sanitizedPaymentId = paymentId.replace("\"", "");
			String urlString = UriComponentsBuilder
					.fromUriString("https://api-m.sandbox.paypal.com/v2/checkout/orders/{sanitizedPaymentId}")
					.buildAndExpand(sanitizedPaymentId).toUriString();
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.setRequestProperty("Authorization", "Basic " + base64Credentials);
			httpConn.setRequestProperty("Accept", "application/json");
			httpConn.setRequestProperty("Accept-Language", "en_US");

			InputStream responseStream = httpConn.getResponseCode() / 100 == 2 ? httpConn.getInputStream()
					: httpConn.getErrorStream();
			Scanner s = new Scanner(responseStream).useDelimiter("\\A");
			String response = s.hasNext() ? s.next() : "";

			PayPalPaymentDetails payPalPaymentDetails = parseJson(response);

			if (payPalPaymentDetails.getValue()
					.compareTo(BigDecimal.valueOf(Double.valueOf(15.00)).add(openResaleDeal.getPrice())) == 0
					&& payPalPaymentDetails.getCurrencyCode().equals("USD")) {
				return true;
			} else {
				refundPayment(captureId);
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	/*
	 * This method uses OkHttp for the API call to PayPal's refund endpoint, because I could not get the
	 * HTTP process and packages that PayPal recommended to work for this endpoint. It did work for
	 * confirming the payment details endpoint in the above two methods.
	 */
	private void refundPayment(String captureId) {
		String clientId = "Aats_uiGNaz44XBspUT1ARSCB0KCZySjSo9QTgESEdIFrNSNPBCdCYwmnE4VvYsHzdRps9WkDx3hoI6t";
		String secret = "EO6UuRg2SDN9pFsC7c_R5at8rHn_YE9gjoUGHbXPABJMkTw13ggQbYYXMkd7lu_gkvjXiw0NynXqEqSe";
		String credentials = clientId + ":" + secret;
		String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
		String sanitizedCaptureId = captureId.replace("\"", "");

		String urlString = UriComponentsBuilder
				.fromUriString("https://api-m.sandbox.paypal.com/v2/payments/captures/{sanitizedCaptureId}/refund")
				.buildAndExpand(sanitizedCaptureId).toUriString();

		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "");
		Request request = new Request.Builder().url(urlString).method("POST", body)
				.addHeader("Content-Type", "application/json").addHeader("Authorization", "Basic " + base64Credentials)
				.build();
		try {
			Response response = client.newCall(request).execute();
			System.out.println(response.toString());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private PayPalPaymentDetails parseJson(String response) {
		JSONParser parser = new JSONParser();

		try {
			JSONObject json = (JSONObject) parser.parse(response);
			JSONArray purchaseUnitsArray = (JSONArray) json.get("purchase_units");

			if (purchaseUnitsArray != null && purchaseUnitsArray.size() > 0) {
				JSONObject purchaseUnit = (JSONObject) purchaseUnitsArray.get(0);
				JSONObject amountObject = (JSONObject) purchaseUnit.get("amount");
				String currencyCode = (String) amountObject.get("currency_code");
				String value = (String) amountObject.get("value");
				return new PayPalPaymentDetails(currencyCode, BigDecimal.valueOf(Double.valueOf(value)));
			} else {
				return null;
			}
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	// Private Utility Class for this Implementation
	private class PayPalPaymentDetails {
		private String currencyCode;
		private BigDecimal value;

		public PayPalPaymentDetails(String currencyCode, BigDecimal value) {
			this.currencyCode = currencyCode;
			this.value = value;
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public BigDecimal getValue() {
			return value;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public void setValue(BigDecimal value) {
			this.value = value;
		}
	}
}
