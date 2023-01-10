package com.fms.customerservice.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

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
import org.springframework.stereotype.Service;

import com.fms.customerservice.controller.ExportController;
import com.fms.customerservice.controller.utils.AppUtils;
import com.fms.customerservice.model.Customer;
import com.fms.customerservice.model.Driver;
import com.fms.customerservice.model.DriverCustomerDTO;
import com.fms.customerservice.model.DriverDTO;
import com.fms.customerservice.model.ExportModel;
import com.fms.customerservice.repository.CustomerRepository;
import com.fms.customerservice.repository.DriverRepository;
import com.fms.module.exception.BusinessException;
import com.fms.module.exception.ErrorException;
import com.fms.module.model.SearchModel;
import com.fms.module.model.UserHeader;
import com.fms.module.utils.Constants;
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
public class DriverServiceImpl implements DriverService {
	private static final Logger LOGGER = LoggerFactory.getLogger(DriverServiceImpl.class);
	@Autowired
	DriverRepository driverRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	private ServletContext servletContext;

	Boolean isExist_Id(Integer driverId) {
		Driver existing = driverRepository.findByDriverId(driverId);
		return existing != null;
	}

	@Override
	public String createDriver(Driver newDriver, String username) {
		try {
			newDriver = validateDriver(newDriver);

			newDriver.audit(Constants.LOCALE_CREATE, username);
			Driver driver = driverRepository.save(newDriver);
			if (driver == null) {
				return Utils.getErrorMessage(Constants.LOCALE_CREATE);
			}
			return Utils.getSuccessMessage(Constants.LOCALE_CREATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	private Driver validateDriver(Driver driver) {
		if (ValidateUtils.isNullOrEmpty(driver.getCustomerId())) {
			throw new BusinessException(Utils.getErrorMessageValidate("customerId", Constants.NOTBLANK));
		} else {
			Customer customer = customerRepository.retrieveCustomer(driver.getCustomerId());
			if (customer == null)
				throw new BusinessException(Utils.getValidateMessage("user.customer-not-exist"));
		}

		if (!ValidateUtils.isNullOrEmpty(driver.getPhone()) && !ValidateUtils.validPhone(driver.getPhone())) {
			throw new BusinessException(Utils.getErrorMessageValidate("Phone", Constants.ERROR_FORMAT));
		}

		if (!ValidateUtils.isNullOrEmpty(driver.getEmail()) && !ValidateUtils.isEmailValid(driver.getEmail())) {
			throw new BusinessException(Utils.getErrorMessageValidate("Email", Constants.ERROR_FORMAT));
		}
		if (driver.getDob() != null) {
			LocalDate now = LocalDate.now();
			LocalDate dob = driver.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			Period period = Period.between(dob, now);
			if (period.getYears() < 18) {
				throw new BusinessException(Utils.getValidateMessage("transport.driver-over-18"));
			}
		}

		return driver.trim(driver);
	}

	@Override
	public String updateDriver(Integer driverId, Driver updateDriver, String username) {
		try {
			Driver driverCurrent = driverRepository.findByDriverId(driverId);
			if (driverCurrent == null) {
				throw new BusinessException(Utils.getErrorMessageWithParams("[driverId]", Constants.LOCALE_NOT_EXIST));
			}
			updateDriver = validateDriver(updateDriver);
			updateDriver.setDriverId(driverId);
			updateDriver.audit(Constants.LOCALE_UPDATE, username);
			updateDriver.setCreatedBy(driverCurrent.getCreatedBy());
			updateDriver.setCreatedDate(driverCurrent.getCreatedDate());
			Utils.mappingDTONullAllowed(updateDriver, driverCurrent);

			Driver driver = driverRepository.save(driverCurrent);
			if (driver == null) {
				return Utils.getErrorMessage(Constants.LOCALE_UPDATE);
			}
			return Utils.getSuccessMessage(Constants.LOCALE_UPDATE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public String deleteByDriverId(Integer driverId, String username) {
		try {
			Driver driver = driverRepository.findByDriverId(driverId);
			if (driver == null) {
				throw new BusinessException(Utils.getErrorMessageWithParams("[driverId]", Constants.LOCALE_NOT_EXIST));
			}
			driver.audit(Constants.LOCALE_DELETE, username);
			Driver driverDeleted = driverRepository.save(driver);
			if (driverDeleted == null) {
				return Utils.getErrorMessage(Constants.LOCALE_DELETE);
			}
			return Utils.getSuccessMessage(Constants.LOCALE_DELETE);
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public Driver findByDriverId(Integer driverId) {
		try {
			Driver driver = driverRepository.findByDriverId(driverId);
			if (driver == null) {
				throw new BusinessException(Utils.getErrorMessageWithParams("[driverId]", Constants.LOCALE_NOT_EXIST));
			}
			return driver;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public Page<DriverDTO> findAll(SearchModel searchModel, UserHeader userHeader, boolean export) {
		try {
			if (userHeader.getRoleId() == Constants.USER_ACCOUNT_ROLE) {
				throw new BusinessException(Utils.getMessageByKey(Constants.LOCALE_UNAUTHORIZED));
			}

			String name = ValidateUtils.isNullOrEmpty(searchModel.getName()) ? null
					: Utils.toLikeString(searchModel.getName());

			String phone = ValidateUtils.isNullOrEmpty(searchModel.getPhone()) ? null
					: Utils.toLikeString(searchModel.getPhone());

			String license = ValidateUtils.isNullOrEmpty(searchModel.getLisence()) ? null
					: Utils.toLikeString(searchModel.getLisence());

			String id = ValidateUtils.isNullOrEmpty(searchModel.getIndentify()) ? null
					: Utils.toLikeString(searchModel.getIndentify());

			String dob = null;
			if (!AppUtils.isNullOrEmpty(searchModel.getDob())) {
				Date date = AppUtils.convertToDate(searchModel.getDob());
				if(date == null) {
					throw new BusinessException(Utils.getValidateMessage("user.date-error"));
				}
				dob = AppUtils.formatDateQuery(searchModel.getDob().trim());
			}

			Pageable pageable = PageRequest.of(searchModel.getPageNumber() - 1, searchModel.getPageSize(),
					Sort.Direction.ASC, searchModel.getOrderBy());
			if (export) {
				pageable = Pageable.unpaged();
			}
			Page<DriverDTO> data = null;

			data = driverRepository.getAllDrivers(name, phone, id, license, dob, userHeader.getProvinceId(),
					userHeader.getCustomerId(), userHeader.getDistrictId(), pageable);

			return data;
		} catch (Exception e) {
			throw new ErrorException(e);
		}
	}

	@Override
	public List<DriverCustomerDTO> getDriverOfCustomer(Integer customerId, Integer roleId, Integer customerIdLogin,
			Integer provinceId, Integer districtId) {
		try {
			if (customerId != -1) {
				int countCus = customerRepository.countCustomerId(customerId);
				if (countCus == 0)
					throw new BusinessException(Utils.getValidateMessage("transport.customer-notexist"));

				Boolean checkPermission = customerRepository.checkPermissionCustomer(roleId, provinceId, districtId,
						customerIdLogin, customerId);
				if (checkPermission != null && !checkPermission)
					throw new BusinessException(Utils.getValidateMessage("user.customer-author"));
			}

			if (roleId == Constants.SYS_ADMIN_ROLE) {
				return driverRepository.getDriverOfCustomer(-1, customerId, -1);
			} else if (roleId == Constants.PROVINCE_ADMIN_ROLE) {
				return driverRepository.getDriverOfCustomer(provinceId, customerId, -1);
			} else if (roleId == Constants.CUSTOMER_ACCOUNT_ROLE || roleId == Constants.USER_ACCOUNT_ROLE) {
				return driverRepository.getDriverOfCustomer(-1, customerIdLogin, -1);
			} else if (roleId == Constants.DISTRICT_ADMIN_ROLE) {
				return driverRepository.getDriverOfCustomer(provinceId, -1, districtId);
			}
		} catch (Exception e) {
			throw new ErrorException(e);
		}

		return null;
	}

	@Override
	public ResponseEntity<InputStreamResource> export(SearchModel searchModel, UserHeader userHeader, String type) {
		try {
			UtilsReportService utilsReportService = new UtilsReportService();
			Page<DriverDTO> page = findAll(searchModel, userHeader, true);
			List<DriverDTO> data = null;

			int stt = 1;
			ByteArrayInputStream fileExport = null;
			ExportController exportController = new ExportController();

			if (page != null) {
				data = page.getContent();
				if ("excel".equals(type)) {
					XSSFWorkbook workbook = utilsReportService.getWorkbookByTemplate(Constants.BC_LAI_XE_FILE_NAME);
					if ("en".equals(userHeader.getAcceptLang())) {
						workbook = utilsReportService.getWorkbookByTemplate(Constants.BC_LAI_XE_FILE_NAME_EN);
					}
					XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0);
					int startRowNo = 2;
					int cellNo = 0;
					CellStyle centeredStyle = utilsReportService.createStyleAndAlignmentForCell(workbook, 0);
					CellStyle leftStyle = utilsReportService.createStyleAndAlignmentForCell(workbook, 1);
					for (DriverDTO item : data) {
						/*Row row = sheet.createRow(startRowNo);
						row.createCell(cellNo++).setCellValue(stt++);
						row.createCell(cellNo++).setCellValue(item.getDriverName());
						row.createCell(cellNo++).setCellValue(item.getEmployeeId());
						row.createCell(cellNo++).setCellValue(item.getDob());
						row.createCell(cellNo++).setCellValue(item.getID());
						row.createCell(cellNo++).setCellValue(item.getPhone());
						row.createCell(cellNo++).setCellValue(item.getAddress());
						row.createCell(cellNo++).setCellValue(item.getCustomerName());

						cellNo = 0;
						startRowNo++;
						*/
						cellNo = 0;
						Row row = sheet.createRow(startRowNo);
						utilsReportService.setValueForCell(row.createCell(cellNo++),String.valueOf(stt),centeredStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getDriverName(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getEmployeeId(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getDob(),centeredStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getID(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getPhone(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo++),item.getAddress(),leftStyle);
						utilsReportService.setValueForCell(row.createCell(cellNo),item.getCustomerName(),leftStyle);
						startRowNo++;
						stt++;
					}

					String fileName = Utils.getFileNameExport(Constants.BC_LAI_XE_FILE_NAME, "xlsx");
					if ("en".equals(userHeader.getAcceptLang())) {
						fileName = Utils.getFileNameExport(Constants.BC_LAI_XE_FILE_NAME_EN, "xlsx");
					}
					
					fileExport = utilsReportService.getExportFile(workbook);

					MediaType mediaType = exportController.getMediaTypeForFileName(this.servletContext, fileName);
					InputStreamResource resource = new InputStreamResource(fileExport);

					return ResponseEntity.ok()
							.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
							.contentType(mediaType).body(resource);
				} else {
					Document document = new Document();
					document.setPageSize(PageSize.A4.rotate());
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					BaseFont baseFont = utilsReportService.getBaseFontReport();
					
					PdfPTable table = utilsReportService.getPdfTemplate(ReportHeaderConstants.DANH_SACH_LAI_XE_TITLE,
							null, null, ReportHeaderConstants.BC_LAI_XE_COLUMN_HEADERS_NAMES,
							ReportHeaderConstants.BC_LAI_XE_COLUMN_HEADERS_WIDTHS, 100f, baseFont);
					
					if ("en".equals(userHeader.getAcceptLang())) {
						table = utilsReportService.getPdfTemplate(ReportHeaderConstants.DANH_SACH_LAI_XE_TITLE_EN,
								null, null, ReportHeaderConstants.BC_LAI_XE_COLUMN_HEADERS_NAMES_EN,
								ReportHeaderConstants.BC_LAI_XE_COLUMN_HEADERS_WIDTHS, 100f, baseFont);
					}
					
					// Body data
					Font bodyFont = new Font(baseFont, 12, Font.NORMAL);
					for (DriverDTO item : data) {
						utilsReportService.setPdfCellValue(stt++, bodyFont, Element.ALIGN_CENTER, table);
						utilsReportService.setPdfCellValue(item.getDriverName(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getEmployeeId(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getDob(), bodyFont, Element.ALIGN_CENTER, table);
						utilsReportService.setPdfCellValue(item.getID(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getPhone(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getAddress(), bodyFont, Element.ALIGN_LEFT, table);
						utilsReportService.setPdfCellValue(item.getCustomerName(), bodyFont, Element.ALIGN_LEFT, table);
					}
					PdfWriter.getInstance(document, out);
					document.open();
					document.add(table);
					document.close();
					fileExport = new ByteArrayInputStream(out.toByteArray());
					
					String fileName = Constants.BC_LAI_XE_FILE_NAME;
					if ("en".equals(userHeader.getAcceptLang())) {
						fileName = Constants.BC_LAI_XE_FILE_NAME_EN;
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

}
