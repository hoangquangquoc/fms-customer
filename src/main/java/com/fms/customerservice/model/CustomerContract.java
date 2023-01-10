package com.fms.customerservice.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fms.module.model.BaseModel;

@Entity
@Table(name = "customer_contract")
public class CustomerContract extends BaseModel{
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "customer_contract_id")
	Integer customerContractId;
	
	@Column(name = "customer_id")
	Integer customerId;
	
	@Column(name = "contract_id")
	String contractId;
	
	@Column(name = "cust_id")
	String custId;
	
	@Column(name = "contract_code")
	String contractCode;
	
	@Column(name = "status")
	Integer status = 1;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
	@Column(name = "start_date")
	LocalDateTime startDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
	@Column(name = "end_date")
	LocalDateTime endDate;
	

	public Integer getCustomerContractId() {
		return customerContractId;
	}


	public void setCustomerContractId(Integer customerContractId) {
		this.customerContractId = customerContractId;
	}


	public Integer getCustomerId() {
		return customerId;
	}


	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}


	public String getContractId() {
		return contractId;
	}


	public void setContractId(String contractId) {
		this.contractId = contractId;
	}


	public String getContractCode() {
		return contractCode;
	}


	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public LocalDateTime getStartDate() {
		return startDate;
	}


	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}


	public LocalDateTime getEndDate() {
		return endDate;
	}


	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public String getCustId() {
		return custId;
	}


	public void setCustId(String custId) {
		this.custId = custId;
	}


	public CustomerContract(Integer customerId, String custId, String contractId, String contractCode,
			Integer status, LocalDateTime startDate, LocalDateTime endDate) {
		super();
		this.customerId = customerId;
		this.custId = custId;
		this.contractId = contractId;
		this.contractCode = contractCode;
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
	}


	public CustomerContract() {
		super();
	}

	
}
