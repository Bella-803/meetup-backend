package com.auca.finalproject.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.auca.finalproject.entity.Category;
import com.auca.finalproject.service.CategoryService;
import com.auca.finalproject.service.MapValidationErrorService;


@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	MapValidationErrorService mapValidationErrorService;
	
	@PostMapping("")
	public ResponseEntity<?> createCategory(@Valid @RequestBody Category category,
			                                BindingResult result, Principal principal) {
		
		ResponseEntity<?> mapError = mapValidationErrorService.mapValidationError(result);
		if(mapError != null) {
			return mapError;
		}
		Category cat = categoryService.createOrUpdateCategory(category, principal.getName());
		return  new ResponseEntity<Category>(cat, HttpStatus.OK);
	}
	
	
	@GetMapping("/{catId}")
	public Category findCatById(@PathVariable int catId) {
		
		Category cat = categoryService.findCategoryById(catId);
		return cat;
	}
	
	@GetMapping("")
	public  List<Category> findAllCat(){
		
		List<Category> cats = categoryService.findAllCategory();	
		
		return cats;
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable int categoryId, Principal principal){
		categoryService.deleteCategory(categoryId, principal.getName());
		return new ResponseEntity<String>("Category deleted successfully",HttpStatus.OK);
	}
	
	@PostMapping(value = "/upload/image/{catId}", consumes = {"multipart/form-data"})
	public ResponseEntity<?> uploadCategoryImage(@PathVariable Integer catId,@RequestParam MultipartFile file,
			                                     Principal principal){
		String msg = categoryService.uploadCategoryImage(file, catId, principal.getName());
		
		return new ResponseEntity<String>(msg, HttpStatus.OK);
	}
	
	@GetMapping(value = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> getCategoryImage(@PathVariable String filename) throws IOException{
		
		ClassPathResource imgFile = new ClassPathResource("image/category/"+filename);
		byte [] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
		
		return ResponseEntity.ok()
				             .contentType(MediaType.IMAGE_JPEG)
				             .body(bytes);
		
	}

}
