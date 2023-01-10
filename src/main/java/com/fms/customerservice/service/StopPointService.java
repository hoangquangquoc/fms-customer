package com.fms.customerservice.service;

import java.util.List;

import com.fms.customerservice.model.StopPoint;
import com.fms.customerservice.model.StopPointDTO;
import com.fms.customerservice.model.StopPointDetails;
import com.fms.module.model.ResponseModel;

public interface StopPointService {
	ResponseModel<List<StopPointDTO>> getAll(Integer pageNumber,Integer pageSize, Integer roleId, Integer provinceId, Integer districtId,
			Integer customerId, Integer customerIdHeader, Integer type, String name);

	String create(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			StopPoint stopPoint);

	String update(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			StopPoint stopPoint);

	String delete(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			Integer stopPointId);

	StopPointDetails getDetails(Integer roleId, Integer provinceId, Integer districtId, Integer customerId,
			Integer stopPointId);
}
