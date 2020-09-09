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

import com.auca.finalproject.entity.location.Sector;
import com.auca.finalproject.service.locationService.SectorService;

@RestController
@RequestMapping("api/location")
@CrossOrigin
public class SectorController {

	@Autowired
	private SectorService sectorService;
	
	@PostMapping("/sector/{districtId}")
	public ResponseEntity<?> saveSector(@RequestBody Sector sector, @PathVariable int districtId){
		
		Sector newSector = sectorService.saveSector(sector, districtId);
		return new ResponseEntity<Sector>(newSector, HttpStatus.CREATED);
	}
	
	@GetMapping("/sector/{sectorId}")
	public ResponseEntity<?> findSector(@PathVariable int sectorId){
		
		Sector sector = sectorService.findSectorById(sectorId);
		return new ResponseEntity<Sector>(sector, HttpStatus.CREATED);
	}
	
	@GetMapping("/sector")
	public List<Sector> findAllSector(){
		
		List<Sector> allSector = sectorService.findAllSector();
		return allSector;
	}
}
