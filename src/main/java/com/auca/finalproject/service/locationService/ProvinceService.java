package com.auca.finalproject.service.locationService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auca.finalproject.dao.ProvinceDao;
import com.auca.finalproject.entity.location.Province;

@Service
public class ProvinceService {

	@Autowired
	private ProvinceDao provinceDao;
	
	public Province saveProvince(Province province) {
		
		return provinceDao.save(province);
	}
	
	public Province findProvinceById(Integer id) {
		Optional<Province> provinces = provinceDao.findById(id);
		
		if(!provinces.isPresent()) {
			throw new RuntimeException("Province Not found");
		}
		return provinces.get();
		
	}
	
	public List<Province> findAllProvince(){
		return provinceDao.findAll();
	}
	
	public Province updateProvince(Province province) {
		if(province.getId() != null) {
			if(provinceDao.existsById(province.getId())) {
				return provinceDao.save(province);
			}
			else {
				throw new RuntimeException("Province Id : "+province.getId()+" does not exists");
			}
		}else {
			throw new RuntimeException("Can not update the province because it does not exists");
		}
	}
	
	public void deleteProvince(Integer id) {
		Province province = findProvinceById(id);
		provinceDao.delete(province);
		
	}
}
