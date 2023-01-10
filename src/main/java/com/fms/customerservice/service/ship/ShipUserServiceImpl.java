package com.fms.customerservice.service.ship;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.builder.DiffResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.fms.customerservice.client.CategoryServiceClient;
import com.fms.customerservice.client.LogServiceClient;
import com.fms.customerservice.model.User;
import com.fms.customerservice.model.UserTranport;
import com.fms.customerservice.model.VehiclesAssign;
import com.fms.customerservice.model.VehiclesAssignDTO;
import com.fms.customerservice.model.ship.ChangePassForm;
import com.fms.customerservice.model.ship.ShipUserDetailsDTO;
import com.fms.customerservice.model.ship.ShipUserMapped;
import com.fms.customerservice.model.ship.ShipUserModel;
import com.fms.customerservice.repository.UserRepository;
import com.fms.customerservice.repository.UserTransportRepository;
import com.fms.module.exception.BusinessException;
import com.fms.module.exception.ErrorException;
import com.fms.module.model.LogAction;
import com.fms.module.model.MetaModel;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
import com.fms.module.utils.LogUtils;
import com.fms.module.utils.Logs;
import com.fms.module.utils.Utils;
import com.fms.module.utils.ValidateUtils;

@Service
public class ShipUserServiceImpl implements ShipUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShipUserServiceImpl.class);
	private static final int MAXLENGTH_255 = 255;
	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public ShipUserServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Autowired
	UserRepository userRepository;

	@Autowired
	CategoryServiceClient categoryServiceClient;

	@Autowired
	UserTransportRepository userTranportRepository;

	@Autowired
	LogServiceClient logServiceClient;

	// TIM KIEM
	@Override
	public ResponseModel<List<ShipUserMapped>> getAllUsers(SearchModel searchModel, Integer roleId, Integer provinceId,
			Integer districtId, Integer customerId, Integer userId) {
		try {
			if (searchModel.getPageNumber() <= 0)
				throw new BusinessException(Utils.getErrorMessageValidate(Constants.PAGE_REQUEST_PARAM, "Pattern"));

			if (searchModel.getPageSize() <= 0)
				throw new BusinessException(
						Utils.getErrorMessageValidate(Constants.PAGE_SIZE_REQUEST_PARAM, "Pattern"));

			ResponseModel<List<ShipUserMapped>> response = new ResponseModel<List<ShipUserMapped>>();
			String procedure = "CALL findAllShipUser(%s, %s,'%s','%s', %s, '%s', %s, %s, %s, %s,%s, @total)";

			String SQL = String.format(procedure, searchModel.getPageNumber(), searchModel.getPageSize(),
					searchModel.getName().trim(),searchModel.getUsername().trim(), searchModel.getId(), searchModel.getPhone().trim(),
					provinceId, districtId,customerId, roleId, userId);
			SQL = SQL.replace("\\", "\\\\");

			List<Map<String, Object>> rows;
			List<ShipUserMapped> listUsers = new ArrayList<ShipUserMapped>();
			try {
				rows = jdbcTemplate.queryForList(SQL);
				for (Map<String, Object> row : rows) {
					ShipUserMapped user = new ShipUserMapped();
					user.setUserName((String) row.get("username"));
					user.setFullName((String) row.get("fullName"));
					user.setUserId((Integer) row.get("userId"));
					user.setPhone((String) row.get("phone"));
					user.setEmail((String) row.get("email"));
					user.setRoleId((Integer) row.get("roleId"));
					user.setCustomerName((String) row.get("customerName"));
					user.setRoleName((String) row.get("roleName"));
					listUsers.add(user);
				}
			} catch (Exception e) {
				throw new ErrorException(e);
			}

			Map<String, Object> subObject = jdbcTemplate.queryForMap("SELECT @total");
			String total = null;
			int totalRecord;

			if (subObject != null) {
				total = (String) subObject.get("@total");
			}

			totalRecord = total != null ? Integer.parseInt(total) : 0;
			int totalPages = (int) Math.ceil((float) totalRecord / searchModel.getPageSize());

			response.setMeta(
					new MetaModel(searchModel.getPageNumber(), searchModel.getPageSize(), totalPages, totalRecord));
			response.setData(listUsers);

			return response;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public String createUser(ShipUserModel userInput, Integer userIdHeader, String username, HttpServletRequest request) {
		try {
			User user = new User();
			user.setCustomerId(userInput.getCustomerId());
			user.setUsername(userInput.getUserName());
			user.setPassword(userInput.getPassword());
			user.setFullname(userInput.getFullName());
			user.setPhone(userInput.getPhone());
			user.setEmail(userInput.getEmail());
			user.setRoleId(userInput.getRoleId());
			
			user = validateUser(user, userIdHeader);

			user.audit(Constants.LOCALE_CREATE, username);
			user.setIsActive(true);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(user.getPassword()));

			User userCreated = userRepository.save(user);
			if (userCreated == null) {
				throw new BusinessException(Utils.getErrorMessage(Constants.LOCALE_CREATE));
			}
			// save log
			saveLogCreate(userCreated, username, request);

			return Utils.getSuccessMessage(Constants.LOCALE_CREATE);

		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void saveLogCreate(User user, String username, HttpServletRequest request) {
		try {
			String content = Utils.getMessageByKey("function.insert-des"); 

			Integer provinceId = userRepository.getProvinceIdOfCustomer(user.getCustomerId());
			Integer districtId = userRepository.getDictrictIdOfCustomer(user.getCustomerId());

			if (user.getRoleId() == Constants.PROVINCE_ADMIN_ROLE || user.getRoleId() == Constants.DISTRICT_ADMIN_ROLE) {
				provinceId = user.getProvinceId();
				districtId = user.getDistrictId();
			}
			LogAction logAction = LogUtils.getActionLog(request, username, Constants.ActionType.CREATE.getValue(),
					Constants.FunctionType.QUANLY_NGUOIDUNG.getValue(), content, user.getUsername(),
					user.getUserId(), 0, user.getCustomerId(), provinceId, districtId);
			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

	private User validateUser(User user, Integer userId) {
		if (ValidateUtils.isNullOrEmpty(user.getUsername())) {
			throw new BusinessException(Utils.getErrorMessageValidate("username", Constants.NOTBLANK));
		}
		if (user.getUserId() == null) {
			if (ValidateUtils.isNullOrEmpty(user.getPassword())) {
				throw new BusinessException(Utils.getErrorMessageValidate("password", Constants.NOTBLANK));
			}
			if (user.getPassword().trim().length() > Constants.MAXLENGTH_50) {
				throw new BusinessException(Utils.getErrorMessageMaxlength("password", Constants.MAXLENGTH_50));
			}
		} else {
			if (!ValidateUtils.isNullOrEmpty(user.getPassword())) {
				if (user.getPassword().trim().length() > Constants.MAXLENGTH_50) {
					throw new BusinessException(Utils.getErrorMessageMaxlength("password", Constants.MAXLENGTH_50));
				}
			}
		}

		if (ValidateUtils.isNullOrEmpty(user.getFullname())) {
			throw new BusinessException(Utils.getErrorMessageValidate("fullName", Constants.NOTBLANK));
		}

		if (user.getFullname().trim().length() > MAXLENGTH_255) {
			throw new BusinessException(Utils.getValidateMessage("user.fullname-maxlength"));
		}

		if (ValidateUtils.isNullOrEmpty(user.getPhone()) && ValidateUtils.isNullOrEmpty(user.getEmail())) {
			throw new BusinessException(Utils.getMessageByKey("validate.phone_email"));
		}

		if (!ValidateUtils.isNullOrEmpty(user.getPhone()) && !ValidateUtils.validPhone(user.getPhone())) {
			throw new BusinessException(Utils.getErrorMessageValidate("Phone", Constants.ERROR_FORMAT));
		}
		if (!ValidateUtils.isNullOrEmpty(user.getEmail()) && !ValidateUtils.isEmailValid(user.getEmail())) {
			throw new BusinessException(Utils.getErrorMessageValidate("Email", Constants.ERROR_FORMAT));
		}

		if (user.getRoleId() == null || user.getRoleId() == 0) {
			throw new BusinessException(Utils.getValidateMessage("user.role-invalid"));
		}

		if (user.getRoleId() == Constants.PROVINCE_ADMIN_ROLE || user.getRoleId() == Constants.DISTRICT_ADMIN_ROLE) {
			if (ValidateUtils.isNullOrEmpty(user.getProvinceId())) {
				throw new BusinessException(Utils.getValidateMessage("user.province-must-select"));
			}
		}
		if (user.getRoleId() == Constants.DISTRICT_ADMIN_ROLE) {
			if (ValidateUtils.isNullOrEmpty(user.getDistrictId())) {
				throw new BusinessException(Utils.getValidateMessage("user.district-must-select"));
			}
		}
		if (user.getRoleId() == Constants.CUSTOMER_ACCOUNT_ROLE || user.getRoleId() == Constants.USER_ACCOUNT_ROLE) {
			if (ValidateUtils.isNullOrEmpty(user.getCustomerId())) {
				throw new BusinessException(Utils.getValidateMessage("user.customerId-mustfill"));
			}
		}

//		String error = userRepository.validateCreateUser(user.getProvinceId(), user.getDistrictId(),
//				user.getCustomerId(), user.getDepartmentId(), user.getUserId(), user.getRoleId(), user.getPhone(),
//				user.getEmail(), userId);
//		if (error != null)
//			throw new BusinessException(Utils.getValidateMessage("user." + error));

		return user.trimUser(user);
	}

	@Override
	public String updateUser(ShipUserModel userInput, Integer userIdHeader, String username, HttpServletRequest request) {
		try {
						
			if (!isAllowAction(userIdHeader, userInput.getUserId())) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

			User userCurrent = getUserById(userInput.getUserId());
			if (userCurrent == null) {
				throw new BusinessException(Utils.getValidateMessage("user.user-notexist"));
			}
			User userOld = (User) userCurrent.clone();

			userCurrent.setCustomerId(userInput.getCustomerId());
			userCurrent.setUsername(userInput.getUserName());
			userCurrent.setPassword(userInput.getPassword());
			userCurrent.setFullname(userInput.getFullName());
			userCurrent.setPhone(userInput.getPhone());
			userCurrent.setEmail(userInput.getEmail());
			userCurrent.setRoleId(userInput.getRoleId());
			
			userCurrent = validateUser(userCurrent, userIdHeader);

			if (!ValidateUtils.isNullOrEmpty(userInput.getPassword())) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				userCurrent.setPassword(encoder.encode(userInput.getPassword()));
			}

			userCurrent.audit(Constants.LOCALE_UPDATE, username);

			userRepository.save(userCurrent);
			
			// save log
			saveLogUpdate(userOld, userCurrent, username, request);

			return Utils.getSuccessMessage(Constants.LOCALE_UPDATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
	/*
	 * kiem tra quyen khi thuc hien thao tac sua/xoa/xem chi tiet
	 */
	private boolean isAllowAction(Integer userId, Integer userIdChecked) {
		Integer result = userRepository.checkUserIdPermission(userId, userIdChecked);
		return result != null ? result == 0 ? false : true : false;
	}
	

	private User getUserById(Integer userId) {
		User user = userRepository.retrieveUser(userId);
		if (user == null) {
			throw new BusinessException(Utils.getValidateMessage("user.userId-notexist"));
		}
		return user;
	}
	private void saveLogUpdate(User userOld, User userUpdated, String username, HttpServletRequest request) {
		try {
			DiffResult diff = userOld.diff(userUpdated);
			String content = LogUtils.getContentChange(diff);
			LogAction logAction = LogUtils.getActionLog(request, username, Constants.ActionType.UPDATE.getValue(),
					Constants.FunctionType.QUANLY_NGUOIDUNG.getValue(), content, userUpdated.getUsername(),
					userUpdated.getUserId(), 0, userOld.getCustomerId(), userOld.getProvinceId(), -1);

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

	//XOA NGUOI DUNG
	@Override
	public String deleteUser(Integer userIdDeleted, String username, Integer userId, HttpServletRequest request) {
		try {
			if (!isAllowAction(userId, userIdDeleted)) {
				throw new BusinessException(Utils.getValidateMessage("user.author"));
			}
			User userCurrent = getUserById(userIdDeleted);
			if (userCurrent == null) {
				throw new BusinessException(Utils.getValidateMessage("user.user-notexist"));
			}
			userCurrent.audit(Constants.LOCALE_DELETE, username);
			userCurrent.setIsActive(false);
			User userDeleted = userRepository.save(userCurrent);

			// save log			
			saveLogDelete(userDeleted, request, username);

			return Utils.getSuccessMessage(Constants.LOCALE_DELETE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
	
	private void saveLogDelete(User userDeleted, HttpServletRequest request, String username) {
		try {
			String content = Utils.getMessageByKey("function.delete-des");

			Integer provinceId = userRepository.getProvinceIdOfCustomer(userDeleted.getCustomerId());
			Integer districtId = userRepository.getDictrictIdOfCustomer(userDeleted.getCustomerId());

			if (userDeleted.getRoleId() == Constants.PROVINCE_ADMIN_ROLE || userDeleted.getRoleId() == Constants.DISTRICT_ADMIN_ROLE) {
				provinceId = userDeleted.getProvinceId();
				districtId = userDeleted.getDistrictId();
			}
			
			LogAction logAction = LogUtils.getActionLog(request, username, Constants.ActionType.DELETE.getValue(),
					Constants.FunctionType.QUANLY_NGUOIDUNG.getValue(), content, userDeleted.getUsername(),
					userDeleted.getUserId(), 0, userDeleted.getCustomerId(), provinceId, districtId);

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

	//XEM CHI TIET
	@Override
	public ShipUserDetailsDTO getUserDetails(Integer userId, Integer userIdCheck) {
		try {
			if (!isAllowAction(userId, userIdCheck)) {
				throw new BusinessException(Utils.getValidateMessage("user.author"));
			}
			ShipUserDetailsDTO user = userRepository.getShipUserDetails(userIdCheck);
			if (user == null) {
				throw new BusinessException(Utils.getValidateMessage("user.userId-notexist"));
			}

			return user;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
	
	/*
	 * lay danh sach xe phan quyen giam sat
	 */
	@Override
	public List<VehiclesAssignDTO> getVehicles(Integer userId, Integer customerId, Integer roleId,
			Integer userIdHeader) {
		try {
			if (userId == null)
				throw new BusinessException(Utils.getErrorMessageValidate("userId", Constants.NOTBLANK));

			validateGetVehicles(userId, userIdHeader);
			String procedure = "CALL getVehicles(%s, %s)";
			String SQL = String.format(procedure, userId, customerId);
			List<Map<String, Object>> rows;
			List<VehiclesAssignDTO> listData = new ArrayList<VehiclesAssignDTO>();
			try {
				rows = jdbcTemplate.queryForList(SQL);
				for (Map<String, Object> row : rows) {
					VehiclesAssignDTO item = new VehiclesAssignDTO();
					item.setRegisterNo((String) row.get("registerNo"));
					item.setTransportId((Integer) row.get("transportId"));
					item.setStatus(Long.parseLong(row.get("status").toString()));
					listData.add(item);
				}
			} catch (Exception e) {
				throw new ErrorException(e);
			}
			return listData;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void validateGetVehicles(Integer userId, Integer userIdHeader) {
		// validate userId co ton tai ko
		getUserById(userId);

		// check userId co thuoc quan ly cua userIdHeader
		if (!isAllowAction(userIdHeader, userId)) {
			throw new BusinessException(Utils.getValidateMessage("user.author"));
		}
	}

	private void validateAssignVehicles(VehiclesAssign vehiclesAssign, UserHeader userHeader) {
		if (vehiclesAssign.getUserId() == null) {
			throw new BusinessException(Utils.getErrorMessageValidate("userId", Constants.NOTBLANK));
		}
		User userInfo = userRepository.retrieveUser(vehiclesAssign.getUserId());
		if (userInfo == null) {
			throw new BusinessException(Utils.getValidateMessage("user.user-notexist"));
		}
		
		// kiem tra role
		if (userInfo.getRoleId() != 5) {
			throw new BusinessException(Utils.getValidateMessage("user.author"));
		}

		// check userId co thuoc quan ly cua userIdHeader
		if (!isAllowAction(userHeader.getUserId(), vehiclesAssign.getUserId())) {
			throw new BusinessException(Utils.getValidateMessage("user.author"));
		}

		for (Integer id : vehiclesAssign.getListVehicles()) {
			int count = userRepository.countTransportId(id);
			if (count == 0) {
				throw new BusinessException(Utils.getValidateMessage("user.transport-notexist"));
			}
			boolean isPermittedTransport = userRepository.checkPermissionTransport(userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(),
					userHeader.getUserId(), id);
			if (!isPermittedTransport)
				throw new BusinessException(Utils.getValidateMessage("user.transport-author"));
		}
	}

	@Override
	public String assignVehicles(VehiclesAssign vehiclesAssign, UserHeader userHeader, HttpServletRequest request) {
		try {
			validateAssignVehicles(vehiclesAssign, userHeader);

			saveLogAssign(vehiclesAssign, userHeader, request);

			// xoa cac xe da duoc phan quyen cho userId trong bang user_transport
			userTranportRepository.deleteVehiclesAssigned(vehiclesAssign.getUserId());

			// thuc hien luu tung xe cho userId
			for (Integer i : vehiclesAssign.getListVehicles()) {
				UserTranport userTranport = new UserTranport(vehiclesAssign.getUserId(), i);
				userTranport.audit(Constants.LOCALE_CREATE, userHeader.getUsername());
				userTranportRepository.save(userTranport);
			}

			return Utils.getSuccessMessage(Constants.LOCALE_UPDATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void saveLogAssign(VehiclesAssign vehiclesAssign, UserHeader userHeader, HttpServletRequest request) {
		try {
			StringBuilder content = new StringBuilder();

			String registerNos = userTranportRepository.getVehiclesAssignedCurrent(vehiclesAssign.getUserId());
			String registerNosAssigned = userTranportRepository
					.getVehiclesAssignedNew(vehiclesAssign.getListVehicles());

			content.append(Utils.getMessageByKey("user.assign-vehicle"));
			String text = String.format(" [%s] -> [%s]", registerNos, registerNosAssigned);
			content.append(text);

			User user = getUserById(vehiclesAssign.getUserId());

			LogAction logAction = LogUtils.getActionLog(request, userHeader.getUsername(),
					Constants.ActionType.UPDATE.getValue(), Constants.FunctionType.QUANLY_NGUOIDUNG.getValue(),
					content.toString(), user.getUsername(), vehiclesAssign.getUserId(), 0, user.getCustomerId(),
					user.getProvinceId(), user.getDistrictId());

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}
	
	@Override
	public String changePassword(ChangePassForm changePasswordForm, HttpServletRequest request) {
		// get thong tin tu form
		Integer userId = changePasswordForm.getUserId();
		String oldPassword = changePasswordForm.getOldPassword().trim();
		String newPassword = changePasswordForm.getNewPassword().trim();
		// check valid
		try {
			if (oldPassword.isEmpty() || newPassword.isEmpty()) {
				throw new BusinessException(Utils.getMessageByKey("validate.not_empty"));
			}
			
			if (newPassword.equals(oldPassword)) {
				throw new BusinessException(Utils.getMessageByKey("password.must_different"));
			}
			
			User userCurrent = getUserById(userId);
			if (userCurrent == null)
				throw new BusinessException(Utils.getMessageByKey("username.not_exist"));
		
			boolean isMatch = encoder.matches(oldPassword, userCurrent.getPassword());
			if (!isMatch)
				throw new BusinessException(Utils.getMessageByKey("password.not_correct"));
			
			// save pass moi sau khi check valid thanh cong
			newPassword = encoder.encode(newPassword);
			// set modifiedBy/ modifiedDate
			String modifiedBy = request.getHeader("username");
			LocalDateTime modifiedDate = LocalDateTime.now();
			userRepository.changePassword(userCurrent.getUsername(), newPassword, modifiedBy, modifiedDate);

			// ghi log update
			Logs.updated(ShipUserServiceImpl.class, "CUSTOMER-SERVICE", modifiedBy, request.getRemoteAddr(),
					request.getHeader("User-Agent"), "PASSWORD", userCurrent.getUsername(), userCurrent.getUsername() + " " + oldPassword,
					userCurrent.getUsername() + " " + newPassword);

			saveLogChangePass(userCurrent.getUsername(), request);
			return Utils.getSuccessMessage("update");

		} catch (Exception e) {
			LOGGER.error("error: ", e);
			throw new ErrorException(e);
		}
	}
	
	private void saveLogChangePass(String userName, HttpServletRequest request) {
		try {
			String content = Utils.getMessageByKey("user.change-password");
			LogAction logAction = LogUtils.getActionLog(request, userName, Constants.ActionType.UPDATE.getValue(),
					Constants.FunctionType.QUANLY_NGUOIDUNG.getValue(), content, userName, null,
					0, null, null, null);

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

}
