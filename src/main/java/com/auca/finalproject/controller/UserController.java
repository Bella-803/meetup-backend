package com.auca.finalproject.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auca.finalproject.entity.UserAccount;
import com.auca.finalproject.payload.JTWLoginSuccessResponse;
import com.auca.finalproject.payload.LoginRequest;
import com.auca.finalproject.security.JwtTokenProvider;
import com.auca.finalproject.service.MapValidationErrorService;
import com.auca.finalproject.service.UserService;
import com.auca.finalproject.validator.UserValidator;
import static com.auca.finalproject.security.SecurityConstant.TOKEN_PREFIX;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;

	
	@PostMapping("/register")
	public ResponseEntity<?> SaveUser(@Valid @RequestBody UserAccount userAccount, BindingResult result){

		//Valid password match
		userValidator.validate(userAccount, result);
		
		ResponseEntity<?> mapError = mapValidationErrorService.mapValidationError(result);
		if(mapError != null) {
			return mapError;
		}
		
		UserAccount user = userService.saveUser(userAccount);
		
		return new ResponseEntity<UserAccount> (user, HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
		ResponseEntity<?> mapError = mapValidationErrorService.mapValidationError(result);
		if(mapError != null) return mapError;
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
						                                loginRequest.getPassword())
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);
		
		
		return new ResponseEntity<JTWLoginSuccessResponse>(new JTWLoginSuccessResponse(true, jwt), HttpStatus.OK);
	}
	
	@PostMapping("/admin")
	public ResponseEntity<?> addAdmin(@Valid @RequestBody UserAccount admin, BindingResult result){
		       
		        //Valid password match
				userValidator.validate(admin, result);
				
				ResponseEntity<?> mapError = mapValidationErrorService.mapValidationError(result);
				if(mapError != null) {
					return mapError;
				}
				
				UserAccount user = userService.setAdmin(admin);
				return new ResponseEntity<UserAccount> (user, HttpStatus.CREATED);
	}
	

}
