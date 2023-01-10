package com.fms.customerservice.client;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fms.customerservice.model.responsemodel.Response;
import com.fms.module.model.UserSubmenuModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

@Component
public class CategoryServiceClientFallBack implements CategoryServiceClient {

	@Override
	public Response retrieveRole(String id) {
		Response response = new Response();
		response.setMessage("Fall Back fail from CategoryServiceClientFallBack");
		return response;
	}

	@Override
	public Response createUserSubmenu(UserSubmenuModel userSubmenu) {
		Response response = new Response();
		response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setMessage(Utils.getMessageByKey(Constants.ERROR_MESSAGE));
		return response;
	}

	@Override
	public Response getFunctions(String username) {
		Response response = new Response();
		response.setMessage("createUserSubmenu Fall Back fail from CategoryServiceClientFallBack");
		return response;
	}

}
