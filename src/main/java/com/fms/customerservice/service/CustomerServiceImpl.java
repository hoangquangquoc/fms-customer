package com.fms.customerservice.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.fms.customerservice.client.LogServiceClient;
import com.fms.customerservice.controller.ExportController;
import com.fms.customerservice.controller.utils.AppUtils;
import com.fms.customerservice.model.AccountForm;
import com.fms.customerservice.model.BccsServiceDTO;
import com.fms.customerservice.model.Customer;
import com.fms.customerservice.model.CustomerAccount;
import com.fms.customerservice.model.CustomerAccountDTO;
import com.fms.customerservice.model.CustomerConfig;
import com.fms.customerservice.model.CustomerContract;
import com.fms.customerservice.model.CustomerDTO;
import com.fms.customerservice.model.CustomerDetailDTO;
import com.fms.customerservice.model.CustomerGroupDTO;
import com.fms.customerservice.model.CustomerModel;
import com.fms.customerservice.model.DepartmentDTO;
import com.fms.customerservice.model.ExportModel;
import com.fms.customerservice.model.OrderPackageDto;
import com.fms.customerservice.model.OrderPackageRequest;
import com.fms.customerservice.model.PackageFullDTO;
import com.fms.customerservice.model.Route;
import com.fms.customerservice.model.VehiclesAssignDTO;
import com.fms.customerservice.repository.ConfigRepository;
import com.fms.customerservice.repository.CustomerAccountRepository;
import com.fms.customerservice.repository.CustomerContractRepository;
import com.fms.customerservice.repository.CustomerRepository;
import com.fms.customerservice.repository.RouteRepository;
import com.fms.customerservice.repository.UserRepository;
import com.fms.module.exception.BusinessException;
import com.fms.module.exception.ErrorException;
import com.fms.module.model.CheckPermissionCustomerForm;
import com.fms.module.model.LogAction;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
import com.fms.module.utils.LogUtils;
import com.fms.module.utils.ReportHeaderConstants;
import com.fms.module.utils.Utils;
import com.fms.module.utils.ValidateUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class CustomerServiceImpl implements CustomerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	CustomerAccountRepository customerAccountRepository;
	@Autowired
	CustomerContractRepository customerContractRepository;
	
	@Autowired
	ConfigRepository configRepository;

	@Autowired
	RouteRepository routeRepository;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private ServletContext servletContext;

	private final JdbcTemplate jdbcTemplate;
	private final int DEFAULT_DAY = 10;

	@Autowired
	LogServiceClient logServiceClient;

	@Autowired
	public CustomerServiceImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<CustomerGroupDTO> getCutomerGroup(Integer roleId, String type, Integer provinceId, Integer districtId,
			Integer customerId) {
		try {
			if ("modify".equals(type)) {
				if (roleId == Constants.SYS_ADMIN_ROLE) {
					return customerRepository.findAllCustomer();
				} else if (roleId == Constants.PROVINCE_ADMIN_ROLE) {
					return customerRepository.findAllCustomerOfProvince(provinceId);
				} else if (roleId == Constants.DISTRICT_ADMIN_ROLE) {
					return customerRepository.findAllCustomerOfDistrict(districtId);
				} else if (roleId == Constants.CUSTOMER_ACCOUNT_ROLE) {
					return customerRepository.getCustomerInfo(customerId);
				}
			} else if ("view".equals(type)) {
				if (roleId == Constants.SYS_ADMIN_ROLE) {
					return customerRepository.getAllProvince();
				} else if (roleId == Constants.PROVINCE_ADMIN_ROLE) {
					return customerRepository.findAllCustomerOfProvince(provinceId);
				} else if (roleId == Constants.DISTRICT_ADMIN_ROLE) {
					return customerRepository.findAllCustomerOfDistrict(districtId);
				} else if (roleId == Constants.CUSTOMER_ACCOUNT_ROLE) {
					return customerRepository.getDepartmentOfCustomer(customerId);
				}
			} else {
				throw new BusinessException(Utils.getValidateMessage("error_input"));
			}
		} catch (Exception e) {
			throw new ErrorException(e);
		}

		return null;
	}

	@Override
	public Boolean checkPermissionCustomer(CheckPermissionCustomerForm checkPermissionCustomerForm) {
		try {
			return customerRepository.checkPermissionCustomer(checkPermissionCustomerForm.getRoleId(),
					checkPermissionCustomerForm.getProvinceId(), checkPermissionCustomerForm.getDistrictId(),
					checkPermissionCustomerForm.getCustomerIdHeader(), checkPermissionCustomerForm.getCustomerId());
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	/*
	 * lay danh sach phong ban theo khach hang
	 */
	@Override
	public List<DepartmentDTO> getDepartmentOfCustomer(Integer customerId) {
		return customerRepository.getDepartments(customerId);
	}

	/*
	 * lay danh sach khach hang
	 */
	@Override
	public Page<CustomerDTO> findAllCustomers(SearchModel searchModel, Integer roleId, Integer provinceId,
			Integer districtId, boolean export) {
		Page<CustomerDTO> data = null;
		try {
			String name = ValidateUtils.isNullOrEmpty(searchModel.getName()) ? null
					: Utils.toLikeString(searchModel.getName());
			String phone = ValidateUtils.isNullOrEmpty(searchModel.getPhone()) ? null
					: Utils.toLikeString(searchModel.getPhone());

			Date fromDate = null;
			Date toDate = null;

			if (!AppUtils.isNullOrEmpty(searchModel.getFromDate())) {
				fromDate = AppUtils.convertToDate(searchModel.getFromDate());
				if (fromDate == null) {
					throw new BusinessException(Utils.getValidateMessage("user.date-error"));
				}
			}
			if (!AppUtils.isNullOrEmpty(searchModel.getToDate())) {
				toDate = AppUtils.convertToDate(searchModel.getToDate());
				if (toDate == null) {
					throw new BusinessException(Utils.getValidateMessage("user.date-error"));
				}
				toDate = DateUtils.addDays(toDate, 1);
			}
			if (fromDate != null && toDate != null) {
				if (fromDate.after(toDate)) {
					throw new BusinessException(Utils.getValidateMessage("user.fromdate-error"));
				}
			}

			Pageable pageable = PageRequest.of(searchModel.getPageNumber() - 1, searchModel.getPageSize(),
					Sort.Direction.ASC, searchModel.getOrderBy());

			if (export) {
				pageable = Pageable.unpaged();
			}

			if (roleId == Constants.SYS_ADMIN_ROLE || roleId == Constants.PROVINCE_ADMIN_ROLE) {
				provinceId = roleId == Constants.SYS_ADMIN_ROLE ? -1 : provinceId;
				data = customerRepository.getCustomersForAdmin(name, phone, fromDate, toDate, provinceId, pageable);
			} else if (roleId == Constants.DISTRICT_ADMIN_ROLE) {
				districtId = districtId != null ? districtId : -1;
				data = customerRepository.getCustomersForDistrict(name, phone, fromDate, toDate, districtId, pageable);
			} else {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

		} catch (Exception e) {
			throw new ErrorException(e);
		}

		return data;
	}

	/*
	 * dieu chuyen khach hang
	 */
	@Override
	public String tranferCustomer(Integer customerId, Integer roleId, Integer id, String username,
			HttpServletRequest request) {
		try {
			validateTransfer(customerId, id);
			Customer customer = customerRepository.retrieveCustomer(customerId);

			if (roleId == Constants.SYS_ADMIN_ROLE) {
				customerRepository.tranferProvinceCustomer(id, username, customerId);
			} else if (roleId == Constants.PROVINCE_ADMIN_ROLE) {
				customerRepository.tranferDistrictCustomer(id, customerId);
			} else {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}
			saveLogTransfer(customer, customerId, roleId, id, username, request);
			return Utils.getMessageByKey(Constants.SUCCESS_MESSAGE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void saveLogTransfer(Customer customer, Integer customerId, Integer roleId, Integer id, String username,
			HttpServletRequest request) {
		try {
			if (Utils.getIntValue(customer.getProvinceId()) == id
					|| Utils.getIntValue(customer.getDistrictId()) == id) {
				return;
			}
			StringBuilder builder = new StringBuilder();
			builder.append(Utils.getMessageByKey("customer.transfer"));

			if (roleId == Constants.SYS_ADMIN_ROLE) {
				String provinceCurrent = userRepository.getProvinceName(customer.getProvinceId());
				String provinceNew = userRepository.getProvinceName(id);
				String content = String.format(" %s : [%s] -> [%s]", Utils.getMessageByKey("user.provinceId"),
						provinceCurrent, provinceNew);
				builder.append(content);
			} else if (roleId == Constants.PROVINCE_ADMIN_ROLE) {
				String districtCurrent = userRepository.getProvinceName(customer.getDistrictId());
				String districtNew = userRepository.getProvinceName(id);
				String content = String.format("%s : [%s] -> [%s]", Utils.getMessageByKey("user.districtId"),
						districtCurrent, districtNew);
				builder.append(content);
			}
			LogAction logAction = LogUtils.getActionLog(request, username, Constants.ActionType.UPDATE.getValue(),
					Constants.FunctionType.CHUCNANG_KHAC.getValue(), builder.toString(), customer.getName(), customerId,
					0, customerId, customer.getProvinceId(), customer.getDistrictId());
			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

	private void saveLogCreate(Customer customer, String userName, HttpServletRequest request) {
		try {
			 String content = Utils.getMessageByKey("function.insert-success");
//			String content = "Thêm mới thành công;";
			LogAction logAction = LogUtils.getActionLog(request, userName, Constants.ActionType.CREATE.getValue(),
					Constants.FunctionType.CHUCNANG_KHAC.getValue(), content, customer.getName(), null, 0,
					customer.getCustomerId(), customer.getProvinceId(), customer.getDistrictId());

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

	private void validateTransfer(Integer customerId, Integer id) {
		if (ValidateUtils.isNullOrEmpty(customerId)) {
			throw new BusinessException(Utils.getValidateMessage("user.customerId-mustfill"));
		}
		if (ValidateUtils.isNullOrEmpty(id)) {
			throw new BusinessException(Utils.getValidateMessage("user.province-must-select"));
		}

		int countCus = customerRepository.countCustomerId(customerId);
		if (countCus == 0)
			throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));

		int countPro = customerRepository.countProvinceById(id);
		if (countPro == 0)
			throw new BusinessException(Utils.getValidateMessage("user.province-not-exist"));
	}

	@Override
	public List<CustomerGroupDTO> getCutomerOfProvince(Integer provinceId) {
		try {
			// kiem tra provinceId co ton tai ko
			int count = customerRepository.countProvinceById(provinceId);
			if (count == 0)
				throw new BusinessException(Utils.getValidateMessage("user.province-not-exist"));
			return customerRepository.findAllCustomerOfProvince(provinceId);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public List<CustomerGroupDTO> getCutomerByProvinceId(Integer provinceId) {
		try {
			// kiem tra provinceId co ton tai ko
			int count = customerRepository.countProvinceById(provinceId);
			if (count == 0)
				throw new BusinessException(Utils.getValidateMessage("user.province-not-exist"));
			return customerRepository.getCustomerByProvinceId(provinceId);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
	private String getCustomerType(Integer isCooperative) {
		if(isCooperative == null || isCooperative == 0)return "Khách hàng";
		else return "Hợp tác xã";
	}

	@Override
	public ResponseEntity<InputStreamResource> export(SearchModel searchModel, UserHeader userHeader, String type) {
		try {
			UtilsReportService utilsReportService = new UtilsReportService();
			Page<CustomerDTO> page = findAllCustomers(searchModel, userHeader.getRoleId(), userHeader.getProvinceId(),
					userHeader.getDistrictId(), true);
			List<CustomerDTO> data = null;

			int stt = 1;
			ByteArrayInputStream fileExport = null;
			ExportController exportController = new ExportController();

			if (page != null) {
				data = page.getContent();
				if ("excel".equals(type)) {
					XSSFWorkbook workbook = utilsReportService.getWorkbookByTemplate(Constants.BC_KHACH_HANG_FILE_NAME);
					if ("en".equals(userHeader.getAcceptLang())) {
						workbook = utilsReportService.getWorkbookByTemplate(Constants.BC_KHACH_HANG_FILE_NAME_EN);
					}

					XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
					int startRowNo = 2;
					int cellNo = 0;
					CellStyle centeredStyle = utilsReportService.createStyleAndAlignmentForCell(workbook, 0);
					CellStyle leftStyle = utilsReportService.createStyleAndAlignmentForCell(workbook, 1);
					for (CustomerDTO item : data) {
						/*Row row = sheet.createRow(startRowNo);
						row.createCell(cellNo++).setCellValue(stt++);
						row.createCell(cellNo++).setCellValue(item.getName());
						row.createCell(cellNo++).setCellValue(item.getProvinceName());
						row.createCell(cellNo++).setCellValue(item.getAddress());
						row.createCell(cellNo++).setCellValue(item.getEmail());
						row.createCell(cellNo++).setCellValue(item.getPhone());

						cellNo = 0;
						startRowNo++;
						*/
						cellNo = 0;
						Row row = sheet.createRow(startRowNo);
						utilsReportService.setValueForCell(row.createCell(cellNo++),String.valueOf(stt),centeredStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getName(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getProvinceName(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),getCustomerType(item.getIsCooperative()),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getAddress(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getEmail(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo),item.getPhone(),leftStyle);
						
						startRowNo++;
						stt++;
					}

					String fileName = Utils.getFileNameExport(Constants.BC_KHACH_HANG_FILE_NAME, "xlsx");
					if ("en".equals(userHeader.getAcceptLang())) {
						fileName = Utils.getFileNameExport(Constants.BC_KHACH_HANG_FILE_NAME_EN, "xlsx");
					}
					fileExport = utilsReportService.getExportFile(workbook);

					MediaType mediaType = exportController.getMediaTypeForFileName(this.servletContext, fileName);
					InputStreamResource resource = new InputStreamResource(fileExport);

					return ResponseEntity.ok()
							.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
							.contentType(mediaType).body(resource);
				} else {
					// pdf
					Document document = new Document();
					document.setPageSize(PageSize.A4.rotate());
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					BaseFont baseFont = utilsReportService.getBaseFontReport();

					PdfPTable table = utilsReportService.getPdfTemplate(
							ReportHeaderConstants.DANH_SACH_KHACH_HANG_TITLE, null, null,
							ReportHeaderConstants.BC_KHACH_HANG_COLUMN_HEADERS_NAMES,
							ReportHeaderConstants.BC_KHACH_HANG_COLUMN_HEADERS_WIDTHS, 100f, baseFont);

					if ("en".equals(userHeader.getAcceptLang())) {
						table = utilsReportService.getPdfTemplate(ReportHeaderConstants.DANH_SACH_KHACH_HANG_TITLE_EN,
								null, null, ReportHeaderConstants.BC_KHACH_HANG_COLUMN_HEADERS_NAMES_EN,
								ReportHeaderConstants.BC_KHACH_HANG_COLUMN_HEADERS_WIDTHS, 100f, baseFont);
					}

					// Body data
					Font bodyFont = new Font(baseFont, 12, Font.NORMAL);
					for (CustomerDTO item : data) {
						utilsReportService.setPdfCellValue(stt++, bodyFont, Element.ALIGN_CENTER, table);
						utilsReportService.setPdfCellValue(item.getName(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getProvinceName(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(getCustomerType(item.getIsCooperative()), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getAddress(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getPhone(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getEmail(), bodyFont, Element.ALIGN_LEFT, table);
					}
					PdfWriter.getInstance(document, out);
					document.open();
					document.add(table);
					document.close();
					fileExport = new ByteArrayInputStream(out.toByteArray());

					String fileName = Constants.BC_KHACH_HANG_FILE_NAME;
					if ("en".equals(userHeader.getAcceptLang())) {
						fileName = Constants.BC_KHACH_HANG_FILE_NAME_EN;
					}

					ExportModel exportModel = new ExportModel(fileExport, fileName, "pdf");

					return exportController.responseExportFile(exportModel, this.servletContext);
				}
			}
		} catch (Exception e) {
			LOGGER.error("error: ",e);
			throw new ErrorException(new BusinessException(Utils.getErrorMessage(Constants.LOCALE_PROCESS)));
		}
		return null;
	}

	@Override
	public List<VehiclesAssignDTO> getVehicles(Integer customerId, Integer routeId, UserHeader userHeader) {
		try {
			if (customerId == -1 || routeId == -1) {
				throw new BusinessException(Utils.getValidateMessage("transport.input-empty"));
			}

			int countCus = customerRepository.countCustomerId(customerId);
			if (countCus == 0)
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));

			// Kiem tra co ton tai lo trinh ko
			Route route = routeRepository.getById(routeId);
			if (route == null) {
				throw new BusinessException(Utils.getValidateMessage("route.not-exist"));
			} else {
				if (Utils.getIntValue(route.getCustomerId()) != customerId) {
					throw new BusinessException(Utils.getValidateMessage("route.not_permission"));
				}
			}

			if (!customerRepository.checkPermissionCustomer(userHeader.getRoleId(), userHeader.getProvinceId(),
					userHeader.getDistrictId(), userHeader.getCustomerId(), customerId)) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}

			String procedure = "CALL getVehiclesOfCustomer(%s, %s)";
			String SQL = String.format(procedure, customerId, routeId);
//			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			/*
			 * hoadp
			 * fix sonar
			 */
			List<Map<String, Object>> rows;
			List<VehiclesAssignDTO> listData = new ArrayList<VehiclesAssignDTO>();
			try {
				rows = jdbcTemplate.queryForList(SQL);
				for (Map<String, Object> row : rows) {
					VehiclesAssignDTO item = new VehiclesAssignDTO();
					item.setRegisterNo((String) row.get("registerNo"));
					item.setTransportId((Integer) row.get("transportId"));
					item.setStatus(Long.parseLong(row.get("status").toString()));
					item.setFromDate((String) row.get("fromDate"));
					item.setToDate((String) row.get("toDate"));

					listData.add(item);
				}
			} catch (Exception e) {
				throw new ErrorException(e);
			}
			return listData;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public String updateConfig(CustomerConfig config, UserHeader userHeader) {
		try {
			if (config.getConfigId() != 0) {
				int count = configRepository.countConfig(config.getConfigId());
				if (count == 0) {
					throw new BusinessException(Utils.getValidateMessage("config.not-exist"));
				}
			}

			int countCus = customerRepository.countCustomerId(config.getCustomerId());
			if (countCus == 0)
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));

			if (config.getCustomerId() != null) {
				if (!customerRepository.checkPermissionCustomer(userHeader.getRoleId(), userHeader.getProvinceId(),
						userHeader.getDistrictId(), userHeader.getCustomerId(), config.getCustomerId())) {
					throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
				}
			}
			if (config.getSendSms() != null && config.getSendSms()) {
				if (ValidateUtils.isNullOrEmpty(config.getPhone())) {
					throw new BusinessException(Utils.getValidateMessage("user.phone-empty"));
				} else {
					if (!ValidateUtils.validPhone(config.getPhone().trim())) {
						throw new BusinessException(Utils.getValidateMessage("user.phone-error-format"));
					} else {
						config.setPhone(config.getPhone().trim());
					}
				}
			}

			if (config.getSendEmail() != null && config.getSendEmail()) {
				if (ValidateUtils.isNullOrEmpty(config.getEmail())) {
					throw new BusinessException(Utils.getValidateMessage("user.email-empty"));
				} else {
					if (!ValidateUtils.isEmailValid(config.getEmail().trim())) {
						throw new BusinessException(Utils.getValidateMessage("user.email-error-format"));
					} else {
						config.setEmail(config.getEmail().trim());
					}
				}
			}
			if (config.getConfigId() != 0) {
				CustomerConfig cofigCurrent = configRepository.findById(config.getConfigId()).get();
				config.setCreatedBy(cofigCurrent.getCreatedBy());
				config.setCreatedDate(cofigCurrent.getCreatedDate());
				config.setModifiedBy(userHeader.getUsername());
				config.setModifiedDate(LocalDateTime.now());
			} else {
				int count = configRepository.countConfigByCustomer(config.getCustomerId());
				if (count > 0) {
					throw new BusinessException(Utils.getValidateMessage("config.exist"));
				}
				config.setCreatedBy(userHeader.getUsername());
				config.setCreatedDate(LocalDateTime.now());
			}

			configRepository.save(config.trim(config));
			return Utils.getSuccessMessage(Constants.LOCALE_UPDATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public Object getConfig(Integer customerId, UserHeader userHeader) {
		try {
			int countCus = customerRepository.countCustomerId(customerId);
			if (countCus == 0)
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));

			if (!customerRepository.checkPermissionCustomer(userHeader.getRoleId(), userHeader.getProvinceId(),
					userHeader.getDistrictId(), userHeader.getCustomerId(), customerId)) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}

//			ConfigDTO item = configRepository.getConfigByCustomerId(customerId);
			CustomerConfig configOfCustomer = configRepository.getConfigOfCustomer(customerId);
			if (configOfCustomer == null) {
				// get default config from parameter_config
				List<String> config = customerRepository.getConfigParam();
				Integer warningRenewalBefore = getDefaultValue(config.get(1));
				Integer maintenanceBefore = getDefaultValue(config.get(0));
				Integer registryBefore = getDefaultValue(config.get(2));
				return new CustomerConfig(customerId, maintenanceBefore, registryBefore, warningRenewalBefore);
//				return configRepository.save(config);
			}
			return configOfCustomer;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private int getDefaultValue(String values) {
		try {
			return Integer.parseInt(values);
		} catch (Exception e) {
			LOGGER.error("error: ",e);	
			return DEFAULT_DAY;
		}
	}

	@Override
	public String create(CustomerModel model, UserHeader userHeader, HttpServletRequest request) {
		try {
			// validate customer
			validateCustomer(model);
			
			// validate account
			List<AccountForm> customerAccounts = model.getCustomerAccounts();
			Map<String,Integer> listAccountCode = new HashMap< String,Integer>();
			for(AccountForm ca: customerAccounts) {
				if (ValidateUtils.isNullOrEmpty(ca.getContractCode())) {
					throw new BusinessException(Utils.getValidateMessage("required"));
				}
				if (ValidateUtils.isNullOrEmpty(ca.getAccountCode())) {
					throw new BusinessException(Utils.getValidateMessage("required"));
				}
				if (ValidateUtils.isNullOrEmpty(ca.getPackageCode())) {
					throw new BusinessException(Utils.getValidateMessage("required"));
				}
				if (ValidateUtils.isNullOrEmpty(ca.getPackageName())) {
					throw new BusinessException(Utils.getValidateMessage("required"));
				}
				// validate length info nhập vào
				if(ca.getAccountCode().length() > Constants.MAXLENGTH_255 ||
				   ca.getContractCode().length() > Constants.MAXLENGTH_255 ||
				   ca.getPackageCode().length() > Constants.MAXLENGTH_255 ||
				   ca.getNetworkedTypeCode().length() > Constants.MAXLENGTH_255 ||
				   ca.getNetworkedTypeName().length() > Constants.MAXLENGTH_255 ||
				   ca.getPrePaymentCode().length() > Constants.MAXLENGTH_255 ||
				   ca.getPrePaymentName().length() > Constants.MAXLENGTH_255 ||
				   ca.getPromotionCode().length() > Constants.MAXLENGTH_255 ||
				   ca.getPromotionName().length() > Constants.MAXLENGTH_255 ||
				   ca.getPackageName().length() > Constants.MAXLENGTH_255 
						)throw new BusinessException(Utils.getMessageByKey("validate.invalid"));
				if(customerAccountRepository.isExistAccountCode(ca.getAccountCode()) > 0) {
					throw new BusinessException(Utils.getMessageByKey("bccs.account.exist"));
				}
				if(customerContractRepository.isExistContractCode(ca.getContractCode()) > 0) {
					throw new BusinessException(Utils.getMessageByKey("bccs.contract.exist"));
				}
				if(listAccountCode.containsKey(ca.getAccountCode()))
					throw new BusinessException(Utils.getMessageByKey("bccs.account.duplicate"));
				listAccountCode.put(ca.getAccountCode(), 1);
			}

			Customer customer = new Customer();
			customer = getCustomerData(customer, model);
			customer.audit(Constants.LOCALE_CREATE, userHeader.getUsername());
			try {
				customer = customerRepository.save(customer);
			} catch (Exception e) {
				throw new BusinessException(Utils.getMessageByKey("validate.invalid"));
			}
			
			saveLogCreate(customer, userHeader.getUsername(), request);
			String custId = model.getCustId().trim();
			Integer customerId = customerRepository.getCustomerIdbyCustId(custId);
			if(customerId == null)throw new BusinessException(Utils.getMessageByKey("validate.invalid"));
			// lưu các account
			for(AccountForm ca: customerAccounts) {
				if(customerContractRepository.isExistContractCode(ca.getContractCode()) == 0) {
					CustomerContract custCon = new CustomerContract(customerId, custId, ca.getContractCode(), ca.getContractCode(), 1, LocalDateTime.now(), null);
					custCon.audit(Constants.LOCALE_CREATE, userHeader.getUsername());
					customerContractRepository.save(custCon);
				}
				CustomerAccount custAcc = new CustomerAccount(ca.getAccountCode(), customerId, null, null, 
						ca.getPackageCode(), ca.getPackageName(), ca.getNetworkedTypeCode(), ca.getNetworkedTypeName(), ca.getPromotionCode(), 
						ca.getPromotionName(), ca.getPrePaymentCode(), ca.getPrePaymentName(), 
						ca.getDuration(), null, ca.getContractCode(), ca.getExpiredDate());
				custAcc.audit(Constants.LOCALE_CREATE, userHeader.getUsername());
				customerAccountRepository.save(custAcc);
			}
			return Utils.getSuccessMessage(Constants.LOCALE_CREATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void validateCustomer(CustomerModel model) {
		
		// ten khach hang bat buoc
		if (ValidateUtils.isNullOrEmpty(model.getName())) {
			throw new BusinessException(Utils.getValidateMessage("required"));
		}
		// mã khách hàng bccs bat buoc
		if (ValidateUtils.isNullOrEmpty(model.getCustId())) {
			throw new BusinessException(Utils.getValidateMessage("required"));
		}
		// provinceId bat buoc nhap
			if (ValidateUtils.isNullOrEmpty(model.getProvinceId())) {
				throw new BusinessException(Utils.getValidateMessage("required"));
			}

		// dia chi , bat buoc nhạp
		if (ValidateUtils.isNullOrEmpty(model.getAddress())) {
			throw new BusinessException(Utils.getValidateMessage("required"));
		}

		// validate phone
		if (!ValidateUtils.isNullOrEmpty(model.getPhone()) && !ValidateUtils.validPhone(model.getPhone())) {
			throw new BusinessException(Utils.getErrorMessageValidate("Phone", Constants.ERROR_FORMAT));
		}

		// validate email
		if (!ValidateUtils.isNullOrEmpty(model.getEmail()) && !ValidateUtils.isEmailValid(model.getEmail())) {
			throw new BusinessException(Utils.getErrorMessageValidate("Email", Constants.ERROR_FORMAT));
		}
		/*
		 * hoadp
		 * 31/01/2020
		 * fix bug bat buoc nhap email hoac so sdt 
		 */
		// validate email
		if (ValidateUtils.isNullOrEmpty(model.getEmail()) && ValidateUtils.isNullOrEmpty(model.getPhone())) {
			throw new BusinessException(Utils.getMessageByKey("validate.phone_or_email"));
		}
		
		// validate length info nhập vào
		if(model.getAddress().length() > Constants.MAXLENGTH_255 ||
		   model.getBusinessLicense().length() > Constants.MAXLENGTH_255 ||
		   model.getEmail().length() > Constants.MAXLENGTH_100 ||
		   model.getName().length() > Constants.MAXLENGTH_255 ||
		   model.getCustId().length() > Constants.MAXLENGTH_255 ||
		   model.getTaxCode().length() > Constants.MAXLENGTH_255 ||
		   model.getPhone().length() > Constants.MAXLENGTH_100 
				)throw new BusinessException(Utils.getMessageByKey("validate.invalid"));
		
		// validate tinh, huyen
		String error = userRepository.validateCreateCustomer(model.getProvinceId(), model.getDistrictId(),
				model.getPhone(), model.getEmail(), model.getCustId(), model.getCustomerId());
		if (error != null)
			throw new BusinessException(Utils.getValidateMessage("user." + error));
		
		
	}

	@Override
	public CustomerDetailDTO getCustomer(Integer customerId, UserHeader userHeader) {
		try {
			Boolean permission = customerRepository.checkPermissionCustomer(userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(), customerId);
			if (!permission) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}
			CustomerDTO customer = customerRepository.getCustomer(customerId);
			
			if (customer == null) {
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));
			}
			List<CustomerAccountDTO> accounts = customerRepository.getListCustomerAccount(customerId);
			CustomerDetailDTO res = new CustomerDetailDTO(customerId, customer.getName(), customer.getAddress(), customer.getEmail(), customer.getPhone(),
									customer.getProvinceId(), customer.getProvinceName(), customer.getDistrictId(), 
									customer.getDistrictName(), customer.getBusinessLicense(), 
									customer.getTaxCode(), customer.getCustId(), customer.getIsCooperative(), accounts);
			return res;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public String update(CustomerModel model, UserHeader userHeader, HttpServletRequest request) {
		try {
			Boolean permission = customerRepository.checkPermissionCustomer(userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(),
					model.getCustomerId());
			if (!permission) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}
			CustomerDTO obj = customerRepository.getCustomer(model.getCustomerId());
			if (obj == null) {
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));
			}

			validateCustomer(model);
			Customer customer = customerRepository.findById(model.getCustomerId()).get();
			Customer old = (Customer) customer.clone();
			
//			customer = getCustomerData(customer, model);
			// update các trường cần cập nhật
			customer.setAddress(model.getAddress());
			customer.setBusinessLicense(model.getBusinessLicense());
			customer.setProvinceId(model.getProvinceId());
			customer.setDistrictId(model.getDistrictId());
			customer.setPhone(model.getPhone());
			customer.setName(model.getName());
			customer.setEmail(model.getEmail());
			customer.setCustId(model.getCustId());
			customer.setTaxCode(model.getTaxCode());
			customer.audit(Constants.LOCALE_UPDATE, userHeader.getUsername());

			customer = customerRepository.save(customer);
			
			saveLogUpdate(old, customer, userHeader.getUsername(), request);
			return Utils.getMessageByKey(Constants.SUCCESS_MESSAGE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private void saveLogUpdate(Customer old, Customer updated, String userName, HttpServletRequest request) {
		try {
			old = getNameProvince(old);
			updated = getNameProvince(updated);
			
			DiffResult diff = old.diff(updated);
			String content = LogUtils.getContentChange(diff);

			LogAction logAction = LogUtils.getActionLog(request, userName, Constants.ActionType.UPDATE.getValue(),
					Constants.FunctionType.CHUCNANG_KHAC.getValue(), content, old.getName(),
					old.getCustomerId(), 0, old.getCustomerId(), old.getProvinceId(), old.getDistrictId());

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

	private Customer getNameProvince(Customer customer) {
		String provinceName = customerRepository.getProvinceName(customer.getProvinceId());
		String districtName = customerRepository.getProvinceName(customer.getDistrictId());
		customer.setProvinceName(provinceName);
		customer.setDistrictName(districtName);
		return customer;
	}

	private Customer getCustomerData(Customer customer, CustomerModel model) {
		if (!ValidateUtils.isNullOrEmpty(model.getName())) {
			customer.setName(model.getName().trim());
		}
		if (!ValidateUtils.isNullOrEmpty(model.getAddress())) {
			customer.setAddress(model.getAddress().trim());
		}
		if (!ValidateUtils.isNullOrEmpty(model.getEmail())) {
			customer.setEmail(model.getEmail().trim());
		}
		if (!ValidateUtils.isNullOrEmpty(model.getPhone())) {
			customer.setPhone(model.getPhone().trim());
		}
		if (!ValidateUtils.isNullOrEmpty(model.getBusinessLicense())) {
			customer.setBusinessLicense(model.getBusinessLicense().trim());
		}

		if (!ValidateUtils.isNullOrEmpty(model.getCode())) {
			customer.setCode(model.getCode().trim());
		}
		
		if (!ValidateUtils.isNullOrEmpty(model.getCustId())) {
			customer.setCustId(model.getCustId().trim());
		}
		if (!ValidateUtils.isNullOrEmpty(model.getTaxCode())) {
			customer.setTaxCode(model.getTaxCode().trim());
		}
		customer.setProvinceId(model.getProvinceId());
		customer.setDistrictId(model.getDistrictId());

		return customer;
	}

	@Override
	public String delete(Integer customerId, UserHeader userHeader, HttpServletRequest request) {
		try {
			Boolean permission = customerRepository.checkPermissionCustomer(userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(), customerId);
			if (!permission) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}
			CustomerDTO obj = customerRepository.getCustomer(customerId);
			if (obj == null) {
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));
			}

			Customer customer = customerRepository.findById(customerId).get();
			customer.audit(Constants.LOCALE_DELETE, userHeader.getUsername());

			customerRepository.save(customer);
			saveLogDelete(customer, userHeader.getUsername(),request);
			return Utils.getMessageByKey(Constants.SUCCESS_MESSAGE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}
	
	private void saveLogDelete(Customer customer, String username, HttpServletRequest request) {
		try {
			// String content = Utils.getMessageByKey("function.delete-des");
			String content = "Xóa thành công;"; 
			LogAction logAction = LogUtils.getActionLog(request, username, Constants.ActionType.DELETE.getValue(),
					Constants.FunctionType.CHUCNANG_KHAC.getValue(), content, customer.getName(),
					customer.getCustomerId(), 0, customer.getCustomerId(), customer.getProvinceId(), customer.getDistrictId());

			logServiceClient.create(logAction);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
		}
	}

	@Override
	public List<BccsServiceDTO> getListBCCSService(Integer roleId, Integer type) {
		try {
			return customerRepository.getListBCCSService(type);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public List<CustomerAccount> getListAccountByCustomerId(Integer customerId, UserHeader userHeader) {
		try {
			Boolean permission = customerRepository.checkPermissionCustomer(userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(),
					customerId);
			if (!permission) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}
			CustomerDTO obj = customerRepository.getCustomer(customerId);
			if (obj == null) {
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));
			}
			return customerAccountRepository.getListAccountByCustomerId(customerId);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public void deleteAccount(Integer customerAccountId, UserHeader userHeader) {
		try {
			Integer customerId = customerAccountRepository.getCustomerIdByCustomerAccountid(customerAccountId); 
			Boolean permission = customerRepository.checkPermissionCustomer(userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(),
					customerId);
			if (!permission) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}
			if(customerAccountRepository.isExistCustomerAccountId(customerAccountId) == 0)throw new BusinessException(Utils.getMessageByKey("bccs.account.not_exist"));
			if(customerAccountRepository.isAssignedTransport(customerAccountId) > 0)throw new BusinessException(Utils.getMessageByKey("bccs.account.assigned"));
			customerAccountRepository.delete(customerAccountId, LocalDateTime.now(), userHeader.getUsername());
		} catch (Exception e) {
			throw new ErrorException(e);
		}
		
	}

	@Override
	public void createAccount(AccountForm accountForm, UserHeader userHeader) {
		try {
			Integer customerId = accountForm.getCustomerId();
			Boolean permission = customerRepository.checkPermissionCustomer(userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(),
					customerId);
			if (!permission) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}
			CustomerDTO obj = customerRepository.getCustomer(customerId);
			if (obj == null) {
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));
			}
			
			// validate account info
			if (ValidateUtils.isNullOrEmpty(accountForm.getCustomerId())) {
				throw new BusinessException(Utils.getValidateMessage("required"));
			}
			if (ValidateUtils.isNullOrEmpty(accountForm.getContractCode())) {
				throw new BusinessException(Utils.getValidateMessage("required"));
			}
			if (ValidateUtils.isNullOrEmpty(accountForm.getAccountCode())) {
				throw new BusinessException(Utils.getValidateMessage("required"));
			}
			if (ValidateUtils.isNullOrEmpty(accountForm.getPackageCode())) {
				throw new BusinessException(Utils.getValidateMessage("required"));
			}
			if (ValidateUtils.isNullOrEmpty(accountForm.getPackageName())) {
				throw new BusinessException(Utils.getValidateMessage("required"));
			}
			// validate length info nhập vào
			if(accountForm.getAccountCode().length() > Constants.MAXLENGTH_255 ||
					accountForm.getContractCode().length() > Constants.MAXLENGTH_255 ||
					accountForm.getPackageCode().length() > Constants.MAXLENGTH_255 ||
					accountForm.getPackageName().length() > Constants.MAXLENGTH_255 
					)throw new BusinessException(Utils.getMessageByKey("validate.invalid"));
			if(customerAccountRepository.isExistAccountCode(accountForm.getAccountCode()) > 0) {
				throw new BusinessException(Utils.getMessageByKey("bccs.account.exist"));
			}
			if(customerContractRepository.isExistContractCodeOfAnotherCustomer(accountForm.getContractCode(), accountForm.getCustomerId()) > 0) {
				throw new BusinessException(Utils.getMessageByKey("bccs.contract.exist"));
			}
			
			Integer zero = 0;
			if(zero.equals(customerContractRepository.isExistContractCode(accountForm.getContractCode()))){
				LocalDateTime startDate = LocalDateTime.now();
				LocalDateTime endDate = null;
				String custId = customerRepository.getCustIdByCustomerId(customerId);
				CustomerContract custContract = new CustomerContract(customerId, custId, accountForm.getContractCode(), accountForm.getContractCode(), 1, startDate, endDate);
				custContract.audit(Constants.LOCALE_CREATE, userHeader.getUsername());
				customerContractRepository.save(custContract);
			}
			CustomerAccount customerAccount = new CustomerAccount(accountForm.getAccountCode(), customerId, "", "",
					accountForm.getPackageCode(), accountForm.getPackageName(), accountForm.getNetworkedTypeCode(), accountForm.getNetworkedTypeName(), 
					accountForm.getPromotionCode(), accountForm.getPromotionName(), accountForm.getPrePaymentCode(), accountForm.getPrePaymentName(), 
					accountForm.getDuration(), "", accountForm.getContractCode(), accountForm.getExpiredDate());
			customerAccount.audit(Constants.LOCALE_CREATE, userHeader.getUsername());
			customerAccountRepository.save(customerAccount);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
		
	}
	
	@Override
	public void orderPackage(OrderPackageRequest orderPackage, UserHeader userHeader) {
		try {
			Integer customerId = orderPackage.getCustomerId();
			Boolean permission = customerRepository.checkPermissionCustomer(userHeader.getRoleId(),
					userHeader.getProvinceId(), userHeader.getDistrictId(), userHeader.getCustomerId(),
					customerId);
			if (!permission) {
				throw new BusinessException(Utils.getMessageByKey("customer.not_permission"));
			}
			CustomerDTO obj = customerRepository.getCustomer(customerId);
			if (obj == null) {
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));
			}
			
			// validate account info
			if (ValidateUtils.isNullOrEmpty(orderPackage.getCustomerId())) {
				throw new BusinessException(Utils.getValidateMessage("required"));
			}
			Integer zero = 0;
			String contractCode = "CTC"+orderPackage.getCustomerId();
			
			if(zero.equals(customerContractRepository.isExistContractCode(contractCode))){
				LocalDateTime startDate = LocalDateTime.now();
				LocalDateTime endDate = null;
				String custId = customerRepository.getCustIdByCustomerId(customerId);
				CustomerContract custContract = new CustomerContract(customerId, custId, contractCode, contractCode, 1, startDate, endDate);
				custContract.audit(Constants.LOCALE_CREATE, userHeader.getUsername());
				customerContractRepository.save(custContract);
			}
			List<OrderPackageDto> orderPackages = orderPackage.getOrderPackages();
			for(OrderPackageDto item:orderPackages) {
				PackageFullDTO packageInfo = customerRepository.getPackageById(item.getPackageId());
				for(int i = 0 ;i<item.getQuantity();i++) {
					String accountCode = genAccountCode(customerId,item.getPackageId());
					CustomerAccount customerAccount = new CustomerAccount(accountCode, customerId, "", "",
							packageInfo.getPackageCode(), packageInfo.getPackageName(), packageInfo.getNetworkedTypeCode(), packageInfo.getNetworkedTypeName(), 
							packageInfo.getPromotionCode(), packageInfo.getPromotionName(), packageInfo.getPrePaymentCode(), packageInfo.getPrePaymentName(), 
							packageInfo.getDuration(), "", contractCode, LocalDate.now().plusDays(1));
					customerAccount.audit(Constants.LOCALE_CREATE, userHeader.getUsername());
					customerAccountRepository.save(customerAccount);
				}
			}
			
		} catch (Exception e) {
			throw new ErrorException(e);
		}
		
	}
	String genAccountCode(Integer customerId, Integer packageId) {
		String template = customerId+"_"+packageId;
		String lastAccountCode = customerRepository.getLastAccountCode("%"+template+"%");
		Integer id = 0;
		if(lastAccountCode == null) id = 0;
		else {
			try {
				int len = lastAccountCode.length();
				id = Integer.parseInt(lastAccountCode.substring(len-4));
			} catch (Exception e) {
				id = 0;
				LOGGER.info("Error",e);
			}
		}
		String res = template + "_"+ String.format("%04d", id+1);
		return res;
	}
}
