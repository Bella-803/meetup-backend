package com.auca.finalproject.validator;



import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.auca.finalproject.entity.UserAccount;

@Component
public class UserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return UserAccount.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		UserAccount user = (UserAccount) target;
		if(user.getPassword().length() < 6) {
			errors.rejectValue("password", "length","Password must be at least 6 characters long");
		}
		
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			errors.rejectValue("confirmPassword", "match","Passwords must match");
		}
	}


}
