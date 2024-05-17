package com.VaryTrade.Service.Jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
	@Value("${varytrade.jwt.secret.key}")
	private String jwtSecret;

	@Override
	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims,email);
	}
	
	private String createToken(Map<String, Object> claims, String email) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(email)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*60*30*2))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();		
	}
	 
	 @Override
	 public String extractEmail(String token) {
		 return extractClaim(token, Claims::getSubject);
	 }
	 
	 @Override
	 public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		 final Claims claims = extractAllClaims(token);
		 return claimsResolver.apply(claims);
	 }
	 
	 @Override
	 public Boolean validateToken(String token, String email) {
		 final String username = extractEmail(token);
		 return (username.equals(email) && !isTokenExpired(token));
	 }
	 
	 @Override
	 public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	 }
	 
	 private Claims extractAllClaims(String token) {
		 return Jwts
			.parserBuilder()
	        .setSigningKey(getSignKey())
	        .build()
	        .parseClaimsJws(token)
	        .getBody();
	 }
	 
	 private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	 }
	 
	 private Key getSignKey() {
		 byte[] keyBytes= Decoders.BASE64.decode(jwtSecret);
		 return Keys.hmacShaKeyFor(keyBytes);
	 }
}
