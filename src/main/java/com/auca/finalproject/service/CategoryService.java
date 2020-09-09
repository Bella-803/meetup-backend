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

import com.auca.finalproject.dao.CategoryDao;
import com.auca.finalproject.dao.UserDao;
import com.auca.finalproject.entity.Category;
import com.auca.finalproject.entity.ERoles;
import com.auca.finalproject.entity.UserAccount;
import com.auca.finalproject.exception.CategoryNotFoundException;
import com.auca.finalproject.exception.ImageStorageException;

@Service
public class CategoryService {

	@Autowired
	private CategoryDao categoryDao;
	
	@Autowired
	private UserDao userDao;
	
	@Value("${upload.category-path}")
	private String path;
	
	//Save or update a category
	public Category createOrUpdateCategory(Category category, String username) {
		
		UserAccount admin = userDao.findByUsername(username);
		if(!admin.geteRoles().equals(ERoles.ADMIN)) {
			throw new RuntimeException("You are not allowed to perform this action");
		}
		
		if(category.getId() != null) {
			
			if(!categoryDao.existsById(category.getId())) {
				throw new CategoryNotFoundException("You can not update this category because it does not exists");
			}
		}
		category.setAdmin(admin);
		Category cat = categoryDao.save(category);
		
		return cat;
	}
	
	//Find a category by id
	public Category findCategoryById(int id) {
		Optional<Category> category = categoryDao.findById(id);
		
		if(!category.isPresent()) {
			throw new CategoryNotFoundException("Category Id: "+id+" does not exist");
		}
		return category.get();
	}
	
	//Find all categories
	public List<Category> findAllCategory(){
		return categoryDao.findAll();
	}

	//delete a category
	public void deleteCategory(int categoryId, String username) {
		
		UserAccount admin = userDao.findByUsername(username);
		if(!admin.geteRoles().equals(ERoles.ADMIN)) {
			throw new RuntimeException("You are not allowed to perform this action");
		}
        Category category = findCategoryById(categoryId);  
        categoryDao.delete(category);
	}

	//Find a category by name
	public Category findCategoryByName(String categoryName) {
	
		Category category = categoryDao.findByCategoryName(categoryName);
		
		if(category == null) {
			throw new CategoryNotFoundException("Category : '"+categoryName+"' does not exists. Check Well the spelling and Condisider Capital Letters");
		}
		return category;
	}
	
	public String uploadCategoryImage(MultipartFile file, Integer categoryId, String username) {
		
		if(file.isEmpty()) {
			throw new ImageStorageException("Could not save an empty file");
		}
		
		Category categ = findCategoryById(categoryId);
		
		String originalFilename = file.getOriginalFilename();
		String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String newFilename = categ.getId()+"_categoryPic"+fileExtension;
		try {
			InputStream bytes = file.getInputStream();
			Files.copy(bytes, Paths.get(path + newFilename), StandardCopyOption.REPLACE_EXISTING);
			
			categ.setPhotoPath("http://localhost:8080/api/categories/image/"+newFilename);
			createOrUpdateCategory(categ, username);
			
			return "Category Image successfully saved";
			
		} catch (Exception e) {
			throw new ImageStorageException("Failed to save image. Try again later");
		}
		
	}
	
	public int getNumberOfCategory(String username) {
		UserAccount admin = userDao.findByUsername(username);
		
		if(!admin.geteRoles().equals(ERoles.ADMIN)) {
			throw new RuntimeException("You are not allowed to perform this operation");
		}
		
		List<Category> allCategory = findAllCategory();
		return allCategory.size();
		
	}
		

}
