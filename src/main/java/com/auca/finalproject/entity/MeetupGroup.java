package com.auca.finalproject.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.auca.finalproject.entity.location.Sector;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class MeetupGroup {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank(message = "Name of Group can not be blank")
	@Column(unique = true)
	private String groupName;
	@NotBlank(message = "Group must have a description")
	@Lob
	private String description;
	private String groupAdminName;
	private String location;
	private String photoPath;
	private int numberOfMembers;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, 
            CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "category_id")
	private Category category;
	
	@JsonIgnore
	@OneToMany(mappedBy = "meetupGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Meetup> meetups;
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, 
			              CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "user_id")
	private UserAccount groupAdmin;
	
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, 
			    cascade = { CascadeType.DETACH , CascadeType.MERGE
			    		   ,CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable (name = "member_group",
	            joinColumns = @JoinColumn(name = "group_id"),
	            inverseJoinColumns = @JoinColumn(name = "member_id"))
	private List<UserAccount> members;
	
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "sector_id")
	private Sector sector;

	
	
	public MeetupGroup() {
	}

	public MeetupGroup(String groupName, String description) {
		this.groupName = groupName;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public UserAccount getGroupAdmin() {
		return groupAdmin;
	}

	public void setGroupAdmin(UserAccount groupAdmin) {
		this.groupAdmin = groupAdmin;
	}


	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<UserAccount> getMembers() {
		return members;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public void setMembers(List<UserAccount> members) {
		this.members = members;
	}

	public int getNumberOfMembers() {
		if(members == null) {
			return 0;
		}
		else {
			return members.size();	
		}
	}

	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}

	public List<Meetup> getMeetups() {
		return meetups;
	}

	public void setMeetups(List<Meetup> meetups) {
		this.meetups = meetups;
	}
	
	public String getGroupAdminName() {
		return groupAdminName;
	}

	public void setGroupAdminName(String groupAdminName) {
		this.groupAdminName = groupAdminName;
	}

	public void addUserInGroup(UserAccount user) {
		if(members == null) {
			members = new ArrayList<>();
		}
		members.add(user);
		user.getMeetupGroupsJoined().add(this);
	}
	
	
	
}
