package com.fms.customerservice.model;

public interface StopPointDetails {
	String getName();

	Integer getStopPointTypeId();

	Integer getCustomerId();

	Integer getStopTime();

	Integer getRadius();

	Double getLat();

	Double getLng();

	String getNote();
}
