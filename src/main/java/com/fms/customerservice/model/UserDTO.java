package com.fms.customerservice.model;

public interface UserDTO {
	String getUsername();

	String getRoleName();

//	Double getLat();
//
//	Double getLng();

	Integer getUserId();

//	String getMapType();

	String getFullName();

	String getPhone();

	String getEmail();

//	Integer getGender();

//	Date getDob();

	String getAddress();

//	String getAvatar();

	Integer getProvinceId();

	Integer getDistrictId();

	Integer getCustomerId();

	Integer getDepartmentId();

//	Integer getRoleId();

	Boolean getIsActive();

	String getCreatedDate();
}
