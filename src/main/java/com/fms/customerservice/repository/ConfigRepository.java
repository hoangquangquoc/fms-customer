package com.fms.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.ConfigDTO;
import com.fms.customerservice.model.CustomerConfig;

@Repository
public interface ConfigRepository extends JpaRepository<CustomerConfig, Integer> {
	@Query(value = "Select config_id as configId, customer_id as customerId, send_email as sendEmail, "
			+ " send_sms as sendSms, email, phone, registry_before as registryBefore, maintenance_before as maintenanceBefore, "
			+ " warning_renewal_before as warningRenewalBefore from customer_config WHERE customer_id = :customerId ", nativeQuery = true)
	ConfigDTO getConfigByCustomerId(Integer customerId);
	
	@Query(value = "SELECT c FROM CustomerConfig c WHERE c.customerId = :customerId ")
	CustomerConfig getConfigOfCustomer(Integer customerId);
	
	@Query(value = "Select count(1) from customer_config WHERE config_id = :id ", nativeQuery = true)
	int countConfig(Integer id);
	
	@Query(value = "Select count(1) from customer_config WHERE customer_id = :id ", nativeQuery = true)
	int countConfigByCustomer(Integer id);
}
