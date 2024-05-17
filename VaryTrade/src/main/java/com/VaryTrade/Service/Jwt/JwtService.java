package com.VaryTrade.Service.Jwt;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;

@Service
public interface JwtService {
	public String generateToken(String userName);
	public String extractEmail(String token);
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver);
	public Boolean validateToken(String token, String userName);
	public Date extractExpiration(String token);
}
