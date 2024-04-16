package com.springmongo.response;

import org.springframework.lang.Nullable;

public class ObjectResponse {
	private Integer statuscode;
	private String message;
	private Boolean status;
	@Nullable
	private Object data;

	public ObjectResponse(Integer statuscode, Boolean status, String message, Object data) {
		this.statuscode = statuscode;
		this.message = message;
		this.status = status;
		this.data = data;
	}

	public Integer getstatuscode() {
		return this.statuscode;
	}

	public void setstatuscode(Integer statuscode) {
		this.statuscode = statuscode;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getStatus() {
		return this.status;
	}

	public void getStatus(Boolean status) {
		this.status = status;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(@Nullable Object getData) {
		this.data = getData;
	}
}
