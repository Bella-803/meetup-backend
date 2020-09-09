package com.auca.finalproject.exception;

public class MeetupNotFoundExceptionResponse {

	private String meetupNotFound;

	public MeetupNotFoundExceptionResponse(String meetupNotFound) {
		this.meetupNotFound = meetupNotFound;
	}

	public String getMeetupNotFound() {
		return meetupNotFound;
	}

	public void setMeetupNotFound(String meetupNotFound) {
		this.meetupNotFound = meetupNotFound;
	}
	
	
}
