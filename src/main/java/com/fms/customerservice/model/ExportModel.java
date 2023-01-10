/**
 * API v1.0 - VTracking v2.0 Project.
 * @author anhth32
 * Created on Mar 4, 2019
 * 
 * Copyright (c) 2019 Viettel Business Solutions Corp.
 */
package com.fms.customerservice.model;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author anhth32 Created on Mar 4, 2019
 */
public class ExportModel extends RuntimeException {
	private static final long serialVersionUID = 1L;
//	private File file;
	private ByteArrayInputStream file;
	private String fileName = "";
	private String fileExtension = "";

//	public File getFile() {
//		return file;
//	}
//
//	public void setFile(File file) {
//		this.file = file;
//	}

	public String getFileName() {
		return fileName;
	}

	public ByteArrayInputStream getFile() {
		return file;
	}

	public void setFile(ByteArrayInputStream file) {
		this.file = file;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Autowired
	public ExportModel(ByteArrayInputStream file, String fileName, String fileExtension) {
		super();
		this.file = file;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
	}
}
