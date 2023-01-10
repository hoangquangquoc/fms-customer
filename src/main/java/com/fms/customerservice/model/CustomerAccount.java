package com.fms.customerservice.model;

import java.time.LocalDate;
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
@Table(name = "customer_account")
public class CustomerAccount extends BaseModel{
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "customer_account_id")
	Integer customerAccountId;
	
	@Column(name = "account_code")
	String accountCode;
	
	@Column(name = "customer_id")
	Integer customerId;
	
	@Column(name = "staff_code")
	String staffCode;
	
	@Column(name = "install_address")
	String installAddress;
		
	@Column(name = "package_code")
	String packageCode;
	
	@Column(name = "package_name")
	String packageName;
	
	@Column(name = "networked_type_code")
	String networkedTypeCode;

	@Column(name = "networked_type_name")
	String networkedTypeName;

	@Column(name = "promotion_code")
	String promotionCode;

	@Column(name = "promotion_name")
	String promotionName;

	@Column(name = "pre_payment_code")
	String prePaymentCode;
	
	@Column(name = "pre_payment_name")
	String prePaymentName;
		
	@Column(name = "duration")
	Integer duration;

	@Column(name = "status")
	Integer status = 1;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Column(name = "active_date")
	LocalDateTime activeDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Column(name = "expired_date")
	LocalDateTime expiredDate;
	
	
	@Column(name = "reason")
	String reason;

	@Column(name = "contract_code")
	String contractCode;
	
	public Integer getCustomerAccountId() {
		return customerAccountId;
	}

	public void setCustomerAccountId(Integer customerAccountId) {
		this.customerAccountId = customerAccountId;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		if(accountCode != null)
			this.accountCode = accountCode.trim();
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}


	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		if(staffCode != null)
		this.staffCode = staffCode.trim();
	}

	public String getInstallAddress() {
		return installAddress;
	}

	public void setInstallAddress(String installAddress) {
		if(installAddress != null)
		this.installAddress = installAddress.trim();
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		if(packageCode != null)
		this.packageCode = packageCode.trim();
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		if(packageName != null)
		this.packageName = packageName.trim();
	}

	public String getNetworkedTypeCode() {
		return networkedTypeCode;
	}

	public void setNetworkedTypeCode(String networkedTypeCode) {
		if(networkedTypeCode != null)
		this.networkedTypeCode = networkedTypeCode.trim();
	}

	public String getNetworkedTypeName() {
		return networkedTypeName;
	}

	public void setNetworkedTypeName(String networkedTypeName) {
		if(networkedTypeName != null)
		this.networkedTypeName = networkedTypeName.trim();
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		if(promotionCode != null)
		this.promotionCode = promotionCode.trim();
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		if(promotionName != null)
		this.promotionName = promotionName.trim();
	}

	public String getPrePaymentCode() {
		return prePaymentCode;
	}

	public void setPrePaymentCode(String prePaymentCode) {
		if(prePaymentCode != null)
		this.prePaymentCode = prePaymentCode.trim();
	}

	public String getPrePaymentName() {
		return prePaymentName;
	}

	public void setPrePaymentName(String prePaymentName) {
		if(prePaymentName != null)
		this.prePaymentName = prePaymentName.trim();
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public LocalDateTime getActiveDate() {
		return activeDate;
	}

	public void setActiveDate(LocalDateTime activeDate) {
		this.activeDate = activeDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		if(reason != null)
		this.reason = reason.trim();
	}

	public LocalDateTime getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(LocalDateTime expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		if(contractCode != null)
		this.contractCode = contractCode.trim();
	}
	public CustomerAccount(String accountCode, Integer customerId, String staffCode, String installAddress,
			String packageCode, String packageName, String networkedTypeCode, String networkedTypeName,
			String promotionCode, String promotionName, String prePaymentCode, String prePaymentName, Integer duration,
			String reason, String contractCode, LocalDate expiredDate) {
		super();
		this.accountCode = accountCode;
		this.customerId = customerId;
		this.staffCode = staffCode;
		this.installAddress = installAddress;
		this.packageCode = packageCode;
		this.packageName = packageName;
		this.networkedTypeCode = networkedTypeCode;
		this.networkedTypeName = networkedTypeName;
		this.promotionCode = promotionCode;
		this.promotionName = promotionName;
		this.prePaymentCode = prePaymentCode;
		this.prePaymentName = prePaymentName;
		this.duration = duration;
		this.reason = reason;
		this.status = 1;
		this.activeDate = LocalDateTime.now();
		if(expiredDate != null)
			this.expiredDate = expiredDate.atTime(0, 0, 0);
		this.contractCode = contractCode;
	}

	public CustomerAccount() {
		super();
	}

	
}
