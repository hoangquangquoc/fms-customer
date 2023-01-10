package com.fms.customerservice.service;

import java.awt.Point;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fms.customerservice.client.MonitoringServiceClient;
import com.fms.customerservice.controller.utils.RouteUtil;
import com.fms.customerservice.model.MatchRouteDTO;
import com.fms.customerservice.model.PointModel;
import com.fms.customerservice.model.PointRoute;
import com.fms.customerservice.model.RawJourneyDataDTO;
import com.fms.customerservice.model.Route;
import com.fms.customerservice.model.RouteDTO;
import com.fms.customerservice.model.RouteDetailsDTO;
import com.fms.customerservice.model.RouteRequest;
import com.fms.customerservice.model.RouteTransport;
import com.fms.customerservice.model.RouteVehicleWarning;
import com.fms.customerservice.model.TollgateDTO;
import com.fms.customerservice.model.TransportDTO;
import com.fms.customerservice.model.TransportRoute;
import com.fms.customerservice.model.VehicleWarningRoute;
import com.fms.customerservice.model.VehiclesAssignRoute;
import com.fms.customerservice.repository.RawJourneyDataRepository;
import com.fms.customerservice.repository.RouteDetailRepository;
import com.fms.customerservice.repository.RouteRepository;
import com.fms.customerservice.repository.RouteTransportRepository;
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
public class RouteServiceImpl implements RouteService {
	private static final String[] DIRECTION_TYPE = { "0", "1" };
	private static final String[] ROUTE_TYPE = { "1", "2" };
	private final int ROUTE_PATH = 1;
	private final int ROUTE_BOUNDARY = 2;
	private final double EARTH_RADIUS = 6378137;

	@Autowired
	RouteRepository routeRepository;

	@Autowired
	RouteTransportRepository routeTransportRepo;

	@Autowired
	RouteDetailRepository routeDetailRepository;

	@Autowired
	MonitoringServiceClient monitoringServiceClient;

	@Autowired
	RawJourneyDataRepository rourneyRepository;

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public RouteServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public String create(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			RouteRequest routeRequest) {
		try {
			// validate route
			validateRoute(routeRequest);

			// kiem tra khach hang co ton tai
			int count = routeRepository.checkCustomer(routeRequest.getCustomerId());
			if (count == 0)
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));
			// kiem tra khach hang co thuoc quan ly cua user dang nhap
			Boolean checkCustomer = routeRepository.checkCustomerBelongUser(roleId, provinceId, districtId, customerId,
					routeRequest.getCustomerId());
			if (!checkCustomer) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

			Route route = new Route(routeRequest.getName().trim(), routeRequest.getCustomerId(),
					routeRequest.getDeviation(), routeRequest.getDirection(), routeRequest.getRouteType(),
					routeRequest.getListPoints());

			route.audit(Constants.LOCALE_CREATE, username);

			Route item = routeRepository.save(route);
			if (item == null) {
				return Utils.getErrorMessage(Constants.LOCALE_CREATE);
			}

