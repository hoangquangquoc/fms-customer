package com.fms.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.PaymentHistoryModel;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentHistoryModel, Integer> {

}
