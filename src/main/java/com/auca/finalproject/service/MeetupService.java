package com.auca.finalproject.service;



import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.auca.finalproject.dao.MeetupDao;
import com.auca.finalproject.dao.UserDao;
import com.auca.finalproject.entity.Category;
import com.auca.finalproject.entity.ERoles;
import com.auca.finalproject.entity.Meetup;
import com.auca.finalproject.entity.MeetupGroup;
import com.auca.finalproject.entity.UserAccount;
import com.auca.finalproject.exception.ImageStorageException;
import com.auca.finalproject.exception.MeetupGroupNotFoundException;
import com.auca.finalproject.exception.MeetupNotFoundException;

@Service
public class MeetupService {
	
	@Value("${upload.meetup-path}")
	private String path;

	@Autowired
	private MeetupDao meetupDao;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserDao userDao;
	
	public Meetup createMeetup(Meetup meetup, int groupId, String username) {
		
		MeetupGroup meetupGroup = groupService.findGroupById(groupId);
		UserAccount groupAdmin = userDao.findByUsername(username);
		if(!meetupGroup.getGroupAdmin().getUsername().equals(groupAdmin.getUsername())) {
			throw new RuntimeException("You are not allowed to post a meetup in this group");
		}
		meetup.setMeetupGroup(meetupGroup);
		Meetup meet = meetupDao.save(meetup);
		
		List<UserAccount> members = meetupGroup.getMembers();
		if(members != null) {
			for(UserAccount user : members) {
				try {
					emailService.sendMeetupScheduledEmail(user, meetup, meetupGroup.getGroupName());
					
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("Could not send email "+ e.getMessage());
				}
			}
		}
		return meet;
	}
	
	
	public Meetup findMeetupById(int id) {
		
		Optional<Meetup> meetups = meetupDao.findById(id);
		
		if(!meetups.isPresent()) {
			throw new MeetupNotFoundException("Meetup of id : '"+id+"' does not exists");
		}
		Meetup meetup = meetups.get();
		return meetup;
	}
	
	
	public void deleteMeetup(int id, String username) {
		UserAccount groupAdmin = userDao.findByUsername(username);
		Meetup meetup = findMeetupById(id);
		if(!meetup.getMeetupGroup().getGroupAdmin().getUsername().equals(groupAdmin.getUsername())) {
			throw new RuntimeException("You are not allowed to delete this group");
		}
		meetupDao.delete(meetup);
	}
	
	
	public List<Meetup> findAllMeetup(){
		return meetupDao.findAll();
	}


	public List<Meetup> findAllMeetup(int groupId) {
		
		MeetupGroup meetupGroup = groupService.findGroupById(groupId);
		return meetupGroup.getMeetups();
	}
	
	public Meetup updateMeetup(Meetup meetup, int groupId, String username) {
		
		MeetupGroup meetupGroup = groupService.findGroupById(groupId);
		UserAccount groupAdmin = userDao.findByUsername(username);
		if(!meetupGroup.getGroupAdmin().getUsername().equals(groupAdmin.getUsername())) {
			throw new RuntimeException("You are not allowed to update a meetup in this group");
		}
		
		if(meetup.getId() != null) {
			Meetup existingMeetup = findMeetupById(meetup.getId());
			if(existingMeetup.getMeetupGroup().getId() == groupId) {
				meetup.setMeetupGroup(existingMeetup.getMeetupGroup());
				meetup.setAttendees(existingMeetup.getAttendees());
				meetupDao.save(meetup);
				return meetup;
			}else {
				throw new MeetupGroupNotFoundException("Meetup you are updating does not belong to the group given");
			}
		}else {
			throw new MeetupNotFoundException("The meetup you are trying to update does not exists");
		}
	}
	
	public String attendMeetup(Integer meetupId, String username) {
		Meetup meetup = findMeetupById(meetupId);
		UserAccount user = userDao.findByUsername(username);
		meetup.attendMeetup(user);
		meetupDao.save(meetup);
		return "Thank you for joining our meetup";
	}
	
	public String cancelAttendance (Integer meetupId, String username) {
		Meetup meetup = findMeetupById(meetupId);
		UserAccount user = userDao.findByUsername(username);
		List<UserAccount> attendees = meetup.getAttendees();
		attendees.remove(user);
		meetup.setAttendees(attendees);
		meetupDao.save(meetup);
		return "You have successfully cancelled your attendance";
	}
	
	public String uploadMeetupImage(MultipartFile file, Integer meetupId, String username) {
		
		Meetup meetup = findMeetupById(meetupId);
		
		if(file.isEmpty()) {
			throw new ImageStorageException("Could not save an empty file");
		}
		
		String originalFilename = file.getOriginalFilename();
		String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String newFilename = meetup.getId()+"_meetupImage"+fileExtension;
		
		try {
			
			InputStream bytes = file.getInputStream();
			Files.copy(bytes, Paths.get(path + newFilename), StandardCopyOption.REPLACE_EXISTING);
			
			meetup.setPhotoPath("http://localhost:8080/api/meetups/image/"+newFilename);
			Integer groupId = meetup.getMeetupGroup().getId();
			updateMeetup(meetup, groupId, username);
			
			return "Meetup Image successfully saved";
			
		} catch (Exception e) {
			throw new ImageStorageException("Could not save Image. Try again later");
		}
		
		
	}
	
	public int getNumberOfMeetups(String username) {
		UserAccount admin = userDao.findByUsername(username);
		
		if(!admin.geteRoles().equals(ERoles.ADMIN)) {
			throw new RuntimeException("You are not allowed to perform this operation");
		}
		
		List<Meetup> allMeetup = findAllMeetup();
		return allMeetup.size();
		
	}
}
