package com.fms.customerservice.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AccountForm{
	Integer customerId = 1;
	String contractCode = "";
	String accountCode = "";
	String packageCode = "";
	String packageName = "";
	String networkedTypeCode = "";
	String networkedTypeName = "";
	String promotionCode = "";
	String promotionName = "";
	String prePaymentCode = "";
	String prePaymentName = "";
	@JsonFormat(pattern = "dd/MM/yyyy")
	LocalDate expiredDate;
	Integer duration = 0;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode.trim();
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode.trim();
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode.trim();
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName.trim();
	}

	public String getNetworkedTypeCode() {
		return networkedTypeCode;
	}

	public void setNetworkedTypeCode(String networkedTypeCode) {
		this.networkedTypeCode = networkedTypeCode.trim();
	}

	public String getNetworkedTypeName() {
		return networkedTypeName;
	}

	public void setNetworkedTypeName(String networkedTypeName) {
		this.networkedTypeName = networkedTypeName.trim();
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode.trim();
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName.trim();
	}

	public String getPrePaymentCode() {
		return prePaymentCode;
	}

	public void setPrePaymentCode(String prePaymentCode) {
		this.prePaymentCode = prePaymentCode.trim();
	}

	public String getPrePaymentName() {
		return prePaymentName;
	}

	public void setPrePaymentName(String prePaymentName) {
		this.prePaymentName = prePaymentName.trim();
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}


	public LocalDate getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(LocalDate expiredDate) {
		this.expiredDate = expiredDate;
	}

	public AccountForm(Integer customerId, String contractCode, String accountCode, String packageCode,
			String packageName, String networkedTypeCode, String networkedTypeName, String promotionCode,
			String promotionName, String prePaymentCode, String prePaymentName,
			LocalDate expiredDate, Integer duration) {
		super();
		this.customerId = customerId;
		this.contractCode = contractCode;
		this.accountCode = accountCode;
		this.packageCode = packageCode;
		this.packageName = packageName;
		this.networkedTypeCode = networkedTypeCode;
		this.networkedTypeName = networkedTypeName;
		this.promotionCode = promotionCode;
		this.promotionName = promotionName;
		this.prePaymentCode = prePaymentCode;
		this.prePaymentName = prePaymentName;
		this.expiredDate = expiredDate;
		this.duration = duration;
	}

	public AccountForm() {
		super();
	}
	
}
