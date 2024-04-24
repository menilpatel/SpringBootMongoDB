package com.springmongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class Users {
	@Id
	private String id;
	String firstName;
	String middleName;
	String lastName;
	String email;
	String password;
	String token;
	Boolean isDeleted = false;
	long createtimestamp = System.currentTimeMillis() / 1000L;
	long updatetimestamp = System.currentTimeMillis() / 1000L;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public long getCreatetimestamp() {
		return createtimestamp;
	}

	public void setCreatetimestamp(long createtimestamp) {
		this.createtimestamp = createtimestamp;
	}

	public long getUpdatetimestamp() {
		return updatetimestamp;
	}

	public void setUpdatetimestamp(long updatetimestamp) {
		this.updatetimestamp = updatetimestamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
