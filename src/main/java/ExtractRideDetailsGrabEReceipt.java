import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.openxml4j.util.Nullable;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import helper.FilesList;
import helper.SortByRideDate;
import model.EReceipt;

public class ExtractRideDetailsGrabEReceipt {
	public static final String SHEET_NAME = "My Grab Rides";
	public static final String WORKBOOK_NAME = "My Grab Rides.xlsx";
	public static final String SOURCE_DIR = "receipts";
	public static final String OUTPUT_DIR = "workbooks";
	private Path workbookPath;
	private Path outputPath;
	private FileInputStream fileInputStream;
	private XSSFWorkbook workbook;
	private Sheet sheet;
	private SpreadsheetTemplate template;
	private String dataBoundary;
	private int dataRows;

	public ExtractRideDetailsGrabEReceipt() throws IOException, ParseException {		
		createOutputPath(OUTPUT_DIR, WORKBOOK_NAME);		
		try {
			outputPath = Paths.get(System.getProperty("user.dir"), OUTPUT_DIR, WORKBOOK_NAME);
			if (!Files.exists(workbookPath)) {
				workbook = createSpreadsheetTemplate();						
			} else {
				fileInputStream = new FileInputStream(new File(outputPath.toString()));
				workbook = new XSSFWorkbook(fileInputStream);
				fileInputStream.close();
			}
			sheet = workbook.getSheetAt(0);
			extract();			
			setExtraMetadata();
			writeWorkbook();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void extract() throws IOException {
		ArrayList<EReceipt> receiptsList = new ArrayList<EReceipt>();
		int numRows = 0;
        int lastDataRow = sheet.getLastRowNum();
        int startRow = lastDataRow + 1;
        SpreadsheetWriter writer;
        File input;
		Document document;
		Iterator<Path> iterator1 = getFilesList().iterator();
		while (iterator1.hasNext()) {
			Path filePath = iterator1.next();
			input = new File(filePath.toString());
			document = Jsoup.parse(input, "UTF-8");
			receiptsList.add(new EReceipt(document));
			Collections.sort(receiptsList, new SortByRideDate());
		}
		Iterator<EReceipt> iterator2 = receiptsList.iterator();
		while (iterator2.hasNext()) {
			numRows++;
			Row row = sheet.createRow(startRow);
			Map<String, Object> map = iterator2.next().getMap();
			writer = new SpreadsheetWriter(workbook, sheet, row, map);
			if (!iterator2.hasNext()) {
				writer.setColumnAutoResize(true);
				dataBoundary = map.get("Booking code").toString();
				dataRows = numRows;
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
	
	private void createOutputPath(String sourceDir, String workbookName) throws IOException {
		Path dir = Paths.get(sourceDir);
		try {
			if (!Files.isDirectory(dir)) Files.createDirectory(dir);
		} catch (IOException e) {
			System.out.println("Cannot create direcotry: " + e.getMessage());
		}
		workbookPath = Paths.get(dir.getFileName().toString(), workbookName);
	}
		
	private List<Path> getFilesList() {
		Path sourcePath = Paths.get(System.getProperty("user.dir"), SOURCE_DIR);
		FilesList filesList = new FilesList(sourcePath);
		return filesList.getList();
	}
	
	private XSSFWorkbook createSpreadsheetTemplate() throws IOException, ParseException {
		template = new SpreadsheetTemplate(SHEET_NAME);
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

	public static void main(String[] args) {
		try {
			new ExtractRideDetailsGrabEReceipt();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}