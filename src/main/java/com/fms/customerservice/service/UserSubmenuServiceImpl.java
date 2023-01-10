 package com.fms.customerservice.service;

 import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fms.customerservice.model.UserSubmenu;
import com.fms.customerservice.repository.UserSubmenuRepository;

 @Service
 public class UserSubmenuServiceImpl implements UserSubmenuService{
	 private static final Logger LOGGER = LoggerFactory.getLogger(UserSubmenuServiceImpl.class);
 	@Autowired
 	UserSubmenuRepository userSubmenuRepository;

	Boolean isExist_Id(Integer userSubmenuId){
		List<UserSubmenu> existing = userSubmenuRepository.findByUserSubmenuId(userSubmenuId);
		return !existing.isEmpty();
	}
	@Override
	public String createUserSubmenu(UserSubmenu newUserSubmenu) {
		try {
			userSubmenuRepository.save(newUserSubmenu);
				return "Create Successful!";
		} catch (Exception e) {
			LOGGER.error("error: ",e);
			return "Create Failed!";
		}
		
	}

	@Override
	public String updateUserSubmenu(Integer userSubmenuId, UserSubmenu updateUserSubmenu) {
		try {
			updateUserSubmenu.setUserSubmenuId(userSubmenuId);
			userSubmenuRepository.save(updateUserSubmenu);
			return "Update Successful!";
		} catch (Exception e) {
			LOGGER.error("error: ",e);
			return "Update Failed!";
		}
	}

 	@Override
 	public String deleteByUserSubmenuId(Integer userSubmenuId) {
 		try {
 			if(!isExist_Id(userSubmenuId)){
 				return "UserSubmenu ID " + userSubmenuId + " is not existing!";
 			}
 			else{
 				userSubmenuRepository.deleteByUserSubmenuId(userSubmenuId);
 				return "Delete Successful!";
 			}
 		} catch (Exception e) {
 			LOGGER.error("error: ",e);
 			return "Delete Failed!";
 		}		
 	}

 	@Override
 	public List<UserSubmenu> findAll() {
 		return userSubmenuRepository.findAll();
 	}

 	@Override
 	public List<UserSubmenu> findByUserSubmenuId(Integer userSubmenuId) {
 		try {
 			return userSubmenuRepository.findByUserSubmenuId(userSubmenuId);
 		} catch (Exception e) {
 			LOGGER.error("error: ",e);
 			return null;
 		}
 	}
 	@Override
 	public List<UserSubmenu> findByUserId(Integer userId) {
 		try {
 			return userSubmenuRepository.findByUserId(userId);
 		} catch (Exception e) {
 			LOGGER.error("error: ",e);
 			return null;
 		}
 	}
 }
