package com.fms.customerservice.service;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.fms.customerservice.model.Driver;
import com.fms.customerservice.model.DriverCustomerDTO;
import com.fms.customerservice.model.DriverDTO;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;

public interface DriverService {
	String createDriver(Driver newDriver, String username);

	String updateDriver(Integer driverId, Driver updateDriver, String username);

	String deleteByDriverId(Integer driverId, String username);

	Page<DriverDTO> findAll(SearchModel searchModel, UserHeader userHeader, boolean export);

	ResponseEntity<InputStreamResource> export(SearchModel searchModel, UserHeader userHeader, String type);
	
	Driver findByDriverId(Integer driverId);
	
	List<DriverCustomerDTO> getDriverOfCustomer(Integer customerId, Integer roleId, Integer customerIdLogin, Integer provinceId, Integer districtId);
}
