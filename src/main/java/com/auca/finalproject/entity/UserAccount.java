package com.auca.finalproject.entity;


import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class UserAccount implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String fullname;
	@NotBlank(message = "Email can not blank")
	@Column(unique = true)
	private String email;
	@NotBlank(message = "Username can not blank")
	@Column(unique = true)
	private String username;
	private String password;
	@Enumerated(EnumType.STRING)
	private ERoles eRoles;
	private int numberOfCreatedGroups;
	@Transient
	private String confirmPassword;
	private String profilePhotoPath;

	
	@JsonIgnore
	@OneToMany(mappedBy = "groupAdmin",
			   cascade = CascadeType.ALL)
	private List<MeetupGroup> createdGroup;
	
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, 
			    cascade = { CascadeType.DETACH , CascadeType.MERGE
			    		   ,CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable (name = "member_group",
	            joinColumns = @JoinColumn(name = "member_id"),
	            inverseJoinColumns = @JoinColumn(name = "group_id"))
	private List<MeetupGroup> meetupGroupsJoined;
	

	@JsonIgnore
	@OneToMany(mappedBy = "admin",
	           cascade = CascadeType.ALL)
	private List <Category> categories;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, 
		        cascade = { CascadeType.DETACH , CascadeType.MERGE
		    		       ,CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable (name = "meeting_attendee",
                joinColumns = @JoinColumn(name = "attendee_id"),
                inverseJoinColumns = @JoinColumn(name = "meetup_id"))
	private List<Meetup> meetups;
	
	
	public UserAccount() {
	}

	public UserAccount(String fullname, String email, String username, String password) {
		this.fullname = fullname;
		this.email = email;
		this.username = username;
		this.password = password;
	}

	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public ERoles geteRoles() {
		return eRoles;
	}

	public void seteRoles(ERoles eRoles) {
		this.eRoles = eRoles;
	}

	public String getProfilePhotoPath() {
		return profilePhotoPath;
	}

	public void setProfilePhotoPath(String profilePhotoPath) {
		this.profilePhotoPath = profilePhotoPath;
	}

	public List<MeetupGroup> getCreatedGroup() {
		return createdGroup;
	}

	public void setCreatedGroup(List<MeetupGroup> createdGroup) {
		this.createdGroup = createdGroup;
	}

	public int getNumberOfCreatedGroups() {
		if(createdGroup == null ) {
			return 0;
		}else {
			return createdGroup.size();
		}
	}

	public void setNumberOfCreatedGroups(int numberOfCreatedGroups) {
		this.numberOfCreatedGroups = numberOfCreatedGroups;
	}

	public List<MeetupGroup> getMeetupGroupsJoined() {
		return meetupGroupsJoined;
	}

	public void setMeetupGroupsJoined(List<MeetupGroup> meetupGroupsJoined) {
		this.meetupGroupsJoined = meetupGroupsJoined;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Meetup> getMeetups() {
		return meetups;
	}

	public void setMeetups(List<Meetup> meetups) {
		this.meetups = meetups;
	}


	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return null;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		
		return true;
	}
	
}
