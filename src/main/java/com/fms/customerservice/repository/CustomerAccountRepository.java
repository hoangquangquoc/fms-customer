package com.fms.customerservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.CustomerAccount;


@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, Integer> {
	@Modifying
	@Transactional
	@Query(value = "UPDATE common.customer_account ca SET ca.status = ?2, ca.reason = ?3, ca.modified_by = ?4, ca.modified_date = ?5 WHERE ca.account_code = ?1 AND ca.is_active = 1 AND ca.status != 3", nativeQuery = true)
	public Integer updateStatusByAccountCode(String accountCode, Integer status, String reason, String modifiedBy, LocalDateTime modifiedDate);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE common.customer_account ca SET ca.is_active = 0 WHERE ca.account_code = ?1", nativeQuery = true)
	public Integer inActiveByAccountCode(String accountCode);
	
	@Modifying
	@Transactional
	@Query(value = "CALL common.proc_customer_account_update_promotion(?1,?2,?3,?4,?5,?6)", nativeQuery = true)
	public Integer updatePromotionByAccountCode(String accountCode, String prePaymentCode, String prePaymentName, String effectDate, String expiredDate, String modifiedBy);
	
	

	@Modifying
	@Transactional
	@Query(value = "UPDATE common.customer_account ca SET ca.status = ?2, ca.reason = ?3, ca.modified_by = ?4, ca.modified_date = ?5 "
			+ "WHERE ca.contract_code = ?1 AND ca.is_active = 1 AND ca.status != 3", nativeQuery = true)
	public Integer updateStatusByContractCode(String contractCode, Integer status, String reason, String modifiedBy, LocalDateTime modifiedDate);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE common.customer_account ca SET ca.is_active = 0 WHERE ca.contract_code = ?1", nativeQuery = true)
	public Integer inActiveByContractCode(String contractCode);
	@Modifying
	@Transactional
	@Query(value = "UPDATE common.customer_account ca SET ca.active_date = ?2, ca.expired_date = ?3 WHERE ca.account_code = ?1", nativeQuery = true)
	public Integer updateActiveDate(String accountCode, LocalDateTime activeDate, LocalDateTime expiredDate);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE common.customer_account ca SET ca.is_active = 0, ca.modified_date = ?2, ca.modified_by = ?3 WHERE ca.customer_account_id = ?1", nativeQuery = true)
	public Integer delete(Integer customerAccountId, LocalDateTime modifiedDate, String modifiedBy);
	
	@Query(value = "SELECT contract_code FROM common.customer_account WHERE account_code = ?1 LIMIT 1", nativeQuery = true)
	public String getContractCodeByAccountCode(String accountCode);
	
	@Query(value = "SELECT * FROM common.customer_account ca WHERE ca.account_code = ?1 LIMIT 1", nativeQuery = true)
	public CustomerAccount getAccountByAccountCode(String accountCode);
	
	@Query(value = "SELECT COUNT(customer_account_id) FROM common.customer_account ca WHERE ca.account_code = ?1 AND ca.is_active = 1 LIMIT 1", nativeQuery = true)
	public Integer isExistAccountCode(String accountCode);
	
	@Query(value = "SELECT COUNT(customer_account_id) FROM common.customer_account ca WHERE ca.customer_account_id = ?1 AND ca.is_active = 1 LIMIT 1", nativeQuery = true)
	public Integer isExistCustomerAccountId(Integer customerAccountId);
	
	@Query(value = "SELECT COUNT(customer_account_id) FROM common.transport t WHERE t.customer_account_id = ?1 AND t.is_active = 1 LIMIT 1", nativeQuery = true)
	public Integer isAssignedTransport(Integer customerAccountId);
	
	@Query(value = "SELECT duration FROM common.customer_account ca WHERE ca.account_code = ?1 LIMIT 1", nativeQuery = true)
	public Integer getDurationByAccountCode(String accountCode);
	
	@Query(value = "SELECT status FROM common.customer_account ca WHERE ca.account_code = ?1 LIMIT 1", nativeQuery = true)
	public Integer getStatusByAccountCode(String accountCode);
	
	@Query(value = "SELECT customer_account_id FROM common.customer_account ca WHERE ca.account_code = ?1 LIMIT 1", nativeQuery = true)
	public Integer getCusstomerAccountIdByAccountCode(String accountCode);
	
	@Query(value = "SELECT customer_id FROM common.customer_account ca WHERE ca.customer_account_id = ?1 LIMIT 1", nativeQuery = true)
	public Integer getCustomerIdByCustomerAccountid(Integer customerAccountId);
	
	@Query(value = "SELECT COUNT(customer_account_id) FROM common.customer_account ca WHERE ca.contract_code = ?1 AND ca.status != 3 AND ca.is_active = 1 LIMIT 1", nativeQuery = true)
	public Integer getNumAccountOfContract(String contractCode);
	
	@Query(value = "SELECT * FROM common.customer_account ca WHERE ca.customer_id = ?1 AND ca.is_active = 1 ORDER BY ca.contract_code, ca.account_code", nativeQuery = true)
	public List<CustomerAccount> getListAccountByCustomerId(Integer customerId);
}
