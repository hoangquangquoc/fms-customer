package com.fms.customerservice.service;

import javax.servlet.http.HttpServletRequest;

import com.fms.customerservice.model.ActionLog;

public interface ActionLogService {
	void createLog(ActionLog log);

	ActionLog getActionLog(HttpServletRequest request, String username, String oldValue, String newValue,
			int actionType, int functionType, String content);
}
