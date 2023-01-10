package com.fms.customerservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fms.customerservice.model.MatchRouteDTO;
import com.fms.customerservice.model.RouteDTO;
import com.fms.customerservice.model.RouteDetailsDTO;
import com.fms.customerservice.model.RouteRequest;
import com.fms.customerservice.model.RouteVehicleWarning;
import com.fms.customerservice.model.TransportDTO;
import com.fms.customerservice.model.VehicleWarningRoute;
import com.fms.customerservice.model.VehiclesAssignRoute;
import com.fms.customerservice.service.RouteService;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

@RestController
@RequestMapping("/v1/routes")
public class RouteController {
	@Autowired
	RouteService routeService;

	/*
	 * lay danh sach lo trinh
	 */
	@GetMapping
	public ResponseModel<List<RouteDTO>> getAll(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerIdHeader,
			@RequestParam(name = "customerId", defaultValue = "-1") Integer customerId,
			@RequestParam(name = "totalType", defaultValue = "0") Integer totalType,
			@RequestParam(name = "routeType", defaultValue = "-1") Integer routeType,
			@RequestParam(name = "name", defaultValue = Constants.QUERY_VALUE_DEFAULT) String name,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "name") String orderBy) {

		SearchModel search = new SearchModel(pageSize, pageNumber, orderBy);
		name = Utils.decodeParam(name);
		return routeService.getAll(search, roleId, provinceId, districtId, customerId, customerIdHeader, name,
				totalType, routeType);
	}

	/*
	 * them moi lo trinh
	 */
	@PostMapping
	public ResponseModel<String> create(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestHeader(value = "username") String username, @Valid @RequestBody RouteRequest object) {

		ResponseModel<String> response = new ResponseModel<>();
		String message = routeService.create(roleId, provinceId, districtId, customerId, username, object);
		response.setMessage(message);
		response.setCode(Constants.SC_CREATED);
		return response;
	}

	/*
	 * sua lo trinh
	 */
	@PutMapping("/{id}")
	public ResponseModel<String> update(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestHeader(value = "username") String username, @PathVariable Integer id,
			@Valid @RequestBody RouteRequest routeRequest) {

		ResponseModel<String> response = new ResponseModel<>();
		routeRequest.setRouteId(id);
		String message = routeService.update(roleId, provinceId, districtId, customerId, username, routeRequest);
		response.setMessage(message);
		return response;
	}

	/*
	 * xoa lo trinh
	 */
	@DeleteMapping("/{id}")
	public ResponseModel<String> delete(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestHeader(value = "username") String username, @PathVariable Integer id) {

		ResponseModel<String> response = new ResponseModel<>();
		String message = routeService.delete(roleId, provinceId, districtId, customerId, username, id);
		response.setMessage(message);
		return response;
	}

	/*
	 * lay thong tin chi tiet lo trinh
	 */
	@GetMapping("/{id}")
	public ResponseModel<RouteDetailsDTO> getDetails(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId, @PathVariable Integer id) {
		ResponseModel<RouteDetailsDTO> response = new ResponseModel<RouteDetailsDTO>();
		RouteDetailsDTO item = routeService.getDetails(roleId, provinceId, districtId, customerId, id);
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(item);
		return response;
	}

	/*
	 * lay danh sach xe cua lo trinh
	 */
	@GetMapping("/vehicles/{id}")
	public ResponseModel<List<TransportDTO>> getVehiclesOfRoute(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "userId") Integer userId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId, @PathVariable Integer id) {
		UserHeader userHeader = new UserHeader(userId, roleId, provinceId, districtId, customerId);
		ResponseModel<List<TransportDTO>> response = new ResponseModel<List<TransportDTO>>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(routeService.getVehicleOfRoute(id, userHeader));
		return response;
	}

	/*
	 * gan xe vao lo trinh
	 */
	@PostMapping("/vehicles")
	public ResponseModel<String> assignVehicles(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "userId") Integer userId, @RequestHeader(value = "username") String username,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestBody VehiclesAssignRoute assignVehicles) {
		ResponseModel<String> response = new ResponseModel<String>();
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerId);
		String message = routeService.assignVehicles(assignVehicles, userHeader);
		response.setMessage(message);
		return response;
	}

	@GetMapping("/matchroute")
	public ResponseModel<List<MatchRouteDTO>> matchRoute(
			HttpServletRequest request,
			@RequestHeader(value = "userId") Integer userId,
			@RequestHeader(value = "username") String username,
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestParam(name = "routeId", required = false) Integer routeId,
			@RequestParam(name = "transportId", required = false) Integer transportId,
			@RequestParam(name = "customerId", required = false) Integer customerIdParam,
			@RequestParam(name = "fromDate", defaultValue = Constants.QUERY_VALUE_DEFAULT) String fromDate,
			@RequestParam(name = "toDate", defaultValue = Constants.QUERY_VALUE_DEFAULT) String toDate,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "name") String orderBy) {

		SearchModel search = new SearchModel(pageSize, pageNumber, orderBy);
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerId);
		userHeader.setRequest(request);
		ResponseModel<List<MatchRouteDTO>> response = new ResponseModel<>();
		List<MatchRouteDTO> data = routeService.matchRoute(search, userHeader, customerIdParam, fromDate, toDate,
				routeId, transportId);
		response.setData(data);
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));

		return response;
	}
	
	/*
	 * canh bao xe di lech lo trinh
	 */
	@PostMapping("/vehicles/warning")
	public ResponseModel<String> warningVehicles(
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "userId") Integer userId, 
			@RequestHeader(value = "username") String username,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestBody VehicleWarningRoute warningVehicle) {
		ResponseModel<String> response = new ResponseModel<String>();
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerId);
		String message = routeService.warningVehicles(warningVehicle, userHeader);
		response.setMessage(message);
		return response;
	}
	
	@GetMapping("/vehicles/warning/{routeId}")
	public ResponseModel<RouteVehicleWarning> getWarningRoute(
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "userId") Integer userId, 
			@RequestHeader(value = "username") String username,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@PathVariable(value = "routeId") Integer routeId) {
		ResponseModel<RouteVehicleWarning> response = new ResponseModel<RouteVehicleWarning>();
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerId);
		response.setData(routeService.getVehicleWarningRoute(routeId, userHeader));
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		return response;
	}
	
}
