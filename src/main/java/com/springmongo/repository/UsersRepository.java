package com.springmongo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.springmongo.entity.Users;

public interface UsersRepository extends MongoRepository<Users, String>{

	boolean existsByEmail(String email);

	Optional<Users> findByEmailIgnoreCase(String email);

	List<Users> findByIsDeleted(boolean b);

}
