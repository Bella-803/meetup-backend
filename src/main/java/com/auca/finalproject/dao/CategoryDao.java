package com.auca.finalproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auca.finalproject.entity.Category;

public interface CategoryDao extends JpaRepository<Category, Integer>{

	public Category findByCategoryName(String categoryName);
	
}
