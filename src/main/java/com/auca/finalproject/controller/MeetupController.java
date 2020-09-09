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
import com.auca.finalproject.entity.UserAccount;
import com.auca.finalproject.service.MapValidationErrorService;
import com.auca.finalproject.service.MeetupService;

@RestController
@RequestMapping("/api/meetups")
@CrossOrigin
public class MeetupController {

	@Autowired
	private MeetupService meetupService;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;
	
	
	@PostMapping("/{groupId}")
	public ResponseEntity<?> saveMeetup(@Valid @RequestBody Meetup meetup,
			                            BindingResult result, @PathVariable int groupId, Principal principal) {
	
		ResponseEntity<?> mapError = mapValidationErrorService.mapValidationError(result);
		if(mapError != null) {
			return mapError;
		}
		
		Meetup theMeetup = meetupService.createMeetup(meetup, groupId, principal.getName());
		return new ResponseEntity<Meetup>(theMeetup, HttpStatus.CREATED);
	}
	

	@GetMapping("/{meetupId}")
	public ResponseEntity<?> getMeetup(@PathVariable int meetupId){
		
		Meetup meetup = meetupService.findMeetupById(meetupId);

		return new ResponseEntity<Meetup>(meetup,HttpStatus.OK);
	}
	
	@GetMapping("/all/{groupId}")
	public ResponseEntity<?> getAllMeetupsOfGroup(@PathVariable int groupId){
	
		List<Meetup> li = meetupService.findAllMeetup(groupId);
		
		return new ResponseEntity<List<Meetup>>(li, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllMeetups(){
	
		List<Meetup> li = meetupService.findAllMeetup();
		
		return new ResponseEntity<List<Meetup>>(li, HttpStatus.OK);
	}
	
	@DeleteMapping("/{meetupId}")
	public ResponseEntity<?> deleteMeetup(@PathVariable int meetupId, Principal principal){
		
		meetupService.deleteMeetup(meetupId, principal.getName());
		return new ResponseEntity<String>("Successfully deleted", HttpStatus.OK);
	}
	
	@PatchMapping("/{groupId}")
	public ResponseEntity<?> updateMeetup(@Valid @RequestBody Meetup meetup,BindingResult result,
			                              @PathVariable int groupId, Principal principal) {
		ResponseEntity<?> mapError = mapValidationErrorService.mapValidationError(result);
		if(mapError != null) {
			return mapError;
		}	
		Meetup updatedMeetup = meetupService.updateMeetup(meetup, groupId, principal.getName());
		return new ResponseEntity<Meetup>(updatedMeetup,HttpStatus.OK);
	}
	
	//Attend a meetup
	@PostMapping("/attend/{meetupId}")
	public ResponseEntity<?> attendMeetup(@PathVariable Integer meetupId, Principal principal){
		
		String msg = meetupService.attendMeetup(meetupId, principal.getName());
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	//Get All Attendees of a specific meetup
	@GetMapping("/attendees/{meetupId}")
	public List<UserAccount> getAllAttendees(@PathVariable int meetupId){
		
		Meetup meetup = meetupService.findMeetupById(meetupId);
		return meetup.getAttendees();
	}
	
	@PatchMapping("/cancel/{meetupId}")
	public ResponseEntity<?> cancelAttendance(@PathVariable Integer meetupId, Principal principal){
		
		String msg = meetupService.cancelAttendance(meetupId, principal.getName());
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	@PostMapping(value = "/upload/image/{meetupId}", consumes = {"multipart/form-data"})
	public ResponseEntity<?> uploadMeetupImage(@PathVariable Integer meetupId, @RequestParam MultipartFile file,
			                                  Principal principal){
		
		String msg = meetupService.uploadMeetupImage(file, meetupId, principal.getName());
		
		return new ResponseEntity<String>(msg,HttpStatus.OK);
	}
	
	@GetMapping(value = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> uploadGroupImage(@PathVariable String filename) throws IOException{
		
		ClassPathResource imgFile = new ClassPathResource("image/meetup/"+filename);
		byte [] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(bytes);
	}
	
}
