package model;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.jsoup.nodes.Element;

public class BookingDetails {
	public final String BOOKING_DETAILS_LABEL = "Booking Details";
	public final String RIDE_DATETIME_LABEL = "DATE | TIME"; //	Pick-up time
	public static final List<String> BOOKING_DETAILS_SUBLABELS = Arrays.asList("Vehicle type", "Issued by driver", "Issued to", "Booking code", "Pick up location", "Drop off location", "Tag", "Profile");
	private final String pickUpTimeIdentifier = "Pick-up time:";
	private final String vehicleTypeIdentifier = "Vehicle type:";
	private final String issuedByDriverIdentifier = "Issued by driver";
	private final String issuedToIdentifier = "Issued to";
	private final String bookingCodeIdentifier = "Booking code";
	private final String pickUpLocationIdentifier = "Pick up location:";
	private final String dropOffLocationIdentifier = "Drop off location:";
	private final String tagIdentifier = "Tag:";
	private final String profileIdentifier = "Profile:";
	private Element body;
	private Row row;
	private Map<String, Object> bookingDetails = new HashMap<String, Object>();
	
	public BookingDetails() { }

	public BookingDetails(Element body) {
		this.body = body;
		setValueFromHtml(vehicleTypeIdentifier);
		setValueFromHtml(issuedByDriverIdentifier);
		setValueFromHtml(issuedToIdentifier);
		setValueFromHtml(bookingCodeIdentifier);
		setValueFromHtml(pickUpLocationIdentifier);
		setValueFromHtml(dropOffLocationIdentifier);
		setValueFromHtml(pickUpTimeIdentifier);
		setValueFromHtml(tagIdentifier);
		setValueFromHtml(profileIdentifier);
	}
	
	public BookingDetails(Row row) {
		this.row = row;
		setValueFromRow(vehicleTypeIdentifier);
		setValueFromRow(issuedByDriverIdentifier);
		setValueFromRow(issuedToIdentifier);
		setValueFromRow(bookingCodeIdentifier);
		setValueFromRow(pickUpLocationIdentifier);
		setValueFromRow(dropOffLocationIdentifier);
		setValueFromRow(pickUpTimeIdentifier);
		setValueFromRow(tagIdentifier);
		setValueFromRow(profileIdentifier);
	}
		
	private void setValueFromHtml(String identifier) {
		String value = new String();
		if (!identifier.equals(pickUpTimeIdentifier)) {
			try {
				value = body.select("span:contains(" + identifier + ")").first().parent().select("span").last().text();
				setMap(identifier, value);
			} catch (NullPointerException e) {
				System.out.println("[" + identifier + "] not found: " + e.getMessage());
				setMap(identifier, null);
			}
		} else {
			try {
				value = body.select("tr:contains(" + identifier + ")").last().parent().select("td").last().select("span").first().text();
				DateFormat read = new SimpleDateFormat("dd MMM yy HH:mm Z");			
				setMap(identifier, read.parse(value));
			} catch (ParseException e) {
				System.out.println("Error encountered when parsing data: " + e.getMessage());
				setMap(identifier, null);
			}			
		}	
	}
	
	private void setValueFromRow(String identifier) {
		if (identifier.equals(pickUpTimeIdentifier)) {
			setMap(identifier, row.getCell(0).getDateCellValue());
		} else {
			int index = BOOKING_DETAILS_SUBLABELS.indexOf(sanitizeIdentifier(identifier)) + 2;
			CellType cellType = row.getCell(index).getCellTypeEnum();
			switch (cellType) {
				case NUMERIC:
					setMap(identifier, row.getCell(index).getNumericCellValue());
					break;
				case BLANK:
					setMap(identifier, null);
					break;
				case STRING:
				default:
					setMap(identifier, row.getCell(index).getStringCellValue());				
			}
		}
	}
	
	public Date getPickUpTime() {
		return (Date) getMap().get(sanitizeIdentifier(pickUpTimeIdentifier));
	}
	
	public String getVehicleType() {
		return (String) getMap().get(sanitizeIdentifier(vehicleTypeIdentifier));
	}
	
	public String getIssuedByDriver() {
		return (String) getMap().get(issuedByDriverIdentifier);
	}
	
	public String getIssuedTo() {
		return (String) getMap().get(issuedToIdentifier);
	}
	
	public String getBookingCode() {
		return (String) getMap().get(bookingCodeIdentifier);
	}
	
	public String getPickUpLocation() {
		return (String) getMap().get(sanitizeIdentifier(pickUpLocationIdentifier));
	}
	
	public String getDropOffLocation() {
		return (String) getMap().get(sanitizeIdentifier(dropOffLocationIdentifier));
	}
	
	public String getTagValue() {
		return (String) getMap().get(sanitizeIdentifier(tagIdentifier));
	}
	
	public String getProfileValue() {
		return (String) getMap().get(sanitizeIdentifier(profileIdentifier));
	}
	
	private String sanitizeIdentifier(String identifier) {
		return identifier.replaceAll("[:]", "");
	}
	
	private void setMap(String key, Object value) {
		bookingDetails.put(sanitizeIdentifier(key), value);
	}
	
	public Map<String, Object> getMap() {
		return bookingDetails;		
	}
	
	public String identifierAtIndex(int index) {
		return BOOKING_DETAILS_SUBLABELS.get(index);
	}
}
