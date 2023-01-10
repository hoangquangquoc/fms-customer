package com.fms.customerservice.service;

import java.util.List;

import com.fms.customerservice.model.UserSubmenu;

public interface UserSubmenuService {
	String createUserSubmenu(UserSubmenu newUserSubmenu);
 	String updateUserSubmenu(Integer UserSubmenuId,UserSubmenu updateUserSubmenu);
 	String deleteByUserSubmenuId(Integer userSubmenuId);
	
 	List<UserSubmenu> findAll();
 	List<UserSubmenu> findByUserSubmenuId(Integer userSubmenuId);
 	List<UserSubmenu> findByUserId(Integer userId);
}
