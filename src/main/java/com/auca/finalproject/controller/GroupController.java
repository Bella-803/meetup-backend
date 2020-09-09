package com.auca.finalproject.controller;


import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auca.finalproject.entity.Meetup;
import com.auca.finalproject.entity.MeetupGroup;
import com.auca.finalproject.entity.UserAccount;
import com.auca.finalproject.service.GroupService;
import com.auca.finalproject.service.MapValidationErrorService;


@RestController
@RequestMapping("/api/groups")
@CrossOrigin
public class GroupController {

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	
	@PostMapping("/{categoryId}/{sectorId}")
	public ResponseEntity<?>saveGroup(@Valid @RequestBody MeetupGroup meetupGroup,
			                          BindingResult result, @PathVariable int categoryId,
			                          @PathVariable int sectorId, Principal principal) throws Exception {
	
		ResponseEntity<?> mapError = mapValidationErrorService.mapValidationError(result);
		if(mapError != null) 
			return mapError;
	
		MeetupGroup group = groupService.createGroup(meetupGroup, categoryId,sectorId, principal.getName());
		return new ResponseEntity<MeetupGroup>(group, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/{groupId}")
	public ResponseEntity<?> getGroup(@PathVariable int groupId){
		
		MeetupGroup group = groupService.findGroupById(groupId);	
		return new ResponseEntity<MeetupGroup>(group, HttpStatus.OK);
	}
	
	
	@GetMapping("/all/{categoryId}")
	public List<MeetupGroup> getAllGroups(@PathVariable int categoryId){
	
		List<MeetupGroup> li = groupService.findAllGroup(categoryId);
		return li;
		//return new ResponseEntity<List<MeetupGroup>>(li, HttpStatus.OK);
	}
	
	//All groups in system
	@GetMapping("/all")
	public List<MeetupGroup> getAllGroups(){
		List<MeetupGroup> allGroups = groupService.findAllGroup();
		return allGroups;
	}
	
	
	@PatchMapping("/{catId}")
	public ResponseEntity<?> updateGroup(@Valid @RequestBody MeetupGroup group, BindingResult result,
			                             @PathVariable int catId, Principal principal){
		ResponseEntity<?> mapError = mapValidationErrorService.mapValidationError(result);
		if(mapError != null) {
			return mapError;
		}
		MeetupGroup updatedGroup = groupService.updateGroup(group, catId, principal.getName());
		return new ResponseEntity<MeetupGroup>(updatedGroup,HttpStatus.OK);
	}
	
	@DeleteMapping("/{groupId}")
	public ResponseEntity<?> deleteGroup(@PathVariable int groupId, Principal principal){
		groupService.deleteGroup(groupId, principal.getName());
		return new ResponseEntity<String>("Group deleted successfully", HttpStatus.OK);
	}

	
	//Get All Meetups of a specific group
	@GetMapping("/meetups/{groupId}")
	public List<Meetup> getAllMeetup(@PathVariable int groupId){
		
		MeetupGroup group = groupService.findGroupById(groupId);	
		return group.getMeetups();
	}
	
	//User join group
	@PostMapping("/join/{groupId}")
	public ResponseEntity<?> joinMeetupGroup(@PathVariable Integer groupId, Principal principal){
		
		String msg = groupService.joinGroup(groupId, principal.getName());
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	//Get All Member of this group
	@GetMapping("/members/{groupId}")
	public List<UserAccount> getAllMembers(@PathVariable Integer groupId){
		MeetupGroup group = groupService.findGroupById(groupId);
		return group.getMembers();
	}
	
	@PostMapping(value = "/upload/image/{groupId}", consumes = {"multipart/form-data"})
	public ResponseEntity<?> uploadGroupImage(@PathVariable Integer groupId, @RequestParam MultipartFile file,
			                                  Principal principal){
		
		String msg = groupService.uploadGroupPhoto(file, groupId, principal.getName());
		
		return new ResponseEntity<String>(msg,HttpStatus.OK);
	}
	
	@GetMapping(value = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> uploadGroupImage(@PathVariable String filename) throws IOException{
		
		ClassPathResource imgFile = new ClassPathResource("image/group/"+filename);
		byte [] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(bytes);
	}
	
}
