import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import model.BookingDetails;
import model.EReceipt;
import model.ReceiptSummary;

public class SpreadsheetTemplate {
	public static final String SHEET_FONT = "Arial";	
	public static final String CREATION_DATE_LABEL = "Created On";
	public static final String UPDATE_DATE_LABEL = "Updated On";
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
	private EReceipt eReceipt;

	public SpreadsheetTemplate() {
		this.workbook = new XSSFWorkbook();
		this.sheet = workbook.createSheet();
		this.labelFont = new SpreadsheetFont.SpreadsheetFontBuilder(workbook).height(12).build().getFont();
		this.eReceipt = new EReceipt();
	}
	
	public SpreadsheetTemplate(String sheetName) {
		this.workbook = new XSSFWorkbook();
		this.sheet = workbook.createSheet();
		this.sheetName = sheetName;
		this.labelFont = new SpreadsheetFont.SpreadsheetFontBuilder(workbook).height(12).bold(true).build().getFont();
		this.metadata = new SpreadsheetMetadata.SpreadsheetMetadataBuilder(workbook.getProperties()).title(sheetName).build();
		this.eReceipt = new EReceipt();
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
	
	private void headerDatesRow(Integer rowIndex, Integer cellIndex, Object cellValue, WriteCell.DataType dataType, XSSFFont font, HorizontalAlignment alignment, Integer adjustCellColumnWidth) throws ParseException {
		Row dateRow;
		if (sheet.getRow(rowIndex) == null) dateRow = sheet.createRow(rowIndex);
		else dateRow = sheet.getRow(rowIndex);		
		new WriteCell.WriteCellBuilder(workbook, sheet, dateRow, cellIndex).value(cellValue).cellStyleDataType(dataType).cellStyleFont(font).cellStyleAlignment(alignment).cellColumnWidth(adjustCellColumnWidth, 4000).build();
	}
	
	private void columnsGroupheadersRow(Integer cellIndex, Object cellValue, Integer ...cellRangeIndices) {
		Row labelsRow;
		if (sheet.getRow(RIDE_LABELS_ROW) == null) labelsRow = sheet.createRow(RIDE_LABELS_ROW);
		else labelsRow = sheet.getRow(RIDE_LABELS_ROW);
		new WriteCell.WriteCellBuilder(workbook, sheet, labelsRow, cellIndex).value(cellValue).cellStyleFont(labelFont).cellStyleAlignment(HorizontalAlignment.CENTER).cellStyleFillForegroundColor(IndexedColors.LIGHT_GREEN).cellStyleFillPattern(FillPatternType.SOLID_FOREGROUND).mergeCells(cellRangeIndices).build();	
	}
	
	private void columnsSubheadersRow(List<String> list, Integer cellIndex) {
		Row sublabelsRow;
		if (sheet.getRow(RIDE_SUBLABELS_ROW) == null) sublabelsRow = sheet.createRow(RIDE_SUBLABELS_ROW);
		else sublabelsRow = sheet.getRow(RIDE_SUBLABELS_ROW);
		Iterator<String> group = list.iterator();
		while (group.hasNext()) {
			new WriteCell.WriteCellBuilder(workbook, sheet, sublabelsRow, cellIndex).value(group.next()).cellStyleFillForegroundColor(IndexedColors.LIGHT_YELLOW).cellStyleFillPattern(FillPatternType.SOLID_FOREGROUND).cellColumnWidth(cellIndex, 4000).build();			
			cellIndex++;
		}
	}
	
	public XSSFWorkbook create() throws ParseException {
		titleNameRow();
		DateFormat read = new SimpleDateFormat("EE MMM dd HH:mm:ss Z yyyy");
		Date date = read.parse(metadata.getCoreProperties().getCreated().toString());
		headerDatesRow(CREATION_DATE_LABEL_ROW, HEADERS_CELL, CREATION_DATE_LABEL, WriteCell.DataType.Text, labelFont, HorizontalAlignment.RIGHT, null);
		headerDatesRow(CREATION_DATE_LABEL_ROW, HEADERS_CELL + 1, date, WriteCell.DataType.DateTime, null, null, null);
		headerDatesRow(UPDATE_DATE_LABEL_ROW, HEADERS_CELL, UPDATE_DATE_LABEL, WriteCell.DataType.Text, labelFont, HorizontalAlignment.RIGHT, null);
		headerDatesRow(UPDATE_DATE_LABEL_ROW, HEADERS_CELL + 1, null, WriteCell.DataType.Empty, null, null, HEADERS_CELL + 1);
		columnsGroupheadersRow(RIDE_DATE_LABEL_CELL, eReceipt.getBookingDetails().RIDE_DATETIME_LABEL, new Integer[]{RIDE_LABELS_ROW, RIDE_LABELS_ROW + 1, RIDE_DATE_LABEL_CELL, RIDE_DATE_LABEL_CELL});
		columnsGroupheadersRow(TOTAL_FARE_LABEL_CELL, eReceipt.getReceiptSummary().TOTAL_FARE_LABEL, new Integer[]{RIDE_LABELS_ROW, RIDE_LABELS_ROW + 1, TOTAL_FARE_LABEL_CELL, TOTAL_FARE_LABEL_CELL});
		columnsGroupheadersRow(BOOKING_DETAILS_LABEL_CELL, eReceipt.getBookingDetails().BOOKING_DETAILS_LABEL, new Integer[]{RIDE_LABELS_ROW, RIDE_LABELS_ROW, BOOKING_DETAILS_LABEL_CELL, BOOKING_DETAILS_LABEL_CELL + (BookingDetails.BOOKING_DETAILS_SUBLABELS.size() - 1)});
		columnsGroupheadersRow(RECEIPT_SUMMARY_LABEL_CELL, eReceipt.getReceiptSummary().RECEIPT_SUMMARY_LABEL, new Integer[]{RIDE_LABELS_ROW, RIDE_LABELS_ROW, RECEIPT_SUMMARY_LABEL_CELL, RECEIPT_SUMMARY_LABEL_CELL + (ReceiptSummary.RECEIPT_SUMMARY_SUBLABELS.size() - 1)});
		columnsSubheadersRow(BookingDetails.BOOKING_DETAILS_SUBLABELS, SUBLABELS_START_CELL);
		columnsSubheadersRow(ReceiptSummary.RECEIPT_SUMMARY_SUBLABELS, SUBLABELS_START_CELL + BookingDetails.BOOKING_DETAILS_SUBLABELS.size());
		return workbook;
	}
}