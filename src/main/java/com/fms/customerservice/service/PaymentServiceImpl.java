package com.fms.customerservice.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fms.customerservice.model.PaymentHistory;
import com.fms.customerservice.model.PaymentMapped;
import com.fms.customerservice.model.PaymentRequest;
import com.fms.customerservice.repository.PaymentRepository;
import com.fms.customerservice.repository.RouteRepository;
import com.fms.module.exception.BusinessException;
import com.fms.module.exception.ErrorException;
import com.fms.module.model.MetaModel;
import com.fms.module.model.ResponseModel;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;
import com.fms.module.utils.ValidateUtils;

@Service
public class PaymentServiceImpl implements PaymentService {
	private final JdbcTemplate jdbcTemplate;
	@Autowired
	RouteRepository routeRepository;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	public PaymentServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// lay lich su thanh toan
	@Override
	public ResponseModel<List<PaymentHistory>> getPaymentHistory(SearchModel searchModel, UserHeader userHeader) {
		try {
			if (searchModel.getPageNumber() <= 0)
				throw new BusinessException(Utils.getErrorMessageValidate(Constants.PAGE_REQUEST_PARAM, "Pattern"));
			if (searchModel.getPageSize() <= 0)
				throw new BusinessException(
						Utils.getErrorMessageValidate(Constants.PAGE_SIZE_REQUEST_PARAM, "Pattern"));

			validateDate(searchModel);

			// TODO check quyen
			ResponseModel<List<PaymentHistory>> response = new ResponseModel<List<PaymentHistory>>();
			String procedure = "CALL getPaymentHistory(%s, %s, %s, '%s', '%s', '%s', @total)";

			String SQL = String.format(procedure, searchModel.getPageNumber(), searchModel.getPageSize(),
					userHeader.getUserId(), searchModel.getRegisterNo(), searchModel.getFromDate(),
					searchModel.getToDate());

//			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			/*
			 * hoadp
			 * fix sonar
			 */
			List<Map<String, Object>> rows;
			List<PaymentHistory> listObjects = new ArrayList<PaymentHistory>();
			try {
				rows = jdbcTemplate.queryForList(SQL);
				for (Map<String, Object> row : rows) {
					PaymentHistory obj = new PaymentHistory();
					obj.setCreatedDate((String) row.get("createdDate"));
					obj.setRegisterNo((String) row.get("registerNo"));
					obj.setSim((String) row.get("sim"));
					obj.setAmCode((String) row.get("amCode"));
					obj.setPackageName((String) row.get("packageName"));
					obj.setFee((Double) row.get("fee"));
					obj.setPaymentBy((String) row.get("paymentBy"));
					listObjects.add(obj);
				}
			} catch (Exception e) {
				throw new ErrorException(e);
			}

			Map<String, Object> subObject = jdbcTemplate.queryForMap("SELECT @total");
			String total = null;
			int totalRecord;

			if (subObject != null) {
				total = (String) subObject.get("@total");
			}

			totalRecord = total != null ? Integer.parseInt(total) : 0;
			int totalPages = (int) Math.ceil((float) totalRecord / searchModel.getPageSize());

			response.setMeta(
					new MetaModel(searchModel.getPageNumber(), searchModel.getPageSize(), totalPages, totalRecord));
			response.setData(listObjects);

			return response;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void validateDate(SearchModel searchModel) throws ParseException {
		Date fDate = null;
		Date tDate = null;

		if (!ValidateUtils.isNullOrEmpty(searchModel.getFromDate())) {
			fDate = Utils.stringConvertToDate(searchModel.getFromDate(), null);
			if (fDate == null)
				throw new BusinessException(Utils.getMessageByKey("validate.date.invalid"));

		}
		if (!ValidateUtils.isNullOrEmpty(searchModel.getToDate())) {
			tDate = Utils.stringConvertToDate(searchModel.getToDate(), null);
			if (tDate == null)
				throw new BusinessException(Utils.getMessageByKey("validate.date.invalid"));

		}

		// check thoi gian nhap vao
		if (fDate != null && tDate != null) {
			if (fDate.after(tDate)) {
				throw new BusinessException(
						Utils.getMessageByKey("validate.datetime.start_time_earlier_than_end_time"));
			}
		}
	}

	// lay danh sach thanh toan
	@Override
	public ResponseModel<List<PaymentMapped>> getPaymentList(SearchModel searchModel, UserHeader userHeader) {
		try {
			if (searchModel.getPageNumber() <= 0)
				throw new BusinessException(Utils.getErrorMessageValidate(Constants.PAGE_REQUEST_PARAM, "Pattern"));
			if (searchModel.getPageSize() <= 0)
				throw new BusinessException(
						Utils.getErrorMessageValidate(Constants.PAGE_SIZE_REQUEST_PARAM, "Pattern"));

			ResponseModel<List<PaymentMapped>> response = new ResponseModel<List<PaymentMapped>>();
			String procedure = "CALL getPaymentList(%s, %s, '%s', %s, @total)";

			String SQL = String.format(procedure, searchModel.getPageNumber(), searchModel.getPageSize(),
					searchModel.getRegisterNo().trim(), userHeader.getUserId());

//			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			/*
			 * hoadp
			 * fix sonar
			 */
			List<Map<String, Object>> rows;
			List<PaymentMapped> listObjs = new ArrayList<PaymentMapped>();
			try {
				rows = jdbcTemplate.queryForList(SQL);
				for (Map<String, Object> row : rows) {
					PaymentMapped obj = new PaymentMapped();
					obj.setTransportId((Integer) row.get("transportId"));
					obj.setExpiredDate((String) row.get("expiredDate"));
					obj.setPackageName((String) row.get("packageName"));
					obj.setRegisterNo((String) row.get("registerNo"));
					obj.setSim((String) row.get("sim"));
					listObjs.add(obj);
				}
			} catch (Exception e) {
				throw new ErrorException(e);
			}

			Map<String, Object> subObject = jdbcTemplate.queryForMap("SELECT @total");
			String total = null;
			int totalRecord;

			if (subObject != null) {
				total = (String) subObject.get("@total");
			}

			totalRecord = total != null ? Integer.parseInt(total) : 0;
			int totalPages = (int) Math.ceil((float) totalRecord / searchModel.getPageSize());

			response.setMeta(
					new MetaModel(searchModel.getPageNumber(), searchModel.getPageSize(), totalPages, totalRecord));
			response.setData(listObjs);

			return response;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public String renewalService(PaymentRequest paymentRequest, UserHeader userHeader) {
		try {
			// thuc hien validate tranportId
			if (ValidateUtils.isNullOrEmpty(paymentRequest.getTransportIds())) {
				throw new BusinessException(Utils.getValidateMessage("transport.listofvehicle-notblank"));
			}

			String[] ids = paymentRequest.getTransportIds().split(",");
			Integer[] transportIds = new Integer[ids.length];

			for (int i = 0; i < transportIds.length; i++) {
				transportIds[i] = ValidateUtils.parseIntFromStr(ids[i]);
			}

			for (Integer transportId : transportIds) {
				checkPermissionTransport(userHeader, transportId);
			}

			// TODO goi bccs

			// cap nhat ngay het han, luu lich su thanh toan
			String procedure = "CALL renewalService('%s', '%s', '%s', %s)";
			String SQL = String.format(procedure, paymentRequest.getTransportIds(), paymentRequest.getAmCode(),
					userHeader.getUsername(), paymentRequest.getClientOs());
			jdbcTemplate.execute(SQL);

			return Utils.getMessageByKey(Constants.SUCCESS_MESSAGE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void checkPermissionTransport(UserHeader user, Integer transportId) {
		Boolean permission = routeRepository.checkPermissionTransport(user.getRoleId(), user.getProvinceId(),
				user.getDistrictId(), user.getCustomerId(), user.getUserId(), transportId);

		if (!permission)
			throw new BusinessException(Utils.getValidateMessage("user.transport-author"));
	}

}
