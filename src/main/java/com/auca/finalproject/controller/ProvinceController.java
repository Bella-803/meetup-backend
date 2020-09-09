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

import com.auca.finalproject.entity.location.Province;
import com.auca.finalproject.service.locationService.ProvinceService;

@RestController
@RequestMapping("api/location")
@CrossOrigin
public class ProvinceController {

	@Autowired
	private ProvinceService provinceService;
	
	@PostMapping("/province")
	public ResponseEntity<?> saveProvince(@RequestBody Province province){
		Province newProvince = provinceService.saveProvince(province);
		return new ResponseEntity<Province>(newProvince, HttpStatus.CREATED);
	}
	
	@GetMapping("/province")
	public List<Province> getAllProvince(){
		List<Province> allProvince = provinceService.findAllProvince();
		return allProvince;
	}
	
	@GetMapping("/province/{provinceId}")
	public ResponseEntity<?> getProvinceById(@PathVariable int provinceId){
		
		Province province = provinceService.findProvinceById(provinceId);
		return new ResponseEntity<Province>(province, HttpStatus.CREATED);
	}
	
}
