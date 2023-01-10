package com.fms.customerservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.StopPointType;
import com.fms.customerservice.model.StopPointTypeDTO;

@Repository
public interface StopPointTypeRepository extends JpaRepository<StopPointType, Integer> {
	@Query(value = "SELECT stop_point_type_id as stopPointTypeId, name, icon "
			+ "FROM stop_point_type where is_active = 1 ORDER BY name COLLATE utf8_vietnamese_ci ", nativeQuery=true)
	List<StopPointTypeDTO> getAllStopPointType();
}
