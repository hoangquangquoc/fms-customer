package com.fms.customerservice.model;

public interface PackageFullDTO {
	public Integer getPackageId();
	public String getPackageCode();
	public String getPackageName();
	public Double getPrice();
	public Integer getDuration();
	public String getDescription();
	public Double getDiscountInstall();
	public String getNetworkedTypeCode();
	public String getNetworkedTypeName();
	public String getPromotionCode();
	public String getPromotionName();
	public String getPrePaymentCode();
	public String getPrePaymentName();
}
