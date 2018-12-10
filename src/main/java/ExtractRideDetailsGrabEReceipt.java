import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.openxml4j.util.Nullable;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import model.EReceipt;

public class ExtractRideDetailsGrabEReceipt {
	private Path workbookPath;
	private Path outputPath;
	private FileInputStream fileInputStream;
	private XSSFWorkbook workbook;
	private Sheet sheet;
	private SpreadsheetTemplate template;
	private String dataBoundary;
	private int dataRows;
	private int addedRows;
	private ArrayList<EReceipt> receiptsList;
	private ArrayList<String> bookingCodesList;
	
	public ExtractRideDetailsGrabEReceipt() throws IOException, ParseException {
		createOutputPath(Properties.getProperties().getApp().getOutputDir(), Properties.getProperties().getWorkbook().getName());		
		try {
			outputPath = Paths.get(System.getProperty("user.dir"), Properties.getProperties().getApp().getOutputDir(), Properties.getProperties().getWorkbook().getName());
			if (!Files.exists(workbookPath)) {
				workbook = createSpreadsheetTemplate();						
			} else {
				fileInputStream = new FileInputStream(new File(outputPath.toString()));
				workbook = new XSSFWorkbook(fileInputStream);
				fileInputStream.close();
			}
			dataRows = getMetadataDataRows();
			sheet = workbook.getSheetAt(0);
			receiptsList = DataFromFiles.getReceiptsData();
			bookingCodesList = new ArrayList<String>();
			if (!getMetadataDataBoundary().equals(SpreadsheetMetadata.META_DATA_BOUNDARY_VALUE)) readReceiptsDataFromSheet();
			writeReceiptsDataToSheet();
			if (addedRows > 0) setExtraMetadata();
			writeWorkbook();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void writeReceiptsDataToSheet() {
		addedRows = 0;
        int startRow = SpreadsheetTemplate.SHEET_DATA_START_ROW + dataRows;
        SpreadsheetWriter writer;
		Iterator<EReceipt> iterator = receiptsList.iterator();
		while (iterator.hasNext()) {			
			Row row = sheet.createRow(startRow);
			Map<String, Object> map = iterator.next().getMap();
			String bookingCode = map.get("Booking code").toString();
			if (hasBookingCode(bookingCode)) continue;
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
				System.out.println("Error encountered when parsing data: " + e.getMessage());
			}
			startRow++;			
			System.out.println("--------------------------------");
		}
	}
	
	private void readReceiptsDataFromSheet() throws IOException {
		int dataBoundaryRow = SpreadsheetTemplate.SHEET_DATA_START_ROW + dataRows;
		for (int index = SpreadsheetTemplate.SHEET_DATA_START_ROW; index < dataBoundaryRow; index++) {
			bookingCodesList.add(new EReceipt(sheet.getRow(index)).getBookingDetails().getBookingCode().toString());
		}
	}
	
	private boolean hasBookingCode(String bookingCode) {
		return bookingCodesList.contains(bookingCode);
	}
	
	private void createOutputPath(String sourceDir, String workbookName) throws IOException {
		Path dir = Paths.get(sourceDir);
		try {
			if (!Files.isDirectory(dir)) Files.createDirectory(dir);
		} catch (IOException e) {
			System.out.println("Cannot create direcotry: " + e.getMessage());
		}
		workbookPath = Paths.get(dir.getFileName().toString(), workbookName);
	}
	
	private XSSFWorkbook createSpreadsheetTemplate() throws IOException, ParseException {
		template = new SpreadsheetTemplate(Properties.getProperties().getWorkbook().getTitle());
		return template.create();
	}
	
	private void writeWorkbook() throws IOException {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(workbookPath.toString());
			workbook.write(fileOutputStream);
			workbook.close();
			fileOutputStream.close();
		} catch (IOException | EncryptedDocumentException e) {
			System.out.println("Cannot write to file: " + e.getMessage());
		}
	}
	
	private void setExtraMetadata() {
		SpreadsheetMetadata metaData = new SpreadsheetMetadata.SpreadsheetMetadataBuilder(workbook.getProperties()).modifyOnly(true).dataBoundary(dataBoundary).dataRows(dataRows).build();
		CoreProperties coreProperties = metaData.getCoreProperties();
		coreProperties.setModified(new Nullable<Date>(new Date()));
		new WriteCell.WriteCellBuilder(workbook, sheet, sheet.getRow(SpreadsheetTemplate.UPDATE_DATE_LABEL_ROW), SpreadsheetTemplate.HEADERS_CELL + 1).value(coreProperties.getModified()).cellStyleDataType(WriteCell.DataType.DateTime).build();		
	}
	
	private String getMetadataDataBoundary() {
		return new SpreadsheetMetadata(workbook.getProperties()).getDataBoundary();		
	}
	
	private Integer getMetadataDataRows() {
		return new SpreadsheetMetadata(workbook.getProperties()).getDataRows();		
	}

	public static void main(String[] args) {
		try {
			new ExtractRideDetailsGrabEReceipt();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}