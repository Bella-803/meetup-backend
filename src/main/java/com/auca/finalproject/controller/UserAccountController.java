package com.auca.finalproject.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auca.finalproject.entity.MeetupGroup;
import com.auca.finalproject.entity.UserAccount;
import com.auca.finalproject.service.MapValidationErrorService;
import com.auca.finalproject.service.UserService;

@RestController
@RequestMapping("/api/users/")
@CrossOrigin
public class UserAccountController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	@GetMapping(value = "/profile/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getUserProfile(@PathVariable String filename) throws IOException{
		
		ClassPathResource imgFile = new ClassPathResource("image/user/"+filename);
		byte [] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(bytes);
	}

	@PostMapping(value = "/upload/profile", consumes = {"multipart/form-data"})
	public ResponseEntity<?> uploadUserProfile(@RequestParam MultipartFile file, Principal principal){

		String msg = userService.uploadUserProfile(file, principal.getName());
		
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUser(@PathVariable int userId){
		
		UserAccount user = userService.findUserById(userId);
	
		return new ResponseEntity<UserAccount>(user, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public List<UserAccount> getAllUsers(){
	
		List<UserAccount> li = userService.findAllUser();
		
		return li;
		
	}

	
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer userId, Principal principal){
		String msg = userService.deleteUser(userId, principal.getName());
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}

	
	//Get All groups that have been created by a specific user
	@GetMapping("/ownedGroup")
	public List<MeetupGroup> getAllGroupOfUser(Principal principal){
		
		UserAccount user = userService.findByUsername(principal.getName());

		return user.getCreatedGroup();
	}

	
	//Groups in which user is member
	@GetMapping("/groups")
	public List<MeetupGroup> getGroups(Principal principal){
		
		UserAccount user = userService.findByUsername(principal.getName());
		return user.getMeetupGroupsJoined();
	}

	
}
