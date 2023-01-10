package com.fms.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.StopPoint;
import com.fms.customerservice.model.StopPointDetails;

@Repository
public interface StopPointRepository extends JpaRepository<StopPoint, Integer> {
	@Query(value = "Select checkPermissionCustomer(?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
	Boolean checkCustomerBelongUser(Integer roleId, Integer provinceId, Integer districtId, Integer customerIdHeader,
			Integer customerId);

	@Query(value = "SELECT * FROM stop_point s where s.stop_point_id = :stopPointId AND s.is_active = 1 ", nativeQuery = true)
	StopPoint getById(@Param("stopPointId") Integer stopPointId);
	
	@Query(value = "SELECT name, customer_id as customerId, latitude lat, longitude as lng, note, "
			+ " stop_point_type_id as stopPointTypeId, radius, stop_time as stopTime FROM stop_point "
			+ " where stop_point_id = :stopPointId AND is_active = 1 ", nativeQuery = true)
	StopPointDetails getDetails(@Param("stopPointId") Integer stopPointId);
	
	// kiem tra ton tai khach hang
	@Query(value = "Select count(1) from customer WHERE customer_id = :id and is_active = 1", nativeQuery = true)
	int countCustomerId(Integer id);
	
	// kiem tra ton tai loai diem dung
	@Query(value = "Select count(1) from stop_point_type WHERE stop_point_type_id = :id and is_active = 1", nativeQuery = true)
	int countStopPointTypeId(Integer id);
}
