package com.springmongo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.springmongo.response.ObjectResponse;
import com.springmongo.services.UserDetailsServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	UserDetailsServiceImpl userService;

	@GetMapping("/auth/getallusers")
	public ResponseEntity<ObjectResponse> getAllUsers() {
		try {
			return new ResponseEntity<ObjectResponse>(
					new ObjectResponse(200, true, "Success", userService.getAllUsers()), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}
}
