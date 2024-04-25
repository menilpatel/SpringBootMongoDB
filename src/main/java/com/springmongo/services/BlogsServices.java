package com.springmongo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springmongo.entity.Blogs;
import com.springmongo.repository.BlogRepository;

@Service
public class BlogsServices {
	@Autowired
	BlogRepository blogRepository;
	
	public Blogs saveBlogDetails(Blogs blog)
	{
		return blogRepository.save(blog);
	}
	
	public List<Blogs> getAllBlogs()
	{
		return blogRepository.findByIsDeleted(false);
	}
}
