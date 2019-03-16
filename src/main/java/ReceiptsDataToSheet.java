import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import model.EReceipt;

public class ReceiptsDataToSheet {
	private XSSFWorkbook workbook;
	private Sheet sheet;
	private String dataBoundary;
	private int dataRows;
	private int addedRows;
	private int dataStartRow;
	private ArrayList<EReceipt> receiptsList;

	public ReceiptsDataToSheet(XSSFWorkbook workbook, Sheet sheet, ArrayList<EReceipt> receiptsList, int dataRows, int dataStartRow) {
		this.workbook = workbook;
		this.sheet = sheet;
		this.receiptsList = receiptsList;
		this.dataRows = dataRows;
		this.dataStartRow = dataStartRow;
		write();
	}
	
	private void write() {
		addedRows = 0;
        SpreadsheetWriter writer;
		Iterator<EReceipt> iterator = receiptsList.iterator();
		while (iterator.hasNext()) {			
			Row row = sheet.createRow(dataStartRow);
			Map<String, Object> map = iterator.next().getMap();
			String bookingCode = map.get("Booking code").toString();
			if (DataFromWorkbook.hasBookingCode(bookingCode)) continue;
			addedRows++;
			writer = new SpreadsheetWriter(workbook, sheet, row, map);
			if (!iterator.hasNext()) {
				writer.setColumnAutoResize(true);
				dataBoundary = bookingCode;
				dataRows += addedRows;
			}
			try {
				writer.writeData();
			} catch (ParseException e) {
				System.err.println("Error encountered when parsing data: " + e.getMessage());
			}
			dataStartRow++;			
			System.out.println("--------------------------------");
		}
	}

	public String getDataBoundary() {
		return dataBoundary;
	}

	public void setDataBoundary(String dataBoundary) {
		this.dataBoundary = dataBoundary;
	}

	public int getDataRows() {
		return dataRows;
	}

	public void setDataRows(int dataRows) {
		this.dataRows = dataRows;
	}

	public int getAddedRows() {
		return addedRows;
	}

	public void setAddedRows(int addedRows) {
		this.addedRows = addedRows;
	}

}