package com.fms.customerservice.service.ship;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import com.fms.customerservice.model.ship.ShipCustomerDTO;
import com.fms.customerservice.model.ship.ShipCustomerDetailDTO;
import com.fms.customerservice.model.ship.ShipCustomerModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;

public interface ShipCustomerService {
	Page<ShipCustomerDTO> findAllCustomers(SearchModel searchModel, Integer roleId, Integer provinceId,
			Integer districtId, boolean export);
	
	String create(ShipCustomerModel model, UserHeader userHeader, HttpServletRequest request);
	
	ShipCustomerDetailDTO getCustomer(Integer customerId, UserHeader userHeader);
	
	String update(ShipCustomerModel model, UserHeader userHeader, HttpServletRequest request);
	
	String delete(Integer customerId, UserHeader userHeader, HttpServletRequest request);
}
