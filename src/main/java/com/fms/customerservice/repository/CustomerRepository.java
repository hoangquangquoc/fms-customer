package com.fms.customerservice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fms.customerservice.model.BccsServiceDTO;
import com.fms.customerservice.model.Customer;
import com.fms.customerservice.model.CustomerAccountDTO;
import com.fms.customerservice.model.CustomerDTO;
import com.fms.customerservice.model.CustomerGroupDTO;
import com.fms.customerservice.model.DepartmentDTO;
import com.fms.customerservice.model.PackageFullDTO;
import com.fms.customerservice.model.ship.ShipCustomerDTO;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	@Query(value = "SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%',:searchTerm, '%')) AND "
			+ " c.isActive = 1 ")
	Page<Customer> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageRequest);

	@Query(value = "SELECT c FROM Customer c where customer_id = :customerId AND c.isActive = 1 ")
	Customer retrieveCustomer(@Param("customerId") Integer customerId);

	@Query(value = "SELECT c.name as name, c.customer_id as id from customer c where customer_id = :customerId and c.is_active = 1 ", nativeQuery = true)
	List<CustomerGroupDTO> getCustomerInfo(@Param("customerId") Integer customerId);

	@Query(value = "SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%',:searchTerm, '%')) AND "
			+ " c.createdDate >= :fromDate AND c.createdDate <= :toDate AND c.isActive = 1 ")
	Page<Customer> findAllCustomers(@Param("searchTerm") String searchTerm, @Param(value = "fromDate") Date fromDate,
			@Param(value = "toDate") Date toDate, Pageable pageRequest);

	// danh sach khach hang cua tk sysadmin
	@Query(value = " SELECT CONCAT(c.name,' - ', p.name) as name, c.customer_id as id, c.is_cooperative as iscooperative, c.cooperative_id as cooperativeid FROM customer c join province p on c.province_id = p.id where c.is_active = 1 and p.is_active = 1 ORDER BY c.name COLLATE utf8_vietnamese_ci", nativeQuery = true)
	List<CustomerGroupDTO> findAllCustomer();

	// chi nhanh tinh
	@Query(value = " SELECT c.name as name, c.customer_id as id, c.is_cooperative as iscooperative, c.cooperative_id as cooperativeid FROM customer c join province p on c.province_id = p.id where c.province_id = :provinceId and c.is_active = 1 and p.is_active = 1 ORDER BY c.name COLLATE utf8_vietnamese_ci", nativeQuery = true)
	List<CustomerGroupDTO> findAllCustomerOfProvince(@Param("provinceId") Integer provinceId);
	
	// chi nhanh huyen
	@Query(value = " SELECT c.name as name, c.customer_id as id, c.is_cooperative as iscooperative, c.cooperative_id as cooperativeid FROM customer c where c.district_id = :districtId and c.is_active = 1 ORDER BY name COLLATE utf8_vietnamese_ci", nativeQuery = true)
	List<CustomerGroupDTO> findAllCustomerOfDistrict(@Param("districtId") Integer districtId);

	// danh sach tinh
	@Query(value = " SELECT name, id FROM province where is_active = 1 and code = 'PROVINCE' ORDER BY name COLLATE utf8_vietnamese_ci", nativeQuery = true)
	List<CustomerGroupDTO> getAllProvince();

	// danh sach phong ban cua khach hang
	@Query(value = " SELECT department_id as id, name FROM department where customer_id = :customerId and is_active = 1 ORDER BY name COLLATE utf8_vietnamese_ci", nativeQuery = true)
	List<CustomerGroupDTO> getDepartmentOfCustomer(@Param("customerId") Integer customerId);

	// check customer co thuoc su quan ly cua user hien tai
	@Query(value = "select checkPermissionCustomer(?1,?2,?3,?4,?5)", nativeQuery = true)
	Boolean checkPermissionCustomer(Integer roleId, Integer provinceId, Integer districtId, Integer customerIdHeader,
			Integer customerId);

	// danh sach phong ban cua khach hang
	@Query(value = " SELECT department_id as departmentId, name as departmentName FROM department where customer_id = :customerId and is_active = 1 ORDER BY name COLLATE utf8_vietnamese_ci", nativeQuery = true)
	List<DepartmentDTO> getDepartments(@Param("customerId") Integer customerId);

	String adminQuery = "SELECT c.province_id as provinceId, c.is_cooperative as isCooperative, c.cooperative_id as cooperativeId, c.name as name, c.phone as phone, p.name as provinceName, c.address as address, c.email as email, c.customer_id as customerId, DATE_FORMAT(c.created_date,'%d/%m/%Y') as createdDate "
			+ " FROM customer c left join province p " + " on p.id = c.province_id "
			+ " WHERE (?1 is null or LOWER(c.name) LIKE ?1 ESCAPE '/') "
			+ " AND (?2 is null or c.phone LIKE ?2 ESCAPE '/') " + " AND (?3 is null or c.created_date >= ?3) "
			+ " AND (?4 is null or c.created_date <= ?4) " + "	AND (?5 = -1 or c.province_id = ?5) "
			+ " AND c.is_active = 1 " + " ORDER BY c.name COLLATE utf8_vietnamese_ci ";

	@Query(value = adminQuery, countQuery = "Select count(*) from (" + adminQuery + ") tmp ", nativeQuery = true)
	Page<CustomerDTO> getCustomersForAdmin(String name, String phone, Date fromDate, Date toDate, Integer provinceId,
			Pageable pageable);

	String districtQuery = "SELECT c.province_id as provinceId, c.is_cooperative as isCooperative, c.cooperative_id as cooperativeId, c.name as name, c.phone as phone, p.name as provinceName, c.address as address, c.email as email, c.customer_id as customerId, DATE_FORMAT(c.created_date,'%d/%m/%Y') as createdDate "
			+ " FROM customer c left join province p " + " on p.id = c.district_id "
			+ " WHERE (?1 is null or LOWER(c.name) LIKE ?1 ESCAPE '/') "
			+ " AND (?2 is null or c.phone LIKE ?2 ESCAPE '/') " + " AND (?3 is null or c.created_date >= ?3) "
			+ " AND (?4 is null or c.created_date <= ?4) " + "	AND (?5 = -1 or c.district_id = ?5) "
			+ " AND c.is_active = 1 " + " ORDER BY c.name COLLATE utf8_vietnamese_ci";

	@Query(value = districtQuery, countQuery = "Select count(*) from (" + districtQuery + ") tmp ", nativeQuery = true)
	Page<CustomerDTO> getCustomersForDistrict(String name, String phone, Date fromDate, Date toDate, Integer districtId,
			Pageable pageable);

	@Transactional
	@Modifying
	@Query(value = "Update customer SET province_id= ?1, modified_by = ?2, modified_date = sysdate() WHERE customer_id = ?3", nativeQuery = true)
	public void tranferProvinceCustomer(Integer provinceId, String modifiedBy, Integer customerId);

	@Transactional
	@Modifying
	@Query(value = "Update customer SET district_id= :districtId WHERE customer_id=:id", nativeQuery = true)
	public void tranferDistrictCustomer(@Param("districtId") Integer districtId, @Param("id") Integer customerId);

	// kiem tra ton tai province
	@Query(value = "Select count(1) from province WHERE id = :id and is_active = 1", nativeQuery = true)
	int countProvinceById(Integer id);

	// kiem tra ton tai khach hang
	@Query(value = "Select count(1) from customer WHERE customer_id = :id and is_active = 1", nativeQuery = true)
	int countCustomerId(Integer id);

	@Query(value = "SELECT value FROM common.parameter_config where name in ('expired_package_warning_day','expired_maintenance_warning_day','expired_registry_warning_day' )", nativeQuery = true)
	List<String> getConfigParam();
	
	// AnhTH32 
	// lay danh sach customer khong phai HTX va chua thuoc HTX nao
	@Query(value = " SELECT c.name as name, c.customer_id as id, c.is_cooperative as iscooperative, c.cooperative_id as cooperativeid FROM customer c join province p on c.province_id = p.id where c.province_id = :provinceId and c.is_active = 1 and p.is_active = 1 AND c.is_cooperative = 0 AND (c.cooperative_id = 0 OR cooperative_id IS NULL) ORDER BY c.name COLLATE utf8_vietnamese_ci", nativeQuery = true)
	List<CustomerGroupDTO> getCustomerByProvinceId(@Param("provinceId") Integer provinceId);
	
	@Query(value = "Select c.customer_id as customerId, c.name, c.email, c.phone, c.address, c.province_id as provinceId, c.district_id as districtId, c.business_license as businessLicense, c.code, c.tax_code as taxCode, c.cust_id as custId, "
			+ "(SELECT p.name FROM province p WHERE p.id = c.province_id) as provinceName, (SELECT p.name FROM province p WHERE p.id = c.district_id) as districtName " + 
			" from customer c where c.customer_id = :customerId and c.is_active = 1", nativeQuery = true)
	CustomerDTO getCustomer(Integer customerId);
	
	// Lay ten tinh/huyen
	@Query(value = "Select name from province where id = :id", nativeQuery = true)
	String getProvinceName(Integer id);
	
	// Kiểm tra branchId có tồn tại không
	@Query(value = "SELECT COUNT(*) FROM common.province WHERE id = ?1", nativeQuery = true)
	Integer isExistBranchId(Integer branchId);
	
	/*
	 * hoadp
	 * kiem tra khach hang da thuoc HTX nào chưa?
	 * 03/02/2020
	 */
	@Query(value = "SELECT cooperative_id FROM common.customer WHERE is_active = 1 AND customer_id = ?1", nativeQuery = true)
	Integer hasCooperative(Integer customerId);
	
	@Query(value = "CALL proc_get_list_account_by_customer_id(?1)", nativeQuery = true)
	List<CustomerAccountDTO> getListCustomerAccount(Integer customerId);
	
	@Query(value = "CALL proc_get_list_bccs_service_by_type(?1)", nativeQuery = true)
	List<BccsServiceDTO> getListBCCSService(Integer type);
	
	// Lấy customerId by custId
	@Query(value = "SELECT customer_id FROM common.customer WHERE cust_id = ?1 LIMIT 1", nativeQuery = true)
	Integer getCustomerIdbyCustId(String custId);
	
	// Lấy custId by customerId
	@Query(value = "SELECT cust_id FROM common.customer WHERE customer_id = ?1 LIMIT 1", nativeQuery = true)
	String getCustIdByCustomerId(Integer customerId);
	
	@Query(value = "CALL common.proc_get_package_by_package_id(?1)", nativeQuery = true)
	public PackageFullDTO getPackageById(Integer id);
	
	@Query(value = "SELECT account_code FROM common.customer_account WHERE account_code LIKE ?1 AND is_active = 1 ORDER BY created_date DESC , account_code DESC LIMIT 1", nativeQuery = true)
	public String getLastAccountCode(String template);
	
	//******************************* SHIP APIs *****************************************
	String shipQuery = "SELECT c.province_id as provinceId, c.is_cooperative as isCooperative, c.cooperative_id as cooperativeId, c.name as name, c.phone as phone, p.name as provinceName, c.address as address, c.email as email, c.customer_id as customerId, DATE_FORMAT(c.created_date,'%d/%m/%Y') as createdDate "
			+ ", SUM(CASE WHEN t.transport_status = 0 THEN 1 ELSE 0 END) as numberOfNonActive" 
			+ ", SUM(CASE WHEN t.transport_status = 1 THEN 1 ELSE 0 END) as numberOfActive"
			+ ", SUM(CASE WHEN t.transport_status = 2 THEN 1 ELSE 0 END) as numberOfMaintainance"
			+ " FROM customer c left join province p " + " on p.id = c.province_id "
			+ " left join transport t on c.customer_id = t.customer_id "
			+ " WHERE (?1 is null or LOWER(c.name) LIKE ?1 ESCAPE '/') "
			+ " AND (?2 is null or c.phone LIKE ?2 ESCAPE '/') " + " AND (?3 is null or c.created_date >= ?3) "
			+ " AND (?4 is null or c.created_date <= ?4) " + "	AND (?5 = -1 or c.province_id = ?5) "
			+ " AND c.is_active = 1 AND c.created_by <> 'sysadmin'" 
			+ " GROUP BY c.customer_id "
			+ " ORDER BY c.created_date desc";
	@Query(value = shipQuery, countQuery = "Select count(*) from (" + shipQuery + ") tmp ", nativeQuery = true)
	Page<ShipCustomerDTO> getShipCustomersForAdmin(String name, String phone, Date fromDate, Date toDate, Integer provinceId,
			Pageable pageable);
	
	@Query(value = districtQuery, countQuery = "Select count(*) from (" + districtQuery + ") tmp ", nativeQuery = true)
	Page<ShipCustomerDTO> getShipCustomersForDistrict(String name, String phone, Date fromDate, Date toDate, Integer districtId,
			Pageable pageable);
	
	
	@Query(value = "Select c.customer_id as customerId, c.name, c.email, c.phone, c.address, c.tax_code as taxCode "
			+ " from customer c where c.customer_id = :customerId and c.is_active = 1", nativeQuery = true)
	ShipCustomerDTO getShipCustomer(Integer customerId);
}
