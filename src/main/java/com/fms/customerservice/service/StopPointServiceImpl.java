package com.fms.customerservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fms.customerservice.model.StopPoint;
import com.fms.customerservice.model.StopPointDTO;
import com.fms.customerservice.model.StopPointDetails;
import com.fms.customerservice.repository.StopPointRepository;
import com.fms.module.exception.BusinessException;
import com.fms.module.exception.ErrorException;
import com.fms.module.model.MetaModel;
import com.fms.module.model.ResponseModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;
import com.fms.module.utils.ValidateUtils;

@Service
public class StopPointServiceImpl implements StopPointService {
	@Autowired
	StopPointRepository stopPointRepository;
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public StopPointServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public String create(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			StopPoint stopPoint) {
		try {
			// kiem tra khach hang co thuoc quan ly cua user dang nhap
			Boolean checkCustomer = stopPointRepository.checkCustomerBelongUser(roleId, provinceId, districtId,
					customerId, stopPoint.getCustomerId());
			if (!checkCustomer) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

			stopPoint = validateStopPoint(stopPoint);

			stopPoint.audit(Constants.LOCALE_CREATE, username);
			StopPoint point = stopPointRepository.save(stopPoint);
			if (point == null) {
				return Utils.getErrorMessage(Constants.LOCALE_CREATE);
			}
			return Utils.getSuccessMessage(Constants.LOCALE_CREATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public String update(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			StopPoint stopPoint) {
		try {
			// Kiem tra co ton tai stoppoint ko
			StopPoint item = stopPointRepository.getById(stopPoint.getStopPointId());
			if (item == null) {
				throw new BusinessException(Utils.getValidateMessage("route.stoppoint-notexist"));
			}

			// kiem tra khach hang co thuoc quan ly cua user dang nhap
			Boolean checkCustomer = stopPointRepository.checkCustomerBelongUser(roleId, provinceId, districtId,
					customerId, stopPoint.getCustomerId());
			if (!checkCustomer) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

			stopPoint = validateStopPoint(stopPoint);

			
//			Utils.mappingDTO(stopPoint, item);
			
			// update các trường cần cập nhật
			item.setCustomerId(stopPoint.getCustomerId());
			item.setName(stopPoint.getName());
			item.setStopPointTypeId(stopPoint.getStopPointTypeId());
			item.setStopTime(stopPoint.getStopTime());
			item.setRadius(stopPoint.getRadius());
			item.setNote(stopPoint.getNote());
			item.setLat(stopPoint.getLat());
			item.setLng(stopPoint.getLng());
			stopPoint.audit(Constants.LOCALE_UPDATE, username);
			StopPoint point = stopPointRepository.save(item);

			if (point == null) {
				return Utils.getErrorMessage(Constants.LOCALE_UPDATE);
			}
			return Utils.getSuccessMessage(Constants.LOCALE_UPDATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public String delete(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			Integer stopPointId) {
		try {
			// Kiem tra co ton tai stoppoint ko
			StopPoint item = stopPointRepository.getById(stopPointId);
			if (item == null) {
				throw new BusinessException(Utils.getValidateMessage("route.stoppoint-notexist"));
			}

			// kiem tra khach hang co thuoc quan ly cua user dang nhap
			Boolean checkCustomer = stopPointRepository.checkCustomerBelongUser(roleId, provinceId, districtId,
					customerId, item.getCustomerId());
			if (!checkCustomer) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

			item.audit(Constants.LOCALE_DELETE, username);
			StopPoint point = stopPointRepository.save(item);

			if (point == null) {
				return Utils.getErrorMessage(Constants.LOCALE_DELETE);
			}
			return Utils.getSuccessMessage(Constants.LOCALE_DELETE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public StopPointDetails getDetails(Integer roleId, Integer provinceId, Integer districtId, Integer customerId,
			Integer stopPointId) {
		try {
			// Kiem tra co ton tai stoppoint ko
			StopPointDetails item = stopPointRepository.getDetails(stopPointId);
			if (item == null) {
				throw new BusinessException(Utils.getValidateMessage("route.stoppoint-notexist"));
			}

			// kiem tra khach hang co thuoc quan ly cua user dang nhap
			Boolean checkCustomer = stopPointRepository.checkCustomerBelongUser(roleId, provinceId, districtId,
					customerId, item.getCustomerId());
			if (!checkCustomer) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

			return item;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public ResponseModel<List<StopPointDTO>> getAll(Integer pageNumber, Integer pageSize, Integer roleId,
			Integer provinceId, Integer districtId, Integer customerId, Integer customerIdHeader, Integer type,
			String name) {
		try {
			if (pageNumber != null && pageNumber <= 0)
				throw new BusinessException(Utils.getErrorMessageValidate(Constants.PAGE_REQUEST_PARAM, "Pattern"));
			if (pageSize != null && pageSize <= 0)
				throw new BusinessException(
						Utils.getErrorMessageValidate(Constants.PAGE_SIZE_REQUEST_PARAM, "Pattern"));

			ResponseModel<List<StopPointDTO>> response = new ResponseModel<List<StopPointDTO>>();
			String procedure = "CALL getStopPointList(%s, %s, '%s', %s, %s, %s, %s, %s, %s, @total_record)";

			String SQL = String.format(procedure, pageNumber, pageSize, name.trim(), type, provinceId, districtId,
					customerIdHeader, customerId, roleId);

//			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			/*
			 * hoadp
			 * fix sonar
			 */
			List<Map<String, Object>> rows;
			List<StopPointDTO> data = new ArrayList<>();
			try {
				rows = jdbcTemplate.queryForList(SQL);
				for (Map<String, Object> row : rows) {
					StopPointDTO item = new StopPointDTO();
					item.setStopPointId((Integer) row.get("stopPointId"));
					item.setStopPointTypeName((String) row.get("stopPointTypeName"));
					item.setName((String) row.get("name"));
					item.setManagerName((String) row.get("managerName"));
					item.setLat((BigDecimal) row.get("lat"));
					item.setLng((BigDecimal) row.get("lng"));
					item.setStopPointTypeId((Integer) row.get("stopPointTypeId"));
					item.setIcon((String) row.get("icon"));
					data.add(item);
				}
			} catch (Exception e) {
				throw new ErrorException(e);
			}

			Map<String, Object> subObject = jdbcTemplate.queryForMap("SELECT @total_record");
			Long total = null;
			int totalRecord;

			if (subObject != null) {
				total = (Long) subObject.get("@total_record");
			}

			totalRecord = (int) (total != null ? total : 0);
						
			if (pageNumber != null && pageSize != null) {
				int totalPages = (int) Math.ceil((float) totalRecord / pageSize);
				response.setMeta(new MetaModel(pageNumber, pageSize, totalPages, totalRecord));
			}

			response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
			response.setData(data);

			return response;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	/*
	 * validate kinh do, vi do trong lanh tho vietnam
	 */
	private StopPoint validateStopPoint(StopPoint stopPoint) {
		if (ValidateUtils.isNullOrEmpty(stopPoint.getName())) {
			throw new BusinessException(Utils.getValidateMessage("route.stoppointname-notblank"));
		}
		if (stopPoint.getCustomerId() == null)
			throw new BusinessException(Utils.getValidateMessage("route.customer-notselect"));

		if (stopPoint.getStopPointTypeId() == null)
			throw new BusinessException(Utils.getValidateMessage("route.stoppointtype-notselect"));

		int countCustomer = stopPointRepository.countCustomerId(stopPoint.getCustomerId());
		if (countCustomer == 0)
			throw new BusinessException(Utils.getValidateMessage("route.customer-notexist"));

		int countStopPointType = stopPointRepository.countStopPointTypeId(stopPoint.getStopPointTypeId());
		if (countStopPointType == 0)
			throw new BusinessException(Utils.getValidateMessage("route.stoppointtype-notexist"));

		if (stopPoint.getLat() < 7 || stopPoint.getLng() < 102 || stopPoint.getLat() > 24 || stopPoint.getLng() > 118) {
			throw new BusinessException(Utils.getValidateMessage("route.latlng-invalid"));
		}

		return stopPoint.trim(stopPoint);
	}
}
