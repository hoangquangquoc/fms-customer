package com.fms.customerservice.model;

public interface TipDTO {
	Integer getTipId();
	String getTipName();
	String getSummary();
	String getContent();
	Integer getOrderNum();
	String getUrlImage();
	String getCreatedDate();
}
