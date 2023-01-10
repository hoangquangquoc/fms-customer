package com.fms.customerservice.model;

public interface TransportDTO {
	String getRegisterNo();

	Integer getTransportId();
	
	String getFromDate();
	
	String getToDate();
	
	Integer getIsWarning();
}
