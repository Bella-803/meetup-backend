package com.auca.finalproject.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.auca.finalproject.dao.GroupDao;
import com.auca.finalproject.dao.UserDao;
import com.auca.finalproject.entity.Category;
import com.auca.finalproject.entity.ERoles;
import com.auca.finalproject.entity.MeetupGroup;
import com.auca.finalproject.entity.UserAccount;
import com.auca.finalproject.entity.location.Sector;
import com.auca.finalproject.exception.CategoryNotFoundException;
import com.auca.finalproject.exception.ImageStorageException;
import com.auca.finalproject.exception.MeetupGroupNotFoundException;
import com.auca.finalproject.service.locationService.SectorService;


@Service
public class GroupService {
	
	@Value("${upload.group-path}")
	private String path;

	@Autowired
	private GroupDao groupDao;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SectorService sectorService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private EmailService emailService;
 
	public MeetupGroup createGroup(MeetupGroup meetupGroup, int categoryId,int sectorId, String username) throws Exception {
		
		UserAccount groupAdmin = userDao.findByUsername(username);
		
		Category category = categoryService.findCategoryById(categoryId);
		
		Sector sector = sectorService.findSectorById(sectorId);
		
		String catName = category.getCategoryName();
		
		meetupGroup.setCategory(category);
		meetupGroup.setGroupAdmin(groupAdmin);
		meetupGroup.setGroupAdminName(groupAdmin.getFullname());
		meetupGroup.setSector(sector);
		meetupGroup.setLocation(sector.getName());
		
		//add group admin role to the user after checking if he is admin
		if(!groupAdmin.geteRoles().equals(ERoles.ADMIN))
		groupAdmin.seteRoles(ERoles.GROUP_ADMIN);
		
		MeetupGroup group = groupDao.save(meetupGroup);
		
		//userService.updateUser(groupAdmin);
        userService.updateUser(groupAdmin); 
		
   
		return group;
		}
		
		
	public MeetupGroup findGroupById(int id) {
		Optional<MeetupGroup> groups = groupDao.findById(id);
		
		if(!groups.isPresent()) {
			throw new MeetupGroupNotFoundException("Group of ID : "+id+" does not exist");
		}
		return groups.get();
	}
	
	public MeetupGroup findGroupById(int id, String username) {
		Optional<MeetupGroup> groups = groupDao.findById(id);
		
		if(!groups.isPresent()) {
			throw new MeetupGroupNotFoundException("Group of ID : "+id+" does not exist");
		}
		MeetupGroup group = groups.get();
		if(!group.getGroupAdmin().getUsername().equals(username)) {
			throw new MeetupGroupNotFoundException("You do not have a group named : "+group.getGroupName());
		}
		return group;
	}
	
	public List<MeetupGroup> findAllGroup() {
		return groupDao.findAll();	
	}
	
	public void deleteGroup(int groupId, String username) {
		UserAccount groupAdmin = userDao.findByUsername(username);
	   MeetupGroup group = findGroupById(groupId);
	   
	   if(group.getGroupAdmin().getUsername().equals(groupAdmin.getUsername()) || (groupAdmin.geteRoles().equals(ERoles.ADMIN))) {
		   groupDao.delete(group);
	   }
	   else {
		   throw new RuntimeException("You are not allowed to delete this group");
	   }
	   
	}

	public List<MeetupGroup> findAllGroup(int categoryId) {
		
		Category category = categoryService.findCategoryById(categoryId);
		return category.getMeetupGroups();

	}
	
	public MeetupGroup updateGroup(MeetupGroup group, int catId, String username) { 
	
		try {
			if(group.getId() != null) {
				MeetupGroup existingGroup = findGroupById(group.getId());
				
				if(existingGroup.getCategory().getId() == catId) {
					if(!existingGroup.getGroupAdmin().getUsername().equals(username)) {
						throw new RuntimeException("You are not allowed to update this group");
					}
					group.setCategory(existingGroup.getCategory()); 
					group.setGroupAdmin(existingGroup.getGroupAdmin());
					group.setSector(existingGroup.getSector());
					group.setLocation(existingGroup.getLocation());
					group.setGroupAdminName(existingGroup.getGroupAdmin().getFullname());
			        group.setMembers(existingGroup.getMembers());
					groupDao.save(group);
					return group;
				}else {
					throw new CategoryNotFoundException("The group you are updating does not belong to the given category");
				}
			}else {
				throw new MeetupGroupNotFoundException("The group you are trying to update does not exists. ");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	public String joinGroup(Integer groupId,String username) {
		try {
			UserAccount user = userDao.findByUsername(username);
			MeetupGroup group = findGroupById(groupId);
			//Check if the user is not already a member
			int check = 0;
			for(UserAccount userAcc : group.getMembers()) {
				if(user.getId() == userAcc.getId()) {
					check ++;
				}
			}
			if(check == 0) {
				group.getMembers().add(user);
				groupDao.save(group);
				
				String message = "Hi "+user.getFullname()+", \n\n Welcome to your new Meetup Group.\n\n ";
				String to = user.getEmail();
				String subject = "Welcome";
				
				emailService.sendEmail(message, subject, to);
			}
			
			return "Successfully joined the group";
			
		} catch (Exception e) {
			e.printStackTrace();
           throw new RuntimeException("You can not join the group! Try again later :"+e.getMessage());
		}
	}
	
	public String uploadGroupPhoto(MultipartFile file, Integer groupId, String username) {
		
		MeetupGroup group = findGroupById(groupId);
		
		if(!group.getGroupAdmin().getUsername().equals(username)) {
			throw new RuntimeException("You are not allowed to upload an image to this group");
		}
		
		if(file.isEmpty()) {
			throw new ImageStorageException("Could not save an empty file");
		}
		
		String originalFilename = file.getOriginalFilename();
		String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String newFilename = group.getId()+"_groupImage"+fileExtension;
		
		try {
			
			InputStream bytes = file.getInputStream();
			Files.copy(bytes, Paths.get(path + newFilename), StandardCopyOption.REPLACE_EXISTING);
			
			group.setPhotoPath("http://localhost:8080/api/groups/image/"+newFilename);
			Integer catId = group.getCategory().getId();
			updateGroup(group, catId, username);
			
			return "Group Image successfully saved";
			
		} catch (Exception e) {
			throw new ImageStorageException("Failed to save image. Try again later");
		}	
		
	}
	
	public int getNumberOfGroups(String username) {
		UserAccount admin = userDao.findByUsername(username);
		
		if(!admin.geteRoles().equals(ERoles.ADMIN)) {
			throw new RuntimeException("You are not allowed to perform this operation");
		}
		
		List<MeetupGroup> allGroups = findAllGroup();
		return allGroups.size();
		
	}


	public String removerMember(int groupId, int userId, String username) {
		
		try {
			UserAccount groupAdmin = userDao.findByUsername(username);
			MeetupGroup group = findGroupById(groupId);
			UserAccount user = userService.findUserById(userId);
			
			if(group.getGroupAdmin().getUsername().equals(groupAdmin.getUsername())) {
			  group.getMembers().remove(user);	
			  updateGroup(group, group.getCategory().getId(), username);
			}
			else {
				throw new RuntimeException("You are not allowed to remove member.You are not the group admin");
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Failed to remove member.Try again later");
		}
		return "Member successfully deleted";
	}
	
}
