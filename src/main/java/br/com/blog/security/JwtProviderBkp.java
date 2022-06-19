package br.com.blog.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.blog.exception.BlogMasterException;
import br.com.blog.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtProviderBkp {

	private KeyStore keyStore;

	@PostConstruct
	public void init() throws BlogMasterException {
		try {
			keyStore = KeyStore.getInstance("JKS");
			InputStream resourceAsStream = getClass().getResourceAsStream("/blogmaster.jks");
			keyStore.load(resourceAsStream, "secret".toCharArray());
		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
			throw new BlogMasterException("Exception occured while loading keystore");
		}

	}

	public String generateToken(Authentication authentication) throws BlogMasterException {
		User principal = (User) authentication.getPrincipal();
		String compact = null;
		try {
			compact = Jwts.builder().setSubject(principal.getUserName()).signWith(getPrivateKey()).compact();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new BlogMasterException("Exception occured while generating token");
		}

		return compact;
	}

	private PrivateKey getPrivateKey() throws BlogMasterException {
		try {
			return (PrivateKey) keyStore.getKey("blogmaster", "secret".toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
			throw new BlogMasterException("Exception occured while retrieving public key from keystore");
		}
	}

	@SuppressWarnings("deprecation")
	public boolean validateToken(String jwt) throws BlogMasterException {
		try {
			Jwts.parser().setSigningKey(getPublickey()).parseClaimsJws(jwt);
		} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
				| IllegalArgumentException e) {
			e.printStackTrace();
			throw new BlogMasterException(e.getMessage());
		}
		return true;
	}

	private PublicKey getPublickey() throws BlogMasterException {
		try {
			return keyStore.getCertificate("springblog").getPublicKey();
		} catch (KeyStoreException e) {
			throw new BlogMasterException("Exception occured while retrieving public key from keystore");
		}
	}

	@SuppressWarnings("deprecation")
	public String getUsernameFromJWT(String token) throws BlogMasterException {
		try {
			Claims claims = Jwts.parser().setSigningKey(getPublickey()).parseClaimsJws(token).getBody();
			return claims.getSubject();
		} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
				| IllegalArgumentException e) {
			throw new BlogMasterException(e.getMessage());
		}
	}

}
