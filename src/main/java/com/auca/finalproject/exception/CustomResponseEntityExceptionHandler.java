package com.auca.finalproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public final ResponseEntity<?> handleCategoryNotFoundException(CategoryNotFoundException ex, WebRequest req){
		
		CategoryNotFoundResponse exceptionResponse = new CategoryNotFoundResponse(ex.getMessage());
		return new ResponseEntity<CategoryNotFoundResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<?> handleGroupNotFoundException(MeetupGroupNotFoundException ex, WebRequest req){
		
		GroupNotFoundExceptionResponse exceptionResponse = new GroupNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity<GroupNotFoundExceptionResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<?> handleMeetupNotFoundException(MeetupNotFoundException ex, WebRequest req){
		
		MeetupNotFoundExceptionResponse exceptionResponse = new MeetupNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity<MeetupNotFoundExceptionResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<?> handleEmailAlreadyExistException(EmailAlreadyExistsException ex, WebRequest req){
		
		EmailAlreadyExistsExceptionResponse exceptionResponse = new EmailAlreadyExistsExceptionResponse(ex.getMessage());
		return new ResponseEntity<EmailAlreadyExistsExceptionResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<?> handleUsernameAlreadyExistException(UsernameAlreadyExistException ex, WebRequest req){
		UsernameAlreadyExistExceptionResponse exceptionResponse = new UsernameAlreadyExistExceptionResponse(ex.getMessage());
		return new ResponseEntity<UsernameAlreadyExistExceptionResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex, WebRequest req){
		
		UserNotFoundExceptionResponse exceptionResponse = new UserNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity<UserNotFoundExceptionResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
}
