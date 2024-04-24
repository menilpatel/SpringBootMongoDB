package com.springmongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.springmongo.entity.Users;

public interface UsersRepository extends MongoRepository<Users, String>{

	boolean existsByEmail(String email);

	Users findByEmailIgnoreCase(String email);

	List<Users> findByIsDeleted(boolean b);

}
