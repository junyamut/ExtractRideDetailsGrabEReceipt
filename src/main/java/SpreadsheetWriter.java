//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
	
	public void writeData() {		
		if (!isRowEmpty(row)) {
			throw new IllegalStateException("Row is not empty. Aborting write.");
		}		
		Iterator<Map.Entry<String, Object>> iterator = dataMap.entrySet().iterator();
		int column = 0; 
		while (iterator.hasNext()) {
			Map.Entry<String, Object> data = iterator.next();
			System.out.println(data.getKey() + ": " + data.getValue());
			column = SpreadsheetTemplate.getColumnHeaderIndex(data.getKey().toString().trim());
			if (column >= 0) {
				new WriteCell.WriteCellBuilder(workbook, sheet, row, (column + 2)).value(data.getValue().toString()).build();
			}
			if (data.getKey().equals("Pick-up time")) { // 27 Oct 18 16:13 +0800
//				DateFormat read = new SimpleDateFormat("dd MMM yy HH:mm Z");
//				DateFormat write = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//				Date date = read.parse(data.getValue().toString());				
//				System.out.println(write.format(date));
				new WriteCell.WriteCellBuilder(workbook, sheet, row, 0).value(data.getValue().toString()).build();
			}
			if (data.getKey().equals("TOTAL")) {
				new WriteCell.WriteCellBuilder(workbook, sheet, row, 1).value(data.getValue().toString()).build();
			}
			column++;
		}
	}
}

