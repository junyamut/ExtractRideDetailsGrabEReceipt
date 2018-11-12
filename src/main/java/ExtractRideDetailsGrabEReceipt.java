import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import helper.FilesList;
import model.EReceipt;

public class ExtractRideDetailsGrabEReceipt {
	public static final String SHEET_NAME = "My Grab Rides";
	public static final String WORKBOOK_NAME = "My Grab Rides.xlsx";
	public static final String SOURCE_DIR = "receipts";
	public static final String OUTPUT_DIR = "workbooks";
	public Path workbookPath;
	public XSSFWorkbook workbook;
	SpreadsheetTemplate template;

	public ExtractRideDetailsGrabEReceipt() throws IOException, ParseException {
		createOutputPath(OUTPUT_DIR, WORKBOOK_NAME);		
		try {
			if (!Files.exists(workbookPath)) {
				createSpreadsheetTemplate();
			}
			extract();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	private List<Path> getFilesList() {
		Path sourcePath = Paths.get(System.getProperty("user.dir"), SOURCE_DIR);
		FilesList filesList = new FilesList(sourcePath);
		return filesList.getList();
	}
	
	private void extract() throws IOException {
		Path outputPath = Paths.get(System.getProperty("user.dir"), OUTPUT_DIR, WORKBOOK_NAME);
		FileInputStream fileInputStream = new FileInputStream(new File(outputPath.toString()));
		workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);        
        int lastDataRow = sheet.getLastRowNum();
        int startRow = lastDataRow + 1;
        SpreadsheetWriter writer;
        File input;
		Document document;
		Iterator<Path> iterator = getFilesList().iterator();	
		while (iterator.hasNext()) {
			Path filePath = iterator.next();
			input = new File(filePath.toString());
			document = Jsoup.parse(input, "UTF-8");
			EReceipt eReceipt = new EReceipt(document);
			Row row = sheet.createRow(startRow);			
			writer = new SpreadsheetWriter(workbook, sheet, row, eReceipt.getMap());
			if (!iterator.hasNext()) writer.setColumnAutoResize(true);
			try {
				writer.writeData();
			} catch (ParseException e) {
				System.out.println("Error encountered when parsing data: " + e.getMessage());
			}
			startRow++;
			System.out.println("--------------------------------");
		}
		writeWorkbook();
		fileInputStream.close();
	}
	
	private void createSpreadsheetTemplate() throws IOException, ParseException {
		template = new SpreadsheetTemplate(SHEET_NAME);
		workbook = template.create();
		writeWorkbook();
	}
	
	private void createOutputPath(String sourceDir, String workbookName) throws IOException {
		Path dir = Paths.get(sourceDir);
		try {
			if (!Files.isDirectory(dir)) {
				Files.createDirectory(dir);
			}
		} catch (IOException e) {
			System.out.println("Cannot create direcotry: " + e.getMessage());
		}
		workbookPath = Paths.get(dir.getFileName().toString(), workbookName);
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

	public static void main(String[] args) {
		try {
			new ExtractRideDetailsGrabEReceipt();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}
