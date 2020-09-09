package com.auca.finalproject.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.auca.finalproject.dao.UserDao;
import com.auca.finalproject.entity.Category;
import com.auca.finalproject.entity.ERoles;
import com.auca.finalproject.entity.UserAccount;
import com.auca.finalproject.exception.EmailAlreadyExistsException;
import com.auca.finalproject.exception.ImageStorageException;
import com.auca.finalproject.exception.UserNotFoundException;
import com.auca.finalproject.exception.UsernameAlreadyExistException;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncode;
	
	@Value("${upload.user-path}")
	private String path;
	
	public UserAccount saveUser(UserAccount userAccount) {
		
		UserAccount userAcc = userDao.findByEmail(userAccount.getEmail());
		if(userAcc != null) {
			throw new EmailAlreadyExistsException("Email already exists");
		}
		UserAccount userAccByUsername = userDao.findByUsername(userAccount.getUsername());
		if(userAccByUsername != null) {
			throw new UsernameAlreadyExistException("Username Already Exists");
		}
		try {
			
			String newPassword = bcryptPasswordEncode.encode(userAccount.getPassword());
			userAccount.setPassword(newPassword);
			userAccount.seteRoles(ERoles.USER);
			userAccount.setConfirmPassword("");
			return userDao.save(userAccount);
		} catch (Exception e) {
			throw new RuntimeException("Could not register the account: " + e.getMessage());
		}
			 
	}
	
	public UserAccount findUserById(int id) {
		
		Optional<UserAccount> user = userDao.findById(id);
		
		if(!user.isPresent()) {
			throw new UserNotFoundException("User Not Found");
		}
		UserAccount uac = user.get();
		return uac;
	}
	
	public List<UserAccount> findAllUser(){
		return userDao.findAll();
	}
	
	public UserAccount updateUser(UserAccount userAcc) {
		UserAccount existingUserAc = new UserAccount();
		if(userAcc.getId() != null) {
			existingUserAc = findUserById(userAcc.getId());
			if(existingUserAc != null) {
				existingUserAc = userAcc;
				userDao.save(existingUserAc);
			}
		}
		else {
			throw new UserNotFoundException("Could not update User. User not exists");
		}
		
		return existingUserAc;
	}
	
	public UserAccount setAdmin(UserAccount admin) {
		UserAccount userAcc = userDao.findByEmail(admin.getEmail());
		if(userAcc != null) {
			throw new EmailAlreadyExistsException("Email already exists");
		}
		UserAccount userAccByUsername = userDao.findByUsername(admin.getUsername());
		if(userAccByUsername != null) {
			throw new UsernameAlreadyExistException("Username Already Exists");
		}
		try {
			
			String newPassword = bcryptPasswordEncode.encode(admin.getPassword());
			admin.setPassword(newPassword);
			admin.seteRoles(ERoles.ADMIN);
			admin.setConfirmPassword("");
			return userDao.save(admin);
		} catch (Exception e) {
			throw new RuntimeException("Could not register the account: " + e.getMessage());
		}
	}

	public UserAccount findByUsername(String username) {
		UserAccount user = userDao.findByUsername(username);
		return user;
	}
	
	public String deleteUser(Integer userId, String username) {
		UserAccount admin = findByUsername(username);
		if(!(admin.geteRoles().equals(ERoles.ADMIN))) {
			throw new RuntimeException("You are not allowed to perform this operation");
		}
		UserAccount user = findUserById(userId);
		userDao.delete(user);
		return "User successfully deleted";
	}
	
	public String uploadUserProfile(MultipartFile file, String username) {
		
		if(file.isEmpty()) {
			throw new ImageStorageException("Could not save an empty file");
		}
		
		UserAccount user = findByUsername(username);
		try {
			
			String originalFilename = file.getOriginalFilename();
			String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
			
			String newFilename = user.getId()+"_profile"+fileExtension;
			
			InputStream bytes = file.getInputStream();
			
			Files.copy(bytes, Paths.get(path + newFilename), StandardCopyOption.REPLACE_EXISTING);
			
			user.setProfilePhotoPath("http://localhost:8080/api/users/profile/"+newFilename);
			
			updateUser(user);
			
			return "file uploaded successfully";
			
		} catch (Exception e) {
			throw new ImageStorageException("Failed to store image. Try again later");
		}
		
	}
	
	public Integer getNumberOfUsers(String username) {
		UserAccount admin = userDao.findByUsername(username);
		
		if(!admin.geteRoles().equals(ERoles.ADMIN)) {
			throw new RuntimeException("You are not allowed to perform this operation");
		}
		
		List<UserAccount> allUsers = findAllUser();
		return allUsers.size();
		
	}
}
