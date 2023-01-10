package com.fms.customerservice.service;

import java.util.List;

import com.fms.customerservice.model.FeedbackRequest;
import com.fms.customerservice.model.Notice;
import com.fms.customerservice.model.NoticeModel;
import com.fms.customerservice.model.NoticeResponse;
import com.fms.customerservice.model.NotificationModel;
import com.fms.module.model.ResponseModel;

public interface NoticeService {

	String createNotice(NoticeModel model, String username, Integer roleId);

	String updateNotice(NoticeModel model, String username, Integer roleId, Integer noticeId);

	String deleteNotice(String username, Integer roleId, Integer noticeId);

	NoticeResponse getAllNotices(Integer userId);

	NoticeResponse markReadNotice(String noticeIds, Integer userId);

	NoticeResponse markReadNotice(List<Integer> noticeIds, Integer userId);
	
	Notice getNoticeDetails(Integer roleId, Integer noticeId);
	
	int countNewNotices(Integer userId);
	
	//mobile
	Object getListNotification(Integer userId, Integer pageNumber, Integer pageSize);
	
	ResponseModel<List<NotificationModel>> getListNotiPaging(Integer userId, Integer pageNumber, Integer pageSize);
	
	String markAsRead(Integer userId, Integer notificationId);

	/**
	 * @param feedback
	 * @param userId
	 * @return
	 * @author quochq
	 */
	String insertFeedback(FeedbackRequest feedback, Integer userId);
}
