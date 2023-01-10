package com.fms.customerservice.client;

import org.springframework.stereotype.Component;

import com.fms.customerservice.controller.utils.Constants;
import com.fms.module.model.LogAction;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Utils;

@Component
public class LogServiceClientFallBack implements LogServiceClient{

	@Override
	public ResponseModel<String> create(LogAction log) {
		ResponseModel<String> response = new ResponseModel<String>();
		response.setMessage(Utils.getMessageByKey(Constants.ERROR_MESSAGE));
		return response;
	}

}
