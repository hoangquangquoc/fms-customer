package com.fms.customerservice.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fms.customerservice.model.ExportModel;
import com.fms.module.exception.BusinessException;
import com.fms.module.exception.ErrorException;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

public class ExportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExportController.class);

	public MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
		String mineType = servletContext.getMimeType(fileName);
		try {
			if (mineType == null) {
				mineType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		    }
			MediaType mediaType = MediaType.parseMediaType(mineType);
			return mediaType;
		} catch (Exception e) {
			LOGGER.error("error: ",e);
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
	
	public ResponseEntity<InputStreamResource> responseExportFile(ExportModel export, ServletContext servletContext) throws IOException {
		//String fileName = String.format("%s.%s", export.getFileName(), export.getFileExtension());
		String fileName = Utils.getFileNameExport(export.getFileName(), export.getFileExtension());
		try {
			ByteArrayInputStream file = export.getFile();
			MediaType mediaType = getMediaTypeForFileName(servletContext, fileName);
			InputStreamResource resource = new InputStreamResource(file);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
					.contentType(mediaType).body(resource);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
			throw new ErrorException(new BusinessException(Utils.getErrorMessage(Constants.LOCALE_PROCESS)));
		}
	}
}
