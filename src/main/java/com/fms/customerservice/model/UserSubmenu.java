package com.fms.customerservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fms.module.model.BaseModel;

@Entity
@Table(name="user_submenu")
public class UserSubmenu extends BaseModel {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "user_submenu_id")
	Integer userSubmenuId;
	
	@Column(name = "user_id")
	Integer userId;
	
	@Column(name = "submenu_id")
	String submenuId;
	
	@Column(name = "submenu_code")
	String submenuCode;
	
	@Column(name = "can_add")
	Boolean canAdd;
	@Column(name = "can_read")
	Boolean canRead;
	@Column(name = "can_edit")
	Boolean canEdit;
	@Column(name = "can_delete")
	Boolean canDelete;
	

	public Integer getUserSubmenuId() {
		return userSubmenuId;
	}

	public void setUserSubmenuId(Integer userSubmenuId) {
		this.userSubmenuId = userSubmenuId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSubmenuId() {
		return submenuId;
	}

	public void setSubmenuId(String submenuId) {
		this.submenuId = submenuId;
	}

	public String getSubmenuCode() {
		return submenuCode;
	}

	public void setSubmenuCode(String submenuCode) {
		this.submenuCode = submenuCode;
	}

	public Boolean getCanAdd() {
		return canAdd;
	}

	public void setCanAdd(Boolean canAdd) {
		this.canAdd = canAdd;
	}

	public Boolean getCanRead() {
		return canRead;
	}

	public void setCanRead(Boolean canRead) {
		this.canRead = canRead;
	}

	public Boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Boolean canEdit) {
		this.canEdit = canEdit;
	}

	public Boolean getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(Boolean canDelete) {
		this.canDelete = canDelete;
	}

	public UserSubmenu(Integer createdBy, Date createdDate, Integer modifiedBy, Date modifiedDate, Boolean isActive,
			Integer userSubmenuId, Integer userId, String submenuId, String submenuCode, Boolean canAdd,
			Boolean canRead, Boolean canEdit, Boolean canDelete) {
//		super(createdBy, createdDate, modifiedBy, modifiedDate, isActive);
		this.userSubmenuId = userSubmenuId;
		this.userId = userId;
		this.submenuId = submenuId;
		this.submenuCode = submenuCode;
		this.canAdd = canAdd;
		this.canRead = canRead;
		this.canEdit = canEdit;
		this.canDelete = canDelete;
	}

	public UserSubmenu() {
	}

	
	
	
}
