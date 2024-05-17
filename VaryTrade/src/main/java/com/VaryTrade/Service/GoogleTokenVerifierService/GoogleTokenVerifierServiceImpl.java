package com.VaryTrade.Service.GoogleTokenVerifierService;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class GoogleTokenVerifierServiceImpl implements GoogleTokenVerifierService {
	@Value("${android.mobile.client.id}")
	private String androidClientId;
	
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String webClientId;
	
	public boolean verifyToken(String token) {
		GooglePublicKeysManager manager = new GooglePublicKeysManager(new NetHttpTransport(), new GsonFactory());
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(manager)
				.setAudience(Arrays.asList(androidClientId, webClientId))
			    .build();

		GoogleIdToken idToken = null;
		try {
			idToken = verifier.verify(token);
		} catch (Exception e) {
			System.out.println("Google Token Verification Failed");
		}
		
		if (idToken != null) {
		  return true;
		} 
			
		else {
			return false;
		}
		
	}
}
