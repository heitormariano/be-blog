package br.com.blog.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.blog.dto.LoginRequest;
import br.com.blog.dto.RegisterRequest;
import br.com.blog.exception.BlogMasterException;
import br.com.blog.model.User;
import br.com.blog.repository.UserRepository;
import br.com.blog.security.JwtProvider;

@Service
public class AuthService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private JwtProvider jwtProvider;

	public User signup(RegisterRequest registerRequest) {
		User user = new User();
		user.setUserName(registerRequest.getUserName());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(encodePassword(registerRequest.getPassword()));

		return userRepository.save(user);
	}

//	public AuthenticationResponse login(LoginRequest loginRequest) throws BlogMasterException {
//		Authentication authenticate = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
//
//		SecurityContextHolder.getContext().setAuthentication(authenticate);
//
//		String authenticationToken = jwtProvider.generateToken(authenticate);
//		return new AuthenticationResponse(authenticationToken, loginRequest.getUserName());
//	}

	public String login(LoginRequest loginRequest) throws BlogMasterException {
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authenticate);

		return jwtProvider.generateToken(authenticate);
	}

	public Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return Optional.of(principal);
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}
}
