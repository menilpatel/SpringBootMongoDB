package com.springmongo.services;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springmongo.entity.Users;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private String id;

	private String firstname;
	private String middlename;
	private String lastname;
	private String email;
	private Boolean isdeleted;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(String id, String email, String firstname, String middlename, String lastname,
			String password, Boolean isdeleted) {
		this.id = id;
		this.email = email;
		this.firstname = firstname;
		this.middlename = middlename;
		this.lastname = lastname;
		this.password = password;
		this.isdeleted = isdeleted;
	}

	public static UserDetailsImpl build(Users user) {
		// List<GrantedAuthority> authorities = user.getRoles().stream()
		// .map(role -> new SimpleGrantedAuthority(role.getName().name()))
		// .collect(Collectors.toList());

		return new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getMiddleName(),
				user.getLastName(), user.getPassword(), user.getIsDeleted());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public Boolean getisdeleted() {
		return isdeleted;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
}