package com.fms.customerservice.service.ship;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.fms.customerservice.model.VehiclesAssign;
import com.fms.customerservice.model.VehiclesAssignDTO;
import com.fms.customerservice.model.ship.ChangePassForm;
import com.fms.customerservice.model.ship.ShipUserDetailsDTO;
import com.fms.customerservice.model.ship.ShipUserMapped;
import com.fms.customerservice.model.ship.ShipUserModel;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;

public interface ShipUserService {	
	
	ResponseModel<List<ShipUserMapped>> getAllUsers(SearchModel searchModel, Integer roleId, Integer provinceId, Integer districtId, Integer customerId, Integer userId);

	String createUser(ShipUserModel user, Integer userIdHeader, String username, HttpServletRequest request);

	String updateUser(ShipUserModel user, Integer currentUserId, String username, HttpServletRequest request);

	String deleteUser(Integer userIdDeleted, String username, Integer userId, HttpServletRequest request);

	ShipUserDetailsDTO getUserDetails(Integer userId, Integer userIdCheck);
	
	List<VehiclesAssignDTO> getVehicles(Integer userId, Integer customerId, Integer roleId, Integer userIdHeader);

	String assignVehicles(VehiclesAssign vehiclesAssign, UserHeader userHeader, HttpServletRequest request);

	String changePassword(ChangePassForm changePasswordForm, HttpServletRequest request);

}
