package com.fms.customerservice.client;

import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class AuthServiceClientFallBack implements AuthServiceClient {

	@Override
	@HystrixCommand(fallbackMethod = "getcurrentUser")
	public Object getcurrentUser() {
		return "Khong co thong tin user";
	}

}
