package com.fms.customerservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.fms.customerservice.model.ship.ShipCustomerDTO;
import com.fms.customerservice.model.ship.ShipCustomerDetailDTO;
import com.fms.customerservice.model.ship.ShipCustomerModel;
import com.fms.customerservice.service.ship.ShipCustomerService;
import com.fms.module.model.MetaModel;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

import brave.internal.Nullable;

@RestController
@RequestMapping("/v1/ship")
public class ShipCustomerController {
	@Autowired
	ShipCustomerService customerService;

	//Lay danh sach khach hang
	@GetMapping
	public ResponseModel<List<ShipCustomerDTO>> findAll(@Nullable @RequestHeader(value = "roleId") Integer roleId,
			@Nullable @RequestHeader(value = "provinceId") Integer provinceId,
			@Nullable @RequestHeader(value = "districtId") Integer districtId,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = "name", defaultValue = Constants.QUERY_VALUE_DEFAULT) String name,
			@RequestParam(name = "phone", defaultValue = Constants.QUERY_VALUE_DEFAULT) String phone,
			@RequestParam(name = Constants.QUERY_REQUEST_FROMDATE, defaultValue = "") String fromDate,
			@RequestParam(name = Constants.QUERY_REQUEST_TODATE, defaultValue = "") String toDate,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "name") String sort) {
		ResponseModel<List<ShipCustomerDTO>> response = new ResponseModel<>();
		name = Utils.decodeParam(name);
		Page<ShipCustomerDTO> data = customerService.findAllCustomers(
				new SearchModel(pageSize, pageNumber, name, phone, sort, fromDate, toDate), roleId, provinceId,
				districtId, false);
		response.setCode(HttpServletResponse.SC_OK);
		response.setMeta(new MetaModel(pageNumber, pageSize, data.getTotalPages(), data.getTotalElements()));
		response.setData(data.getContent());
		return response;
	}
	
	//Them moi khach hàng
	@PostMapping
	public ResponseModel<String> create(HttpServletRequest request,
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader("username") String username,
			@RequestHeader("userId") Integer userId,
			@RequestHeader(value = "customerId", required = false) Integer customerIdHeader,
			@RequestBody ShipCustomerModel model){
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<String> response = new ResponseModel<>();
		response.setMessage(customerService.create(model, userHeader, request));
		response.setCode(HttpServletResponse.SC_CREATED);
		return response;
	}
	
	//Cap nhat khach hang
	@PutMapping(value = "/{customerId}")
	public ResponseModel<String> update(HttpServletRequest request,
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader("userId") Integer userId, 
			@RequestHeader("username") String username,
			@RequestHeader(value = "customerId",required = false) Integer customerIdHeader,
			@PathVariable Integer customerId,
			@RequestBody ShipCustomerModel model) {
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<String> response = new ResponseModel<String>();
		model.setCustomerId(customerId);
		response.setMessage(customerService.update(model, userHeader,request));
		
		return response;
	}
	
	//Xoa khach hang
	@DeleteMapping(value = "/{customerId}")
	public ResponseModel<String> delete(HttpServletRequest request,
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader("userId") Integer userId, 
			@RequestHeader("username") String username,
			@RequestHeader(value = "customerId", required = false) Integer customerIdHeader,
			@PathVariable Integer customerId) {
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<String> response = new ResponseModel<String>();
		response.setMessage(customerService.delete(customerId, userHeader,request));
		
		return response;
	}
	
	//Lay thong tin chi tiet
	@GetMapping(value = "/{customerId}")
	public ResponseModel<ShipCustomerDetailDTO> getCustomer(
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId",required = false) Integer provinceId,
			@RequestHeader(value = "districtId",required = false) Integer districtId,
			@RequestHeader("userId") Integer userId, 
			@RequestHeader(value = "customerId",required = false) Integer customerIdHeader,
			@PathVariable Integer customerId) {
		UserHeader userHeader = new UserHeader(userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<ShipCustomerDetailDTO> response = new ResponseModel<ShipCustomerDetailDTO>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(customerService.getCustomer(customerId, userHeader));
		
		return response;
	}
}
