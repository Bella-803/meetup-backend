package com.auca.finalproject.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Meetup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Integer id;
	@NotBlank(message = "Meeting Title can not blank")
	private String meetingTitle;
	@Lob
	private String description;
	private String location;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date dateAndTime;
	private String photoPath;
	private int numberOfAttendees = 0;
	
	
	//@JsonIgnore
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, 
                          CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "group_id")
	private MeetupGroup meetupGroup;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, 
	        cascade = { CascadeType.DETACH , CascadeType.MERGE
	    		       ,CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable (name = "meeting_attendee",
                joinColumns = @JoinColumn(name = "meetup_id"),
                inverseJoinColumns = @JoinColumn(name = "attendee_id"))
	private List<UserAccount> attendees;

	public Meetup() {
	}

	public Meetup(String meetingTitle, String description, String location, Date dateAndTime) {
		
		this.meetingTitle = meetingTitle;
		this.description = description;
		this.location = location;
		this.dateAndTime = dateAndTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMeetingTitle() {
		return meetingTitle;
	}

	public void setMeetingTitle(String meetingTitle) {
		this.meetingTitle = meetingTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(Date dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public MeetupGroup getMeetupGroup() {
		return meetupGroup;
	}

	public void setMeetupGroup(MeetupGroup meetupGroup) {
		this.meetupGroup = meetupGroup;
	}

	public List<UserAccount> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<UserAccount> attendees) {
		this.attendees = attendees;
	}
	
	
   public int getNumberOfAttendees() {
	   if(attendees == null) {
		   return 0;
	   }else {
		   return attendees.size();
	   }	
	}


   public void setNumberOfAttendees(int numberOfAttendees) {
    	this.numberOfAttendees = numberOfAttendees;
   }

public void attendMeetup(UserAccount user) {
	   if(attendees == null) {
		   attendees = new ArrayList<>();
	   }
	   attendees.add(user);
   }
   
	
}
