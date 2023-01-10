package com.fms.customerservice.controller;
///**
// * API v1.0 - VTracking v2.0 Project.
// * @author anhth32
// * Created on May 9, 2019
// * 
// * Copyright (c) 2019 Viettel Business Solutions Corp.
// */
//package com.vtracking.customerservice.controller;
//
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
//import org.springframework.boot.web.servlet.error.ErrorAttributes;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.vtracking.module.exception.BusinessException;
//import com.vtracking.module.exception.ErrorException;
//import com.vtracking.module.utils.Constants;
//import com.vtracking.module.utils.Utils;
//
///**
// * @author anhth32 Created on May 9, 2019
// */
//@Controller
//@RequestMapping("${server.error.path:${error.path:/error}}")
//public class BaseReportController extends AbstractErrorController {
//	/**
//	 * @param errorAttributes
//	 */
//	public BaseReportController(ErrorAttributes errorAttributes) {
//		super(errorAttributes);
//		// TODO Auto-generated constructor stub
//	}
//
//	@RequestMapping
//	public void error(HttpServletRequest request) {
//		Map<String, Object> body = getErrorAttributes(request, true);
//		String message = body.get("message") != null ? body.get("message").toString().toLowerCase() : "";
//		if (message.contains("failed to convert value") || message.contains("json parse error")) {
//			throw new ErrorException(new BusinessException(Utils.getValidateMessage(Constants.LOCALE_INVALID_INPUT)));
//		} else {
//			throw new ErrorException(new BusinessException(Utils.getErrorMessage(Constants.LOCALE_PROCESS)));
//		}
//	}
//
//	@Override
//	public String getErrorPath() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
