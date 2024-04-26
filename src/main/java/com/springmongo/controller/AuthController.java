package com.springmongo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springmongo.entity.Users;
import com.springmongo.response.ObjectResponse;
import com.springmongo.security.JwtUtils;
import com.springmongo.services.UserDetailsImpl;
import com.springmongo.services.UserDetailsServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AuthController {
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserDetailsServiceImpl userService;

	@PostMapping("/public/signup")
	public ResponseEntity<ObjectResponse> SignUp(@RequestBody Users users) {
		try {
			if (userService.checkUserExistOrNotByEmail(users.getEmail())) {
				return new ResponseEntity<ObjectResponse>(new ObjectResponse(400, false,
						"User already exist with email!", userService.saveUpdateUsers(users)), HttpStatus.OK);
			}
			users.setPassword(encoder.encode(users.getPassword()));
			return new ResponseEntity<ObjectResponse>(
					new ObjectResponse(200, true, "Success", userService.saveUpdateUsers(users)), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}

	@PostMapping("/public/signin")
	public ResponseEntity<ObjectResponse> SignIn(@RequestBody Users users) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(users.getEmail(), users.getPassword()));
			if (authentication.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
				String jwt = jwtUtils.generateJwtToken(authentication);
				userDetails.setToken(jwt);
				return new ResponseEntity<ObjectResponse>(
						new ObjectResponse(200, true, "User login successfully", userDetails), HttpStatus.OK);
			} else {
				return new ResponseEntity<ObjectResponse>(
						new ObjectResponse(401, false, "Email password combination does not matched", null),
						HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<ObjectResponse>(
					new ObjectResponse(401, false, "Email password combination doesnot matched", null), HttpStatus.OK);
		}
	}
}
