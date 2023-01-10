/**
 * API v1.0 - VTracking v2.0 Project.
 * @author anhth32
 * Created on Apr 10, 2019
 * 
 * Copyright (c) 2019 Viettel Business Solutions Corp.
 */
package com.fms.customerservice.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fms.module.model.ValidateModel;
import com.fms.module.utils.Constants;
import com.fms.module.utils.Utils;
import com.fms.module.utils.ValidateUtils;
import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * @author anhth32 Created on Apr 10, 2019
 */
public class UtilsReportService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UtilsReportService.class);
	public ValidateModel checkValidate(Long userId, List<Long> ids, String fromDate, String toDate,
			LocalDateTime beginTime, LocalDateTime endTime, Short reportType) {
		LocalDateTime currentDateTime = Utils.getDateTimeWithZoneOffset(LocalDateTime.now(), Constants.GMT_OFFSET);
		currentDateTime = currentDateTime.withHour(23).withMinute(59).withSecond(59);
		ValidateModel model = new ValidateModel();
		if (ValidateUtils.isNullOrEmpty(fromDate)) {
			model.setHasError(true);
			model.setMessage(Utils.getValidateMessage(
					Constants.LOCALE_DATETIME_VALIDATE_ROOT + '.' + Constants.LOCALE_START_TIME_NOT_BLANK));
			return model;
		}
		if (ValidateUtils.isNullOrEmpty(toDate)) {
			model.setHasError(true);
			model.setMessage(Utils.getValidateMessage(
					Constants.LOCALE_DATETIME_VALIDATE_ROOT + '.' + Constants.LOCALE_END_TIME_NOT_BLANK));
			return model;
		}
		if (beginTime == null) {
			model.setHasError(true);
			model.setMessage(Utils.getValidateMessage(
					Constants.LOCALE_DATETIME_VALIDATE_ROOT + '.' + Constants.LOCALE_START_TIME_INCORRECT_FORMAT));
			return model;
		}
		if (endTime == null) {
			model.setHasError(true);
			model.setMessage(Utils.getValidateMessage(
					Constants.LOCALE_DATETIME_VALIDATE_ROOT + '.' + Constants.LOCALE_END_TIME_INCORRECT_FORMAT));
			return model;
		}
		if (beginTime.isAfter(endTime)) {
			model.setHasError(true);
			model.setMessage(Utils.getValidateMessage(
					Constants.LOCALE_DATETIME_VALIDATE_ROOT + '.' + Constants.LOCALE_START_TIME_EARLIER_THAN_END_TIME));
			return model;
		}
		if (beginTime.isAfter(currentDateTime)) {
			model.setHasError(true);
			model.setMessage(Utils.getValidateMessage(Constants.LOCALE_DATETIME_VALIDATE_ROOT + '.'
					+ Constants.LOCALE_START_TIME_EARLIER_THAN_CURRENT_TIME));
			return model;
		}
		if (endTime.isAfter(currentDateTime)) {
			model.setHasError(true);
			model.setMessage(Utils.getValidateMessage(Constants.LOCALE_DATETIME_VALIDATE_ROOT + '.'
					+ Constants.LOCALE_END_TIME_EARLIER_THAN_CURRENT_TIME));
			return model;
		}

		long totalReportDays = ChronoUnit.DAYS.between(beginTime.toLocalDate(), endTime.toLocalDate());
		if (totalReportDays > Constants.REPORT_QCVN2014_DAYS_LIMIT) {
			model.setHasError(true);
			model.setMessage(Utils.getValidateMessage(Constants.LOCALE_DATETIME_VALIDATE_ROOT + '.'
					+ Constants.LOCALE_END_TIME_EARLIER_THAN_CURRENT_TIME));
			return model;
		}
		if (ids == null || ids.size() <= 0) {
			model.setHasError(true);
			model.setMessage(Utils.getValidateMessage(
					Constants.LOCALE_DATETIME_VALIDATE_ROOT + '.' + Constants.LOCALE_TRANSPORT_ID_NULL));
			return model;
		}
		if (reportType == Constants.REPORT_QCVN2014_BCHTXC) {
			if (ids != null && ids.size() > 1) {
				model.setHasError(true);
				model.setMessage(Utils.getValidateMessage(
						Constants.LOCALE_DATETIME_VALIDATE_ROOT + '.' + Constants.LOCALE_TRANSPORT_ID_NULL));
				return model;
			}
		}
		return model;
	}

	public ValidateModel checkExportValidate(String fileType) {
		ValidateModel model = new ValidateModel();
		if (Constants.EXCEL_EXTENSION_1.equals(fileType.toLowerCase())
				|| Constants.EXCEL_EXTENSION_2.equals(fileType.toLowerCase())) {
			model.setFileType(Constants.EXCEL_EXTENSION_1);
		} else if (Constants.PDF_EXTENSION.equals(fileType.toLowerCase())) {
			model.setFileType(Constants.PDF_EXTENSION);
		} else {
			model.setHasError(true);
			model.setMessage(Utils.getValidateMessage(
					Constants.LOCALE_REPORT_ROOT + '.' + Constants.LOCALE_REPORT_INCORRECT_EXPORT_FILE_TYPE));
		}
		return model;
	}

	public Object convertJsonToObject(String json, Class<?> classObject) {
		Gson gson = new Gson();
		Object results = gson.fromJson(json, classObject);
		return results;
	}

	public BaseFont getBaseFontReport() throws DocumentException, IOException, URISyntaxException {
		ClassLoader classLoader = getClass().getClassLoader();
		byte[] fontByte = IOUtils.toByteArray(classLoader.getResourceAsStream("fonts/TIMESNEWROMAN.TTF"));
		BaseFont baseFont = BaseFont.createFont("TIMESNEWROMAN.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontByte,
				null);
		return baseFont;
	}

	// :::::: === EXCEL COMMON === :::::::
	public XSSFWorkbook getWorkbookByTemplate(String fileTemplateName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = null;
//		String filePath = String.format("%s%s.%s", "reports/", fileTemplateName, Constants.EXCEL_EXTENSION_1);
		/*
		 * đổi format file excel sang .xlsx
		 * @Date 9/1/2020
		 */
		String filePath = String.format("%s%s.%s", "reports/", fileTemplateName, Constants.EXCEL_EXTENSION_DEFAULT);
		try {
			inputStream = classLoader.getResourceAsStream(filePath);
		} catch (Exception e) {
			LOGGER.error("error: ",e);
			inputStream = classLoader.getResourceAsStream(filePath);
		}
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		inputStream.close();
		return workbook;
	}

	public ByteArrayInputStream getExportFile(XSSFWorkbook workbook) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();
		return new ByteArrayInputStream(out.toByteArray());
	}

	public void setWorkbookCellWrapText(Cell cell, XSSFWorkbook workbook) {
		CellStyle cs = workbook.createCellStyle();
		cs.setWrapText(true);
		cell.setCellStyle(cs);
	}
	// :::::: === END EXCEL COMMON === :::::::::

	// :::::: === PDF COMMON === ::::::::::
	public PdfPTable getPdfTemplate(String title, List<String> colSpanHeaderName, int[] colSpan,
			List<String> headerName, float[] headerWidth, float widthPercentage, BaseFont baseFont)
			throws DocumentException, IOException {
		List<Integer> lstNoBoder = new ArrayList<>();
		int totalColumn = headerName.size();
		PdfPTable table = new PdfPTable(totalColumn);
		table.setWidthPercentage(widthPercentage);
		table.setWidths(headerWidth);

		Font headerFont = new Font(baseFont, 12, Font.BOLD);
		Font titleFont = new Font(baseFont, 21, Font.BOLD);
		PdfPCell cell;
		// Title
		cell = new PdfPCell(new Phrase(title.toUpperCase(), titleFont));
		cell.setColspan(totalColumn);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingBottom(15f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		
		// Header template
		if (colSpanHeaderName != null && colSpanHeaderName.size() > 0) {
			int index = 0;
			int mergeIndex = 0;
			for (String headerItem : colSpanHeaderName) {
				cell = new PdfPCell(new Phrase(headerItem, headerFont));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.disableBorderSide(Rectangle.BOTTOM);
				cell.setPaddingBottom(5f);
				if (colSpan != null && colSpan.length > 0) {
					if (!ValidateUtils.isNullOrEmpty(headerItem)) {
						cell.setColspan(colSpan[mergeIndex]);
						index += colSpan[mergeIndex];
						mergeIndex++;
					} else {
						lstNoBoder.add(index);
						index++;
					}
				} else {
					index++;
				}
				table.addCell(cell);
			}
		}
		int i = 0;
		for (String headerItem : headerName) {
			cell = new PdfPCell(new Phrase(headerItem, headerFont));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setPaddingBottom(5f);
			if (lstNoBoder.contains(i)) {
				cell.disableBorderSide(Rectangle.TOP);
			}
			table.addCell(cell);
			i++;
		}
		return table;
	}

	public void setPdfCellValue(Object data, Font bodyFont, int align, PdfPTable table) {
		String value = String.format("%s", (data == null) ? "" : data);
		value = ValidateUtils.isNullOrEmpty(value) ? "" : value;
		PdfPCell cell = new PdfPCell(new Phrase(value, bodyFont));
		cell.setHorizontalAlignment(align);
		cell.setPaddingBottom(5f);
		table.addCell(cell);
	}
	// :::::: === END PDF COMMON === :::::

	public String executeExportShellPatch(String fileName) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder();
			String path = Utils.getFolderPath(Constants.EXPORT_BATCH_FILE_PATH);
			String filePath = Utils.getFolderPath(Constants.REPORT_EXPORT_PATH);
			if (Utils.checkWindowsOS()) {
				// -- Windows --
				// Run a command
				// processBuilder.command("cmd.exe", "/c", "dir C:\\Users\\quantri\\app");
				// Run a bat file
				String batchFilePath = String.format("%s\\%s", path, Constants.WINDOWS_EXPORT_BATCH_FILE)
						.replaceAll("/", "");
				processBuilder.command(batchFilePath, filePath, filePath, fileName);
			} else {
				// -- Linux --
				// Run a shell command
				// processBuilder.command("bash", "-c", "ls /home/app/");
				// Run a shell script
				String batchFilePath = String.format("%s\\%s", path, Constants.LINUX_EXPORT_BATCH_FILE).replaceAll("/",
						"");
				processBuilder.command(batchFilePath, filePath, filePath, fileName);
			}
			Process process = processBuilder.start();
			StringBuilder output = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line);
				break;
			}
			if (0 != process.waitFor()) {
				return null;
			}
			return output.toString();
		} catch (Exception e) {
			LOGGER.error("error: ",e);
			return null;
		}
	}

	/*
	 * hoadp
	 *	format cell for file xlsx
	 */
	public CellStyle createStyleAndAlignmentForCell(XSSFWorkbook workbook, int alignmentType) {
		CellStyle style = workbook.createCellStyle();
		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		if(alignmentType == 0) {
			style.setAlignment(HorizontalAlignment.CENTER);	
		}
		else if(alignmentType == 1) {
			style.setAlignment(HorizontalAlignment.LEFT);	
		}
		else if(alignmentType == 2) {
			style.setAlignment(HorizontalAlignment.RIGHT);	
		}
		style.setWrapText(true);
		return style;
	}
	/*
	 * hoadp
	 *	set value anh format text for cell in file xlsx
	 */
	public void setValueForCell(Cell cell, String value, CellStyle style) {
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
}
