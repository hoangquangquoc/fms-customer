package com.fms.customerservice.model;

public class UserMapped {
	private Integer userId;
	private String username;
	private String fullName;
	private String phone;
	private String email;
//	private Integer gender;
//	private Date dob;
	private String address;
//	private Integer provinceId;
//	private Integer districtId;
//	private Integer customerId;
//	private Integer departmentId;
	private Integer roleId;
	private String roleName;
	private Boolean status;
	private String managementUnit;
	private String createdDate;
	private String customerName;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public Integer getGender() {
//		return gender;
//	}
//
//	public void setGender(Integer gender) {
//		this.gender = gender;
//	}
//
//	public Date getDob() {
//		return dob;
//	}
//
//	public void setDob(Date dob) {
//		this.dob = dob;
//	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	public Integer getProvinceId() {
//		return provinceId;
//	}
//
//	public void setProvinceId(Integer provinceId) {
//		this.provinceId = provinceId;
//	}

//	public Integer getDistrictId() {
//		return districtId;
//	}
//
//	public void setDistrictId(Integer districtId) {
//		this.districtId = districtId;
//	}
//
//	public Integer getCustomerId() {
//		return customerId;
//	}
//
//	public void setCustomerId(Integer customerId) {
//		this.customerId = customerId;
//	}
//
//	public Integer getDepartmentId() {
//		return departmentId;
//	}
//
//	public void setDepartmentId(Integer departmentId) {
//		this.departmentId = departmentId;
//	}
//
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getManagementUnit() {
		return managementUnit;
	}

	public void setManagementUnit(String managementUnit) {
		this.managementUnit = managementUnit;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
