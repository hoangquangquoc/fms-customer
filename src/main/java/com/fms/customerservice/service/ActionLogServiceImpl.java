package com.fms.customerservice.service;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fms.customerservice.model.ActionLog;
import com.fms.customerservice.repository.ActionLogRepository;
import com.fms.module.utils.Constants;
import com.fms.module.utils.LogUtils;

@Service
public class ActionLogServiceImpl implements ActionLogService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ActionLogServiceImpl.class);
	@Autowired
	ActionLogRepository actionLogRepository;

	@Override
	public void createLog(ActionLog log) {
		try {
			actionLogRepository.save(log);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

	@Override
	public ActionLog getActionLog(HttpServletRequest request, String username, String oldValue, String newValue,
			int actionType, int functionType, String content) {
		ActionLog log = new ActionLog();
		log.setModifiedBy(username);
		String ipClientAddress = LogUtils.getIpClientAddress(request);
		log.setNewValue(newValue);
		log.setOldValue(oldValue);
		log.setCreatedDate(LocalDateTime.now());
		log.setIpAddress(ipClientAddress);
		log.setActionType(actionType);
		log.setActionName(Constants.ActionType.parseValue(actionType).getName());
		log.setFunctionType(functionType);
		log.setFunctionName(Constants.FunctionType.parseValue(functionType).getName());
		log.setContent(content);
		return log;
	}

}
