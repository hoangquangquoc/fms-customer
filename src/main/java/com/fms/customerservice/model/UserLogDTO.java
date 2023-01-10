package com.fms.customerservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fms.module.model.BaseModel;
import com.fms.module.utils.ValidateUtils;

public class UserLogDTO extends BaseModel implements Cloneable, Diffable<UserLogDTO> {
		private Integer userId;

	private String username;

	private String fullname;
	private String phone;
	private String email;
	private Integer gender;
	private String genderName;
	private Date dob;
	private String address;
	private String avatar;
	private Integer provinceId;
	private String provinceName;
	private Integer districtId;
	private String districtName;
	private Integer customerId;
	private String customerName;
	private Integer departmentId;
	private String departmentName;
	private Integer roleId;
	private String roleName;
	private Integer mapType;
	private String mapName;
	Double lat;
	Double lng;
	Boolean isActive;
	private Integer zoom;
	private Integer showDefault = 1;

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getMapType() {
		return mapType;
	}

	public void setMapType(Integer mapType) {
		this.mapType = mapType;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
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

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}

	public Integer getShowDefault() {
		return showDefault;
	}

	public void setShowDefault(Integer showDefault) {
		this.showDefault = showDefault;
	}

	@Override
	public DiffResult diff(UserLogDTO obj) {
		return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE).append("address", this.address, obj.address)
				.append("customerId", this.customerId, obj.customerId)
				.append("departmentId", this.departmentId, obj.departmentId)
				.append("districtId", this.districtId, obj.districtId).append("dob", this.dob, obj.dob)
				.append("email", this.email, obj.email).append("fullname", this.fullname, obj.fullname)
				.append("gender", this.gender, obj.gender).append("lat", this.lat, obj.lat)
				.append("lng", this.lng, obj.lng).append("mapType", this.mapType, obj.mapType)
				.append("phone", this.phone, obj.phone).append("provinceId", this.provinceId, obj.provinceId)
				.append("roleId", this.roleId, obj.roleId).append("zoom", this.zoom, obj.zoom).build();
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

}
