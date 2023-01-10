package com.fms.customerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fms.module.model.LogAction;
import com.fms.module.model.ResponseModel;

@FeignClient(name = "log-service", fallback = LogServiceClientFallBack.class)
public interface LogServiceClient {
	@PostMapping("/logs/v1/logs")
	public ResponseModel<String> create(@RequestBody LogAction log);
}
