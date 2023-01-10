package com.fms.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fms.customerservice.model.ActionLog;


public interface ActionLogRepository extends JpaRepository<ActionLog, Integer>{

}
