package com.auca.finalproject.entity.location;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.auca.finalproject.entity.MeetupGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Sector {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(unique = true)
	private String name;
	
	
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,
			              CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "district_id")
	private District district;
	
	@JsonIgnore
	@OneToMany(mappedBy = "sector", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	private List<MeetupGroup> meetupGroups;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public List<MeetupGroup> getMeetupGroups() {
		return meetupGroups;
	}

	public void setMeetupGroups(List<MeetupGroup> meetupGroups) {
		this.meetupGroups = meetupGroups;
	}
	
	
	
	
}
