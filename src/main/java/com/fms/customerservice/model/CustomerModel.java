package com.fms.customerservice.model;

import java.util.List;

public class CustomerModel {
	private Integer customerId;
	private String name;
	private String code;
	private String address;
	private String phone;
	private String email;
	private Integer provinceId;
	private Integer districtId;
	private String businessLicense;
	private String taxCode;
	private String custId;

	private List<AccountForm> customerAccounts;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name != null)
		this.name = name.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if(address != null)
		this.address = address.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		if(phone != null)
		this.phone = phone.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if(email != null)
		this.email = email.trim();
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		if(businessLicense != null)
		this.businessLicense = businessLicense.trim();
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		if(code != null)
		this.code = code.trim();
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		if(taxCode != null)
		this.taxCode = taxCode.trim();
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		if(custId != null)
		this.custId = custId.trim();
	}

	public List<AccountForm> getCustomerAccounts() {
		return customerAccounts;
	}

	public void setCustomerAccounts(List<AccountForm> customerAccounts) {
		this.customerAccounts = customerAccounts;
	}
	
	
}
