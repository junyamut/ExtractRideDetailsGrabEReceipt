import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Date;

import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.openxml4j.util.Nullable;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreateWorkbook extends WorkbookOutputPath {
	private static XSSFWorkbook workbook;
	private static Sheet sheet;

	public CreateWorkbook() { }
	
	static {		
		try {			
			if (!Files.exists(getPath())) {
				SpreadsheetTemplate template = new SpreadsheetTemplate(Properties.getProperties().getWorkbook().getTitle());
				workbook = template.create();						
			} else {
				FileInputStream fileInputStream = new FileInputStream(new File(getPath().toString()));
				workbook = new XSSFWorkbook(fileInputStream);
				fileInputStream.close();
			}
			sheet = workbook.getSheetAt(0);
		} catch (IOException | ParseException e) {
			System.err.println(e.getMessage());
		}	
	}

	public static XSSFWorkbook getWorkbook() {
		return workbook;
	}
	
	public static Sheet getSheet() {
		return sheet;
	}

	public static void writeWorkbook() throws IOException {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(getPath().toString());
			workbook.write(fileOutputStream);
			workbook.close();
			fileOutputStream.close();			
		} catch (IOException e) {
			System.err.println("Cannot write to file: " + e.getMessage());
		}
	}
	
	public static void setExtraMetadata(String dataBoundary, int dataRows) {
		SpreadsheetMetadata metaData = new SpreadsheetMetadata.SpreadsheetMetadataBuilder(workbook.getProperties()).modifyOnly(true).dataBoundary(dataBoundary).dataRows(dataRows).build();
		CoreProperties coreProperties = metaData.getCoreProperties();
		coreProperties.setModified(new Nullable<Date>(new Date()));
		new WriteCell.WriteCellBuilder(workbook, sheet, sheet.getRow(SpreadsheetTemplate.UPDATE_DATE_LABEL_ROW), SpreadsheetTemplate.HEADERS_CELL + 1).value(coreProperties.getModified()).cellStyleDataType(WriteCell.DataType.DateTime).build();		
	}
	
	public static String getMetadataDataBoundary() {
		return new SpreadsheetMetadata(workbook.getProperties()).getDataBoundary();		
	}
	
	public static Integer getMetadataDataRows() {
		return new SpreadsheetMetadata(workbook.getProperties()).getDataRows();		
	}
}