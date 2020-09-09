package com.auca.finalproject.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auca.finalproject.service.CategoryService;
import com.auca.finalproject.service.GroupService;
import com.auca.finalproject.service.MeetupService;
import com.auca.finalproject.service.UserService;

@RestController
@RequestMapping("api/admin")
@CrossOrigin
public class AdminController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private MeetupService meetupService;
	
	@GetMapping("/number/users")
	public ResponseEntity<?> getNumberOfUsers(Principal principal){
		int numberOfUsers = userService.getNumberOfUsers(principal.getName());
		
		return new ResponseEntity<Integer>(numberOfUsers, HttpStatus.OK);
	}
	
	@GetMapping("/number/categories")
	public ResponseEntity<?> getNumberOfCategories(Principal principal){
		int numberOfCategories = categoryService.getNumberOfCategory(principal.getName());
		
		return new ResponseEntity<Integer>(numberOfCategories, HttpStatus.OK);
	}
	
	@GetMapping("/number/groups")
	public ResponseEntity<?> getNumberOfGroups(Principal principal){
		int numberOfGroups = groupService.getNumberOfGroups(principal.getName());
		
		return new ResponseEntity<Integer>(numberOfGroups, HttpStatus.OK);
	}
	
	@GetMapping("/number/meetups")
	public ResponseEntity<?> getNumberOfMeetusps(Principal principal){
		int numberOfMeetups = meetupService.getNumberOfMeetups(principal.getName());
		
		return new ResponseEntity<Integer>(numberOfMeetups, HttpStatus.OK);
	}
}