			// luu routedetail
			// saveRouteDetail(routeRequest.getListPoints(), routeRequest.getListMarkers(),
			// item.getRouteId(), username, false);
			return Utils.getSuccessMessage(Constants.LOCALE_CREATE);

		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void validateRoute(RouteRequest routeRequest) {
		if (ValidateUtils.isNullOrEmpty(routeRequest.getName())
				|| ValidateUtils.isNullOrEmpty(routeRequest.getListPoints()) || routeRequest.getDeviation() == null
				|| routeRequest.getRouteType() == null) {
			throw new BusinessException(Utils.getValidateMessage("not_empty"));
		}

		if (routeRequest.getRouteType() == ROUTE_PATH) {
			if (routeRequest.getDirection() == null) {
				throw new BusinessException(Utils.getValidateMessage("not_empty"));
			}
		}

		boolean isExist = Arrays.asList(DIRECTION_TYPE).contains(routeRequest.getDirection().toString());
		if (!isExist)
			throw new BusinessException(Utils.getValidateMessage("input_invalid"));

		isExist = Arrays.asList(ROUTE_TYPE).contains(routeRequest.getRouteType().toString());
		if (!isExist)
			throw new BusinessException(Utils.getValidateMessage("input_invalid"));

		// String[] listPointsArray = routeRequest.getListPoints().split(",");

		// lo trinh duong
//		if (routeRequest.getRouteType() == ROUTE_PATH) {
//			if (listPointsArray.length < 2) {
//				throw new BusinessException(Utils.getValidateMessage("route.invalid"));
//			}
//		}
//		// lo trinh vung bao
//		if (routeRequest.getRouteType() == ROUTE_BOUNDARY) {
//			if (listPointsArray.length < 4) {
//				throw new BusinessException(Utils.getValidateMessage("route.invalid"));
//			}
//		}
	}

//	private void saveRouteDetail(String listPoints, String listMarker, Integer routeId, String username,
//			boolean isUpdate) {
//		if (isUpdate) {
//			// xoa routedetail cu
//			routeDetailRepository.deleteRouteDetail(routeId, username);
//		}
//
//		List<RouteDetail> routeDetails = new ArrayList<>();
//		String[] data = listPoints.split(",");
//		String[] markers = null;
//
//		if (listMarker != null) {
//			markers = listMarker.split(",");
//		}
//
//		int index = 0;
//		for (int i = 0; i < data.length - 1; i = i + 2) {
//			RouteDetail item = new RouteDetail();
//			item.audit(Constants.LOCALE_CREATE, username);
//
//			if (markers != null) {
//				for (String marker : markers) {
//					if (Integer.parseInt(marker.trim()) == (i / 2)) {
//						item.setIsMarker(true);
//						break;
//					}
//				}
//			}
//
//			item.setLat(Utils.convertStringToDouble(data[i]));
//			item.setLng(Utils.convertStringToDouble(data[i + 1]));
//			item.setRouteId(routeId);
//			item.setPointOrder(index);
//			routeDetails.add(item);
//			index++;
//		}
//		routeDetailRepository.saveAll(routeDetails);
//	}

	@Override
	public String update(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			RouteRequest routeRequest) {
		try {
			// validate route
			validateRoute(routeRequest);

			// kiem tra khach hang co ton tai
			int count = routeRepository.checkCustomer(routeRequest.getCustomerId());
			if (count == 0)
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));

			// Kiem tra co ton tai lo trinh ko
			Route item = routeRepository.getById(routeRequest.getRouteId());
			if (item == null) {
				throw new BusinessException(Utils.getValidateMessage("route.not-exist"));
			}

			// kiem tra khach hang co thuoc quan ly cua user dang nhap
			Boolean checkCustomer = routeRepository.checkCustomerBelongUser(roleId, provinceId, districtId, customerId,
					routeRequest.getCustomerId());

			if (!checkCustomer) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

			Route route = new Route(routeRequest.getName().trim(), routeRequest.getCustomerId(),
					routeRequest.getDeviation(), routeRequest.getDirection(), routeRequest.getRouteType(),
					routeRequest.getListPoints());

			route.audit(Constants.LOCALE_UPDATE, username);
			Utils.mappingDTO(route, item);

			Route itemSave = routeRepository.save(item);

			if (itemSave == null) {
				return Utils.getErrorMessage(Constants.LOCALE_UPDATE);
			}

			// luu routedetail
//			saveRouteDetail(routeRequest.getListPoints(), routeRequest.getListMarkers(), item.getRouteId(), username, true);

