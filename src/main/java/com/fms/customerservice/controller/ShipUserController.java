package com.fms.customerservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.fms.customerservice.model.VehiclesAssign;
import com.fms.customerservice.model.VehiclesAssignDTO;
import com.fms.customerservice.model.responsemodel.Response;
import com.fms.customerservice.model.ship.ChangePassForm;
import com.fms.customerservice.model.ship.ShipUserMapped;
import com.fms.customerservice.model.ship.ShipUserModel;
import com.fms.customerservice.service.ship.ShipUserService;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;
@RestController
@RequestMapping("/v1/ship/users")
public class ShipUserController {
	@Autowired
	ShipUserService userService;

	//Tim kiem
	@GetMapping
	public ResponseModel<List<ShipUserMapped>> findAll(
			@RequestHeader("roleId") Integer roleId,
			@RequestHeader("userId") Integer userId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = "userName", defaultValue = Constants.QUERY_VALUE_DEFAULT) String username,
			@RequestParam(name = "phone", defaultValue = Constants.QUERY_VALUE_DEFAULT) String phone,
			@RequestParam(name = "fullName", defaultValue = Constants.QUERY_VALUE_DEFAULT) String fullname,
			@RequestParam(name = "userRoleId", defaultValue = "-1") Integer searchRoleId,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "username") String orderBy) {

		SearchModel search = new SearchModel(searchRoleId, pageSize,pageNumber, fullname, username, phone);
		return userService.getAllUsers(search, roleId, provinceId, districtId, customerId, userId);
	}
	
	//Them moi
	@PostMapping
	public Response createUser(HttpServletRequest request, @RequestHeader("username") String username,
			@RequestHeader("userId") Integer userId, @RequestBody ShipUserModel user) {
		Response response = new Response();
		response.setMessage(userService.createUser(user, userId, username, request));
		response.setCode(HttpServletResponse.SC_CREATED);
		return response;
	}
	
	//Cap nhat
	@PutMapping(value = "/{userId}")
	public Response updateUser(HttpServletRequest request, @RequestHeader("username") String username,
			@RequestHeader("userId") Integer id, @PathVariable Integer userId, @RequestBody ShipUserModel user) {
		user.setUserId(userId);
		Response response = new Response();
		response.setMessage(userService.updateUser(user, id, username, request));
		return response;
	}
	
	//Chi tiet
	@GetMapping(value = "/{userIdChecked}")
	public Response getUserDetails(@RequestHeader("userId") Integer userId, @PathVariable Integer userIdChecked) {
		Response response = new Response();
		response.setData(userService.getUserDetails(userId, userIdChecked));
		return response;
	}
	
	//Xoa
	@DeleteMapping(value = "/{userIdDeleted}")
	public Response deleteBy_Id(HttpServletRequest request, @RequestHeader("userId") Integer userId,
			@RequestHeader("username") String username, @PathVariable Integer userIdDeleted) {
		Response response = new Response();
		response.setMessage(userService.deleteUser(userIdDeleted, username, userId, request));
		return response;
	}
	
	//Lay danh sach xe phan quyen giam sat
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

	//Phan quyen giam sat
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
	
	//doi mat khau cho danh sach user
	@RequestMapping(value = "/changepasswords", method = RequestMethod.PUT)
	public Response mobileChangePassword(@RequestBody ChangePassForm changePasswordForm, HttpServletRequest request) {
		Response response = new Response();
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage(userService.changePassword(changePasswordForm,request));
		return response;
	}
}
