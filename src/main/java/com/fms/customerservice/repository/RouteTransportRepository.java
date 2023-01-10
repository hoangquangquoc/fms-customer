package com.fms.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.RouteTransport;

@Repository
public interface RouteTransportRepository extends JpaRepository<RouteTransport, Integer> {
	@Query(value = "SELECT * FROM common.route_transport_fms WHERE route_id = ?1 AND transport_id = ?2 AND is_active = 1", nativeQuery = true)
	RouteTransport findByTransportIdAndRouteId(Integer routeId, Integer transportId);
}
