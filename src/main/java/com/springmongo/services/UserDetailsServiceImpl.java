package com.springmongo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springmongo.entity.Users;
import com.springmongo.repository.UsersRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UsersRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Users user = userRepository.findByEmailIgnoreCase(email);
		if (user == null) {
			new UsernameNotFoundException("User Not Found with email: " + email);
		}

		return UserDetailsImpl.build(user);
	}

	public Users saveUpdateUsers(Users users) {
		return userRepository.save(users);
	}

	public boolean checkUserExistOrNotByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public List<Users> getAllUsers() {
		return userRepository.findByIsDeleted(false);
	}

	public Optional<Users> getUserById(String id) {
		return userRepository.findById(id);
	}

	public Users deleteUserById(String id) {
		Optional<Users> objUser = userRepository.findById(id);
		if (objUser.isPresent()) {
			objUser.get().setIsDeleted(true);
			return saveUpdateUsers(objUser.get());
		}
		return null;
	}
}
