import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.BookingDetails;
import model.EReceipt;

public class SpreadsheetWriter {
	private XSSFWorkbook workbook;
	private Sheet sheet;
	private Row row;
	private Map<String, Object> dataMap;
	private boolean autoResize;
	private EReceipt eReceipt;
	
	public SpreadsheetWriter(XSSFWorkbook workbook, Sheet sheet, Row row, Map<String, Object> dataMap) { 
		this.workbook = workbook;
		this.sheet = sheet;
		this.row = row;
		this.dataMap = dataMap;
		this.autoResize = false;
		this.eReceipt = new EReceipt();
	}
	
	public static boolean isRowEmpty(Row row) {
	    if (row == null || row.getLastCellNum() <= 0) {
	        return true;
	    }
	    for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
	        Cell cell = row.getCell(cellNum);
	        if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && !cell.getStringCellValue().isEmpty()) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public void setColumnAutoResize(boolean autoResize) {
		this.autoResize = autoResize;
	}
	
	public void writeData() throws ParseException {
		if (!isRowEmpty(row)) {
			throw new IllegalStateException("Row is not empty. Aborting write.");
		}
		int column = 0;
		eReceipt.getBookingDetails();
		int receiptSummaryColumnStart = BookingDetails.BOOKING_DETAILS_SUBLABELS.size();
		Iterator<Map.Entry<String, Object>> iterator = dataMap.entrySet().iterator();		
		while (iterator.hasNext()) {
			Map.Entry<String, Object> data = iterator.next();
			System.out.println("Getting next pair>> " + data.getKey() + ": " + data.getValue());
			// The column is the Header index value from BD + RS List<String> BOOKING_DETAILS_SUBLABELS & List<String> RECEIPT_SUMMARY_SUBLABELS combined
			column = eReceipt.getColumnHeaderIndex(data.getKey().toString().trim());
			// If column is found it should have an index >= 0
			// Pick-up time & TOTAL are inserted on Columns 0 & 1 which is not in the combined list above
			// Add 2 to the value of column to move it 2 Columns to the right
			if (column >= 0) {
				if (column <= receiptSummaryColumnStart) {
					new WriteCell.WriteCellBuilder(workbook, sheet, row, (column + 2)).value(data.getValue()).build();
				} else {
					if (data.getValue() != null) {
						new WriteCell.WriteCellBuilder(workbook, sheet, row, (column + 2)).value(data.getValue()).cellStyleDataType(WriteCell.DataType.NumericDecimal).build();
					} else {
						new WriteCell.WriteCellBuilder(workbook, sheet, row, (column + 2)).value(data.getValue()).cellStyleDataType(WriteCell.DataType.Empty).build();
					}
				}
			}
			if (data.getKey().equals("Pick-up time")) {
				new WriteCell.WriteCellBuilder(workbook, sheet, row, 0).value(data.getValue()).cellStyleDataType(WriteCell.DataType.DateTime).cellStyleFont(new SpreadsheetFont.SpreadsheetFontBuilder(workbook).color(HSSFColor.HSSFColorPredefined.BLUE.getIndex()).height(12).bold(true).build().getFont()).build();
			}
			if (data.getKey().equals("TOTAL")) {
				new WriteCell.WriteCellBuilder(workbook, sheet, row, 1).value(data.getValue()).cellStyleDataType(WriteCell.DataType.NumericDecimal).cellStyleFont(new SpreadsheetFont.SpreadsheetFontBuilder(workbook).color(HSSFColor.HSSFColorPredefined.RED.getIndex()).height(12).bold(true).build().getFont()).build();
			}
			if (autoResize)	sheet.autoSizeColumn(column + 2);
			column++;
		}
	}
}