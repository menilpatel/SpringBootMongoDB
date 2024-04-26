package com.springmongo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.springmongo.entity.Blogs;

public interface BlogRepository extends MongoRepository<Blogs, String>{

	List<Blogs> findByIsDeleted(boolean b);

	Optional<Blogs> findByIdAndIsDeleted(String id, boolean b);

	List<Blogs> findByPublishedByAndIsDeleted(String userid, boolean b);

}
