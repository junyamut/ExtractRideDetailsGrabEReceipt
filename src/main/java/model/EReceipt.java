package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.jsoup.nodes.Document;

public class EReceipt {
	private BookingDetails bookingDetails;
	private ReceiptSummary receiptSummary;
	private Map<String, Object> jointList = new HashMap<String, Object>();
	
	public EReceipt() { 
		this.bookingDetails = new BookingDetails();
		this.receiptSummary = new ReceiptSummary();
	}
	
	public EReceipt(Row row) { 
		this.bookingDetails = new BookingDetails(row);
		this.receiptSummary = new ReceiptSummary(row);
	}

	public EReceipt(Document document) {
		this.bookingDetails = new BookingDetails(document.body());
		this.receiptSummary = new ReceiptSummary(document.body());
	}

	public BookingDetails getBookingDetails() {
		return bookingDetails;
	}

	public void setBookingDetails(BookingDetails bookingDetails) {
		this.bookingDetails = bookingDetails;
	}

	public ReceiptSummary getReceiptSummary() {
		return receiptSummary;
	}

	public void setReceiptSummary(ReceiptSummary receiptSummary) {
		this.receiptSummary = receiptSummary;
	}
	
	public int getColumnHeaderIndex(String identifier) {
		List<String> allColumnHeaders = new ArrayList<String>(BookingDetails.BOOKING_DETAILS_SUBLABELS);
		allColumnHeaders.addAll(ReceiptSummary.RECEIPT_SUMMARY_SUBLABELS);
		return allColumnHeaders.indexOf(identifier);		
	}
	
	public Map<String, Object> getMap() {
		jointList.putAll(bookingDetails.getMap());
		jointList.putAll(receiptSummary.getMap());
		return jointList;
	}
}
