package com.fms.customerservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fms.customerservice.model.BccsServiceDTO;
import com.fms.customerservice.service.CustomerService;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

@RestController
@RequestMapping("/v1/bccsservices")
public class BccsServiceController {
	@Autowired
	CustomerService customerService;
	// danh sách cđt hthm,km
		@GetMapping(value = "")
		public ResponseModel<List<BccsServiceDTO>> getListBCCSService(@RequestHeader(value = "roleId") Integer roleId,
				@RequestParam(name = "type", defaultValue = "-1") Integer type) {
			ResponseModel<List<BccsServiceDTO>> response = new ResponseModel<>();
			response.setData(customerService.getListBCCSService(roleId, type));
			response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
			return response;
		}
}
