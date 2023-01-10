package com.fms.customerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fms.customerservice.model.CooperativeInforRequest;
import com.fms.customerservice.service.CooperativeService;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Constants;

@RestController
@RequestMapping("/v1/cooperatives")
public class CooperativeController {
	@Autowired
	CooperativeService cooperativeService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseModel<Long> processRenewalInfor(@RequestHeader("roleId") String roleId,
			@RequestHeader(name = "provinceId", defaultValue = "") String provinceId,
			@RequestHeader(name = "districtId", defaultValue = "") String districtId,
			@RequestHeader(name = "userId", defaultValue = "") String userId,
			@RequestHeader(name = "username", defaultValue = "") String username,
			@RequestParam(name = "actiontype", required = false, defaultValue = "") String actionType,
			@RequestBody CooperativeInforRequest request) {
		ResponseModel<Long> result = cooperativeService.saveCooperative(roleId, provinceId, userId, username, actionType, request);
		result.setCode(Constants.SC_CREATED);
		return result;
	}
}
