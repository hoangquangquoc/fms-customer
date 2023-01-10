package com.fms.customerservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fms.module.model.BaseModel;
import com.fms.module.utils.ValidateUtils;

@Entity
@Table(name = "driver")
public class Driver extends BaseModel {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "driver_id")
	private Integer driverId;
	
	@Column(name = "name")
	@NotBlank
	@Size(max = 255)
	private String driverName;
	
	@Column(name = "indentify")
	@Size(max = 50)
	private String indentify;

	@Column(name = "license")
	@Size(max = 50)
	private String license;

	@Column(name = "license_expired_date")
	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat
	private Date expiredDate;

	@Column(name = "date_of_birth")
	@JsonFormat(pattern = "dd/MM/yyyy")
	@DateTimeFormat
	private Date dob;

	@Column(name = "phone")
	private String phone;

	@Column(name = "email")
	@Size(max = 100)
	private String email;

	@Column(name = "address")
	@Size(max = 255)
	private String address;

	@Column(name = "customer_id")
	private Integer customerId;
		
	@Column(name = "driver_code")
	@Size(max = 100)
	private String employeeId;

	public Driver trim(Driver driver) {
		driver.setAddress(ValidateUtils.trimValue(driver.getAddress()));
		driver.setDriverName(ValidateUtils.trimValue(driver.getDriverName()));
		driver.setEmail(ValidateUtils.trimValue(driver.getEmail()));
		driver.setIndentify(ValidateUtils.trimValue(driver.getIndentify()));
		driver.setLicense(ValidateUtils.trimValue(driver.getLicense()));
		driver.setPhone(ValidateUtils.trimValue(driver.getPhone()));
		driver.setEmployeeId(ValidateUtils.trimValue(driver.getEmployeeId()));
		
		return driver;
	}
	
	public Integer getDriverId() {
		return driverId;
	}

	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getIndentify() {
		return indentify;
	}

	public void setIndentify(String indentify) {
		this.indentify = indentify;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
}
