package com.fms.customerservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fms.customerservice.model.PointDTO;
import com.fms.customerservice.model.RouteDetail;

@Repository
public interface RouteDetailRepository extends JpaRepository<RouteDetail, Integer> {
	@Query(value = "SELECT r.latitude as lat, r.longitude as lng, r.is_marker as isMarker, r.point_order as pointOrder FROM route_detail r "
			+ "where r.route_id = :routeId AND r.is_active = 1 order by point_order asc ", nativeQuery = true)
	List<PointDTO> getListRouteDetail(@Param("routeId") Integer routeId);
	
	@Query(value = "SELECT points FROM common.route_fms WHERE route_id = ?1 AND is_active = 1",nativeQuery = true)
	String getStringListRouteDetail(Integer routeId);
	@Transactional
	@Modifying
	@Query(value ="Update route_detail SET is_active = 0, modified_date = sysdate(), modified_by = :modifiedBy WHERE route_id = :routeId", nativeQuery=true)
	void deleteRouteDetail(Integer routeId, String modifiedBy);
}
