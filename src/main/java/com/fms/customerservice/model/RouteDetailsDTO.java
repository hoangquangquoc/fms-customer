package com.fms.customerservice.model;

public interface RouteDetailsDTO {
	Integer getRouteId();
	String getName();
	Integer getCustomerId();
	Integer getDeviation();
	Integer getDirection();
	Integer getRouteType();
	String getPoints();
	String getEmailWarning();
	String getPhoneWarning();
}
