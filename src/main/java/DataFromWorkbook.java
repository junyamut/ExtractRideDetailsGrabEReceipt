import java.io.IOException;
import java.util.ArrayList;

import model.EReceipt;

public class DataFromWorkbook extends CreateWorkbook {
	private static ArrayList<String> bookingCodesList;
	private static int dataRows;
	private static int dataStartRow;

	public DataFromWorkbook() { }
	
	static {
		bookingCodesList = new ArrayList<String>();
		dataRows = getMetadataDataRows();
		dataStartRow = SpreadsheetTemplate.SHEET_DATA_START_ROW + dataRows;
	}
	
	public static void readBookingCodes() throws IOException {
		for (int index = SpreadsheetTemplate.SHEET_DATA_START_ROW; index < dataStartRow; index++) {
			bookingCodesList.add(new EReceipt(getSheet().getRow(index)).getBookingDetails().getBookingCode().toString());
		}
	}
	
	public static int getDataRows() {
		return dataRows;
	}

	public static int getDataStartRow() {
		return dataStartRow;
	}

	public static boolean hasBookingCode(String bookingCode) {
		return bookingCodesList.contains(bookingCode);
	}
}
