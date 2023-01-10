package com.fms.customerservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fms.customerservice.model.User;
import com.fms.customerservice.model.UserDTO;
import com.fms.customerservice.model.UserDetailsDTO;
import com.fms.customerservice.model.VehiclesDTO;
import com.fms.customerservice.model.ship.ShipUserDetailsDTO;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	@Query(value = "SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%',:searchTerm, '%')) AND u.isActive = 1 ")
	Page<User> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageRequest);

	@Query(value = "SELECT u FROM User u where u.userId = :userId AND u.isActive = 1 ")
	User retrieveUser(@Param("userId") Integer userId);

	@Query(value = "SELECT user_id as userId, username, fullname as fullName, map_type as mapType, phone, email, "
			+ "address, gender, DATE_FORMAT(u.dob,'%d/%m/%Y') as dob, province_id as provinceId, district_id as districtId, customer_id as customerId, "
			+ "department_id as departmentId, role_id as roleId, show_default as showDefault FROM user u where u.user_id = :userId AND u.is_active = 1 ", nativeQuery = true)
	UserDetailsDTO getUserDetails(@Param("userId") Integer userId);

	@Query(value = "SELECT u FROM User u where u.username = :username AND u.isActive = 1 ")
	User retrieveUserByUserName(@Param("username") String username);

	@Query(value = "Call findAllUsers()", nativeQuery = true)
	Page<UserDTO> findUsers(String username, List<Boolean> status, String phone, String email, Integer provinceId,
			Integer districtId, Integer customerId, Pageable pageable);

	@Query(value = "select count(1) from (select p1.id from province p1 join province p2 on p1.value = p2.value"
			+ " where p1.code = 'DISTRICT' and p2.id = ?1) tmp where id = ?2", nativeQuery = true)
	int checkDistrict(Integer provinceId, Integer districtId);

	@Query(value = "select t.register_no as registerNo, t.transport_id as transportId, "
			+ "(case when ut.user_transport_id is null then false " + "else true end) as status from transport t "
			+ "left join user_transport ut on ut.transport_id = t.transport_id " + "and ut.user_id = ?1 "
			+ "where 1=1 and t.is_active = 1 and (?2 = -1 or t.customer_id = ?2) ", nativeQuery = true)
	List<VehiclesDTO> getVehicles(Integer userId, Integer customerId);

	@Query(value = "select count(1) from user u where u.user_id = ?1 and u.customer_id = ?2 and u.is_active = 1", nativeQuery = true)
	int checkUserBelongCustomer(Integer userId, Integer customerId);

	@Query(value = "CALL common.checkUserIdPermission(?1, ?2)", nativeQuery = true)
	Integer checkUserIdPermission(Integer userId, Integer userIdChecked);

	@Query(value = "Select validateCreateUser(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9)", nativeQuery = true)
	String validateCreateUser(Integer provinceId, Integer districtId, Integer customerId, Integer departmentId,
			Integer userId, Integer roleId, String phone, String email, Integer currentUserId);
	
	@Query(value = "Select validateCreateCustomer(?1, ?2, ?3, ?4, ?5, ?6)", nativeQuery = true)
	String validateCreateCustomer(Integer provinceId, Integer districtId, String phone, String email, String custId, Integer customerId);

	// kiem tra ton tai phuong tien
	@Query(value = "SELECT count(1) FROM transport t where t.transport_id = :transportId and t.is_active = 1 ", nativeQuery = true)
	int countTransportId(Integer transportId);

	// kiem tra ton tai nguoi dung
	@Query(value = "SELECT count(1) FROM user where user_id = :userId and is_active = 1 ", nativeQuery = true)
	int countUserId(Integer userId);

	@Query(value = "select checkPermissionTransport(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
	Boolean checkPermissionTransport(Integer roleId, Integer provinceId, Integer districtId, Integer customerId,
			Integer userId, Integer transportId);

	// check userid co thuoc su quan ly cua user hien tai
	@Query(value = "select checkPermissionUser(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
	Boolean checkPermissionUser(Integer roleId, Integer provinceId, Integer districtId, Integer customerId,
			Integer userIdHeader, Integer userId);

	// Lay ten tinh/huyen
	@Query(value = "Select name from province where id = :id", nativeQuery = true)
	String getProvinceName(Integer id);

	// Lay ten phong ban theo id
	@Query(value = "Select name from department where department_id = :id", nativeQuery = true)
	String getDeparmentName(Integer id);

	// Lay ten khach hàng theo id
	@Query(value = "Select name from customer where customer_id = :id", nativeQuery = true)
	String getCustomerName(Integer id);

	// Lay ten khach hàng theo id
	@Query(value = "Select role_name from role where role_id = :id", nativeQuery = true)
	String getRoleName(Integer id);

	// Lay provinceId theo id
	@Query(value = "Select province_id from customer where customer_id = :id", nativeQuery = true)
	Integer getProvinceIdOfCustomer(Integer id);

	@Query(value = "Select district_id from customer where customer_id = :id", nativeQuery = true)
	Integer getDictrictIdOfCustomer(Integer id);
	
	@Query(value = "SELECT COUNT(*) FROM common.user WHERE is_active = 1 AND (username = ?1)", nativeQuery = true)
	Integer isExistUsername(String username);
	
	@Query(value = "SELECT COUNT(*) FROM common.user WHERE is_active = 1 AND (email = ?1)", nativeQuery = true)
	Integer isExistEmail(String email);
	
	@Query(value = "SELECT COUNT(*) FROM common.user WHERE is_active = 1 AND (phone = ?1)", nativeQuery = true)
	Integer isExistPhone(String phone);
	
	@Query(value = "CALL common.proc_ship_get_user_detail(?1)", nativeQuery = true)
	ShipUserDetailsDTO getShipUserDetails(@Param("userId") Integer userId);
	
	@Transactional
	@Modifying
	@Query("UPDATE User u SET u.password = ?2, u.modifiedBy = ?3, u.modifiedDate = ?4 WHERE u.username = ?1 AND u.isActive = 1")
	void changePassword(String username, String newPassword, String modifiedBy, LocalDateTime modifiedDate);
	
}
