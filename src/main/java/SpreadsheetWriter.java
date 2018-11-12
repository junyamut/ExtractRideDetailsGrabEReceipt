import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.util.Nullable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SpreadsheetWriter {
	private XSSFWorkbook workbook;
	private Sheet sheet;
	private Row row;
	private Map<String, Object> dataMap;
	
	public SpreadsheetWriter(XSSFWorkbook workbook, Sheet sheet, Row row, Map<String, Object> dataMap) { 
		this.workbook = workbook;
		this.sheet = sheet;
		this.row = row;
		this.dataMap = dataMap;
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
	
	public void writeData() throws ParseException {		
		if (!isRowEmpty(row)) {
			throw new IllegalStateException("Row is not empty. Aborting write.");
		}
		DateFormat read = new SimpleDateFormat("dd MMM yy HH:mm Z");
		int column = 0;
		int numericColumnsStart = SpreadsheetTemplate.SUBLABELS_BD_LIST.size() + 1;
		Iterator<Map.Entry<String, Object>> iterator = dataMap.entrySet().iterator();		
		while (iterator.hasNext()) {
			Map.Entry<String, Object> data = iterator.next();
			System.out.println(data.getKey() + ": " + data.getValue());
			// The column is the Header index value from SpreadsheetTemplate List<String> SUBLABELS_BD_LIST & List<String> SUBLABELS_RS_LIST combined
			column = SpreadsheetTemplate.getColumnHeaderIndex(data.getKey().toString().trim());
			// If column is found it should have an index >= 0
			// Pick-up time & TOTAL are inserted on Columns 0 & 1 which is not in the combined list above
			// Add 2 to the value of column to move it 2 Columns to the right
			if (column >= 0) {
				if (column <= numericColumnsStart) {					
					new WriteCell.WriteCellBuilder(workbook, sheet, row, (column + 2)).value(data.getValue()).build();
				} else {
					new WriteCell.WriteCellBuilder(workbook, sheet, row, (column + 2)).value(data.getValue()).cellStyleDataType(WriteCell.DataType.NumericDecimal).build();
				}
			}
			if (data.getKey().equals("Pick-up time")) {				
				Date date = read.parse(data.getValue().toString());
				new WriteCell.WriteCellBuilder(workbook, sheet, row, 0).value(date).cellStyleDataType(WriteCell.DataType.DateTime).cellStyleFont(new SpreadsheetFont.SpreadsheetFontBuilder(workbook).color(HSSFColor.HSSFColorPredefined.BLUE.getIndex()).height(12).bold(true).build().getFont()).build();
			}
			if (data.getKey().equals("TOTAL")) {
				new WriteCell.WriteCellBuilder(workbook, sheet, row, 1).value(data.getValue()).cellStyleDataType(WriteCell.DataType.NumericDecimal).cellStyleFont(new SpreadsheetFont.SpreadsheetFontBuilder(workbook).color(HSSFColor.HSSFColorPredefined.RED.getIndex()).height(12).bold(true).build().getFont()).build();
			}			
			column++;
		}
		setMetadataModifiedDate();
	}
	
	private void setMetadataModifiedDate() {
		CoreProperties coreProperties = new SpreadsheetMetadata(this.workbook.getProperties()).getCoreProperties();
		coreProperties.setModified(new Nullable<Date>(new Date()));		;
		new WriteCell.WriteCellBuilder(workbook, sheet, sheet.getRow(SpreadsheetTemplate.UPDATE_DATE_LABEL_ROW), SpreadsheetTemplate.HEADERS_CELL + 1).value(coreProperties.getModified()).cellStyleDataType(WriteCell.DataType.DateTime).build();		
		
	}
}