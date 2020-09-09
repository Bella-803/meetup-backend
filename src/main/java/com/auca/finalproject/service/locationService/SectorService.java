package com.auca.finalproject.service.locationService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auca.finalproject.dao.DistrictDao;
import com.auca.finalproject.dao.SectorDao;
import com.auca.finalproject.entity.location.District;
import com.auca.finalproject.entity.location.Sector;

@Service
public class SectorService {

	@Autowired
	private SectorDao sectorDao;
	
	@Autowired
	private DistrictService districtService;
	
	public Sector saveSector(Sector sector, Integer districtId) {
		District district = districtService.findDistrictById(districtId);
		sector.setDistrict(district);
		return sectorDao.save(sector);
	}
	
	public Sector findSectorById(Integer id) {
		Optional<Sector> sector = sectorDao.findById(id);
		
		if(!sector.isPresent()) {
			throw new RuntimeException("Sector Not Found");
		}
		return sector.get();
	}
	
	public List<Sector> findAllSector(){
		return sectorDao.findAll();
	}
}
