import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SpreadsheetTemplate {	
	public static final List<String> SUBLABELS_BD_LIST = Arrays.asList("Vehicle type", "Issued by driver", "Issued to", "Booking code", "Pick up location", "Drop off location", "Tag");
	public static final List<String> SUBLABELS_RS_LIST = Arrays.asList("Payment Method", "Ride Fare", "Base Fare", "Adjustment for Min Fare", "Distance", "Time", "Surge Charges", "Share Discount");
	public static final String SHEET_FONT = "Arial";	
	public static final String CREATION_DATE_LABEL = "Created On";
	public static final String UPDATE_DATE_LABEL = "Updated On";	
	public static final String RIDE_DATE_LABEL = "Date";
	public static final String TOTAL_FARE_LABEL = "TOTAL";
	public static final String BOOKING_DETAILS_LABEL = "Booking Details";
	public static final String RECEIPT_SUMMARY_LABEL = "Receipt Summary";
	public static final int SHEET_DATA_START_ROW = 7;
	public static final int SHEET_NAME_ROW = 0;
	public static final int CREATION_DATE_LABEL_ROW = 1;
	public static final int UPDATE_DATE_LABEL_ROW = 2;
	public static final int HEADERS_CELL = 0;
	public static final int RIDE_LABELS_ROW = 5;
	public static final int RIDE_SUBLABELS_ROW = 6;
	public static final int RIDE_DATE_LABEL_CELL = 0;
	public static final int TOTAL_FARE_LABEL_CELL = 1;
	public static final int BOOKING_DETAILS_LABEL_CELL = 2;
	public static final int RECEIPT_SUMMARY_LABEL_CELL = 9;
	public static final int SUBLABELS_START_CELL = 2;
	private SpreadsheetMetadata metadata;
	private XSSFWorkbook workbook;	
	private XSSFFont labelFont;
	private Sheet sheet;
	private String sheetName;

	public SpreadsheetTemplate() {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet();
		labelFont = new SpreadsheetFont.SpreadsheetFontBuilder(workbook).height(12).build().getFont();
	}
	
	public SpreadsheetTemplate(String sheetName) {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet();
		this.sheetName = sheetName;
		labelFont = new SpreadsheetFont.SpreadsheetFontBuilder(workbook).height(12).bold(true).build().getFont();
		metadata = new SpreadsheetMetadata.SpreadsheetMetadataBuilder(workbook.getProperties()).title(sheetName).build();
	}
	
	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {	
		this.sheetName = WorkbookUtil.createSafeSheetName(sheetName);
	}
	
	private void titleNameRow() {
		Row titleNameRow = sheet.createRow(SHEET_NAME_ROW);
		titleNameRow.setHeightInPoints((short) 24);
		new WriteCell.WriteCellBuilder(workbook, sheet, titleNameRow, 0).value(sheetName).cellStyleFont(new SpreadsheetFont.SpreadsheetFontBuilder(workbook).color(HSSFColor.HSSFColorPredefined.GREEN.getIndex()).height(20).bold(true).build().getFont()).cellColumnWidth(HEADERS_CELL, 4000).build();
	}
	
	private void headerDatesRow() throws ParseException {
		DateFormat read = new SimpleDateFormat("EE MMM dd HH:mm:ss Z yyyy");
		Date date = read.parse(metadata.getCoreProperties().getCreated().toString());
		Row creationDateRow = sheet.createRow(CREATION_DATE_LABEL_ROW);		
		new WriteCell.WriteCellBuilder(workbook, sheet, creationDateRow, HEADERS_CELL).value(CREATION_DATE_LABEL).cellStyleFont(labelFont).cellStyleAlignment(HorizontalAlignment.RIGHT).build();
		new WriteCell.WriteCellBuilder(workbook, sheet, creationDateRow, HEADERS_CELL + 1).value(date).cellStyleDataType(WriteCell.DataType.DateTime).build();		
		Row updateDateRow = sheet.createRow(UPDATE_DATE_LABEL_ROW);
		new WriteCell.WriteCellBuilder(workbook, sheet, updateDateRow, HEADERS_CELL).value(UPDATE_DATE_LABEL).cellStyleFont(labelFont).cellStyleAlignment(HorizontalAlignment.RIGHT).build();
		new WriteCell.WriteCellBuilder(workbook, sheet, updateDateRow, HEADERS_CELL + 1).cellColumnWidth(HEADERS_CELL + 1, 4000).build();
	}
	
	private void columnGroupHeadersRow() {
		Row labelsRow = sheet.createRow(RIDE_LABELS_ROW);
		new WriteCell.WriteCellBuilder(workbook, sheet, labelsRow, RIDE_DATE_LABEL_CELL).value(RIDE_DATE_LABEL).cellStyleFont(labelFont).cellStyleAlignment(HorizontalAlignment.CENTER).cellStyleFillForegroundColor(IndexedColors.LIGHT_GREEN).cellStyleFillPattern(FillPatternType.SOLID_FOREGROUND).mergeCells(RIDE_LABELS_ROW, RIDE_LABELS_ROW + 1, RIDE_DATE_LABEL_CELL, RIDE_DATE_LABEL_CELL).build();
		new WriteCell.WriteCellBuilder(workbook, sheet, labelsRow, TOTAL_FARE_LABEL_CELL).value(TOTAL_FARE_LABEL).cellStyleFont(labelFont).cellStyleAlignment(HorizontalAlignment.CENTER).cellStyleFillForegroundColor(IndexedColors.LIGHT_GREEN).cellStyleFillPattern(FillPatternType.SOLID_FOREGROUND).mergeCells(RIDE_LABELS_ROW, RIDE_LABELS_ROW + 1, TOTAL_FARE_LABEL_CELL, TOTAL_FARE_LABEL_CELL).build();
		new WriteCell.WriteCellBuilder(workbook, sheet, labelsRow, BOOKING_DETAILS_LABEL_CELL).value(BOOKING_DETAILS_LABEL).cellStyleFont(labelFont).cellStyleAlignment(HorizontalAlignment.CENTER).cellStyleFillForegroundColor(IndexedColors.LIGHT_GREEN).cellStyleFillPattern(FillPatternType.SOLID_FOREGROUND).mergeCells(RIDE_LABELS_ROW, RIDE_LABELS_ROW, BOOKING_DETAILS_LABEL_CELL, BOOKING_DETAILS_LABEL_CELL + (SUBLABELS_BD_LIST.size() - 1)).build();
		new WriteCell.WriteCellBuilder(workbook, sheet, labelsRow, RECEIPT_SUMMARY_LABEL_CELL).value(RECEIPT_SUMMARY_LABEL).cellStyleFont(labelFont).cellStyleAlignment(HorizontalAlignment.CENTER).cellStyleFillForegroundColor(IndexedColors.LIGHT_GREEN).cellStyleFillPattern(FillPatternType.SOLID_FOREGROUND).mergeCells(RIDE_LABELS_ROW, RIDE_LABELS_ROW, RECEIPT_SUMMARY_LABEL_CELL, RECEIPT_SUMMARY_LABEL_CELL + (SUBLABELS_RS_LIST.size() - 1)).build();
	}
	
	private void columnSubheadersRow() {
		Row rideSublabelsRow = sheet.createRow(RIDE_SUBLABELS_ROW);
		Iterator<String> group1 = SUBLABELS_BD_LIST.iterator();
		int index1 = SUBLABELS_START_CELL;
		while (group1.hasNext()) {
			new WriteCell.WriteCellBuilder(workbook, sheet, rideSublabelsRow, index1).value(group1.next()).cellStyleFillForegroundColor(IndexedColors.LIGHT_YELLOW).cellStyleFillPattern(FillPatternType.SOLID_FOREGROUND).cellColumnWidth(index1, 4000).build();			
			index1++;
		}
		Iterator<String> group2 = SUBLABELS_RS_LIST.iterator();
		int index2 = SUBLABELS_START_CELL + SUBLABELS_BD_LIST.size();
		while (group2.hasNext()) {
			new WriteCell.WriteCellBuilder(workbook, sheet, rideSublabelsRow, index2).value(group2.next()).cellStyleFillForegroundColor(IndexedColors.LIGHT_YELLOW).cellStyleFillPattern(FillPatternType.SOLID_FOREGROUND).cellColumnWidth(index2, 4000).build();
			index2++;
		}
	}
	
	public static int getColumnHeaderIndex (String value) {
		List<String> allColumnHeaders = new ArrayList<String>(SUBLABELS_BD_LIST);
		allColumnHeaders.addAll(SUBLABELS_RS_LIST);
		return allColumnHeaders.indexOf(value);		
	}
	
	public XSSFWorkbook create() throws ParseException {
		titleNameRow();
		headerDatesRow();
		columnGroupHeadersRow();
		columnSubheadersRow();
		return workbook;
	}
}