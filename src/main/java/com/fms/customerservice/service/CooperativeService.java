package com.fms.customerservice.service;

import com.fms.customerservice.model.CooperativeInforRequest;
import com.fms.module.model.ResponseModel;

public interface CooperativeService {

	ResponseModel<Long> saveCooperative(String roleId, String provinceId, String userId,  String username,
			String actionType, CooperativeInforRequest request);
}
