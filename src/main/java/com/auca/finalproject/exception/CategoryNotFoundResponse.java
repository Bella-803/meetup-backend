package com.auca.finalproject.exception;

public class CategoryNotFoundResponse {

	private String categoryNotFound;

	public CategoryNotFoundResponse(String categoryNotFound) {
		this.categoryNotFound = categoryNotFound;
	}

	public String getCategoryNotFound() {
		return categoryNotFound;
	}

	public void setCategoryNotFound(String categoryNotFound) {
		this.categoryNotFound = categoryNotFound;
	}
	
	
}
