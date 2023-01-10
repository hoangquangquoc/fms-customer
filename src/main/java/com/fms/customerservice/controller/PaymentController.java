package com.fms.customerservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fms.customerservice.model.PaymentHistory;
import com.fms.customerservice.model.PaymentMapped;
import com.fms.customerservice.model.PaymentRequest;
import com.fms.customerservice.service.PaymentService;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {
	@Autowired
	PaymentService paymentService;

	@GetMapping
	public ResponseModel<List<PaymentMapped>> getPaymentList(
			@RequestHeader(value = "userId", required = false) Integer userId,
			@RequestHeader(value = "roleId", required = false) Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = "registerNo", defaultValue = Constants.QUERY_VALUE_DEFAULT) String registerNo,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "register_no") String orderBy) {
		
		SearchModel searchModel = new SearchModel(pageSize, pageNumber, orderBy);
		searchModel.setRegisterNo(registerNo);
		UserHeader userHeader = new UserHeader(userId, roleId, provinceId, districtId, customerId);
		
		return paymentService.getPaymentList(searchModel, userHeader);
	}
	
	// thuc hien thanh toan
	@PostMapping
	public ResponseModel<String> renewalService(
			@RequestHeader(value = "username", required = false) String username,
			@RequestHeader(value = "userId", required = false) Integer userId,
			@RequestHeader(value = "roleId", required = false) Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestBody PaymentRequest paymentRequest) {
		
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerId);
		ResponseModel<String> response = new ResponseModel<String>();
		response.setMessage(paymentService.renewalService(paymentRequest, userHeader));
		response.setCode(Constants.SC_OK);
		return response;
	}
	
	@GetMapping(value="/histories")
	public ResponseModel<List<PaymentHistory>> getPaymentHistory(
			@RequestHeader(value = "userId", required = false) Integer userId,
			@RequestHeader(value = "roleId", required = false) Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = "registerNo", defaultValue = Constants.QUERY_VALUE_DEFAULT) String registerNo,
			@RequestParam(name = "fromDate", defaultValue = Constants.QUERY_VALUE_DEFAULT) String fromDate,
			@RequestParam(name = "toDate", defaultValue = Constants.QUERY_VALUE_DEFAULT) String toDate,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "register_no") String orderBy) {
		
		SearchModel searchModel = new SearchModel(pageSize, pageNumber, orderBy);
		searchModel.setRegisterNo(registerNo);
		searchModel.setFromDate(fromDate);
		searchModel.setToDate(toDate);
		UserHeader userHeader = new UserHeader(userId, roleId, provinceId, districtId, customerId);
		
		return paymentService.getPaymentHistory(searchModel, userHeader);
	}
}
