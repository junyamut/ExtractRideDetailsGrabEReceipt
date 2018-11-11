package model;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;

public class EReceipt {
	private BookingDetails bookingDetails;
	private ReceiptSummary receiptSummary;
	private Map<String, Object> jointList = new HashMap<String, Object>();

	public EReceipt(Document document) {	
		bookingDetails = new BookingDetails(document.body());
		receiptSummary = new ReceiptSummary(document.body());
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
	
	public Map<String, Object> getMap() {
		jointList.putAll(bookingDetails.getMap());
		jointList.putAll(receiptSummary.getMap());
		return jointList;
	}
}
