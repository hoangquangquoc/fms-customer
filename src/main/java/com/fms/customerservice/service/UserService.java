package com.fms.customerservice.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import com.fms.customerservice.model.Coordinate;
import com.fms.customerservice.model.User;
import com.fms.customerservice.model.UserDetailsDTO;
import com.fms.customerservice.model.UserMapped;
import com.fms.customerservice.model.VehiclesAssign;
import com.fms.customerservice.model.VehiclesAssignDTO;
import com.fms.module.model.CheckPermissionUserForm;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;

public interface UserService {
	String createUser(User user, Integer userIdHeader, String username, HttpServletRequest request);

	String updateUser(User user, Integer currentUserId, String username, HttpServletRequest request);

	String deleteUser(Integer userIdDeleted, String username, Integer userId, HttpServletRequest request);

	UserDetailsDTO getUserDetails(Integer userId, Integer userIdCheck);

	String setCenterMap(String userName, Integer userId, Coordinate coords);

	String resetPassword(String _username, Integer roleId, String username, HttpServletRequest request);

	List<VehiclesAssignDTO> getVehicles(Integer userId, Integer customerId, Integer roleId, Integer userIdHeader);

	String assignVehicles(VehiclesAssign vehiclesAssign, UserHeader userHeader, HttpServletRequest request);

	ResponseModel<List<UserMapped>> getAllUsers(SearchModel searchModel, Integer roleId, Integer provinceId, Integer districtId, Integer customerId, Integer userId, boolean export);
	
	ResponseEntity<InputStreamResource> export(SearchModel searchModel, UserHeader userHeader, String type);
	
	// kiem tra userid co thuoc quan ly cua user hien tai
	Boolean checkPermissionUser(CheckPermissionUserForm checkPermissionUserForm);
}
