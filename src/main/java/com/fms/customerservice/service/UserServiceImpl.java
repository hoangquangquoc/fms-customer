package com.fms.customerservice.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.DiffResult;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fms.customerservice.client.CategoryServiceClient;
import com.fms.customerservice.client.LogServiceClient;
import com.fms.customerservice.controller.ExportController;
import com.fms.customerservice.model.Coordinate;
import com.fms.customerservice.model.ExportModel;
import com.fms.customerservice.model.User;
import com.fms.customerservice.model.UserDetailsDTO;
import com.fms.customerservice.model.UserMapped;
import com.fms.customerservice.model.UserTranport;
import com.fms.customerservice.model.VehiclesAssign;
import com.fms.customerservice.model.VehiclesAssignDTO;
import com.fms.customerservice.repository.UserRepository;
import com.fms.customerservice.repository.UserTransportRepository;
import com.fms.module.exception.BusinessException;
import com.fms.module.exception.ErrorException;
import com.fms.module.model.CheckPermissionUserForm;
import com.fms.module.model.LogAction;
import com.fms.module.model.MetaModel;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
import com.fms.module.utils.LogUtils;
import com.fms.module.utils.ReportHeaderConstants;
import com.fms.module.utils.Utils;
import com.fms.module.utils.ValidateUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
//	private static final String[] ROLE_TYPE = { "1", "2", "3", "4", "5" };
	private static final String[] GENDER_TYPE = { "0", "1", "2" };
	private static final String[] MAP_TYPE = { "1", "2" };
	private static final String[] STATUS = { "-1", "0", "1" };
	private static final int MAXLENGTH_255 = 255;

	@Autowired
	private ServletContext servletContext;

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public UserServiceImpl(JdbcTemplate jdbcTemplate) {
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

	/*
	 * them moi nguoi dung
	 */
	@Override
	public String createUser(User user, Integer userIdHeader, String username, HttpServletRequest request) {
		try {
			user = validateUser(user, userIdHeader);

			user.audit(Constants.LOCALE_CREATE, username);
			user.setIsActive(true);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(user.getPassword()));

			User userCreated = userRepository.save(user);
			if (userCreated == null) {
				throw new BusinessException(Utils.getErrorMessage(Constants.LOCALE_CREATE));
			}

//			categoryServiceClient.createUserSubmenu(new UserSubmenuModel(user.getUsername(), user.getFullname(), user.getRoleId()));

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
		if (!ValidateUtils.isNullOrEmpty(user.getUsername())
				&& !isValidUsername(user.getUsername().trim())) {
			throw new BusinessException(Utils.getValidateMessage("user.invalid_username"));
		}

		if (user.getUserId() == null) {
			if (ValidateUtils.isNullOrEmpty(user.getPassword())) {
				throw new BusinessException(Utils.getErrorMessageValidate("password", Constants.NOTBLANK));
			}
			if (user.getPassword().trim().length() > Constants.MAXLENGTH_50) {
				throw new BusinessException(Utils.getErrorMessageMaxlength("password", Constants.MAXLENGTH_50));
			}
			if (!ValidateUtils.isValidPass(user.getPassword().trim())) {
				throw new BusinessException(Utils.getValidateMessage(Constants.LOCALE_PD_NOT_STRONG));
			}
		} else {
			if (!ValidateUtils.isNullOrEmpty(user.getPassword())) {
				if (!ValidateUtils.isValidPass(user.getPassword().trim())) {
					throw new BusinessException(Utils.getValidateMessage(Constants.LOCALE_PD_NOT_STRONG));
				}
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

		validateType(user);

		User userCreated = userRepository.retrieveUserByUserName(user.getUsername().trim());

		if (userCreated != null) {
			if (user.getUserId() == null) {
				throw new BusinessException(Utils.getValidateMessage("user.username-exist"));
			} else {
				if (userCreated.getUserId().intValue() != user.getUserId().intValue()) {
					throw new BusinessException(Utils.getValidateMessage("user.username-exist"));
				}
			}
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

		String error = userRepository.validateCreateUser(user.getProvinceId(), user.getDistrictId(),
				user.getCustomerId(), user.getDepartmentId(), user.getUserId(), user.getRoleId(), user.getPhone(),
				user.getEmail(), userId);
		if (error != null)
			throw new BusinessException(Utils.getValidateMessage("user." + error));

		return user.trimUser(user);
	}

	/*
	 * cap nhat thong tin nguoi dung
	 */
	@Override
	public String updateUser(User user, Integer userIdHeader, String username, HttpServletRequest request) {
		try {
			if (!isAllowAction(userIdHeader, user.getUserId())) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

			User userCurrent = getUserById(user.getUserId());
			User userOld = (User) userCurrent.clone();

			user = validateUser(user, userIdHeader);

			if (!ValidateUtils.isNullOrEmpty(user.getPassword())) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				user.setPassword(encoder.encode(user.getPassword()));
			}

			user.audit(Constants.LOCALE_UPDATE, username);
			user = saveOldFeilds(user, userCurrent);

			User userUpdated = userRepository.save(user);
			if (userUpdated == null) {
				throw new BusinessException(Utils.getErrorMessage(Constants.LOCALE_UPDATE));
			}

			// save log
			saveLogUpdate(userOld, userUpdated, username, request);

			return Utils.getSuccessMessage(Constants.LOCALE_UPDATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
	
	private void saveLogUpdate(User userOld, User userUpdated, String username, HttpServletRequest request) {
		try {
			userOld = setNameComboboxUsers(userOld);
			userUpdated = setNameComboboxUsers(userUpdated);

			DiffResult diff = userOld.diff(userUpdated);
			String content = LogUtils.getContentChange(diff);

			Integer provinceId = userRepository.getProvinceIdOfCustomer(userUpdated.getCustomerId());
			Integer districtId = userRepository.getDictrictIdOfCustomer(userUpdated.getCustomerId());

			if (userUpdated.getRoleId() == Constants.PROVINCE_ADMIN_ROLE || userUpdated.getRoleId() == Constants.DISTRICT_ADMIN_ROLE) {
				provinceId = userUpdated.getProvinceId();
				districtId = userUpdated.getDistrictId();
			}
			
			LogAction logAction = LogUtils.getActionLog(request, username, Constants.ActionType.UPDATE.getValue(),
					Constants.FunctionType.QUANLY_NGUOIDUNG.getValue(), content, userUpdated.getUsername(),
					userUpdated.getUserId(), 0, userOld.getCustomerId(), provinceId, districtId);

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

	private User saveOldFeilds(User userUpdate, User userCurrent) {
		userUpdate.setIsActive(userCurrent.getIsActive());
		userUpdate.setLat(userCurrent.getLat());
		userUpdate.setLng(userCurrent.getLng());
		userUpdate.setCreatedBy(userCurrent.getCreatedBy());
		userUpdate.setCreatedDate(userCurrent.getCreatedDate());

		if (ValidateUtils.isNullOrEmpty(userUpdate.getPassword())) {
			userUpdate.setPassword(userCurrent.getPassword());
		}

		return userUpdate;
	}

	/*
	 * xoa nguoi dung
	 */
	@Override
	public String deleteUser(Integer userIdDeleted, String username, Integer userId, HttpServletRequest request) {
		try {
			if (!isAllowAction(userId, userIdDeleted)) {
				throw new BusinessException(Utils.getValidateMessage("user.author"));
			}
			User userCurrent = getUserById(userIdDeleted);

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

	/*
	 * lay thong tin chi tiet nguoi dung
	 */
	@Override
	public UserDetailsDTO getUserDetails(Integer userId, Integer userIdCheck) {
		try {
			if (!isAllowAction(userId, userIdCheck)) {
				throw new BusinessException(Utils.getValidateMessage("user.author"));
			}
			UserDetailsDTO user = userRepository.getUserDetails(userIdCheck);
			if (user == null) {
				throw new BusinessException(Utils.getValidateMessage("user.userId-notexist"));
			}

			return user;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private User getUserById(Integer userId) {
		User user = userRepository.retrieveUser(userId);
		if (user == null) {
			throw new BusinessException(Utils.getValidateMessage("user.userId-notexist"));
		}
		return user;
	}

	@Override
	public String resetPassword(String _username, Integer roleId, String username, HttpServletRequest request) {
		try {
			User user = userRepository.retrieveUserByUserName(_username.trim());
			if (user == null) {
				throw new BusinessException(Utils.getErrorMessageWithParams("username", Constants.LOCALE_NOT_EXIST));
			}
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			user.setPassword(encoder.encode(Constants.DEFAULT_PD));
			user.audit(Constants.LOCALE_UPDATE, username);
			userRepository.save(user);
			
			saveLogReset(user, username, request);
			return Utils.getMessageByKey(Constants.LOCALE_SUCCESS);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void saveLogReset(User user, String username, HttpServletRequest request) {
		try {
			String content = Utils.getMessageByKey("reset-password");

			Integer provinceId = user.getProvinceId();
			Integer districtId = user.getDistrictId();

			if (user.getRoleId() == Constants.CUSTOMER_ACCOUNT_ROLE) {
				provinceId = userRepository.getProvinceIdOfCustomer(user.getCustomerId());
				districtId = userRepository.getDictrictIdOfCustomer(user.getCustomerId());
			}
			LogAction logAction = LogUtils.getActionLog(request, username, Constants.ActionType.UPDATE.getValue(),
					Constants.FunctionType.QUANLY_NGUOIDUNG.getValue(), content, user.getUsername(), user.getUserId(),
					0, user.getCustomerId(), provinceId, districtId);

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

	@Override
	public String setCenterMap(String userName, Integer userId, Coordinate coords) {
		try {
			User currentUser = getUserById(userId);

			if (!(userName).equals(currentUser.getUsername())) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}
			currentUser.setLat(coords.getLat());
			currentUser.setLng(coords.getLng());
			currentUser.setZoom(coords.getZoom());
			currentUser.audit(Constants.LOCALE_UPDATE, userName);
			User results = userRepository.save(currentUser);
			if (results == null) {
				throw new BusinessException(Utils.getErrorMessage(Constants.LOCALE_UPDATE));
			}
			return Utils.getSuccessMessage(Constants.LOCALE_UPDATE);
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
//			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			/*
			 * hoadp
			 * fix sonar
			 */
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
		int countUser = userRepository.countUserId(vehiclesAssign.getUserId());
		if (countUser == 0) {
			throw new BusinessException(Utils.getValidateMessage("user.user-notexist"));
		}

		// check userId co thuoc quan ly cua userIdHeader
		if (!isAllowAction(userHeader.getUserId(), vehiclesAssign.getUserId())) {
			throw new BusinessException(Utils.getValidateMessage("user.author"));
		}

//		if (vehiclesAssign.getListVehicles() == null || vehiclesAssign.getListVehicles().size() == 0) {
//			throw new BusinessException(Utils.getValidateMessage("user.vehicles-notblank"));
//		}

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
	public ResponseModel<List<UserMapped>> getAllUsers(SearchModel searchModel, Integer roleId, Integer provinceId,
			Integer districtId, Integer customerId, Integer userId, boolean export) {
		try {
			if (searchModel.getPageNumber() <= 0)
				throw new BusinessException(Utils.getErrorMessageValidate(Constants.PAGE_REQUEST_PARAM, "Pattern"));

			if (searchModel.getPageSize() <= 0)
				throw new BusinessException(
						Utils.getErrorMessageValidate(Constants.PAGE_SIZE_REQUEST_PARAM, "Pattern"));

			boolean isExist = Arrays.asList(STATUS).contains(String.valueOf(searchModel.getStatus()));
			if (!isExist)
				throw new ErrorException(new BusinessException(Utils.getValidateMessage("input_invalid")));

			ResponseModel<List<UserMapped>> response = new ResponseModel<List<UserMapped>>();
			String procedure = "CALL findAllUser(%s, %s, '%s', %s, '%s', '%s', %s, %s, %s, %s, %s,%s, @total)";

			if (export) {
				searchModel.setPageSize(Integer.MAX_VALUE);
			}

			String SQL = String.format(procedure, searchModel.getPageNumber(), searchModel.getPageSize(),
					searchModel.getUsername().trim(), searchModel.getStatus(), searchModel.getPhone().trim(),
					searchModel.getRegisterNo().trim(), searchModel.getCustomerGroupId(), provinceId, districtId,
					customerId, roleId, userId);
			SQL = SQL.replace("\\", "\\\\");

//			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			/*
			 * hoadp
			 * fix sonar
			 */
			List<Map<String, Object>> rows;
			List<UserMapped> listUsers = new ArrayList<UserMapped>();
			try {
				rows = jdbcTemplate.queryForList(SQL);
				for (Map<String, Object> row : rows) {
					UserMapped user = new UserMapped();
					user.setUsername((String) row.get("username"));
					user.setFullName((String) row.get("fullName"));
					user.setUserId((Integer) row.get("userId"));
					user.setPhone((String) row.get("phone"));
					user.setEmail((String) row.get("email"));
					user.setRoleId((Integer) row.get("roleId"));
					user.setStatus((Boolean) row.get("status"));
					user.setManagementUnit((String) row.get("managementUnit"));
					user.setRoleName((String) row.get("roleName"));
					user.setCreatedDate((String) row.get("createdDate"));
					user.setCustomerName((String) row.get("customerName"));
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

	/*
	 * kiem tra quyen khi thuc hien thao tac sua/xoa/xem chi tiet
	 */
	private boolean isAllowAction(Integer userId, Integer userIdChecked) {
		Integer result = userRepository.checkUserIdPermission(userId, userIdChecked);
		return result != null ? result == 0 ? false : true : false;
	}

	@Override
	public ResponseEntity<InputStreamResource> export(SearchModel searchModel, UserHeader userHeader, String type) {
		try {
			UtilsReportService utilsReportService = new UtilsReportService();
			ResponseModel<List<UserMapped>> response = getAllUsers(searchModel, userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(),
					userHeader.getUserId(), true);
			List<UserMapped> data = null;

			int stt = 1;
			ByteArrayInputStream fileExport = null;
			ExportController exportController = new ExportController();

			if (response != null) {
				data = response.getData();
				if ("excel".equals(type)) {
					XSSFWorkbook workbook = utilsReportService.getWorkbookByTemplate(Constants.BC_NGUOI_DUNG_FILE_NAME);
					if ("en".equals(userHeader.getAcceptLang())) {
						workbook = utilsReportService.getWorkbookByTemplate(Constants.BC_NGUOI_DUNG_FILE_NAME_EN);
					}

					XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
					int startRowNo = 2;
					int cellNo = 0;
					CellStyle centeredStyle = utilsReportService.createStyleAndAlignmentForCell(workbook, 0);
					CellStyle leftStyle = utilsReportService.createStyleAndAlignmentForCell(workbook, 1);
					for (UserMapped item : data) {
						/*
						Row row = sheet.createRow(startRowNo);
						row.createCell(cellNo++).setCellValue(stt++);
						row.createCell(cellNo++).setCellValue(checkNullValue(item.getUsername()));
						row.createCell(cellNo++).setCellValue(checkNullValue(item.getFullName()));
						row.createCell(cellNo++).setCellValue(checkNullValue(item.getEmail()));
						row.createCell(cellNo++).setCellValue(checkNullValue(item.getPhone()));
						row.createCell(cellNo++).setCellValue(checkNullValue(item.getManagementUnit()));
						row.createCell(cellNo++).setCellValue(checkNullValue(item.getRoleName()));
						row.createCell(cellNo++).setCellValue(convertStatus(item.getStatus()));
						row.createCell(cellNo++).setCellValue(checkNullValue(item.getCreatedDate()));

						cellNo = 0;
						startRowNo++;
						*/
						cellNo = 0;
						Row row = sheet.createRow(startRowNo);
						utilsReportService.setValueForCell(row.createCell(cellNo++),String.valueOf(stt),centeredStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getUsername(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getFullName(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getEmail(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getPhone(),centeredStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getManagementUnit(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getRoleName(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),convertStatus(item.getStatus()),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo),item.getCreatedDate(),leftStyle);
						startRowNo++;
						stt++;
					}

					String fileName = Utils.getFileNameExport(Constants.BC_NGUOI_DUNG_FILE_NAME, "xlsx");
					if ("en".equals(userHeader.getAcceptLang())) {
						fileName = Utils.getFileNameExport(Constants.BC_NGUOI_DUNG_FILE_NAME_EN, "xlsx");
					}
					fileExport = utilsReportService.getExportFile(workbook);

					MediaType mediaType = exportController.getMediaTypeForFileName(this.servletContext, fileName);
					InputStreamResource resource = new InputStreamResource(fileExport);

					return ResponseEntity.ok()
							.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
							.contentType(mediaType).body(resource);
				} else {
					Document document = new Document();
					document.setPageSize(PageSize.A4.rotate());
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					BaseFont baseFont = utilsReportService.getBaseFontReport();

					PdfPTable table = utilsReportService.getPdfTemplate(
							ReportHeaderConstants.DANH_SACH_NGUOI_DUNG_TITLE, null, null,
							ReportHeaderConstants.BC_NGUOI_DUNG_COLUMN_HEADERS_NAMES,
							ReportHeaderConstants.BC_NGUOI_DUNG_COLUMN_HEADERS_WIDTHS, 100f, baseFont);

					if ("en".equals(userHeader.getAcceptLang())) {
						table = utilsReportService.getPdfTemplate(ReportHeaderConstants.DANH_SACH_NGUOI_DUNG_TITLE_EN,
								null, null, ReportHeaderConstants.BC_NGUOI_DUNG_COLUMN_HEADERS_NAMES_EN,
								ReportHeaderConstants.BC_NGUOI_DUNG_COLUMN_HEADERS_WIDTHS, 100f, baseFont);
					}

					// Body data
					Font bodyFont = new Font(baseFont, 12, Font.NORMAL);
					for (UserMapped item : data) {
						utilsReportService.setPdfCellValue(stt++, bodyFont, Element.ALIGN_CENTER, table);
						utilsReportService.setPdfCellValue(item.getUsername(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getFullName(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getEmail(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getPhone(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getManagementUnit(), bodyFont, Element.ALIGN_LEFT,
								table);
						utilsReportService.setPdfCellValue(item.getRoleName(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(convertStatus(item.getStatus()), bodyFont,
								Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getCreatedDate(), bodyFont, Element.ALIGN_CENTER,
								table);
					}
					PdfWriter.getInstance(document, out);
					document.open();
					document.add(table);
					document.close();
					fileExport = new ByteArrayInputStream(out.toByteArray());

					String fileName = Constants.BC_NGUOI_DUNG_FILE_NAME;
					if ("en".equals(userHeader.getAcceptLang())) {
						fileName = Constants.BC_NGUOI_DUNG_FILE_NAME_EN;
					}

					ExportModel exportModel = new ExportModel(fileExport, fileName, "pdf");

					return exportController.responseExportFile(exportModel, this.servletContext);
				}
			}
		} catch (Exception e) {
			LOGGER.error("error: ",e);
			throw new ErrorException(new BusinessException(Utils.getErrorMessage(Constants.LOCALE_PROCESS)));
		}
		return null;
	}

	private String convertStatus(Boolean status) {
		if (status != null && status)
			return "Đang hoạt động";
		return "Ngưng hoạt động";
	}

//	private String checkNullValue(String value) {
//		return value != null ? value : "";
//	}

	@Override
	public Boolean checkPermissionUser(CheckPermissionUserForm checkPermissionUserForm) {
		try {
			return userRepository.checkPermissionUser(checkPermissionUserForm.getRoleId(),
					checkPermissionUserForm.getProvinceId(), checkPermissionUserForm.getDistrictId(),
					checkPermissionUserForm.getCustomerId(), checkPermissionUserForm.getUserIdHeader(),
					checkPermissionUserForm.getUserId());
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void validateType(User user) {
		if (user.getGender() != null) {
			boolean isExist = Arrays.asList(GENDER_TYPE).contains(user.getGender().toString());
			if (!isExist)
				throw new BusinessException(Utils.getValidateMessage("input_invalid"));
		}
		if (user.getMapType() != null) {
			boolean isExist = Arrays.asList(MAP_TYPE).contains(user.getMapType().toString());
			if (!isExist)
				throw new BusinessException(Utils.getValidateMessage("input_invalid"));
		}
//		if (user.getRoleId() != null) {
//			boolean isExist = Arrays.asList(ROLE_TYPE).contains(user.getRoleId().toString());
//			if (!isExist)
//				throw new BusinessException(Utils.getValidateMessage("input_invalid"));
//		}
	}

	private User setNameComboboxUsers(User user) {
		String customerName = userRepository.getCustomerName(user.getCustomerId());
		String provinceName = userRepository.getProvinceName(user.getProvinceId());
		String districtName = userRepository.getProvinceName(user.getDistrictId());
		String departmentName = userRepository.getDeparmentName(user.getDepartmentId());
		String roleName = userRepository.getRoleName(user.getRoleId());
		String mapName = Utils.getMessageByKey("user.map" + user.getMapType());
		String genderName = Utils.getMessageByKey("user.gender" + user.getGender());

		user.setCustomerName(customerName);
		user.setProvinceName(provinceName);
		user.setDistrictName(districtName);
		user.setDepartmentName(departmentName);
		user.setRoleName(roleName);
		user.setMapName(mapName);
		user.setGenderName(genderName);

		return user;
	}
	
	private boolean isValidUsername(String username) {
//		String pattern = "([a-zA-Z0-9]|\\_|\\-|\\.)*";
		String pattern = "([a-zA-Z0-9]|\\_|\\-|\\.)";
		return username.matches(pattern);
	}
}
