package com.VaryTrade.Service.GoogleTokenVerifierService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppGoogleTokenVerifierService {
	@Autowired
	private GoogleTokenVerifierService googleTokenVerifierService;
	
	public boolean verifyToken(String token) {
		return googleTokenVerifierService.verifyToken(token);
	}
}
