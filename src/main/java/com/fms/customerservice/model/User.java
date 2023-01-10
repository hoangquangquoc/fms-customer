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
import com.fms.module.utils.Utils;
import com.fms.module.utils.ValidateUtils;

@Entity
@Table(name = "user", schema = "common")
public class User extends BaseModel implements Cloneable, Diffable<User> {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "username")
	// @NotBlank
	@Size(max = 100)
	private String username;

	// an thong tin password
	@JsonProperty(access = Access.WRITE_ONLY)
	// @NotBlank
//	@Size(max = 50)
	@Column(name = "password")
	private String password;

	@Column(name = "fullname")
	@Size(max = 255)
	private String fullname;

	@Column(name = "phone")
	private String phone;

	@Column(name = "email")
	// @Email
	@Size(max = 100)
	private String email;

	@Column(name = "gender")
	private Integer gender;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String genderName;

	@Column(name = "dob")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dob;

	@Column(name = "address")
	@Size(max = 255)
	private String address;

	@Column(name = "avatar")
	private String avatar;

	@Column(name = "province_id")
	private Integer provinceId;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String provinceName;

	@Column(name = "district_id")
	private Integer districtId;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String districtName;

	@Column(name = "customer_id")
	private Integer customerId;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String customerName;

	@Column(name = "department_id")
	private Integer departmentId;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String departmentName;

	@Column(name = "role_id")
	private Integer roleId;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String roleName;

	@Column(name = "map_type")
	private Integer mapType;

	@Transient
	@JsonProperty(access = Access.WRITE_ONLY)
	private String mapName;

	@Column(name = "lat")
	Double lat;

	@Column(name = "lng")
	Double lng;

	@Column(name = "is_active")
	Boolean isActive;

	@Column(name = "zoom")
	private Integer zoom;

	@Column(name = "show_default")
	private Integer showDefault = 1;

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public User trimUser(User user) {
		user.setUsername(ValidateUtils.trimValue(user.getUsername()));
		user.setPassword(ValidateUtils.trimValue(user.getPassword()));
		user.setPhone(ValidateUtils.trimValue(user.getPhone()));
		user.setEmail(ValidateUtils.trimValue(user.getEmail()));
		user.setAddress(ValidateUtils.trimValue(user.getAddress()));
		user.setFullname(ValidateUtils.trimValue(user.getFullname()));

		return user;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	@Override
	public DiffResult diff(User obj) {
		String customerName = Utils.getMessageByKey("user.customerId");
		String departmentName = Utils.getMessageByKey("user.departmentId");
		String districtName = Utils.getMessageByKey("user.districtId");
		String dob = Utils.getMessageByKey("user.dob");
		String fullname = Utils.getMessageByKey("user.fullname");
		String gender = Utils.getMessageByKey("user.gender");
		String mapType = Utils.getMessageByKey("user.mapType");
		String phone = Utils.getMessageByKey("user.phone");
		String provinceName = Utils.getMessageByKey("user.provinceId");
		String roleName = Utils.getMessageByKey("user.roleId");
		String address = Utils.getMessageByKey("user.address");
		String dob1 = Utils.convertDateToString(this.getDob(), null);
		String dob2 = Utils.convertDateToString(obj.getDob(), null);
		String password  = Utils.getMessageByKey("password");
		String oldPass = Utils.getMessageByKey("user.oldpass");
		String newPass = oldPass; 
		if(!this.getPassword().equals(obj.getPassword())) {
			newPass = Utils.getMessageByKey("user.newpass");
		}
		return new DiffBuilder(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
				.append(address, this.address, obj.address)
				.append(customerName, this.customerName, obj.customerName)
				.append(departmentName, this.departmentName, obj.departmentName)
				.append(districtName, this.districtName, obj.districtName)
				.append(dob, dob1, dob2)
				.append(password, oldPass, newPass)
				.append("email", this.email, obj.email)
				.append(fullname, this.fullname, obj.fullname)
				.append(gender, this.genderName, obj.genderName)
				.append("lat", this.lat, obj.lat)
				.append("lng", this.lng, obj.lng)
				.append(mapType, this.mapName, obj.mapName)
				.append(phone, this.phone, obj.phone)
				.append(provinceName, this.provinceName, obj.provinceName)
				.append(roleName, this.roleName, obj.roleName)
				.append("zoom", this.zoom, obj.zoom).build();
	}
}
