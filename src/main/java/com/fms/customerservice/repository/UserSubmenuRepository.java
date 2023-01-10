package com.fms.customerservice.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.UserSubmenu;


@Repository
public interface UserSubmenuRepository extends JpaRepository<UserSubmenu, Integer>{

	List<UserSubmenu> findByUserSubmenuId(Integer userSubmenuId);
	List<UserSubmenu> findByUserId(Integer userId);
 	List<UserSubmenu> findAll();
 	@Transactional
	@Modifying
 	@Query(value="DELETE FROM UserSubmenu us WHERE us.userSubmenuId =?1")
 	void deleteByUserSubmenuId(Integer userSubmenuId);
}
