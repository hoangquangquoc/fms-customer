package com.fms.customerservice.model;

public interface VideoDTO {
	Integer getVideoId();
	String getVideoName();
	String getDescription();
	String getLink();
	Integer getOrderNum();
	Integer getType();
	String getUploadedDate();
}
