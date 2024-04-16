package com.springmongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.springmongo.entity.Users;

public interface UsersRepository extends MongoRepository<Users, String>{

	boolean existsByEmail(String email);

	Users findByEmailIgnoreCase(String email);

}
