package com.fms.customerservice.service;

import java.util.List;

import com.fms.customerservice.model.MatchRouteDTO;
import com.fms.customerservice.model.RouteDTO;
import com.fms.customerservice.model.RouteDetailsDTO;
import com.fms.customerservice.model.RouteRequest;
import com.fms.customerservice.model.RouteVehicleWarning;
import com.fms.customerservice.model.TollgateDTO;
import com.fms.customerservice.model.TransportDTO;
import com.fms.customerservice.model.VehicleWarningRoute;
import com.fms.customerservice.model.VehiclesAssignRoute;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;

public interface RouteService {
	ResponseModel<List<RouteDTO>> getAll(SearchModel search, Integer roleId, Integer provinceId, Integer districtId,
			Integer customerId, Integer customerIdHeader, String name, Integer totalType, Integer routeType);
	
	List<MatchRouteDTO> matchRoute(SearchModel search, UserHeader userHeader, Integer customerIdParam, String fromDate, String toDate, Integer routeId, Integer transportId);

	String create(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			RouteRequest route);

	String update(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			RouteRequest route);

	String delete(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			Integer routeId);

	RouteDetailsDTO getDetails(Integer roleId, Integer provinceId, Integer districtId, Integer customerId,
			Integer routeId);

	List<TransportDTO> getVehicleOfRoute(Integer routeId, UserHeader userHeader);

	String assignVehicles(VehiclesAssignRoute assignVehicles, UserHeader userHeader);
	
	List<TollgateDTO> getAllTollgate();
	
	String warningVehicles(VehicleWarningRoute warningVehicles, UserHeader userHeader);
	
	RouteVehicleWarning getVehicleWarningRoute(Integer routeId, UserHeader userHeader);
}
