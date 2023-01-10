package com.fms.customerservice.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class CooperativeInforRequest {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long cooperativeId;
	private String name;
	private String address;
	private String phone;
	private String email;
	private String branchId;
	private String customerIds;
	private String username;
	private String password;

	
	public Long getCooperativeId() {
		return cooperativeId;
	}

	public void setCooperativeId(Long cooperativeId) {
		this.cooperativeId = cooperativeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId.trim();
	}

	public String getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(String customerIds) {
		this.customerIds = customerIds.trim();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password.trim();
	}
}
