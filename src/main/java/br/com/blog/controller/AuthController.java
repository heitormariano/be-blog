package br.com.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.blog.dto.LoginRequest;
import br.com.blog.dto.RegisterRequest;
import br.com.blog.exception.BlogMasterException;
import br.com.blog.model.User;
import br.com.blog.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<User> signup(@RequestBody RegisterRequest registerRequest) {
		try {
			User _user = authService.signup(registerRequest);
			return new ResponseEntity<>(_user, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@PostMapping("/login")
//	public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) throws BlogMasterException {
//		return authService.login(loginRequest);
//	}

	@PostMapping("/login")
	public String login(@RequestBody LoginRequest loginRequest) throws BlogMasterException {
		return authService.login(loginRequest);
	}
}
