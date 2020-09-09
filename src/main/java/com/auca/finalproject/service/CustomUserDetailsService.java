package com.auca.finalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auca.finalproject.dao.UserDao;
import com.auca.finalproject.entity.UserAccount;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
       UserAccount user = userDao.findByUsername(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("User Not Found");
		}
		return user;
	}
	
	@Transactional
	public UserAccount loadUserById(Integer id) {
		UserAccount user = userDao.getById(id);
		if(user == null) {
			throw new UsernameNotFoundException("User Not Found");
		}
		return user;
		
	}

}
