package com.fms.customerservice.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.fms.customerservice.model.AccountForm;
import com.fms.customerservice.model.BccsServiceDTO;
import com.fms.customerservice.model.CustomerAccount;
import com.fms.customerservice.model.CustomerConfig;
import com.fms.customerservice.model.CustomerDTO;
import com.fms.customerservice.model.CustomerDetailDTO;
import com.fms.customerservice.model.CustomerGroupDTO;
import com.fms.customerservice.model.CustomerModel;
import com.fms.customerservice.model.DepartmentDTO;
import com.fms.customerservice.model.OrderPackageRequest;
import com.fms.customerservice.model.VehiclesAssignDTO;
import com.fms.module.model.CheckPermissionCustomerForm;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;

public interface CustomerService {
	Page<CustomerDTO> findAllCustomers(SearchModel searchModel, Integer roleId, Integer provinceId,
			Integer districtId, boolean export);
	
	ResponseEntity<InputStreamResource> export(SearchModel searchModel, UserHeader userHeader, String type);

	// lay danh sach nhom khach hang
	List<CustomerGroupDTO> getCutomerGroup(Integer roleId, String type, Integer provinceId, Integer districtId,
			Integer customerId);
	// kiem tra customer co thuoc quan ly cua user hien tai
	Boolean checkPermissionCustomer(CheckPermissionCustomerForm checkPermissionCustomerForm);

	// lay danh sach phong ban theo khach hang
	List<DepartmentDTO> getDepartmentOfCustomer(Integer customerId);

	// dieu chuyen khach hang
	String tranferCustomer(Integer customerId, Integer roleId, Integer id, String username, HttpServletRequest request);

	// lay danh sach nhom khach hang theo tinh
	List<CustomerGroupDTO> getCutomerOfProvince(Integer provinceId);
	
	//lay danh sach xe thuoc quan ly cua khach hang
	List<VehiclesAssignDTO> getVehicles(Integer customerId, Integer routeId, UserHeader userHeader);
	
	String updateConfig(CustomerConfig config, UserHeader userHeader);
	
	Object getConfig(Integer customerId, UserHeader userHeader);

	/**
	 * @param provinceId
	 * @return
	 */
	List<CustomerGroupDTO> getCutomerByProvinceId(Integer provinceId);
	
	String create(CustomerModel model, UserHeader userHeader, HttpServletRequest request);
	
	CustomerDetailDTO getCustomer(Integer customerId, UserHeader userHeader);
	
	String update(CustomerModel model, UserHeader userHeader, HttpServletRequest request);
	
	String delete(Integer customerId, UserHeader userHeader, HttpServletRequest request);
	
	//lay danh sach gói bccs services
	List<BccsServiceDTO> getListBCCSService(Integer roleId, Integer type);
	
	//lay danh sach account theo khách hàng
	List<CustomerAccount> getListAccountByCustomerId(Integer customerId,UserHeader userHeader);
	
	//lay danh sach account theo khách hàng
	void deleteAccount(Integer customerAccountId,UserHeader userHeader);
	
	//thêm mới account cho khách hàng
	void createAccount(AccountForm accountForm,UserHeader userHeader);
	
	//Mua gói cước cho khách hàng
	void orderPackage(OrderPackageRequest orderPackage,UserHeader userHeader);
}
