package com.fms.customerservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fms.customerservice.model.Driver;
import com.fms.customerservice.model.SupportInfoDTO;
import com.fms.customerservice.model.SupportSMSDTO;
import com.fms.customerservice.model.TipDTO;
import com.fms.customerservice.model.VideoDTO;

@Repository
public interface SupportRepository extends JpaRepository<Driver, Integer>{
	@Query(value = " SELECT name, email, phone, position FROM support_info where is_active = 1 ORDER BY agent_order", nativeQuery = true)
	List<SupportInfoDTO> getSupportInfo();
	
	@Query(value = " SELECT type, title, get_sms as getSms, set_sms as setSms, description FROM support_sms where is_active = 1 ORDER BY type, title COLLATE utf16_vietnamese_ci", nativeQuery = true)
	List<SupportSMSDTO> getSupportSMS();
	
	@Query(value = " SELECT video_id as videoId, video_name as videoName, link, DATE_FORMAT(uploaded_date, '%d/%m/%Y %H:%i:%s') as uploadedDate, description, order_num as orderNum, type FROM videos where is_active = 1 ORDER BY type, order_num", nativeQuery = true)
	List<VideoDTO> getListVideo();
	
	@Query(value = " SELECT tip_id as tipId, tip_name as tipName, summary, order_num as orderNum, content, url_image as urlImage, DATE_FORMAT(created_date, '%d/%m/%Y %H:%i:%s') as createdDate FROM tips " + 
					" where is_active = 1 ORDER BY order_num ", nativeQuery = true)
	List<TipDTO> getListTip();
}
