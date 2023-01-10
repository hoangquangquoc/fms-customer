package com.fms.customerservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fms.customerservice.model.Driver;
import com.fms.customerservice.model.DriverCustomerDTO;
import com.fms.customerservice.model.DriverDTO;
import com.fms.customerservice.model.responsemodel.Response;
import com.fms.customerservice.service.DriverService;
import com.fms.module.model.MetaModel;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

@RestController
@RequestMapping("/v1/drivers")
public class DriverController {
	@Autowired
	DriverService driverService;

	@GetMapping
	public ResponseModel<List<DriverDTO>> findAll(
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "userId") Integer userId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = "name", defaultValue = Constants.QUERY_VALUE_DEFAULT) String name,
			@RequestParam(name = "id", defaultValue = Constants.QUERY_VALUE_DEFAULT) String id,
			@RequestParam(name = "license", defaultValue = "") String license,
			@RequestParam(name = "phone", defaultValue = "") String phone,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "name") String orderBy) {
		name = Utils.decodeParam(name);
		SearchModel searchModel = new SearchModel(pageSize, pageNumber, name, phone, orderBy, license, null, id);
		ResponseModel<List<DriverDTO>> response = new ResponseModel<>();
		UserHeader userHeader = new UserHeader(userId, roleId, provinceId, districtId, customerId);
		Page<DriverDTO> data = driverService.findAll(searchModel, userHeader, false);
		
		response.setCode(HttpServletResponse.SC_OK);
		response.setMeta(new MetaModel(searchModel.getPageNumber(), searchModel.getPageSize(), data.getTotalPages(),
				data.getTotalElements()));
		response.setData(data.getContent());
		
		return response;
	}
		
	@GetMapping("/export")
	public ResponseEntity<InputStreamResource> export(
			@RequestHeader(name = "Accept-Language", defaultValue = "vi") String acceptLang,
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "userId") Integer userId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize,
			@RequestParam(name = "name", defaultValue = Constants.QUERY_VALUE_DEFAULT) String name,
			@RequestParam(name = "id", defaultValue = Constants.QUERY_VALUE_DEFAULT) String id,
			@RequestParam(name = "license", defaultValue = "") String license,
			@RequestParam(name = "dob", defaultValue = Constants.QUERY_VALUE_DEFAULT) String dob,
			@RequestParam(name = "phone", defaultValue = "") String phone,
			@RequestParam(name = "type", defaultValue = "excel") String type,
			@RequestParam(name = Constants.SORT_REQUEST_PARAM, defaultValue = "name") String orderBy) {
		UserHeader userHeader = new UserHeader(userId, roleId, provinceId, districtId, customerId);
		userHeader.setAcceptLang(acceptLang);
		name = Utils.decodeParam(name);
		SearchModel searchModel = new SearchModel(pageSize, pageNumber, name, phone, orderBy, license, dob, id);
		return driverService.export(searchModel, userHeader, type);
	}

	@PostMapping
	public ResponseModel<String> createDriver(
			@RequestHeader(value = "username") String username,
			@RequestBody @Valid Driver newDriver) {
		ResponseModel<String> response = new ResponseModel<String>();
		String responseMessage = driverService.createDriver(newDriver, username);
		response.setCode(HttpServletResponse.SC_CREATED);
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setMessage(responseMessage);
		return response;
	}

	@RequestMapping(value = "/{driverId}", method = RequestMethod.GET)
	public Response findBy_Id(@PathVariable Integer driverId) {
		Response response = new Response();
		response.setCode(HttpServletResponse.SC_OK);
		response.setData(driverService.findByDriverId(driverId));
		return response;
	}

	@RequestMapping(value = "/{driverId}", method = RequestMethod.DELETE)
	public ResponseModel<String> deleteBy_Id(
			@RequestHeader(value = "username") String username,
			@PathVariable Integer driverId) {
		ResponseModel<String> response = new ResponseModel<String>();
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage(driverService.deleteByDriverId(driverId, username));
		return response;
	}

	@RequestMapping(value = "/{driverId}", method = RequestMethod.PUT)
	public ResponseModel<String> updateDriver(
			@RequestHeader(value = "username") String username,
			@PathVariable Integer driverId, 
			@RequestBody Driver updateDriver) {
		ResponseModel<String> response = new ResponseModel<String>();
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage(driverService.updateDriver(driverId, updateDriver, username));
		return response;
	}
	
	@GetMapping(value = "/customers/{customerId}")
	public ResponseModel<List<DriverCustomerDTO>> getDriverOfCustomer(
			@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerIdLogin,
			@PathVariable Integer customerId) {
		ResponseModel<List<DriverCustomerDTO>> response = new ResponseModel<List<DriverCustomerDTO>>();
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(driverService.getDriverOfCustomer(customerId, roleId, customerIdLogin, provinceId, districtId));
		return response;
	}
}
