package com.auca.finalproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MeetupNotFoundException extends RuntimeException{

	public MeetupNotFoundException(String message) {
		super(message);
	}

	
}
