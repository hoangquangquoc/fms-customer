package com.fms.customerservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import com.fms.module.model.BaseModel;

@Entity
@Table(name = "department")
public class Department extends BaseModel{
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "department_id")
	Integer departmentId;
	
	@Column(name = "name")
	String name;
	
	@Column(name = "phone")
	String phone;
	
	@Email
	@Column(name = "email")
	String email;
	
	@Column(name = "address")
	String address;
		
	@Column(name = "customer_id")
	String customerId;

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
}
