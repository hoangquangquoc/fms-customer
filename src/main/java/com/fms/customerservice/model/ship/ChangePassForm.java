package com.fms.customerservice.model.ship;

public class ChangePassForm {
	Integer userId;
	String oldPassword;
	String newPassword;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public ChangePassForm(Integer userId, String oldPassword, String newPassword) {
		super();
		this.userId = userId;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}
	
	
}
