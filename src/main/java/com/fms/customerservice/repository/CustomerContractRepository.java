package com.fms.customerservice.repository;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.CustomerContract;

@Repository
public interface CustomerContractRepository extends JpaRepository<CustomerContract, Integer> {
	@Modifying
	@Transactional
	@Query(value = "UPDATE common.customer_contract cc SET cc.is_active = 0, cc.status = ?2, cc.modified_by = ?3, cc.modified_date = ?4, cc.end_date = ?4 WHERE cc.contract_code = ?1 AND cc.is_active = 1", nativeQuery = true)
	public Integer updateStatusByContractCode(String contractCode, Integer status, String modifiedBy, LocalDateTime modifiedDate);
	
	@Query(value = "SELECT COUNT(customer_contract_id) FROM common.customer_contract cc WHERE cc.contract_code = ?1 AND cc.is_active = 1 LIMIT 1", nativeQuery = true)
	public Integer isExistContractCode(String contractCode);
	
	@Query(value = "SELECT COUNT(customer_contract_id) FROM common.customer_contract cc WHERE cc.contract_code = ?1 AND cc.cust_id = ?2 AND cc.is_active = 1 LIMIT 1", nativeQuery = true)
	public Integer isExactContract(String contractCode,String custId);
	
	@Query(value = "SELECT COUNT(customer_contract_id) FROM common.customer_contract cc WHERE cc.contract_code = ?1 AND cc.customer_id != ?2 AND cc.is_active = 1 LIMIT 1", nativeQuery = true)
	public Integer isExistContractCodeOfAnotherCustomer(String contractCode,Integer customerId);
	
}
