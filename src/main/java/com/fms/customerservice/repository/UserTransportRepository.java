package com.fms.customerservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fms.customerservice.model.UserTranport;

@Repository
public interface UserTransportRepository extends JpaRepository<UserTranport, Integer> {
	@Transactional
	@Modifying
	@Query(value = "delete from user_transport where user_id = ?1", nativeQuery = true)
	void deleteVehiclesAssigned(Integer userId);
	
	@Query(value = "Select group_concat(register_no ORDER BY register_no) from transport where transport_id in (select transport_id from  user_transport where user_id = ?1)", nativeQuery = true)
	String getVehiclesAssignedCurrent(Integer userId);
	
	@Query(value = "Select group_concat(register_no ORDER BY register_no) from transport where transport_id in (:transportIds)", nativeQuery = true)
	String getVehiclesAssignedNew(List<Integer> transportIds);
}
