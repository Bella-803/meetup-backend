package com.auca.finalproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auca.finalproject.entity.UserAccount;

public interface UserDao extends JpaRepository<UserAccount, Integer>{
	
   public UserAccount findByEmail(String email);
   public UserAccount findByUsername(String username);
   public UserAccount getById(Integer id);
   
}
