package com.fms.customerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fms.customerservice.model.responsemodel.Response;
import com.fms.module.model.UserSubmenuModel;

@FeignClient(name = "category-service", fallback = CategoryServiceClientFallBack.class)
public interface CategoryServiceClient {
	@GetMapping(value = "/categories/v1/roles/roleId={_id}")
	public Response retrieveRole(@PathVariable String _id);

	@PostMapping(value = "/categories/v1/usersubmenu")
	public Response createUserSubmenu(@RequestBody UserSubmenuModel userSubmenu);

	@GetMapping(value = "/categories/v1/usersubmenu")
	public Response getFunctions(String username);
}
