/**
 * API v1.0 - VTracking v2.0 Project.
 * @author anhth32
 * Created on Apr 8, 2019
 * 
 * Copyright (c) 2019 Viettel Business Solutions Corp.
 */
package com.fms.customerservice.repository;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fms.customerservice.model.CooperativeInforRequest;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;


/**
 * @author anhth32 Created on Apr 8, 2019
 */

@Service
public class CooperativeRepositoryImpl implements CooperativeRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(CooperativeRepositoryImpl.class);
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public CooperativeRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public ResponseModel<Long> saveCooperative(String roleId, String userId, String username, String actionType,
			CooperativeInforRequest request) {
		ResponseModel<Long> results = new ResponseModel<Long>();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		request.setPassword(encoder.encode(request.getPassword()));
		try {
			String SQL = String.format(
					"CALL common.proc_save_cooperative_infor(%s, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', @access_allow, @result)",
					roleId, userId, username, actionType, request.getCooperativeId(), request.getName(), request.getAddress(),
					request.getPhone(), request.getEmail(), request.getUsername(), request.getPassword(), request.getBranchId(), request.getCustomerIds());
//			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			try {
				jdbcTemplate.queryForList(SQL);
			} catch (Exception e) {
				LOGGER.error("error: ",e);
			}
			Map<String, Object> subObject = jdbcTemplate.queryForMap("SELECT @access_allow, @result");
			long accessAllow = 0;
			if (subObject != null) {
				results.setData((Long) subObject.get("@result"));
				accessAllow = (Long) subObject.get("@access_allow");
			}
			// Kiem tra permission (1: pass; 0: fail)
			if (accessAllow == 0) {
				results.setMessage(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			} else {
				if (results.getData() > 0) {
					results.setMessage(Utils.getMessageByKey(Constants.LOCALE_SUCCESS));
				} else {
					results.setMessage(Utils.getErrorMessage(Constants.LOCALE_PROCESS));
				}
			}
			return results;
		} catch (Exception e) {
			LOGGER.error("error: ",e);
			results.setData(Utils.convertToLong(0));
			results.setMessage(Utils.getErrorMessage(Constants.LOCALE_PROCESS));
			return results;
		}
	}

}
