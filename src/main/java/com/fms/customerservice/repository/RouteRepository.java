package com.fms.customerservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fms.customerservice.model.Route;
import com.fms.customerservice.model.RouteDetailsDTO;
import com.fms.customerservice.model.TollgateDTO;
import com.fms.customerservice.model.TransportDTO;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
	@Query(value = "Select checkPermissionCustomer(?1, ?2, ?3, ?4, ?5)", nativeQuery = true)
	Boolean checkCustomerBelongUser(Integer roleId, Integer provinceId, Integer districtId, Integer customerIdHeader,
			Integer customerId);

	@Query(value = "SELECT * FROM route_fms r where r.route_id = :routeId AND r.is_active = 1 ", nativeQuery = true)
	Route getById(@Param("routeId") Integer routeId);

	@Query(value = "SELECT count(1) FROM route_fms r where r.route_id = :routeId AND r.customer_id = :customerId AND r.is_active = 1 ", nativeQuery = true)
	int checkExistByCustomerId(Integer routeId, Integer customerId);

	@Query(value = "SELECT route_id as routeId, name, mail_warning as emailWarning, phone_warning as phoneWarning, customer_id as customerId, deviation, direction, route_type as routeType, points "
			+ "  FROM route_fms  where route_id = :routeId AND is_active = 1 ", nativeQuery = true)
	RouteDetailsDTO getDetails(@Param("routeId") Integer routeId);

	@Query(value = "select rt.transport_id as transportId, t.register_no as registerNo, "
			+ " rt.is_warning as isWarning, DATE_FORMAT(rt.from_date,'%d/%m/%Y') as fromDate, DATE_FORMAT(rt.to_date,'%d/%m/%Y') as toDate from route_transport_fms rt "
			+ " join transport t on t.transport_id = rt.transport_id and t.is_active = 1 "
			+ " where rt.route_id = :routeId ", nativeQuery = true)
	List<TransportDTO> getVehiclesOfRoute(Integer routeId);

	@Transactional
	@Modifying
	@Query(value = "delete from route_transport_fms where route_id = ?1", nativeQuery = true)
	void deleteVehiclesAssigned(Integer routeId);

	// check customer co thuoc su quan ly cua user hien tai
	@Query(value = "select checkPermissionCustomer(?1,?2,?3,?4,?5)", nativeQuery = true)
	Boolean checkPermissionCustomer(Integer roleId, Integer provinceId, Integer districtId, Integer customerIdHeader,
			Integer customerId);

	@Query(value = "select checkPermissionTransport(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
	Boolean checkPermissionTransport(Integer roleId, Integer provinceId, Integer districtId, Integer customerId,
			Integer userId, Integer transportId);

	@Query(value = "select count(1) from customer where customer_id = ?1 and is_active = 1", nativeQuery = true)
	int checkCustomer(Integer customerId);

	@Query(value = "select name, latitude as lat, longitude as lng, address from tollgate where is_active = 1 ORDER BY name COLLATE utf16_vietnamese_ci", nativeQuery = true)
	List<TollgateDTO> getAllTollgate();

	@Query(value = "select count(1) from transport where transport_id = ?1 and customer_id = ?2 and is_active = 1", nativeQuery = true)
	int checkTransport(Integer transportId, Integer customerId);

	@Query(value = "select count(1) from transport where transport_id = ?1 and is_active = 1", nativeQuery = true)
	int checkExistTransport(Integer transportId);

	@Query(value = "select count(1) from route_transport_fms where transport_id = ?1 and route_id = ?2 and is_active = 1", nativeQuery = true)
	int checkTransportInRoute(Integer transportId, Integer routeId);
}
