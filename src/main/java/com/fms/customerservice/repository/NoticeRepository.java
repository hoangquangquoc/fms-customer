package com.fms.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
	@Query(value = "select * from notice where notice_id = ?1 and is_active = 1", nativeQuery = true)
	Notice findByNoticeId(Integer noticeId);

	@Query(value = "Select countNewNotice(?1)", nativeQuery = true)
	int countNewNotice(Integer userId);
}
