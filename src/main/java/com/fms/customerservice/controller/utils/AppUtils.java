package com.fms.customerservice.controller.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppUtils.class);
	private static MessageSource messageSource;

	@Autowired
	AppUtils(MessageSource messageSource) {
		AppUtils.messageSource = messageSource;
	}

	public static String getMessage(String msgCode) {
		try {
			Locale locale = LocaleContextHolder.getLocale();
			return messageSource.getMessage(msgCode, null, locale);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
			return "";
		}
	}

	/*
	 * ham kiem tra do manh mat khau
	 */
	public static boolean isValidPass(String pass) {
//		 (?=.*[0-9]) a digit must occur at least once
//		 (?=.*[a-z]) a lower case letter must occur at least once
//		 (?=.*[A-Z]) an upper case letter must occur at least once
//		 (?=.*[@#$%^&+=]) a special character must occur at least once
//		 (?=\\S+$) no whitespace allowed in the entire string
//		 .{8,} at least 8 characters
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}";
		return pass.matches(pattern);
	}

	public static boolean isNullOrEmpty(String strInput) {
		if (strInput == null || strInput.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public static Date convertToDate(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		simpleDateFormat.setLenient(false);
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			LOGGER.error("error: ",e);
			return null;
		}
	}

	public static String formatDateQuery(String date){
		try {
			Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		    String parsedDate = formatter.format(initDate);

		    return parsedDate;
		} catch (ParseException e) {
			LOGGER.error("error: ",e);
			return null;
		}
	    
	}
}
