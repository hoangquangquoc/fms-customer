package com.fms.customerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;

@FeignClient(name = "A-service", fallback = AuthServiceClientFallBack.class)
public interface AuthServiceClient {
	@RequestMapping(method = RequestMethod.GET, value = "/test", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	Object getcurrentUser();
}
