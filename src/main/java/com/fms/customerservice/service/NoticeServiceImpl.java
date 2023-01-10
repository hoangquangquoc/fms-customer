package com.fms.customerservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fms.customerservice.model.Feedback;
import com.fms.customerservice.model.FeedbackRequest;
import com.fms.customerservice.model.Notice;
import com.fms.customerservice.model.NoticeDTO;
import com.fms.customerservice.model.NoticeModel;
import com.fms.customerservice.model.NoticeResponse;
import com.fms.customerservice.model.Notification;
import com.fms.customerservice.model.NotificationModel;
import com.fms.customerservice.repository.FeedbackRepository;
import com.fms.customerservice.repository.NoticeRepository;
import com.fms.customerservice.repository.NotificationRepository;
import com.fms.module.exception.BusinessException;
import com.fms.module.exception.ErrorException;
import com.fms.module.model.MetaModel;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;
import com.fms.module.utils.ValidateUtils;
import com.google.common.base.Joiner;

@Service
public class NoticeServiceImpl implements NoticeService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NoticeServiceImpl.class);
	@Autowired
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public NoticeServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Autowired
	NoticeRepository noticeRepository;
	
	@Autowired
	NotificationRepository notificationRepository;

	@Autowired
	FeedbackRepository feedbackRepository;
	@Override
	public String createNotice(NoticeModel model, String username, Integer roleId) {
		try {
			validateNotice(model, roleId);

			Notice notice = new Notice(model.getTitle(), model.getContent(), model.getType(), model.getIsImportant(),
					model.getLink());
			notice.audit(Constants.LOCALE_CREATE, username);
			noticeRepository.save(notice);

			return Utils.getSuccessMessage(Constants.LOCALE_CREATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void validateNotice(NoticeModel model, Integer roleId) {
		if (!Constants.SYS_ADMIN_ROLE.equals(roleId)) {
			throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
		}
		if (ValidateUtils.isNullOrEmpty(model.getTitle())) {
			throw new BusinessException(Utils.getValidateMessage("notice.title-blank"));
		}
		if (ValidateUtils.isNullOrEmpty(model.getContent())) {
			throw new BusinessException(Utils.getValidateMessage("notice.content-blank"));
		}
	}

	@Override
	public String updateNotice(NoticeModel model, String username, Integer roleId, Integer noticeId) {
		try {
			Notice noticeCurrent = findNotice(noticeId);
			validateNotice(model, roleId);
			Notice notice = new Notice(model.getTitle(), model.getContent(), model.getType(), model.getIsImportant(),
					model.getLink());

			notice.setNoticeId(noticeId);
			notice.audit(Constants.LOCALE_UPDATE, username);
			Utils.mappingDTO(notice, noticeCurrent);

			noticeRepository.save(noticeCurrent);

			return Utils.getSuccessMessage(Constants.LOCALE_UPDATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private Notice findNotice(Integer noticeId) {
		Notice notice = noticeRepository.findByNoticeId(noticeId);
		if (notice == null) {
			throw new BusinessException(Utils.getValidateMessage("notice.notexist"));
		}
		return notice;
	}

	@Override
	public String deleteNotice(String username, Integer roleId, Integer noticeId) {
		try {
			if (!Constants.SYS_ADMIN_ROLE.equals(roleId)) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}
			Notice noticeCurrent = findNotice(noticeId);
			noticeCurrent.audit(Constants.LOCALE_DELETE, username);

			noticeRepository.save(noticeCurrent);

			return Utils.getSuccessMessage(Constants.LOCALE_DELETE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public NoticeResponse getAllNotices(Integer userId) {
		return getAllNotice(userId, null);
	}

	private NoticeResponse getAllNotice(Integer userId, String noticeIds) {
		try {
			NoticeResponse noticeResponse = new NoticeResponse();
			String procedure = "CALL getNotice(%s,'%s', @count)";

			String SQL = String.format(procedure, userId, noticeIds);

//			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			/*
			 * hoadp
			 * fix sonar
			 */
			List<Map<String, Object>> rows;
			
			List<NoticeDTO> listNotices = new ArrayList<NoticeDTO>();
			try {
				rows = jdbcTemplate.queryForList(SQL);
				for (Map<String, Object> row : rows) {
					NoticeDTO notice = new NoticeDTO();
					notice.setContent((String) row.get("content"));
					notice.setCreatedDate((String) row.get("createdDate"));
					notice.setNoticeId((Integer) row.get("noticeId"));
					notice.setStatus((Long) row.get("status"));
					notice.setTitle((String) row.get("title"));
					notice.setIsImportant((Integer) row.get("isImportant"));
					listNotices.add(notice);
				}
			} catch (Exception e) {
				throw new ErrorException(e);
			}

			Map<String, Object> subObject = jdbcTemplate.queryForMap("SELECT @count");
			long countNew = 0;

			if (subObject != null) {
				countNew = (Long) subObject.get("@count");
			}

			noticeResponse.setListNotices(listNotices);
			noticeResponse.setCountNew(countNew);
			return noticeResponse;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public NoticeResponse markReadNotice(String noticeIds, Integer userId) {
		return getAllNotice(userId, noticeIds);
	}

	@Override
	public Notice getNoticeDetails(Integer roleId, Integer noticeId) {
		try {
			if (!Constants.SYS_ADMIN_ROLE.equals(roleId)) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}
			return noticeRepository.findByNoticeId(noticeId);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public NoticeResponse markReadNotice(List<Integer> noticeIds, Integer userId) {
		if (noticeIds == null || noticeIds.size() == 0) {
			throw new ErrorException(new BusinessException(Utils.getValidateMessage("input_invalid")));
		}
		
		noticeIds = noticeIds.stream().distinct().collect(Collectors.toList());
		String listNoticeId = Joiner.on(',').join(noticeIds);
		return getAllNotice(userId, listNoticeId);
	}

	@Override
	public int countNewNotices(Integer userId) {
		try {
			return noticeRepository.countNewNotice(userId);
		}catch (Exception e) {
			LOGGER.error("error: ",e);
			return 0;
		}
	}

	@Override
	public Object getListNotification(Integer userId, Integer pageNumber, Integer pageSize) {
		try {
			if (userId == null) {
				throw new BusinessException(Utils.getValidateMessage("user.userId-notexist"));
			}
			Object results = notificationRepository.getAllNotificationOfUser(userId);
			
			return results;
		} catch (Exception e) {
			throw new ErrorException(e);
		}

	}
	
	@Override
	public ResponseModel<List<NotificationModel>> getListNotiPaging(Integer userId, Integer pageNumber, Integer pageSize) {
		try {
			if (pageNumber <= 0)
				throw new BusinessException(Utils.getErrorMessageValidate(Constants.PAGE_REQUEST_PARAM, "Pattern"));
			if (pageSize <= 0)
				throw new BusinessException(
						Utils.getErrorMessageValidate(Constants.PAGE_SIZE_REQUEST_PARAM, "Pattern"));

			ResponseModel<List<NotificationModel>> response = new ResponseModel<List<NotificationModel>>();
			String procedure = "CALL mobile_getUserNotifications(%s, %s, %s)";

			String strQR = String.format(procedure, userId, pageNumber, pageSize);

			Long totalRecord = (long) 0;
			List<Map<String, Object>> rows;
			List<NotificationModel> listData = new ArrayList<NotificationModel>();
			try {
				rows = jdbcTemplate.queryForList(strQR);
				for (Map<String, Object> row : rows) {
					if (totalRecord == 0) {
						totalRecord = (Long) row.get("totalRecord");
					}
					NotificationModel notification = new NotificationModel();
					notification.setSubject((String) row.get("subject"));
					notification.setSubTitle((String) row.get("subTitle"));
					notification.setBody((String) row.get("body"));
					notification.setCreatedDate((String) row.get("createdDate"));
					notification.setId((Integer) row.get("id"));
					notification.setStatus((Integer) row.get("status"));
					notification.setType((Integer) row.get("type"));
					listData.add(notification);
				}
			} catch (Exception e) {
				throw new ErrorException(e);
			}

			int totalPages = (int) Math.ceil((float) totalRecord / pageSize);

			response.setMeta(new MetaModel(pageNumber, pageSize, totalPages, totalRecord));
			response.setData(listData);

			return response;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
	
	@Override
	public String markAsRead(Integer userId, Integer notificationId) {
		try {
			if (userId == null || notificationId == null) {
				throw new BusinessException(Utils.getValidateMessage("input_invalid"));
			}
			Notification noti = notificationRepository.findById(notificationId).get();
			if (noti == null || !noti.getUserId().equals(userId)) {
				throw new BusinessException(Utils.getMessageByKey("unauthorized.not_permission"));
			}
			notificationRepository.setNotificationStatus(Constants.OFF_STATE, noti.getId());//update 0
			
			return Utils.getSuccessMessage(Constants.LOCALE_UPDATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public String insertFeedback(FeedbackRequest feedbackRequest, Integer userId) {
		try {
	        //validate
			boolean isValidRating = Arrays.asList(Constants.FEEDBACK_RATING_ARR).contains(feedbackRequest.getRating());
	        if(ValidateUtils.isNullOrEmpty(feedbackRequest.getContent())
	        		|| feedbackRequest.getRating() == null
	        		|| !isValidRating)
	            throw new BusinessException(Utils.getValidateMessage("input_invalid"));
	        
	        
	        Feedback feedback = new Feedback(userId, feedbackRequest.getRating(), feedbackRequest.getContent().trim(), 1, LocalDateTime.now());
	        feedbackRepository.save(feedback);
	        
			return Utils.getSuccessMessage("create");
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

}
