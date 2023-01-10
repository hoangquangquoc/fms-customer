package com.fms.customerservice.model.ship;

import java.util.List;

import com.fms.customerservice.model.AccountForm;

public class ShipCustomerModel {
	private Integer customerId;
	private String name;
	private String address;
	private String phone;
	private String email;
	private Integer provinceId;
	private Integer districtId;
	private String taxCode;

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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		if(taxCode != null)
		this.taxCode = taxCode.trim();
	}

	public List<AccountForm> getCustomerAccounts() {
		return customerAccounts;
	}

	public void setCustomerAccounts(List<AccountForm> customerAccounts) {
		this.customerAccounts = customerAccounts;
	}
	
	
}
