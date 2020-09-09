package com.auca.finalproject.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class MapValidationErrorService {

	public ResponseEntity<?> mapValidationError(BindingResult result){
		
		if(result.hasErrors()) {
			
			Map<String, String> mapError = new HashMap<>();
			
			for(FieldError error : result.getFieldErrors()) {
				mapError.put(error.getField(),error.getDefaultMessage());
			}
			
			//return new ResponseEntity<List<FieldError>>(result.getFieldErrors(),HttpStatus.BAD_REQUEST);
              return new ResponseEntity<Map<String, String>>(mapError,HttpStatus.BAD_REQUEST);
		}
		return null;
	}
}
