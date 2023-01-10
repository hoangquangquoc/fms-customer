package com.fms.customerservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.fms.customerservice.model.StopPoint;
import com.fms.customerservice.model.StopPointDTO;
import com.fms.customerservice.model.StopPointDetails;
import com.fms.customerservice.service.StopPointService;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

@RestController
@RequestMapping("/v1/stoppoints")
public class StopPointController {
	@Autowired
	StopPointService stopPointService;

	@GetMapping
	public ResponseModel<List<StopPointDTO>> getAll(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerIdHeader,
			@RequestParam(name = "customerId", defaultValue = "-1") Integer customerId,
			@RequestParam(name = "name", defaultValue = Constants.QUERY_VALUE_DEFAULT) String name,
			@RequestParam(name = "type", defaultValue = "-1") Integer type,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, required = false) Integer pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, required = false) Integer pageSize) {
		name = Utils.decodeParam(name);
		return stopPointService.getAll(pageNumber, pageSize, roleId, provinceId, districtId, customerId, customerIdHeader, type,
				name);
	}

	@PostMapping
	public ResponseModel<String> create(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestHeader(value = "username") String username, @Valid @RequestBody StopPoint stopPoint) {

		ResponseModel<String> response = new ResponseModel<>();
		String message = stopPointService.create(roleId, provinceId, districtId, customerId, username, stopPoint);
		response.setMessage(message);
		response.setCode(Constants.SC_CREATED);
		return response;
	}

	@PutMapping("/{stopPointId}")
	public ResponseModel<String> update(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestHeader(value = "username") String username, @PathVariable Integer stopPointId,
			@Valid @RequestBody StopPoint stopPoint) {

		ResponseModel<String> response = new ResponseModel<>();
		stopPoint.setStopPointId(stopPointId);
		String message = stopPointService.update(roleId, provinceId, districtId, customerId, username, stopPoint);
		response.setMessage(message);
		return response;
	}

	@DeleteMapping("/{stopPointId}")
	public ResponseModel<String> delete(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@RequestHeader(value = "username") String username, @PathVariable Integer stopPointId) {

		ResponseModel<String> response = new ResponseModel<>();
		String message = stopPointService.delete(roleId, provinceId, districtId, customerId, username, stopPointId);
		response.setMessage(message);
		return response;
	}

	@GetMapping("/{stopPointId}")
	public ResponseModel<StopPointDetails> getDetails(@RequestHeader(value = "roleId") Integer roleId,
			@RequestHeader(value = "provinceId", required = false) Integer provinceId,
			@RequestHeader(value = "districtId", required = false) Integer districtId,
			@RequestHeader(value = "customerId", required = false) Integer customerId,
			@PathVariable Integer stopPointId) {
		ResponseModel<StopPointDetails> response = new ResponseModel<StopPointDetails>();
		StopPointDetails item = stopPointService.getDetails(roleId, provinceId, districtId, customerId, stopPointId);
		response.setMessage(Constants.SUCCESS_MESSAGE);
		response.setData(item);
		return response;
	}
}
