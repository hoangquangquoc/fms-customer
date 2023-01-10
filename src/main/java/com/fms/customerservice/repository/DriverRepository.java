package com.fms.customerservice.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.Driver;
import com.fms.customerservice.model.DriverCustomerDTO;
import com.fms.customerservice.model.DriverDTO;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
	@Query("select d from Driver d where d.employeeId = ?1 and d.isActive = 1")
	Driver findByDriverCode(String driverCode);

	@Query("select d from Driver d where d.driverId =?1 and d.isActive = 1")
	Driver findByDriverId(Integer driverId);

	List<Driver> findAll();

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM Driver d WHERE d.driverId =?1")
	void deleteByDriverId(Integer driverId);
	
	String query = "SELECT d.driver_id as driverId,d.name as driverName, d.driver_code as employeeId,  " + 
			"DATE_FORMAT(d.date_of_birth,'%d/%m/%Y') as dob, d.indentify as ID, d.license, " + 
			"d.phone, d.address, c.name as customerName, c.customer_id " + 
			"FROM driver d left join customer c on d.customer_id = c.customer_id " + 
			"where d.is_active = 1 " + 
			"and (?1 is null or lower(d.name) like ?1 ESCAPE '/')  " + 
			"and (?2 is null or d.phone like ?2 ESCAPE '/') " + 
			"and (?3 is null or lower(d.indentify) like ?3 ESCAPE '/') " + 
			"and (?4 is null or lower(d.license) like ?4 ESCAPE '/') " + 
			"and (?5 is null or d.date_of_birth = ?5 ) " +
			"and (?6 = -1 or c.province_id = ?6) " + 
			"and (?7 = -1 or c.customer_id = ?7) " +
			"and (?8 = -1 or c.district_id = ?8) order by d.name COLLATE utf8_vietnamese_ci ";
	
	@Query(value = query, countQuery = "select count(*) from (" + query + ") tmp", nativeQuery = true)
	Page<DriverDTO> getAllDrivers( String name, String phone,String id, String license, String dob, Integer provinceId, Integer customerId,Integer districtId, Pageable pageable);

	String queryDriverOfCustomer = "SELECT d.driver_id as driverId,d.name as driverName FROM driver d "
			+ "left join customer c on d.customer_id = c.customer_id and c.is_active = 1 "
			+ "where d.is_active = 1 "
			+ "and (?1 = -1 or c.province_id = ?1) "
			+ "and (?2 = -1 or c.customer_id = ?2) "
			+ "and (?3 = -1 or c.district_id = ?3) ";
	@Query(value = queryDriverOfCustomer, countQuery = "select count(*) from (" + queryDriverOfCustomer + ") tmp", nativeQuery = true)
	List<DriverCustomerDTO> getDriverOfCustomer(Integer provinceId, Integer customerId, Integer districtId);
}
