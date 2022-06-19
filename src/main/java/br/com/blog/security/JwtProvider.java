package br.com.blog.security;

import java.security.Key;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import br.com.blog.exception.BlogMasterException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {

	private Key key;

	@PostConstruct
	public void init() {

		key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	}

	public String generateToken(Authentication authentication) throws BlogMasterException {
		// user from package org.springframework.security.core.userdetails.User
		User principal = (User) authentication.getPrincipal();
		String token = null;
		try {
			token = Jwts.builder().setSubject(principal.getUsername()).signWith(key).compact();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new BlogMasterException("Exception occured while generating token");
		}
		return token;
	}

	@SuppressWarnings("deprecation")
	public boolean validateToken(String jwt) {
		Jwts.parser().setSigningKey(key).parseClaimsJws(jwt);
		return true;
	}

	@SuppressWarnings("deprecation")
	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

}
