import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.jsoup.Jsoup;

import helper.SortByRideDate;
import model.EReceipt;

public class DataFromFiles extends EReciptsFilePaths {
	private static ArrayList<EReceipt> receiptsList;
	private static Iterator<Path> iterator;
	
	public DataFromFiles() { }
	
	static {
		receiptsList = new ArrayList<EReceipt>();
		iterator = getList().iterator();
		setReceiptsData();
		Collections.sort(receiptsList, new SortByRideDate());
	}
	
	private static void setReceiptsData() {
		while (iterator.hasNext()) {
			try {
				receiptsList.add(new EReceipt(Jsoup.parse(new File(iterator.next().toString()), "UTF-8")));							
			} catch (IOException e) {
				System.out.println("Error reading E-Receipt files: " + e.getMessage());
			}			
		}	
	}
	
	public static ArrayList<EReceipt> getReceiptsData() {
		return receiptsList;
	}
}