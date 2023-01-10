package com.fms.customerservice.repository;

import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
	@Query(value = "select * from notification where user_id = ?1 and is_active = 1", nativeQuery = true)
	Object getAllNotificationOfUser(Integer userId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE common.notification SET status = ?1 WHERE id = ?2", nativeQuery = true)
	public Integer setNotificationStatus(Integer status, Integer id);
	
	@Query(value = "select * from notification where id = ?1 and is_active = 1", nativeQuery = true)
	Optional<Notification> findById(Integer notificationId);
}
