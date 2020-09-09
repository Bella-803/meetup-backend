package com.auca.finalproject.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank(message = "Name of Category can not be blank")
	@Size(min = 3,message = "Name must be at least 3 characters long")
	@Column(unique = true)
	private String categoryName;
	@NotBlank(message = "Description can not be blank")
	@Lob
	private String description;
	private String photoPath;
	private int numberOfGroups = 0;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
	private List<MeetupGroup> meetupGroups;
	

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, 
                          CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "user_id")
	private UserAccount admin;

		
	public Category() {
	}

	public Category(String categoryName, String description) {
		
		this.categoryName = categoryName;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	
	
	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public UserAccount getAdmin() {
		return admin;
	}

	public void setAdmin(UserAccount admin) {
		this.admin = admin;
	}


	public void setNumberOfGroups(int numberOfGroups) {
		this.numberOfGroups = numberOfGroups;
	}

	public List<MeetupGroup> getMeetupGroups() {
		return meetupGroups;
	}

	public void setMeetupGroups(List<MeetupGroup> meetupGroups) {
		this.meetupGroups = meetupGroups;
	}
	
	public int getNumberOfGroups() {
		
		if(meetupGroups == null) {
			return 0;
		}
		for(MeetupGroup group : meetupGroups) {
			numberOfGroups ++;
		}
		return numberOfGroups;
	}
	
	
}
