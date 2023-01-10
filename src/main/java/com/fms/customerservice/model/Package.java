package com.fms.customerservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "package")
public class Package {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "package_id")
	private Integer packageId;

	@Column(name = "package_code")
	private String packageCode;
	
	@Column(name = "networked_type_code")
	private String networkedTypeCode;
	
	@Column(name = "networked_type_name")
	private String networkedTypeName;
	
	@Column(name = "promotion_code")
	private String promotionCode;
	
	@Column(name = "promotion_name")
	private String promotionName;
	
	@Column(name = "pre_payment_code")
	private String prePaymentCode;
	
	@Column(name = "pre_payment_name")
	private String prePaymentName;
	@Transient
	@Column(name = "name")
	private String packageName;
	
	@Column(name = "fee")
	private Double price;

	@Column(name = "duration")
	private Integer duration;
	
	@Column(name = "package_type")
	private String packageType;

	@Column(name = "description")
	private String description;
	
	@Column(name = "discount_install")
	private Double discountInstall;

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getPackageCode() {
		return packageCode;
	}

	public void setPackageCode(String packageCode) {
		this.packageCode = packageCode;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	public Double getDiscountInstall() {
		return discountInstall;
	}

	public void setDiscountInstall(Double discountInstall) {
		this.discountInstall = discountInstall;
	}

	public String getNetworkedTypeCode() {
		return networkedTypeCode;
	}

	public void setNetworkedTypeCode(String networkedTypeCode) {
		this.networkedTypeCode = networkedTypeCode;
	}

	public String getNetworkedTypeName() {
		return networkedTypeName;
	}

	public void setNetworkedTypeName(String networkedTypeName) {
		this.networkedTypeName = networkedTypeName;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getPrePaymentCode() {
		return prePaymentCode;
	}

	public void setPrePaymentCode(String prePaymentCode) {
		this.prePaymentCode = prePaymentCode;
	}

	public String getPrePaymentName() {
		return prePaymentName;
	}

	public void setPrePaymentName(String prePaymentName) {
		this.prePaymentName = prePaymentName;
	}

	public Package(Integer packageId, String packageCode, String networkedTypeCode, String networkedTypeName,
			String promotionCode, String promotionName, String prePaymentCode, String prePaymentName,
			String packageName, Double price, Integer duration, String packageType, String description,
			Double discountInstall) {
		super();
		this.packageId = packageId;
		this.packageCode = packageCode;
		this.networkedTypeCode = networkedTypeCode;
		this.networkedTypeName = networkedTypeName;
		this.promotionCode = promotionCode;
		this.promotionName = promotionName;
		this.prePaymentCode = prePaymentCode;
		this.prePaymentName = prePaymentName;
		this.packageName = packageName;
		this.price = price;
		this.duration = duration;
		this.packageType = packageType;
		this.description = description;
		this.discountInstall = discountInstall;
	}

	public Package(Integer packageId, String packageCode, String packageName, Integer productId, Double price,
			Integer duration) {
		super();
		this.packageId = packageId;
		this.packageCode = packageCode;
		this.packageName = packageName;
		this.price = price;
		this.duration = duration;
	}

	public Package() {
		super();
	}

}
