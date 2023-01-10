package com.fms.customerservice.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fms.customerservice.model.FeedbackRequest;
import com.fms.customerservice.model.Notice;
import com.fms.customerservice.model.NoticeMark;
import com.fms.customerservice.model.NoticeModel;
import com.fms.customerservice.model.NoticeResponse;
import com.fms.customerservice.model.responsemodel.Response;
import com.fms.customerservice.service.NoticeService;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;

@RestController
@RequestMapping("/v1/notices")
public class NoticeController {
	@Autowired
	NoticeService noticeService;

	/*
	 * lay danh sach thong bao
	 */
	@GetMapping
	public ResponseModel<NoticeResponse> getAllNotice(@RequestHeader("userId") Integer userId) {
		ResponseModel<NoticeResponse> response = new ResponseModel<NoticeResponse>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(noticeService.getAllNotices(userId));
		return response;
	}
	
	/*
	 * lay danh sach thong bao chua doc
	 */
	@GetMapping("/count")
	public ResponseModel<Integer> countNewNotice(@RequestHeader("userId") Integer userId) {
		ResponseModel<Integer> response = new ResponseModel<Integer>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(noticeService.countNewNotices(userId));
		return response;
	}
	
	/*
	 * danh dau da doc thong bao
	 */
	@GetMapping("/markread/{noticeIds}")
	public ResponseModel<NoticeResponse> markRead(
			@RequestHeader("userId") Integer userId,
			@PathVariable String noticeIds) {
		ResponseModel<NoticeResponse> response = new ResponseModel<NoticeResponse>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(noticeService.markReadNotice(noticeIds, userId));
		return response;
	}
	
	/*
	 * danh dau da doc thong bao
	 */
	@PostMapping("/markread")
	public ResponseModel<NoticeResponse> markReadNotice(
			@RequestHeader("userId") Integer userId,
			@RequestBody NoticeMark noticeIds) {
		ResponseModel<NoticeResponse> response = new ResponseModel<NoticeResponse>();
		response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
		response.setData(noticeService.markReadNotice(noticeIds.getNoticeIds(), userId));
		return response;
	}

	/*
	 * tao moi thong bao
	 */
	@PostMapping
	public ResponseModel<String> createNotice(
			@RequestHeader("roleId") Integer roleId,
			@RequestHeader("username") String username, 
			@RequestBody NoticeModel model) {
		ResponseModel<String> response = new ResponseModel<String>();
		String result = noticeService.createNotice(model, username, roleId);
		response.setCode(Constants.SC_CREATED);
		response.setMessage(result);
		return response;
	}

	/*
	 * cap nhat thong bao
	 */
	@PutMapping("/{noticeId}")
	public ResponseModel<String> updateNotice(@PathVariable Integer noticeId, @RequestHeader("roleId") Integer roleId,
			@RequestHeader("username") String username, @RequestBody NoticeModel model) {
		ResponseModel<String> response = new ResponseModel<String>();
		String result = noticeService.updateNotice(model, username, roleId, noticeId);
		response.setMessage(result);
		return response;
	}
	
	/*
	 * lay thong tin chi tiet thong bao
	 */
	@GetMapping("/{noticeId}")
	public ResponseModel<Notice> getNoticeDetail(
			@PathVariable Integer noticeId, 
			@RequestHeader("roleId") Integer roleId) {
		ResponseModel<Notice> response = new ResponseModel<Notice>();
		Notice data = noticeService.getNoticeDetails(roleId, noticeId);
		response.setData(data);
		return response;
	}

	/*
	 * xoa thong bao
	 */
	@DeleteMapping("/{noticeId}")
	public ResponseModel<String> deleteNotice(@PathVariable Integer noticeId, @RequestHeader("roleId") Integer roleId,
			@RequestHeader("username") String username) {
		ResponseModel<String> response = new ResponseModel<String>();
		String result = noticeService.deleteNotice(username, roleId, noticeId);
		response.setMessage(result);
		return response;
	}
	
	/**
	 * API cho mobile
	 * @author quochq
	 *
	 */	
	@GetMapping("/mobile")
	public Object getListNotification(@RequestHeader("userId") Integer userId,
			@RequestParam(name = Constants.PAGE_REQUEST_PARAM, defaultValue = Constants.PAGE_DEFAULT) int pageNumber,
			@RequestParam(name = Constants.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.PAGE_SIZE_DEFAULT) int pageSize) {
		Object result = noticeService.getListNotiPaging(userId, pageNumber, pageSize);
		return result;
	}
	
	/*
	 * mobile - đánh dấu đã đọc
	 */
	@GetMapping("/mobile/markread/{notificationId}")
	public ResponseModel<Object> markAsRead(
			@RequestHeader("userId") Integer userId,
			@PathVariable Integer notificationId) {
		ResponseModel<Object> response = new ResponseModel<Object>();
		response.setCode(Constants.SC_OK);
		response.setMessage(noticeService.markAsRead(userId, notificationId));
		return response;
	}
	
	/**
     * API - insertFeedback
     *
     */
    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public Response insertFeedback(@RequestBody FeedbackRequest feedback, @RequestHeader("userId") Integer userId) {
    	Response response = new Response();
        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage(noticeService.insertFeedback(feedback,userId));
        return response;
    }
}
