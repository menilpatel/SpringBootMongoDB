package com.springmongo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springmongo.entity.Users;
import com.springmongo.response.ObjectResponse;
import com.springmongo.services.UserDetailsServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController {
	@Autowired
	PasswordEncoder encoder;

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

	@GetMapping("/auth/getuserbyid")
	public ResponseEntity<ObjectResponse> getUserById(@RequestParam("id") String id) {
		try {
			return new ResponseEntity<ObjectResponse>(
					new ObjectResponse(200, true, "Success", userService.getUserById(id)), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}

	@PostMapping("/auth/updateuserbyid")
	public ResponseEntity<ObjectResponse> updateUserById(@RequestBody Users users) {
		try {
			if (users.getId() == null) {
				users.setPassword(encoder.encode(users.getPassword()));
			}
			return new ResponseEntity<ObjectResponse>(
					new ObjectResponse(200, true, "Success", userService.saveUpdateUsers(users)), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}

	@GetMapping("/auth/deleteuserbyid")
	public ResponseEntity<ObjectResponse> deleteUserById(@RequestParam("id") String id) {
		try {
			Users user = userService.deleteUserById(id);
			if (user != null) {
				return new ResponseEntity<ObjectResponse>(new ObjectResponse(200, true, "Success", user),
						HttpStatus.OK);
			}
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(401, false, "User not found", null),
					HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}
}
