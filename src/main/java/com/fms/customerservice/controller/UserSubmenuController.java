 package com.fms.customerservice.controller;

 import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fms.customerservice.model.UserSubmenu;
import com.fms.customerservice.model.responsemodel.Response;
import com.fms.customerservice.service.UserSubmenuService;


 @RestController
 @RequestMapping("/v1/usersubmenus")
 public class UserSubmenuController {
 	/* API UserSubmenu
 	 * author: hoadp
 	 */
 	@Autowired
 	UserSubmenuService userSubmenuService;
	
 	@RequestMapping(value = "",method = RequestMethod.GET)
 	public Response findAll(){
 		Response response = new Response();
 		response.setCode(HttpServletResponse.SC_OK);
 		response.setMessage("Success");
 		response.setData(userSubmenuService.findAll());
 		return response;
 	}
	
 	@RequestMapping(value = "",method = RequestMethod.POST)
 	public Response createUserSubmenu(@RequestBody UserSubmenu newUserSubmenu){
 		Response response = new Response();
 		String responseMassage = userSubmenuService.createUserSubmenu(newUserSubmenu);
 		if("UserSubmenu".equals(responseMassage.substring(0,11)))response.setCode(HttpServletResponse.SC_BAD_REQUEST);
 		else response.setCode(HttpServletResponse.SC_CREATED);
 		response.setMessage(responseMassage);
 		return response;
 	}
	
 	@RequestMapping(value = "/{userSubmenuId}",method = RequestMethod.GET)
 	public Response findBy_Id(@PathVariable Integer userSubmenuId){
 		Response response = new Response();
 		response.setCode(HttpServletResponse.SC_OK);
 		response.setData(userSubmenuService.findByUserSubmenuId(userSubmenuId));
 		return response;	
 	}
	
 	@RequestMapping(value = "/id={userSubmenuId}",method = RequestMethod.DELETE)
 	public Response deleteBy_Id(@PathVariable Integer userSubmenuId){
 		Response response = new Response();
 		response.setCode(HttpServletResponse.SC_OK);
 		response.setMessage(userSubmenuService.deleteByUserSubmenuId(userSubmenuId));
 		return response;
 	}	
 	@RequestMapping(value = "/id={userSubmenuId}",method = RequestMethod.PUT)
 	public Response updateUserSubmenu(@PathVariable Integer userSubmenuId, @RequestBody UserSubmenu updateUserSubmenu){
 		Response response = new Response();
 		response.setCode(HttpServletResponse.SC_OK);
 		response.setMessage(userSubmenuService.updateUserSubmenu(userSubmenuId, updateUserSubmenu));
 		return response;
 	}
 }
