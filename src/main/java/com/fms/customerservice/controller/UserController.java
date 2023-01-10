package com.fms.customerservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fms.customerservice.model.Coordinate;
import com.fms.customerservice.model.User;
import com.fms.customerservice.model.UserMapped;
import com.fms.customerservice.model.VehiclesAssign;
import com.fms.customerservice.model.VehiclesAssignDTO;
import com.fms.customerservice.model.responsemodel.Response;
import com.fms.customerservice.service.UserService;
import com.fms.module.model.CheckPermissionUserForm;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

@RestController
@RequestMapping("/v1/users")
public class UserController {
	@Autowired
	UserService userService;

	@GetMapping
	public ResponseModel<List<UserMapped>> findAll(
			@RequestHeader("roleId") Integer roleId,
			@RequestHeader("userId") Integer userId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = "username", defaultValue = Constants.QUERY_VALUE_DEFAULT) String username,
			@RequestParam(name = "phone", defaultValue = Constants.QUERY_VALUE_DEFAULT) String phone,
			@RequestParam(name = "status", defaultValue = "-1") Integer status,
			@RequestParam(name = "registerNo", defaultValue = Constants.QUERY_VALUE_DEFAULT) String registerNo,
			@RequestParam(name = "customerGroup", defaultValue = "-1") Integer customerGroupId,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "username") String orderBy) {

		SearchModel search = new SearchModel(pageSize, pageNumber, username, phone, status, registerNo, orderBy);
		search.setCustomerGroupId(customerGroupId);
		return userService.getAllUsers(search, roleId, provinceId, districtId, customerId, userId, false);
	}

	@GetMapping("/export")
	public ResponseEntity<InputStreamResource> export(
			@RequestHeader(name = "Accept-Language", defaultValue = "vi") String acceptLang,
			@RequestHeader("roleId") Integer roleId,
			@RequestHeader("userId") Integer userId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = "username", defaultValue = Constants.QUERY_VALUE_DEFAULT) String username,
			@RequestParam(name = "phone", defaultValue = Constants.QUERY_VALUE_DEFAULT) String phone,
			@RequestParam(name = "status", defaultValue = "-1") Integer status,
			@RequestParam(name = "registerNo", defaultValue = Constants.QUERY_VALUE_DEFAULT) String registerNo,
			@RequestParam(name = "customerGroup", defaultValue = "-1") Integer customerGroupId,
			@RequestParam(name = "type", defaultValue = "excel") String type,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "username") String orderBy) {

		SearchModel search = new SearchModel(pageSize, pageNumber, username, phone, status, registerNo, orderBy);
		search.setCustomerGroupId(customerGroupId);
		UserHeader userHeader =  new UserHeader(userId, roleId, provinceId, districtId, customerId);
		userHeader.setAcceptLang(acceptLang);
		return userService.export(search, userHeader, type);
	}

	@PostMapping
	public Response createUser(HttpServletRequest request, @RequestHeader("username") String username,
			@RequestHeader("userId") Integer userId, @Valid @RequestBody User user) {
		Response response = new Response();
		response.setMessage(userService.createUser(user, userId, username, request));
		response.setCode(HttpServletResponse.SC_CREATED);
		return response;
	}

	@PutMapping(value = "/{userId}")
	public Response updateUser(HttpServletRequest request, @RequestHeader("username") String username,
			@RequestHeader("userId") Integer id, @PathVariable Integer userId, @Valid @RequestBody User user) {
		user.setUserId(userId);
		Response response = new Response();
		response.setMessage(userService.updateUser(user, id, username, request));
		return response;
	}

	@DeleteMapping(value = "/{userIdDeleted}")
	public Response deleteBy_Id(HttpServletRequest request, @RequestHeader("userId") Integer userId,
			@RequestHeader("username") String username, @PathVariable Integer userIdDeleted) {
		Response response = new Response();
		response.setMessage(userService.deleteUser(userIdDeleted, username, userId, request));
		return response;
	}

	@GetMapping(value = "/{userIdChecked}")
	public Response getUserDetails(@RequestHeader("userId") Integer userId, @PathVariable Integer userIdChecked) {
		Response response = new Response();
		response.setData(userService.getUserDetails(userId, userIdChecked));
		return response;
	}

	@GetMapping(value = "/reset/{_username}")
	public Response ressetPassword(HttpServletRequest request, @RequestHeader("username") String username, @RequestHeader("roleId") Integer roleId,
			@PathVariable String _username) {
		Response response = new Response();
		response.setMessage(userService.resetPassword(_username, roleId, username, request));
		return response;
	}

	@PutMapping(value = "/{userId}/centermaps")
	public ResponseModel<String> setCenterMap(@RequestHeader("username") String userName, @PathVariable Integer userId,
			@RequestBody Coordinate coords) {
		ResponseModel<String> response = new ResponseModel<String>();
		String results = userService.setCenterMap(userName, userId, coords);
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage(results);
		return response;
	}

	@GetMapping(value = "/vehicles")
	public ResponseModel<List<VehiclesAssignDTO>> getVehicles(
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestHeader(value = "roleId", required = false) Integer roleId,
			@RequestHeader(value = "userId", required = false) Integer userIdHeader,
			@RequestParam(name = "userid", defaultValue = "") Integer userId) {
		ResponseModel<List<VehiclesAssignDTO>> response = new ResponseModel<List<VehiclesAssignDTO>>();

		customerId = customerId == null ? -1 : customerId;
		List<VehiclesAssignDTO> data = userService.getVehicles(userId, customerId, roleId, userIdHeader);

		response.setCode(HttpServletResponse.SC_OK);
		response.setData(data);
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));

		return response;
	}

	@PostMapping(value = "/vehicles")
	public ResponseModel<String> assignVehicles(
			HttpServletRequest request,
			@RequestHeader("roleId") Integer roleId,
			@RequestHeader("userId") Integer userId,
			@RequestHeader(value = "username", required = false) String username,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerIdHeader,
			@RequestBody VehiclesAssign vehiclesAssgin) {

		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<String> response = new ResponseModel<String>();
		response.setCode(HttpServletResponse.SC_OK);
		String result = userService.assignVehicles(vehiclesAssgin, userHeader, request);
		response.setMessage(result);
		return response;
	}

	@RequestMapping(value = "/permission", method = RequestMethod.POST)
	public ResponseModel<Boolean> checkPermissionUser(@RequestBody CheckPermissionUserForm checkPermissionUserForm) {
		ResponseModel<Boolean> response = new ResponseModel<Boolean>();
		response.setData(userService.checkPermissionUser(checkPermissionUserForm));
		return response;
	}

}
