package com.fms.customerservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fms.customerservice.controller.utils.Constants;
import com.fms.customerservice.model.SupportInfoList;
import com.fms.customerservice.model.SupportSMSList;
import com.fms.customerservice.model.TipDTO;
import com.fms.customerservice.model.VideoDTO;
import com.fms.customerservice.service.SupportService;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Utils;

@RestController
@RequestMapping("/v1/")
public class SupportController {
	@Autowired
	SupportService service;

	@GetMapping("supportinfo")
	public ResponseModel<SupportInfoList> getSupportInfo() {
		ResponseModel<SupportInfoList> response = new ResponseModel<>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(service.getSupportInfo());
		return response;
	}
	
	@GetMapping("supportsms")
	public ResponseModel<List<SupportSMSList>> getSupportSms() {
		ResponseModel<List<SupportSMSList>> response = new ResponseModel<>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(service.getSupportSMS());
		return response;
	}
	
	@GetMapping("videos")
	public ResponseModel<List<VideoDTO>> getVideos() {
		ResponseModel<List<VideoDTO>> response = new ResponseModel<>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(service.getListVideo());
		return response;
	}
	
	@GetMapping("tips")
	public ResponseModel<List<TipDTO>> getTips() {
		ResponseModel<List<TipDTO>> response = new ResponseModel<>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(service.getListTip());
		return response;
	}
}
