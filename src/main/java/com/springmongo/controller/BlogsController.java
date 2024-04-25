package com.springmongo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springmongo.entity.Blogs;
import com.springmongo.response.ObjectResponse;
import com.springmongo.services.BlogsServices;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class BlogsController {
	@Autowired
	BlogsServices blogsServices;
	
	@GetMapping("/auth/getallblogs")
	public ResponseEntity<ObjectResponse> getAllBlogs() {
		try {
			return new ResponseEntity<ObjectResponse>(
					new ObjectResponse(200, true, "Success", blogsServices.getAllBlogs()), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}

	@PostMapping("/auth/addblog")
	public ResponseEntity<ObjectResponse> addBlog(@RequestBody Blogs blogs) {
		try {
			return new ResponseEntity<ObjectResponse>(
					new ObjectResponse(200, true, "Success", blogsServices.saveBlogDetails(blogs)), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}
}
