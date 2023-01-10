package com.fms.customerservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fms.customerservice.model.StopPointTypeDTO;
import com.fms.customerservice.service.StopPointTypeService;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

@RestController
@RequestMapping("/v1/stoppointtypes")
public class StopPointTypeController {
	@Autowired
	StopPointTypeService stopPointTypeService;

	@GetMapping
	public ResponseModel<List<StopPointTypeDTO>> findAll() {
		ResponseModel<List<StopPointTypeDTO>> response = new ResponseModel<>();

		List<StopPointTypeDTO> data = stopPointTypeService.getAllStopPointType();
		response.setData(data);
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		return response;
	}
}
