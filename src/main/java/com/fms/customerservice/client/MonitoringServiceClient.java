package com.fms.customerservice.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fms.customerservice.model.JourneyDataResponse;
import com.fms.module.model.ResponseModel;

import brave.internal.Nullable;

@FeignClient(name = "monitoring-service", fallback= MonitoringServiceClientFallBack.class)
public interface MonitoringServiceClient {
	@RequestMapping(value = "/monitorings/v1/rawjourneydatas",method = RequestMethod.GET)
 	public ResponseModel<JourneyDataResponse> findRawJourneyDataByTransportIdAndTimeBetween(
 			HttpServletRequest request,
 			@RequestHeader("username") String username,
 			@Nullable @RequestHeader("roleId") Integer roleId,
			@Nullable @RequestHeader("provinceId") Integer provinceId,
			@Nullable @RequestHeader("districtId") Integer districtId,
			@Nullable @RequestHeader("customerId") Integer customerId,
			@Nullable @RequestHeader("userId") Integer userId,
			@RequestHeader(value = "Accept-Language", defaultValue = "vi") String lang,
 			@RequestParam(name="transportid") Integer transportId,
 			@RequestParam(name="fromdate",required=true) String fromDate,
 			@RequestParam(name="todate",required=true) String toDate);
	
	@RequestMapping(value = "/monitorings/v1/rawjourneydatas",method = RequestMethod.GET)
 	public Object test(
 			HttpServletRequest request,
 			@RequestHeader("username") String username,
 			@Nullable @RequestHeader("roleId") Integer roleId,
			@Nullable @RequestHeader("provinceId") Integer provinceId,
			@Nullable @RequestHeader("districtId") Integer districtId,
			@Nullable @RequestHeader("customerId") Integer customerId,
			@Nullable @RequestHeader("userId") Integer userId,
			@RequestHeader(value = "Accept-Language", defaultValue = "vi") String lang,
 			@RequestParam(name="transportid") Integer transportId,
 			@RequestParam(name="fromdate",required=true) String fromDate,
 			@RequestParam(name="todate",required=true) String toDate);
}
