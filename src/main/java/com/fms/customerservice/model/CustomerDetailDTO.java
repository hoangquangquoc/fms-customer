package com.fms.customerservice.model;

import java.util.List;

public class CustomerDetailDTO {
	Integer customerId;
	String name;
	String address;
	String email;
	String phone;
	Integer provinceId;
	String provinceName;
	Integer districtId;
	String districtName;
	String businessLicense;
	String taxCode;
	String custId;
	Integer isCooperative;
	List<CustomerAccountDTO> accounts;
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public Integer getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getBusinessLicense() {
		return businessLicense;
	}
	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public Integer getIsCooperative() {
		return isCooperative;
	}
	public void setIsCooperative(Integer isCooperative) {
		this.isCooperative = isCooperative;
	}
	public List<CustomerAccountDTO> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<CustomerAccountDTO> accounts) {
		this.accounts = accounts;
	}
	public CustomerDetailDTO(Integer customerId, String name, String address, String email, String phone,
			Integer provinceId, String provinceName, Integer districtId, String districtName, String businessLicense,
			String taxCode, String custId, Integer isCooperative, List<CustomerAccountDTO> accounts) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.provinceId = provinceId;
		this.provinceName = provinceName;
		this.districtId = districtId;
		this.districtName = districtName;
		this.businessLicense = businessLicense;
		this.taxCode = taxCode;
		this.custId = custId;
		this.isCooperative = isCooperative;
		this.accounts = accounts;
	}
	public CustomerDetailDTO() {
		super();
	}
	
}
