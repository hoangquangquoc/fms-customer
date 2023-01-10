/**
 * API v1.0 - VTracking v2.0 Project.
 * @author anhth32
 * Created on Mar 4, 2019
 * 
 * Copyright (c) 2019 Viettel Business Solutions Corp.
 */
package com.fms.customerservice;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fms.module.exception.BusinessException;
import com.fms.module.exception.ErrorException;
import com.fms.module.exception.NotFoundException;
import com.fms.module.model.ErrorModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Logs;
import com.fms.module.utils.Utils;

/**
 * @author anhth32 Created on Mar 4, 2019
 */
@ControllerAdvice
public class HandleExceptionControllerAdvice {
	@ExceptionHandler(value = ErrorException.class)
	public ResponseEntity<ErrorModel> errorException(ErrorException exception) {
		ErrorModel error = null;
		HttpStatus status = null;
		if (exception.getErrorException() != null
				&& exception.getErrorException().getClass().equals(BusinessException.class)) {
			BusinessException businessException = (BusinessException) exception.getErrorException();
			error = new ErrorModel(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
					businessException.getErrorMessage());
			status = HttpStatus.BAD_REQUEST;
		} else if (exception.getErrorException() != null
				&& exception.getErrorException().getClass().equals(NotFoundException.class)) {
			error = new ErrorModel(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(),
					Utils.getMessageByKey(Constants.LOCALE_NOT_FOUND));
			status = HttpStatus.NOT_FOUND;
		} else {
			Logs.error(exception.getClass(),
					String.format("%s\n%s", exception.getMessage(), exception.getStackTrace()));
			error = new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					Utils.getErrorMessage(Constants.LOCALE_PROCESS));
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(error, status);
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public ResponseEntity<ErrorModel> handleBindingErrors(Exception exception) {
		ErrorModel error = null;
		HttpStatus status = null;
		BindingResult result = ((MethodArgumentNotValidException) exception).getBindingResult();
		FieldError fieldError = result.getFieldErrors().get(0);
		Logs.error(exception.getClass(), String.format("%s\n%s", exception.getMessage(), exception.getStackTrace()));
		if ("Size".equals(fieldError.getCode())) {
			String field = fieldError.getField();
			int size = (int) fieldError.getArguments()[1];
			String errorMsg = Utils.getMessageMaxlength(field, size);

			error = new ErrorModel(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), errorMsg);
			status = HttpStatus.BAD_REQUEST;
		} else if ("NotBlank".equals(fieldError.getCode())) {
			String field = fieldError.getField();
			String errorMsg = Utils.getErrorMessageValidate(field, Constants.NOTBLANK);
			error = new ErrorModel(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), errorMsg);
			status = HttpStatus.BAD_REQUEST;
		} else if ("Email".equals(fieldError.getCode())) {
			String field = fieldError.getField();
			String errorMsg = Utils.getErrorMessageValidate(field, Constants.ERROR_FORMAT);
			error = new ErrorModel(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), errorMsg);
			status = HttpStatus.BAD_REQUEST;
		} else if ("Range".equals(fieldError.getCode())) {
			String field = fieldError.getField();
			Long size = (Long) fieldError.getArguments()[1];
			String errorMsg = Utils.getMessageRange(field, size.intValue());
			error = new ErrorModel(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), errorMsg);
			status = HttpStatus.BAD_REQUEST;
		} else {
			error = new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
					Utils.getErrorMessage(Constants.LOCALE_PROCESS));
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(error, status);
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public ResponseEntity<ErrorModel> handleHttpErrors(Exception exception) {
		ErrorModel error = null;
		HttpStatus status = null;
		error = new ErrorModel(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
				Utils.getValidateMessage("error_input"));
		status = HttpStatus.BAD_REQUEST;
		return new ResponseEntity<>(error, status);
	}

	@ExceptionHandler(TypeMismatchException.class)
	public ResponseEntity<ErrorModel> handleConverterErrors(TypeMismatchException exception) {
		ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(),
				Utils.getValidateMessage("error_input"));
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return new ResponseEntity<>(error, status);
	}

}
