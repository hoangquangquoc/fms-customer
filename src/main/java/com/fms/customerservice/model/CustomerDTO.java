package com.fms.customerservice.model;

public interface CustomerDTO {
	String getName();

	String getProvinceName();
	
	String getDistrictName();

	String getAddress();

	String getEmail();

	String getPhone();

	Integer getCustomerId();

	String getCreatedDate();
	
	Integer getIsCooperative();
	
	Long getCooperativeId();
	
	Integer getProvinceId();
	
	Integer getDistrictId();
	
	String getBusinessLicense();
	
	String getCode();
	
	String getTaxCode();
	
	String getCustId();
}
