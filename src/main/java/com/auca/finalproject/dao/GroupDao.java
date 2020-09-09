package com.auca.finalproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;


import com.auca.finalproject.entity.MeetupGroup;

public interface GroupDao extends JpaRepository<MeetupGroup, Integer>{

	public MeetupGroup findByGroupName(String groupName);
}
