package com.fms.customerservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
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

import com.fms.customerservice.client.MonitoringServiceClient;
import com.fms.customerservice.model.AccountForm;
import com.fms.customerservice.model.CustomerAccount;
import com.fms.customerservice.model.CustomerConfig;
import com.fms.customerservice.model.CustomerDTO;
import com.fms.customerservice.model.CustomerDetailDTO;
import com.fms.customerservice.model.CustomerModel;
import com.fms.customerservice.model.DepartmentDTO;
import com.fms.customerservice.model.OrderPackageRequest;
import com.fms.customerservice.model.TransferCustomer;
import com.fms.customerservice.model.VehiclesAssignDTO;
import com.fms.customerservice.model.responsemodel.Response;
import com.fms.customerservice.service.CustomerService;
import com.fms.module.model.CheckPermissionCustomerForm;
import com.fms.module.model.MetaModel;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

import brave.internal.Nullable;

@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
	@Autowired
	CustomerService customerService;
	@Autowired
	MonitoringServiceClient monitoringServiceClient;

	@GetMapping
	public ResponseModel<List<CustomerDTO>> findAll(@Nullable @RequestHeader(value = "roleId") Integer roleId,
			@Nullable @RequestHeader(value = "provinceId") Integer provinceId,
			@Nullable @RequestHeader(value = "districtId") Integer districtId,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = "name", defaultValue = Constants.QUERY_VALUE_DEFAULT) String name,
			@RequestParam(name = "phone", defaultValue = Constants.QUERY_VALUE_DEFAULT) String phone,
			@RequestParam(name = Constants.QUERY_REQUEST_FROMDATE, defaultValue = "") String fromDate,
			@RequestParam(name = Constants.QUERY_REQUEST_TODATE, defaultValue = "") String toDate,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "name") String sort) {
		ResponseModel<List<CustomerDTO>> response = new ResponseModel<>();
		name = Utils.decodeParam(name);
		Page<CustomerDTO> data = customerService.findAllCustomers(
				new SearchModel(pageSize, pageNumber, name, phone, sort, fromDate, toDate), roleId, provinceId,
				districtId, false);
		response.setCode(HttpServletResponse.SC_OK);
		response.setMeta(new MetaModel(pageNumber, pageSize, data.getTotalPages(), data.getTotalElements()));
		response.setData(data.getContent());
		return response;
	}

	@GetMapping("/export")
	public ResponseEntity<InputStreamResource> export(
			@RequestHeader(name = "Accept-Language", defaultValue = "vi") String acceptLang,
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@Nullable @RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = "name", defaultValue = Constants.QUERY_VALUE_DEFAULT) String name,
			@RequestParam(name = "phone", defaultValue = Constants.QUERY_VALUE_DEFAULT) String phone,
			@RequestParam(name = "type", defaultValue = "excel") String type,
			@RequestParam(name = Constants.QUERY_REQUEST_FROMDATE, defaultValue = "") String fromDate,
			@RequestParam(name = Constants.QUERY_REQUEST_TODATE, defaultValue = "") String toDate,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "name") String sort) {
		name = Utils.decodeParam(name);
		SearchModel searchModel = new SearchModel(pageSize, pageNumber, name, phone, sort, fromDate, toDate);
		UserHeader userHeader = new UserHeader(null, roleId, provinceId, districtId, null);
		userHeader.setAcceptLang(acceptLang);
		return customerService.export(searchModel, userHeader, type);
	}

	// dieu chuyen khach hang
	@PostMapping("/transfer")
	public ResponseModel<String> transferCustomer(HttpServletRequest request, @RequestHeader("username") String username,
			@RequestHeader(value = "roleId") Integer roleId, @RequestBody TransferCustomer transfer) {
		ResponseModel<String> response = new ResponseModel<>();
		response.setMessage(customerService.tranferCustomer(transfer.getCustomerId(), roleId, transfer.getProvinceId(), username, request));
		return response;
	}

	// nhom khach hang theo role
	@GetMapping(value = "/groups")
	public Response retrieveCustomer(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestParam(name = "type", defaultValue = "view") String type) {

		Response response = new Response();
		response.setData(customerService.getCutomerGroup(roleId, type, provinceId, districtId, customerId));
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		return response;
	}

	// lay nhom khach hang theo provinceId
	@GetMapping(value = "/provinces/{provinceId}")
	public Response getCustomerByProvince(@PathVariable Integer provinceId) {
		Response response = new Response();
		response.setData(customerService.getCutomerOfProvince(provinceId));
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		return response;
	}
	
	@RequestMapping(value = "/permission", method = RequestMethod.POST)
	public ResponseModel<Boolean> checkPermissionCustomer(
			@RequestBody CheckPermissionCustomerForm checkPermissionCustomerForm) {
		ResponseModel<Boolean> response = new ResponseModel<Boolean>();
		response.setData(customerService.checkPermissionCustomer(checkPermissionCustomerForm));
		return response;
	}

	@GetMapping(value = "/departments")
	public ResponseModel<List<DepartmentDTO>> getDepartments(
			@RequestParam(name = "customerId", defaultValue = Constants.QUERY_VALUE_DEFAULT) Integer customerId) {
		ResponseModel<List<DepartmentDTO>> response = new ResponseModel<List<DepartmentDTO>>();
		response.setData(customerService.getDepartmentOfCustomer(customerId));
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));

		return response;
	}

	// lay danh sach xe theo khach hang (quan ly lo trinh)
	@GetMapping("/vehicles")
	public ResponseModel<List<VehiclesAssignDTO>> getVehicles(
			@RequestHeader("roleId") Integer roleId,
			@RequestHeader("username") String username,
			@RequestHeader("userId") Integer userId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerIdHeader,
			@RequestParam(name = "customerId", defaultValue = "-1") Integer customerId,
			@RequestParam(name = "routeId", defaultValue = "-1") Integer routeId) {
		UserHeader userHeader = new UserHeader(userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<List<VehiclesAssignDTO>> response = new ResponseModel<List<VehiclesAssignDTO>>();
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(customerService.getVehicles(customerId, routeId, userHeader));
		return response;
	}

	//luu cau hinh
	@PutMapping(value = "/config/{configId}")
	public ResponseModel<String> config(
			@PathVariable Integer configId,
			@RequestHeader("roleId") Integer roleId,
			@RequestHeader("username") String username,
			@RequestHeader("userId") Integer userId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@Valid @RequestBody CustomerConfig config) {
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerId);
		ResponseModel<String> response = new ResponseModel<String>();
		config.setConfigId(configId);
		response.setMessage(customerService.updateConfig(config, userHeader));
		return response;
	}

	//lay thong tin cau hinh
	@GetMapping(value = "/config/{customerId}")
	public ResponseModel<Object> getConfig(
			@PathVariable Integer customerId,
			@RequestHeader("roleId") Integer roleId,
			@RequestHeader("username") String username,
			@RequestHeader("userId") Integer userId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerIdHeader) {
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<Object> response = new ResponseModel<Object>();
		response.setData(customerService.getConfig(customerId, userHeader));
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		
		return response;
	}
	
	// Lay danh sach customer khong phai HTX va chua thuoc HTX nao
	@GetMapping(value = "/copperatives/provinces/{provinceId}")
	public Response getCustomerByProvinceId(@PathVariable Integer provinceId) {
		Response response = new Response();
		response.setData(customerService.getCutomerByProvinceId(provinceId));
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		return response;
	}
	
	@PostMapping
	public ResponseModel<String> create(HttpServletRequest request,
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader("username") String username,
			@RequestHeader("userId") Integer userId,
			@RequestHeader(value = "customerId", required = false) Integer customerIdHeader,
			@RequestBody CustomerModel model){
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<String> response = new ResponseModel<>();
		response.setMessage(customerService.create(model, userHeader, request));
		response.setCode(HttpServletResponse.SC_CREATED);
		return response;
	}
	
	@GetMapping(value = "/{customerId}")
	public ResponseModel<CustomerDetailDTO> getCustomer(
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId",required = false) Integer provinceId,
			@RequestHeader(value = "districtId",required = false) Integer districtId,
			@RequestHeader("userId") Integer userId, 
			@RequestHeader(value = "customerId",required = false) Integer customerIdHeader,
			@PathVariable Integer customerId) {
		UserHeader userHeader = new UserHeader(userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<CustomerDetailDTO> response = new ResponseModel<CustomerDetailDTO>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(customerService.getCustomer(customerId, userHeader));
		
		return response;
	}
	
	@PutMapping(value = "/{customerId}")
	public ResponseModel<String> update(HttpServletRequest request,
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader("userId") Integer userId, 
			@RequestHeader("username") String username,
			@RequestHeader(value = "customerId",required = false) Integer customerIdHeader,
			@PathVariable Integer customerId,
			@RequestBody CustomerModel model) {
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<String> response = new ResponseModel<String>();
		model.setCustomerId(customerId);
		response.setMessage(customerService.update(model, userHeader,request));
		
		return response;
	}
	
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
	
	@GetMapping(value = "/{customerId}/accounts")
	public ResponseModel<List<CustomerAccount>> getListAccountByCustomerId(
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId",required = false) Integer provinceId,
			@RequestHeader(value = "districtId",required = false) Integer districtId,
			@RequestHeader("userId") Integer userId, 
			@RequestHeader(value = "customerId",required = false) Integer customerIdHeader,
			@PathVariable Integer customerId) {
		UserHeader userHeader = new UserHeader(userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<List<CustomerAccount>> response = new ResponseModel<List<CustomerAccount>>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(customerService.getListAccountByCustomerId(customerId, userHeader));
		
		return response;
	}
	
	@DeleteMapping(value = "/accounts/{customerAccountId}")
	public ResponseModel<String> deleteAccount(
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId",required = false) Integer provinceId,
			@RequestHeader(value = "districtId",required = false) Integer districtId,
			@RequestHeader("userId") Integer userId, 
			@RequestHeader("username") String username,
			@RequestHeader(value = "customerId",required = false) Integer customerIdHeader,
			@PathVariable Integer customerAccountId) {
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<String> response = new ResponseModel<String>();
		customerService.deleteAccount(customerAccountId, userHeader);
		response.setMessage(Utils.getSuccessMessage(Constants.LOCALE_DELETE));
		
		return response;
	}
	
	@PostMapping(value = "/accounts")
	public ResponseModel<String> createAccount(
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId",required = false) Integer provinceId,
			@RequestHeader(value = "districtId",required = false) Integer districtId,
			@RequestHeader("userId") Integer userId, 
			@RequestHeader("username") String username,
			@RequestHeader(value = "customerId",required = false) Integer customerIdHeader,
			@RequestBody AccountForm accountForm) {
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<String> response = new ResponseModel<String>();
		customerService.createAccount(accountForm, userHeader);
		response.setMessage(Utils.getSuccessMessage(Constants.LOCALE_CREATE));
		
		return response;
	}
	
	@PostMapping(value = "/orders/packages")
	public ResponseModel<String> orderPackage(
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId",required = false) Integer provinceId,
			@RequestHeader(value = "districtId",required = false) Integer districtId,
			@RequestHeader("userId") Integer userId, 
			@RequestHeader("username") String username,
			@RequestHeader(value = "customerId",required = false) Integer customerIdHeader,
			@RequestBody OrderPackageRequest orderPackage) {
		UserHeader userHeader = new UserHeader(username, userId, roleId, provinceId, districtId, customerIdHeader);
		ResponseModel<String> response = new ResponseModel<String>();
		customerService.orderPackage(orderPackage, userHeader);
		response.setMessage(Utils.getSuccessMessage(Constants.LOCALE_CREATE));
		
		return response;
	}
}
