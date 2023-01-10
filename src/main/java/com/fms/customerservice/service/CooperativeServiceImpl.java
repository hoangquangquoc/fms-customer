package com.fms.customerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fms.customerservice.model.CooperativeInforRequest;
import com.fms.customerservice.model.Customer;
import com.fms.customerservice.repository.CooperativeRepository;
import com.fms.customerservice.repository.CustomerRepository;
import com.fms.customerservice.repository.UserRepository;
import com.fms.module.exception.BusinessException;
import com.fms.module.exception.ErrorException;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;
import com.fms.module.utils.ValidateUtils;

@Service
public class CooperativeServiceImpl implements CooperativeService {
	@Autowired
	CooperativeRepository cooperativeRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CustomerRepository customerRepository;
	private boolean isValidUsername(String username) {
		String pattern = "([a-zA-Z0-9]|\\_|\\-|\\.)*";
		return username.matches(pattern);
	}
	void validateInfoCooperative(String roleId, String provinceId, CooperativeInforRequest request) {
		
		// check cac truong bat buoc nhap
		if(ValidateUtils.isNullOrEmpty(request.getName()) || 
		   ValidateUtils.isNullOrEmpty(request.getAddress()) ||
		   ValidateUtils.isNullOrEmpty(request.getUsername()) ||
		   ValidateUtils.isNullOrEmpty(request.getPassword()) ||
		   (ValidateUtils.isNullOrEmpty(request.getEmail()) && ValidateUtils.isNullOrEmpty(request.getPhone())) ||
		   ValidateUtils.isNullOrEmpty(request.getCustomerIds()) )
			throw new BusinessException(Utils.getValidateMessage("required"));
		
		// check maxlength string
		if(request.getName().length() > Constants.MAXLENGTH_255 || 
		   request.getAddress().length() > Constants.MAXLENGTH_255 ||
		   request.getBranchId().length() > Constants.MAXLENGTH_255 || 
		   request.getCustomerIds().length() > Constants.MAXLENGTH_255 || 
		   request.getEmail().length() > Constants.MAXLENGTH_255 || 
		   request.getUsername().length() > Constants.MAXLENGTH_100 ||
		   request.getPassword().length() > Constants.MAXLENGTH_50 || 
		   request.getPhone().length() > Constants.MAXLENGTH_255 )
			throw new BusinessException(Utils.getValidateMessage("exceed_max_length"));
		
		// check dinh dang email, phone
		// validate phone
		if (!ValidateUtils.isNullOrEmpty(request.getPhone()) && !ValidateUtils.validPhone(request.getPhone())) {
			throw new BusinessException(Utils.getErrorMessageValidate("Phone", Constants.ERROR_FORMAT));
		}

		// validate email
		if (!ValidateUtils.isNullOrEmpty(request.getEmail()) && !ValidateUtils.isEmailValid(request.getEmail())) {
			throw new BusinessException(Utils.getErrorMessageValidate("Email", Constants.ERROR_FORMAT));
		}
		
		// check kiểu số branchId và customerIds
		if(!ValidateUtils.isNumberList(request.getBranchId()) || !ValidateUtils.isNumberList(request.getCustomerIds()))
			throw new BusinessException(Utils.getValidateMessage("error_input"));
		
		// Kiem tra trung phone,email,username
		
		if(!ValidateUtils.isNullOrEmpty(request.getUsername()) && userRepository.isExistUsername(request.getUsername()) > 0)
			throw new BusinessException(Utils.getValidateMessage("user.username-exist"));
		
		if(!ValidateUtils.isNullOrEmpty(request.getEmail()) && userRepository.isExistEmail(request.getEmail()) > 0)
			throw new BusinessException(Utils.getValidateMessage("user.email-exist"));
		
		if(!ValidateUtils.isNullOrEmpty(request.getPhone()) && userRepository.isExistPhone(request.getPhone()) > 0)
			throw new BusinessException(Utils.getValidateMessage("user.phone-exist"));
		
		// Kiem tra branchId có thuộc sự quản lý của tài khoản đang đăng nhập
		if ((!ValidateUtils.isNullOrEmpty(request.getBranchId()) && customerRepository.isExistBranchId(Integer.parseInt(request.getBranchId())) == 0) || 
				(Constants.PROVINCE_ADMIN_ROLE.toString().equals(roleId) && !provinceId.equals(request.getBranchId()))) {
			throw new BusinessException(Utils.getMessageByKey("validate.branch-author"));
		}	
		// Kiểm tra các customerId đã được gán cho HTX nào chưa và có được quản lý bởi user đang đăng nhập không
		String lstCustomerId[] = request.getCustomerIds().split(",");
		for(int i = 0;i<lstCustomerId.length;i++) {
			Customer cust = customerRepository.retrieveCustomer(Integer.parseInt(lstCustomerId[i]));
			if(cust == null || (Constants.PROVINCE_ADMIN_ROLE.toString().equals(roleId) && !provinceId.equals(cust.getProvinceId().toString()))) {
				throw new BusinessException(Utils.getMessageByKey("validate.user.customer-author"));
			}
			if(!(cust.getCooperativeId() == null || cust.getCooperativeId() == 0)) {
				throw new BusinessException(Utils.getValidateMessage("customer_has_cooperative"));
			}
		}
		
		// Kiểm tra thông tin username
		if (!ValidateUtils.isNullOrEmpty(request.getUsername())
				&& !isValidUsername(request.getUsername().trim())) {
			throw new BusinessException(Utils.getValidateMessage("user.invalid_username"));
		}
		
		// Kiểm tra thông tin password
		if (request.getPassword().trim().length() > Constants.MAXLENGTH_50) {
			throw new BusinessException(Utils.getErrorMessageMaxlength("password", Constants.MAXLENGTH_50));
		}
		if (!ValidateUtils.isValidPass(request.getPassword().trim())) {
			throw new BusinessException(Utils.getValidateMessage(Constants.LOCALE_PD_NOT_STRONG));
		}
	}
	@Override
	public ResponseModel<Long> saveCooperative(String roleId, String provinceId, String userId, String username, String actionType,
			CooperativeInforRequest request) {
		try {
			if (!Constants.SYS_ADMIN_ROLE.toString().equals(roleId)
					&& !Constants.PROVINCE_ADMIN_ROLE.toString().equals(roleId)) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}
			
			
			if(ValidateUtils.isNullOrEmpty(actionType)) {
				throw new BusinessException(Utils.getValidateMessage("error_input"));
			}
			validateInfoCooperative(roleId, provinceId, request);
			
			// Save DB
			ResponseModel<Long> results = cooperativeRepository.saveCooperative(roleId, userId, username, actionType, request);
			if (results.getData() > 0) {
				if (results.getData() <= 0) {
					throw new BusinessException(results.getMessage());
				}
				return results;
			} else {
				throw new BusinessException(Utils.getErrorMessage(Constants.LOCALE_PROCESS));
			}
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

}
