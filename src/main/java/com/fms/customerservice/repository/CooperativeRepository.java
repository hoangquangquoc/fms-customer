package com.fms.customerservice.repository;

import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.CooperativeInforRequest;
import com.fms.module.model.ResponseModel;

@Repository
public interface CooperativeRepository {
	ResponseModel<Long> saveCooperative(String roleId,String userId, String username, String actionType, CooperativeInforRequest request);
}
