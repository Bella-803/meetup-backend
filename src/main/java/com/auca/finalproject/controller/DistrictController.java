package com.auca.finalproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auca.finalproject.dao.DistrictDao;
import com.auca.finalproject.entity.location.District;
import com.auca.finalproject.service.locationService.DistrictService;

@RestController
@RequestMapping("api/location")
@CrossOrigin
public class DistrictController {

	@Autowired
	private DistrictService districtService;
	
	@PostMapping("/district/{provinceId}")
	public ResponseEntity<?> saveDistrict(@RequestBody District district, @PathVariable int provinceId) {
		
		District newDistrict = districtService.saveDistrict(district, provinceId);
		return new ResponseEntity<District>(newDistrict, HttpStatus.CREATED);
	}
	
	@GetMapping("/district")
	public List<District> findAllDistrict(){
		
		List<District> allDistrict = districtService.findAllDistrict();
		return allDistrict;
	}
	
	@GetMapping("/district/{districtId}")
	public ResponseEntity<?> findDistrict(@PathVariable int districtId){
		
		District district = districtService.findDistrictById(districtId);
		return new ResponseEntity<District>(district, HttpStatus.OK);
	}
	
	@GetMapping("/district/province/{provinceId}")
	public List<District> findAllDistrictByProvinceId(@PathVariable Integer provinceId){
		
		List<District> allDistrict = districtService.findAllDistrictByProvinceId(provinceId);
		return allDistrict;
	}
	
	
}

