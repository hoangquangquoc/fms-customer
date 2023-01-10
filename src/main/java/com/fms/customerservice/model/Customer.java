package com.fms.customerservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fms.module.model.BaseModel;

@Entity
@Table(name = "customer")
public class Customer extends BaseModel implements Cloneable, Diffable<Customer>{
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "customer_id")
	Integer customerId;
	
	@Column(name = "name")
	String name;
	
	@Column(name = "phone")
	String phone;
	
	@Column(name = "email")
	String email;
	
	@Column(name = "address")
	String address;
		
	@Column(name = "province_id")
	Integer provinceId;
	
	@Column(name = "district_id")
	Integer districtId;
		
	@Column(name = "customer_type_id")
	Integer customerTypeId;

	@Column(name = "business_license")
	String businessLicense;

	@Column(name = "is_cooperative")
	Integer isCooperative;
	
	@Column(name = "cooperative_id")
	Integer CooperativeId;
	
	
	@Column(name = "code")
	String code;
	
	@Column(name = "cust_id")
	String custId;
	
	@Column(name = "tax_code")
	String taxCode;
	
	@Transient
	private String provinceName;
	
	@Transient
	private String districtName;
	
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

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getCustomerTypeId() {
		return customerTypeId;
	}

	public void setCustomerTypeId(Integer customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public Integer getIsCooperative() {
		return isCooperative;
	}

	public void setIsCooperative(Integer isCooperative) {
		this.isCooperative = isCooperative;
	}
	
	public Integer getCooperativeId() {
		return CooperativeId;
	}

	public void setCooperativeId(Integer cooperativeId) {
		CooperativeId = cooperativeId;
	}

	public Customer() {
		
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	@Override
	public DiffResult diff(Customer obj) {
		String name = "Tên khách hàng"; //Utils.getMessageByKey("customer.name");
		String address = "Địa chỉ"; // Utils.getMessageByKey("customer.address");
		String phone = "Số điện thoại"; // Utils.getMessageByKey("customer.phone");
		String email = "Email"; //Utils.getMessageByKey("customer.email");
		String province = "Tỉnh/Thành phố";//Utils.getMessageByKey("customer.province");
		String district = "Quận/Huyện"; //Utils.getMessageByKey("customer.district");
		String code = "Mã khách hàng"; //Utils.getMessageByKey("customer.code");
		String businessNo = "Giấy phép kinh doanh"; //Utils.getMessageByKey("customer.businessNo");
		
		return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
				.append(name, this.name, obj.name)
				.append(address, this.address, obj.address)
				.append(phone, this.phone, obj.phone)
				.append(email, this.email, obj.email)
				.append(province, this.provinceName, obj.provinceName)
				.append(district, this.districtName, obj.districtName)
				.append(code, this.code, obj.code)
				.append(businessNo, this.businessLicense, obj.businessLicense)
				.build();
	}
	
}
