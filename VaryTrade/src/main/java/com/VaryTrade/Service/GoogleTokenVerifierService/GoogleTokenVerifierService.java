package com.VaryTrade.Service.GoogleTokenVerifierService;

import org.springframework.stereotype.Service;

@Service
public interface GoogleTokenVerifierService {
	public boolean verifyToken(String token);
}
