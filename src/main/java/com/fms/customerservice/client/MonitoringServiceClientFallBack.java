package com.fms.customerservice.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.fms.customerservice.model.JourneyDataResponse;
import com.fms.module.model.ResponseModel;

@Component
public class MonitoringServiceClientFallBack implements MonitoringServiceClient{

	@Override
	public ResponseModel<JourneyDataResponse> findRawJourneyDataByTransportIdAndTimeBetween(HttpServletRequest request,
			String username, Integer roleId, Integer provinceId, Integer districtId, Integer customerId, Integer userId,
			String lang, Integer transportId, String fromDate, String toDate) {
		return new ResponseModel<JourneyDataResponse>();
	}

	@Override
	public ResponseModel<Object> test(HttpServletRequest request, String username, Integer roleId, Integer provinceId,
			Integer districtId, Integer customerId, Integer userId, String lang, Integer transportId, String fromDate,
			String toDate) {
		return new ResponseModel<Object>();
	}

}
