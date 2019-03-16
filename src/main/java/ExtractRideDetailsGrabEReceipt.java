import java.io.IOException;
import java.text.ParseException;

public class ExtractRideDetailsGrabEReceipt {
	
	public ExtractRideDetailsGrabEReceipt() throws IOException, ParseException {		
		try {
			if (!DataFromWorkbook.getMetadataDataBoundary().equals(SpreadsheetMetadata.META_DATA_BOUNDARY_VALUE)) DataFromWorkbook.addBookingCodes();
			ReceiptsDataToSheet dataToSheet = new ReceiptsDataToSheet(CreateWorkbook.getWorkbook(), CreateWorkbook.getSheet(), DataFromFiles.getReceiptsData(), DataFromWorkbook.getDataRows(), DataFromWorkbook.getDataStartRow());
			if (dataToSheet.getAddedRows() > 0) CreateWorkbook.setExtraMetadata(dataToSheet.getDataBoundary(), dataToSheet.getDataRows());
			CreateWorkbook.writeWorkbook();
		} catch (IOException e) {
			System.err.println(e.getMessage());
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