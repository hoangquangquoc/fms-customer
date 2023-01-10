package com.fms.customerservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment_history")
public class PaymentHistoryModel {
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Id
	@Column(name = "payment_history_id")
	private Integer paymentHistoryId;

	@Column(name = "sim")
	private String sim;

	@Column(name = "content")
	private String content;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "package_name")
	private String packageName;

	@Column(name = "payment_by")
	private String paymentBy;

	@Column(name = "package_id")
	private Integer packageId;

	@Column(name = "am_code")
	private String amCode;

	@Column(name = "client_os")
	private Integer clientOs;

	@Column(name = "cust_id")
	private String custId;

	@Column(name = "fee")
	private Integer fee;

	@Column(name = "duration")
	private Integer duration;

	@Column(name = "expiration_date_old")
	private Date expirationDateOld;

	@Column(name = "expiration_date_new")
	private Date expirationDateNew;

	public Integer getPaymentHistoryId() {
		return paymentHistoryId;
	}

	public void setPaymentHistoryId(Integer paymentHistoryId) {
		this.paymentHistoryId = paymentHistoryId;
	}

	public String getSim() {
		return sim;
	}

	public void setSim(String sim) {
		this.sim = sim;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPaymentBy() {
		return paymentBy;
	}

	public void setPaymentBy(String paymentBy) {
		this.paymentBy = paymentBy;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getAmCode() {
		return amCode;
	}

	public void setAmCode(String amCode) {
		this.amCode = amCode;
	}

	public Integer getClientOs() {
		return clientOs;
	}

	public void setClientOs(Integer clientOs) {
		this.clientOs = clientOs;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Date getExpirationDateOld() {
		return expirationDateOld;
	}

	public void setExpirationDateOld(Date expirationDateOld) {
		this.expirationDateOld = expirationDateOld;
	}

	public Date getExpirationDateNew() {
		return expirationDateNew;
	}

	public void setExpirationDateNew(Date expirationDateNew) {
		this.expirationDateNew = expirationDateNew;
	}
	
	
}
