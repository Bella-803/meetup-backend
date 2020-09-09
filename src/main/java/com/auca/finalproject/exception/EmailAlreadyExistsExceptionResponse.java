package com.auca.finalproject.exception;

public class EmailAlreadyExistsExceptionResponse {

	private String email;

	
	public EmailAlreadyExistsExceptionResponse(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
