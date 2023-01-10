package com.fms.customerservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fms.customerservice.model.TollgateDTO;
import com.fms.customerservice.service.RouteService;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

@RestController
@RequestMapping("/v1/tollgates")
public class TollgateController {
	@Autowired
	RouteService routeService;
	
	@GetMapping
	public ResponseModel<List<TollgateDTO>> getAll() {
		ResponseModel<List<TollgateDTO>> response = new ResponseModel<>();
		response.setData(routeService.getAllTollgate());
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setCode(Constants.SC_OK);
		return response;
	}
}
