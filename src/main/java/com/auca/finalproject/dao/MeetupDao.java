package com.auca.finalproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auca.finalproject.entity.Meetup;

public interface MeetupDao extends JpaRepository<Meetup, Integer>{

}
