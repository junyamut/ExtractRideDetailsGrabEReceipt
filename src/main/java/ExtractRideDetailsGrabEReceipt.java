import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import model.EReceipt;

public class ExtractRideDetailsGrabEReceipt {
	private XSSFWorkbook workbook;
	private Sheet sheet;
	private String dataBoundary;
	private int dataRows;
	private int addedRows;
	private int dataStartRow;
	private ArrayList<EReceipt> receiptsList;
	
	public ExtractRideDetailsGrabEReceipt() throws IOException, ParseException {		
		try {
			workbook = CreateWorkbook.getWorkbook();
			sheet = CreateWorkbook.getSheet();
			receiptsList = DataFromFiles.getReceiptsData();
			dataRows = DataFromWorkbook.getDataRows();
			dataStartRow = DataFromWorkbook.getDataStartRow();
			if (!DataFromWorkbook.getMetadataDataBoundary().equals(SpreadsheetMetadata.META_DATA_BOUNDARY_VALUE)) DataFromWorkbook.addBookingCodes();
			writeReceiptsDataToSheet();
			if (addedRows > 0) CreateWorkbook.setExtraMetadata(dataBoundary, dataRows);
			CreateWorkbook.writeWorkbook();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	private void writeReceiptsDataToSheet() {
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

	public static void main(String[] args) {
		try {
			new ExtractRideDetailsGrabEReceipt();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}