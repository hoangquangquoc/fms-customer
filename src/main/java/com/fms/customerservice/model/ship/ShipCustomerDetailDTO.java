package com.fms.customerservice.model.ship;

public class ShipCustomerDetailDTO {
	Integer customerId;
	String name;
	String address;
	String email;
	String phone;
	String taxCode;
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
	
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public ShipCustomerDetailDTO(Integer customerId, String name, String address, String email, String phone,String taxCode) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.address = address;
		this.email = email;
		this.phone = phone;
		this.taxCode = taxCode;
	}
	public ShipCustomerDetailDTO() {
		super();
	}
	
}