			return Utils.getSuccessMessage(Constants.LOCALE_UPDATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public String delete(Integer roleId, Integer provinceId, Integer districtId, Integer customerId, String username,
			Integer routeId) {
		try {
			// Kiem tra co ton tai stoppoint ko
			Route route = routeRepository.getById(routeId);
			if (route == null) {
				throw new BusinessException(Utils.getValidateMessage("route.not-exist"));
			}

			// kiem tra khach hang co thuoc quan ly cua user dang nhap
			Boolean checkCustomer = routeRepository.checkCustomerBelongUser(roleId, provinceId, districtId, customerId,
					route.getCustomerId());
			if (!checkCustomer) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

			route.audit(Constants.LOCALE_DELETE, username);
			Route item = routeRepository.save(route);

			if (item == null) {
				return Utils.getErrorMessage(Constants.LOCALE_DELETE);
			}
			return Utils.getSuccessMessage(Constants.LOCALE_DELETE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public RouteDetailsDTO getDetails(Integer roleId, Integer provinceId, Integer districtId, Integer customerId,
			Integer routeId) {
		try {
			// Kiem tra co ton tai stoppoint ko
			RouteDetailsDTO item = routeRepository.getDetails(routeId);
			if (item == null) {
				throw new BusinessException(Utils.getValidateMessage("route.not-exist"));
			}

			// kiem tra khach hang co thuoc quan ly cua user dang nhap
			Boolean checkCustomer = routeRepository.checkCustomerBelongUser(roleId, provinceId, districtId, customerId,
					item.getCustomerId());
			if (!checkCustomer) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

//			List<PointDTO> routeDetail = routeDetailRepository.getListRouteDetail(routeId);
//
//			RouteDetailResponse response = new RouteDetailResponse();
//			response.setRouteId(item.getRouteId());
//			response.setName(item.getName());
//			response.setCustomerId(item.getCustomerId());
//			response.setDeviation(item.getDeviation());
//			response.setDirection(item.getDirection());
//			response.setRouteType(item.getRouteType());
//			response.setListPoints(routeDetail);

			return item;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	// lay danh sach xe cua lo trinh
	@Override
	public List<TransportDTO> getVehicleOfRoute(Integer routeId, UserHeader userHeader) {
		try {
			// kiem tra lo trinh co ton tai khong
			Route route = checkExistRoute(routeId);

			// kiem tra lo trinh co thuoc quan ly cua user dang nhap
			checkPermissionCustomer(userHeader, route.getCustomerId());

			return routeRepository.getVehiclesOfRoute(routeId);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public String assignVehicles(VehiclesAssignRoute assignVehicles, UserHeader userHeader) {
		try {
			// validate cac gia tri bat buoc
			validateInput(assignVehicles);

			// kiem tra khach hang co thuoc quan ly cua user dang nhap
			checkPermissionCustomer(userHeader, assignVehicles.getCustomerId());

			// kiem tra lo trinh co ton tai khong
			checkRouteOfCustomer(assignVehicles.getRouteId(), assignVehicles.getCustomerId());

			// validate
			for (TransportRoute item : assignVehicles.getListVehicles()) {
				// validate date
				validateDateAssignRoute(item.getFromDate(), item.getToDate());

				// kiem tra ton tai transport
				checkExistTransportWithCustomer(item.getTransportId(), assignVehicles.getCustomerId());

				// kiem tra phuong tien co thuoc quan ly user dang nhap
				checkPermissionTransport(userHeader, item.getTransportId());
			}

			// xoa cac xe cu trong bang route_transport
			routeRepository.deleteVehiclesAssigned(assignVehicles.getRouteId());

			for (TransportRoute item : assignVehicles.getListVehicles()) {
				RouteTransport routeTransport = new RouteTransport();
				Date tfromDate = Utils.stringConvertToDate(item.getFromDate(), null);
				Date ttoDate = Utils.stringConvertToDate(item.getToDate(), null);

				routeTransport.setRouteId(assignVehicles.getRouteId());
				routeTransport.setTransportId(item.getTransportId());
				routeTransport.setCreatedBy(userHeader.getUsername());
				routeTransport.setCreatedDate(LocalDateTime.now());
				routeTransport.setFromDate(tfromDate);
				routeTransport.setToDate(ttoDate);

				routeTransport.audit(Constants.LOCALE_CREATE, userHeader.getUsername());
				routeTransportRepo.save(routeTransport);
			}
			return Utils.getMessageByKey(Constants.SUCCESS_MESSAGE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void validateInput(VehiclesAssignRoute assignVehicles) {
		if (assignVehicles.getListVehicles() == null || assignVehicles.getCustomerId() == null
				|| assignVehicles.getRouteId() == null)
			throw new BusinessException(Utils.getValidateMessage("transport.input-empty"));
		for (TransportRoute item : assignVehicles.getListVehicles()) {
			if (item.getTransportId() == null) {
				throw new BusinessException(Utils.getValidateMessage("transport.input-empty"));
			}
		}
	}

	private void validateWarningVehicle(VehicleWarningRoute warningVehicles) {
		// validate require listTransportIds
		if (ValidateUtils.isNullOrEmpty(warningVehicles.getListTransportId())) {
			throw new BusinessException(Utils.getValidateMessage("transport.listofvehicle-notblank"));
		}
		// require phone or email
		if (ValidateUtils.isNullOrEmpty(warningVehicles.getPhone()) && ValidateUtils.isNullOrEmpty(warningVehicles.getEmail())) {
			throw new BusinessException(Utils.getMessageByKey("validate.phone_email"));
		}
		// validate phone
		if (!ValidateUtils.isNullOrEmpty(warningVehicles.getPhone()) && !ValidateUtils.validPhone(warningVehicles.getPhone())) {
			throw new BusinessException(Utils.getErrorMessageValidate("Phone", Constants.ERROR_FORMAT));
		}
		// validate email
		if (!ValidateUtils.isNullOrEmpty(warningVehicles.getEmail()) && !ValidateUtils.isEmailValid(warningVehicles.getEmail())) {
			throw new BusinessException(Utils.getErrorMessageValidate("Email", Constants.ERROR_FORMAT));
		}
	}

	private void checkPermissionCustomer(UserHeader user, Integer customerId) {
		Boolean permission = routeRepository.checkPermissionCustomer(user.getRoleId(), user.getProvinceId(),
				user.getDistrictId(), user.getCustomerId(), customerId);
		if (!permission)
			throw new BusinessException(Utils.getValidateMessage("user.customer-author"));
	}

	private void checkRouteOfCustomer(Integer routeId, Integer customerId) {
		int count = routeRepository.checkExistByCustomerId(routeId, customerId);
		if (count == 0)
			throw new BusinessException(Utils.getValidateMessage("route.not-exist-2"));
	}

	private void checkExistTransportWithCustomer(Integer transportId, Integer customerId) {
		int count = routeRepository.checkTransport(transportId, customerId);
		if (count == 0)
			throw new BusinessException(Utils.getValidateMessage("user.transport-notexist-2"));
	}

	private void checkExistTransport(Integer transportId) {
		int count = routeRepository.checkExistTransport(transportId);
		if (count == 0)
			throw new BusinessException(Utils.getValidateMessage("user.transport-notexist"));
	}

	private void checkTransportInRoute(Integer transportId, Integer routeId) {
		int count = routeRepository.checkTransportInRoute(transportId, routeId);
		if (count == 0)
			throw new BusinessException(Utils.getValidateMessage("user.transport-route"));
	}

	private Route checkExistRoute(Integer routeId) {
		Route route = routeRepository.getById(routeId);
		if (route == null)
			throw new BusinessException(Utils.getValidateMessage("route.not-exist"));

		return route;
	}

	private void checkPermissionTransport(UserHeader user, Integer transportId) {
		Boolean permission = routeRepository.checkPermissionTransport(user.getRoleId(), user.getProvinceId(),
				user.getDistrictId(), user.getCustomerId(), user.getUserId(), transportId);

		if (!permission)
			throw new BusinessException(Utils.getValidateMessage("user.transport-author"));
	}

	@Override
	public ResponseModel<List<RouteDTO>> getAll(SearchModel searchModel, Integer roleId, Integer provinceId,
			Integer districtId, Integer customerId, Integer customerIdHeader, String name, Integer totalType,
			Integer routeType) {
		try {
			if (searchModel.getPageNumber() <= 0)
				throw new BusinessException(Utils.getErrorMessageValidate(Constants.PAGE_REQUEST_PARAM, "Pattern"));
			if (searchModel.getPageSize() <= 0)
				throw new BusinessException(
						Utils.getErrorMessageValidate(Constants.PAGE_SIZE_REQUEST_PARAM, "Pattern"));

			ResponseModel<List<RouteDTO>> response = new ResponseModel<List<RouteDTO>>();
			String procedure = "CALL getRouteList(%s, %s, '%s', %s, %s, %s, %s, %s, %s, %s, @total_record)";

			String SQL = String.format(procedure, searchModel.getPageNumber(), searchModel.getPageSize(), name.trim(),
					provinceId, districtId, customerIdHeader, customerId, roleId, totalType, routeType);

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			/*
			 * hoadp
			 * fix sonar
			 */
//			List<Map<String, Object>> rows;
			List<RouteDTO> data = new ArrayList<>();
			try {
				rows = jdbcTemplate.queryForList(SQL);
				for (Map<String, Object> row : rows) {
					RouteDTO item = new RouteDTO();
					item.setName((String) row.get("name"));
					item.setManagerName((String) row.get("managerName"));
					item.setTotal((Long) row.get("total"));
					item.setRouteId((Integer) row.get("routeId"));
					item.setCustomerId((Integer) row.get("customerId"));
					item.setRouteType((Integer) row.get("routeType"));
					item.setDirection((Integer) row.get("direction"));
					item.setDeviation((Integer) row.get("deviation"));
					item.setPhone((String) row.get("phone"));
					item.setEmail((String) row.get("email"));
					
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
			int totalPages = (int) Math.ceil((float) totalRecord / searchModel.getPageSize());

			response.setMessage(Utils.getMessageByKey(Constants.SUCCESS_MESSAGE));
			response.setMeta(
					new MetaModel(searchModel.getPageNumber(), searchModel.getPageSize(), totalPages, totalRecord));
			response.setData(data);

			return response;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
	private List<PointModel> convertRouteDetailStrToList(Integer routeType, String routeDetailStr, Double deviation){
		List<PointModel> result = new ArrayList<PointModel>();
		if(routeType == ROUTE_PATH) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				List<PointRoute> lstPoint = mapper.readValue(routeDetailStr, new TypeReference<List<PointRoute>>(){});
				for(PointRoute point : lstPoint) {
					result.add(new PointModel(point.getLatitude(),point.getLongitude(), deviation));
				}
			} catch (Exception e) {
				throw new ErrorException(e);
			}
		}
		else if(routeType == ROUTE_BOUNDARY) {
			try {
				String lst[] = routeDetailStr.split(",");
				for(int i = 0;i< lst.length;i+=2) {
					result.add(new PointModel(Double.parseDouble(lst[i]), Double.parseDouble(lst[i+1]), deviation));
				}
			} catch (Exception e) {
				throw new ErrorException(e);
			}
		}
		return result;
	}

	@Override
	public List<MatchRouteDTO> matchRoute(SearchModel search, UserHeader userHeader, Integer customerIdParam,
			String fromDate, String toDate, Integer routeId, Integer transportId) {
		try {
			// validate input
			validateMatchRoute(customerIdParam, fromDate, toDate, routeId, transportId);
			// validate fromDate, toDate
			
			/*
			 * check thời gian phương tiện gán vào lộ trình
			 */
			RouteTransport routeTransport = routeTransportRepo.findByTransportIdAndRouteId(routeId, transportId);
			
			validateDate(fromDate, toDate,routeTransport.getFromDate(), routeTransport.getToDate());
			// lay chi tiet lo trinh
			Route route = routeRepository.getById(routeId);
			if (route == null)
				throw new BusinessException(Utils.getValidateMessage("route.not-exist"));

			// kiem tra ton tai transport
			checkExistTransport(transportId);
			// kiem tra phuong tien co thuoc quan ly user dang nhap
			checkPermissionTransport(userHeader, transportId);
			// kiem tra khach hang co thuoc quan ly cua user dang nhap
			checkPermissionCustomer(userHeader, route.getCustomerId());
			// kiem tra phuong tien da gan vao lo trinh chua
			checkTransportInRoute(transportId, routeId);

			List<MatchRouteDTO> data = new ArrayList<>();
			// do lech
			double deviation = route.getDeviation();
			// loai lo trinh
			Integer routeType = route.getRouteType();
			

			// lo trinh xe chay thuc te
			LocalDateTime fDate = changeISODate(fromDate + ":00", Constants.DATE_TIME_DEFAULT_FORMAT);
			LocalDateTime tDate = changeISODate(toDate + ":00", Constants.DATE_TIME_DEFAULT_FORMAT);
			List<RawJourneyDataDTO> rawData = rourneyRepository.findRawJourneyData(transportId, fDate, tDate);
			/*
			 * hoadp
			 * Lấy thông tin chi tiết lộ trình đã tạo dạng string lưu trong bảng route
			 * Sau đó convert từ string JSON sang List <PointDTO>
			 */
			String  routeDetailStr = routeDetailRepository.getStringListRouteDetail(routeId);

			List<PointModel> sampleLatLng = convertRouteDetailStrToList(routeType, routeDetailStr, deviation);

//			for (PointDTO point : listRouteCreate) {
//				PointModel p = new PointModel(point.getLat(), point.getLng(), deviation);
//				sampleLatLng.add(p);
//			}

			if (routeType == ROUTE_PATH) {
				for (RawJourneyDataDTO journey : rawData) {
					double distance = calDistance(journey.getLat(), journey.getLng(), sampleLatLng, deviation);
					if (distance > 0) {
						MatchRouteDTO item = new MatchRouteDTO();
						item.setLat(journey.getLat());
						item.setLng(journey.getLng());
						item.setDeviation(distance);
						item.setTime(journey.getCurrentTime());
//						item.setPosition(journey.getAddress());
						item.setPosition(Utils.getAddressByLatLng(journey.getLat(),journey.getLng()));
						data.add(item);
					}
				}
			} else if (routeType == ROUTE_BOUNDARY) {
				for (RawJourneyDataDTO journey : rawData) {
					boolean isInside = coordinateInRegion(sampleLatLng, journey);
					if (!isInside) {
						double distance = distancePointToPolygon(sampleLatLng, journey);
						if (distance > deviation) {
							MatchRouteDTO item = new MatchRouteDTO();
							item.setLat(journey.getLat());
							item.setLng(journey.getLng());
							item.setDeviation(distance);
							item.setTime(journey.getCurrentTime());
//							item.setPosition(journey.getAddress());
							item.setPosition(Utils.getAddressByLatLng(journey.getLat(),journey.getLng()));
							data.add(item);
						}
					}
				}
			}

			return data;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void validateMatchRoute(Integer customerIdParam, String fromDate, String toDate, Integer routeId,
			Integer transportId) {
		if (customerIdParam == null || fromDate == null || toDate == null || routeId == null || transportId == null)
			throw new BusinessException(Utils.getValidateMessage("transport.input-empty"));
		
	}

	private void validateDate(String fromDate, String toDate, Date fromDateRoute, Date toDateRoute) throws ParseException {
		LocalDateTime tfromDate = changeISODate(fromDate, Constants.DATE_TIME_WITHOUT_SECOND_FORMAT);
		LocalDateTime ttoDate = changeISODate(toDate, Constants.DATE_TIME_WITHOUT_SECOND_FORMAT);

		if (tfromDate == null || ttoDate == null)
			throw new BusinessException(Utils.getMessageByKey("validate.datetime.invalid"));

		// check thoi gian nhap vao
		LocalDateTime now = LocalDateTime.now();
		if (tfromDate.isAfter(ttoDate))
			throw new BusinessException(Utils.getMessageByKey("validate.datetime.start_time_earlier_than_end_time"));

		if (tfromDate.isAfter(now) || ttoDate.isAfter(LocalDate.now().atTime(23, 59, 59)))
			throw new BusinessException(
					Utils.getMessageByKey("validate.datetime.start_time_earlier_than_current_time"));

		Duration duration = Duration.between(tfromDate, ttoDate);
		if (duration.getSeconds() > 24 * 3600 * Constants.REVIEW_JOURNEY_DAY_PACKAGE_1)
			throw new BusinessException(Utils.getValidateMessage("route.exceed"));
		/*
		 * Kiểm tra thời gian có nằm trong thời gian bắt đầu và kết thúc khi gán phương tiện vào lộ trình
		 */
		Date fDate = Date.from(tfromDate.atZone(ZoneId.systemDefault()).toInstant());
		Date tDate = Date.from(tfromDate.atZone(ZoneId.systemDefault()).toInstant());
		if(!((fDate.after(fromDateRoute) || fDate.equals(fromDateRoute)) && (tDate.before(toDateRoute) || tDate.equals(toDateRoute)))) {
			throw new BusinessException(Utils.getMessageByKey("validate.datetime.not_belong_to_period_time"));
		}
	}

	private void validateDateAssignRoute(String fromDate, String toDate) throws ParseException {
		Date fDate = null;
		Date tDate = null;

		if (!ValidateUtils.isNullOrEmpty(fromDate)) {
			fDate = Utils.stringConvertToDate(fromDate, null);
			if (fDate == null)
				throw new BusinessException(Utils.getMessageByKey("validate.date.invalid"));

		}
		if (!ValidateUtils.isNullOrEmpty(toDate)) {
			tDate = Utils.stringConvertToDate(toDate, null);
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

	private double calDistance(Double latSrc, Double lngSrc, List<PointModel> sampleLatLng, double deviation) {
		List<Double> distances = new ArrayList<>();
		double distance = 0;
		for (PointModel samplePoint : sampleLatLng) {
			double d = GetDistance(latSrc, lngSrc, samplePoint.getLat(), samplePoint.getLng());
			distances.add(d);
		}
		int minIndex = distances.indexOf(Collections.min(distances));
		distance = distances.get(minIndex);
		if (distance <= deviation) {
			return 0;
		}
		return distance;
	}

	private double GetDistance(Double latSrc, Double lngSrc, Double latDes, Double lngDes) {
		double R = EARTH_RADIUS; // Radius of the earth in km

		double dLat = (latDes - latSrc) * Math.PI / 180;
		double dLon = (lngDes - lngSrc) * Math.PI / 180;

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(latSrc * Math.PI / 180)
				* Math.cos(latDes * Math.PI / 180) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double distance = (R * c); // Distance in m

		return distance;
	}

	// lay danh sach tram thu phi
	@Override
	public List<TollgateDTO> getAllTollgate() {
		try {
			return routeRepository.getAllTollgate();
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private LocalDateTime changeISODate(String dateTime, String format) throws ParseException {
		LocalDateTime res = Utils.stringConvertToDateTime(dateTime, format);
		return res;
	}

	private boolean coordinateInRegion(List<PointModel> region, RawJourneyDataDTO coord) {
//		int i, j;
		boolean isInside = false;
		// create an array of coordinates from the region boundary list
		int sides = region.size();
		for (int i = 0, j = sides - 1; i < sides; j = i++) {
			// verifying if your coordinate is inside your region
			if ((((region.get(i).getLng() <= coord.getLng()) && (coord.getLng() < region.get(j).getLng()))
					|| ((region.get(j).getLng() <= coord.getLng()) && (coord.getLng() < region.get(i).getLng())))
					&& (coord.getLat() < (region.get(j).getLat() - region.get(i).getLat())
							* (coord.getLng() - region.get(i).getLng())
							/ (region.get(j).getLng() - region.get(i).getLng()) + region.get(i).getLat())) {
				isInside = !isInside;
			}
		}
		return isInside;
	}

	private double distancePointToPolygon(List<PointModel> region, RawJourneyDataDTO coord) {
		List<Double> distances = new ArrayList<>();
		ArrayList<Point> listPoint = new ArrayList<>();
		double distance = 0;

//		LatLng pLatLng = new LatLng(coord.getLat(), coord.getLng());
//		Point pCheck = new Point((int) Math.round(pLatLng.toUTMRef().getNorthing()),
//				(int) Math.round(pLatLng.toUTMRef().getEasting()));
//
//		for (PointModel point : region) {
//			LatLng latlon = new LatLng(point.getLat(), point.getLng());
//			Point point2D = new Point((int) Math.round(latlon.toUTMRef().getNorthing()),
//					(int) Math.round(latlon.toUTMRef().getEasting()));
//			listPoint.add(point2D);
//		}
//
//		for (int i = 0; i < listPoint.size() - 1; i++) {
//			Point ss = listPoint.get(i);
//			Point se = listPoint.get(i + 1);
//			double d = RouteUtil.getDistanceToSegment(ss, se, pCheck);
//			distances.add(d);
//		}
//		int minIndex = distances.indexOf(Collections.min(distances));
//		distance = distances.get(minIndex);

		return distance;
	}

	@Override
	public String warningVehicles(VehicleWarningRoute warningVehicles, UserHeader userHeader) {
		try {
			// validate cac gia tri bat buoc
			validateWarningVehicle(warningVehicles);

			// kiem tra lo trinh co ton tai khong
			checkRouteOfCustomer(warningVehicles.getRouteId(), warningVehicles.getCustomerId());

			// kiem tra khach hang co thuoc quan ly cua user dang nhap
			checkPermissionCustomer(userHeader, warningVehicles.getCustomerId());

			String[] ids = warningVehicles.getListTransportId().split(",");
			Integer[] transportIds = new Integer[ids.length];

			for (int i = 0; i < transportIds.length; i++) {
				transportIds[i] = ValidateUtils.parseIntFromStr(ids[i]);
			}
			
			// validate
			for (Integer transportId : transportIds) {
				// kiem tra ton tai transport
				checkExistTransportWithCustomer(transportId, warningVehicles.getCustomerId());

				// kiem tra phuong tien co thuoc quan ly user dang nhap
				checkPermissionTransport(userHeader, transportId);
			}

			// update route with email, phone for receiving warning
			String procedure = "CALL warningVehicleRoute('%s', '%s', '%s', %s)";
			
			String SQL = String.format(procedure, warningVehicles.getListTransportId(), warningVehicles.getEmail(),
					warningVehicles.getPhone(), warningVehicles.getRouteId());
			
			jdbcTemplate.execute(SQL);
			
			return Utils.getMessageByKey(Constants.SUCCESS_MESSAGE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public RouteVehicleWarning getVehicleWarningRoute(Integer routeId, UserHeader userHeader) {
		try {
			// kiem tra lo trinh co ton tai khong
			RouteDetailsDTO item = routeRepository.getDetails(routeId);
			if (item == null) {
				throw new BusinessException(Utils.getValidateMessage("route.not-exist"));
			}

			// kiem tra lo trinh co thuoc quan ly cua user dang nhap
			checkPermissionCustomer(userHeader, item.getCustomerId());
			
			RouteVehicleWarning data = new RouteVehicleWarning();
			
			data.setListVehicles(routeRepository.getVehiclesOfRoute(routeId));
			data.setEmailWarning(item.getEmailWarning());
			data.setPhoneWarning(item.getPhoneWarning());
			data.setRouteId(routeId);
			data.setCustomerId(item.getCustomerId());
			return data;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
}
