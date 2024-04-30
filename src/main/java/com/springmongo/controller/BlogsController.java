package com.springmongo.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.springmongo.entity.Blogs;
import com.springmongo.entity.Users;
import com.springmongo.response.ObjectResponse;
import com.springmongo.security.JwtUtils;
import com.springmongo.services.BlogsServices;
import com.springmongo.services.FileUploadServices;
import com.springmongo.services.UserDetailsServiceImpl;
import com.springmongo.services._GlobalFunService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class BlogsController {
	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	BlogsServices blogsServices;
	
	@Autowired
	_GlobalFunService _globalFunService;

	@Autowired
	FileUploadServices fileUploadServices;

	@GetMapping("/public/getallblogs")
	public ResponseEntity<ObjectResponse> getAllBlogs() {
		try {
			return new ResponseEntity<ObjectResponse>(
					new ObjectResponse(200, true, "Success", blogsServices._getAllBlogs()), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}

	@GetMapping("/auth/getblogsbyauthor")
	public ResponseEntity<ObjectResponse> getBlogsByAuthor(@RequestHeader("Authorization") String authorizationHeader) {
		try {
			String authtoken = authorizationHeader.replace("Bearer ", "");
			String email = jwtUtils.getUserNameFromJwtToken(authtoken);
			Optional<Users> userdata = userDetailsServiceImpl._getUserByEmailIgnoreCase(email);
			if (userdata.isPresent()) {
				String loginuserid = userdata.get().getId();
				return new ResponseEntity<ObjectResponse>(
						new ObjectResponse(200, true, "Success", blogsServices._getBlogsByAuthor(loginuserid)),
						HttpStatus.OK);
			}
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(400, false, "User not found!", null),
					HttpStatus.OK);

		} catch (Exception ex) {
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}

	@PostMapping("/auth/addblog")
	public ResponseEntity<ObjectResponse> addBlog(@RequestParam("title") String title,
			@RequestParam("description") String description, @RequestParam("isPublished") Boolean isPublished,
			@RequestParam("publishedBy") String publishedBy, @RequestParam(required = false) MultipartFile image) {
		try {
			Blogs newBlog = new Blogs();
			newBlog.setTitle(title);
			newBlog.setDescription(description);
			newBlog.setPublishedBy(publishedBy);
			newBlog.setIsPublished(isPublished);
			if (image != null) {
				String imageresult = fileUploadServices.uploadFile(image, "blog", _globalFunService.replaceSpCharAndSpace(title));
				newBlog.setImage("/public/download/blog/" + imageresult);
			}
			return new ResponseEntity<ObjectResponse>(
					new ObjectResponse(200, true, "Success", blogsServices._saveBlogDetails(newBlog)), HttpStatus.OK);
		} catch (Exception ex) {
			//System.out.println("addBlog : " + ex.getMessage());
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}

	@PostMapping("/auth/updateblog")
	public ResponseEntity<ObjectResponse> updateBlog(@RequestParam(required = false) String id,
			@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("isPublished") Boolean isPublished, @RequestParam("publishedBy") String publishedBy,
			@RequestParam(required = false) MultipartFile image) {
		try {
			Optional<Blogs> objBlog = blogsServices._getBlogById(id);
			if (objBlog.isPresent()) {
				objBlog.get().setTitle(title);
				objBlog.get().setDescription(description);
				objBlog.get().setPublishedBy(publishedBy);
				objBlog.get().setIsPublished(isPublished);
				if (image != null) {
					String imageresult = fileUploadServices.uploadFile(image, "blog", _globalFunService.replaceSpCharAndSpace(title));
					objBlog.get().setImage("/public/download/blog/" + imageresult);
				}
			}
			return new ResponseEntity<ObjectResponse>(
					new ObjectResponse(200, true, "Success", blogsServices._saveBlogDetails(objBlog.get())),
					HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}

	@GetMapping("/auth/getblogbyid")
	public ResponseEntity<ObjectResponse> getBlogById(@RequestParam("id") String id) {
		try {
			return new ResponseEntity<ObjectResponse>(
					new ObjectResponse(200, true, "Success", blogsServices._getBlogById(id)), HttpStatus.OK);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}

	@GetMapping("/auth/deleteblogbyid")
	public ResponseEntity<ObjectResponse> deleteBlogById(@RequestParam("id") String id) {
		try {
			Blogs blog = blogsServices._deleteBlogById(id);
			if (blog != null) {
				return new ResponseEntity<ObjectResponse>(new ObjectResponse(200, true, "Success", blog),
						HttpStatus.OK);
			} else {
				return new ResponseEntity<ObjectResponse>(new ObjectResponse(404, false, "Blog not found", null),
						HttpStatus.OK);
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return new ResponseEntity<ObjectResponse>(new ObjectResponse(500, false, "Something went wrong!", null),
					HttpStatus.OK);
		}
	}

	@GetMapping("/public/download/{folder}/{fileName:.+}")
	public ResponseEntity<Resource> loadBlogImage(@PathVariable String folder, @PathVariable String fileName,
			HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileUploadServices.loadFileAsResource(fileName, folder);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			System.out.println("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}
