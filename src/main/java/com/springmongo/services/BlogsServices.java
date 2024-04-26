package com.springmongo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmongo.entity.Blogs;
import com.springmongo.repository.BlogRepository;

@Service
public class BlogsServices {
	@Autowired
	BlogRepository blogRepository;

	public Blogs _saveBlogDetails(Blogs blog) {
		return blogRepository.save(blog);
	}

	public List<Blogs> _getAllBlogs() {
		return blogRepository.findByIsDeleted(false);
	}

	public List<Blogs> _getBlogsByAuthor(String userid) {
		return blogRepository.findByPublishedByAndIsDeleted(userid, false);
	}

	public Optional<Blogs> _getBlogById(String id) {
		return blogRepository.findByIdAndIsDeleted(id, false);
	}

	public Blogs _deleteBlogById(String id) {
		Optional<Blogs> objBlog = blogRepository.findByIdAndIsDeleted(id, false);
		if (objBlog.isPresent()) {
			objBlog.get().setIsDeleted(true);
			return _saveBlogDetails(objBlog.get());
		}
		return null;
	}
}
