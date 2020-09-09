package com.auca.finalproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MeetupGroupNotFoundException extends RuntimeException{

	public MeetupGroupNotFoundException(String message) {
		super(message);
	}

	
}
