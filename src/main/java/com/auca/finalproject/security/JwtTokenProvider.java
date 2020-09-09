package com.auca.finalproject.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auca.finalproject.entity.UserAccount;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import static com.auca.finalproject.security.SecurityConstant.EXPIRATION_TIME;
import static com.auca.finalproject.security.SecurityConstant.SECRET;;

@Component
public class JwtTokenProvider {

	//Generate the token
	public String generateToken(Authentication authetication) {
		
		 UserAccount user = (UserAccount) authetication.getPrincipal();
		 Date now = new Date(System.currentTimeMillis());
		 
		 Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
		 
		 String userId = Integer.toString(user.getId());
		 
		 Map<String, Object> claims = new HashMap<>();
		 claims.put("id", (Integer.toString(user.getId())));
		 claims.put("username", user.getUsername());
		 claims.put("fullname", user.getFullname());
		 claims.put("role", user.geteRoles());
		 
		 return Jwts.builder()
				 .setSubject(userId)
				 .setClaims(claims)
				 .setIssuedAt(now)
				 .setExpiration(expiryDate)
				 .signWith(SignatureAlgorithm.HS512, SECRET)
				 .compact();
		
	}
	
	//Validate the token
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			System.out.println("Inavlid JWT Signature");
		}
		catch (MalformedJwtException e) {
			System.out.println("Invalid JWT token");
		}
		catch (ExpiredJwtException e) {
			System.out.println("Expired JWT token");
		}
		catch (UnsupportedJwtException e) {
			System.out.println("Unsported jwt exception");
		}
		catch (IllegalArgumentException e) {
			System.out.println("JWT claims string is empty");
		}
		
		return false;
	}
	
	//Get user Id from token
	public Integer getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		String id = (String) claims.get("id");
		Integer idInteger = Integer.parseInt(id);
		return idInteger;
		
	}
}
