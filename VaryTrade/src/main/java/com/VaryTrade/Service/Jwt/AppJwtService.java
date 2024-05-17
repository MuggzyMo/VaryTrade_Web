package com.VaryTrade.Service.Jwt;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;

@Service
public class AppJwtService {
	@Autowired
	private JwtService jwtService;
	
	public String generateToken(String userName) {
		return jwtService.generateToken(userName);
	}
	
	public String extractEmail(String token) {
		return jwtService.extractEmail(token);
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		return jwtService.extractClaim(token, claimResolver);
	}
	
	public Boolean validateToken(String token, String userName) {
		return jwtService.validateToken(token, userName);
	}
	
	public Date extractExpiration(String token) {
		return jwtService.extractExpiration(token);
	}
}
