package model;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Element;

public class BookingDetails {
	private final String pickUpTimeIdentifier = "Pick-up time:";
	private final String vehicleTypeIdentifier = "Vehicle type:";
	private final String issuedByDriverIdentifier = "Issued by driver";
	private final String issuedToIdentifier = "Issued to";
	private final String bookingCodeIdentifier = "Booking code";
	private final String pickUpLocationIdentifier = "Pick up location:";
	private final String dropOffLocationIdentifier = "Drop off location:";
	private final String tagIdentifier = "Tag:";	
	private Element body;
	private Map<String, Object> bookingDetails = new HashMap<String, Object>();

	public BookingDetails(Element body) { 
		this.body = body;
		setValue(vehicleTypeIdentifier);
		setValue(issuedByDriverIdentifier);
		setValue(issuedToIdentifier);
		setValue(bookingCodeIdentifier);
		setValue(pickUpLocationIdentifier);
		setValue(dropOffLocationIdentifier);
		setValue(pickUpTimeIdentifier);
		setValue(tagIdentifier);
	}
		
	private void setValue(String identifier) {
		String value = new String();
		if (!identifier.equals(pickUpTimeIdentifier)) {
			value = body.select("span:contains(" + identifier + ")").first().parent().select("span").last().text();
			setMap(identifier, value);
		} else {
			value = body.select("tr:contains(" + identifier + ")").last().parent().select("td").last().select("span").first().text();
			DateFormat read = new SimpleDateFormat("dd MMM yy HH:mm Z");
			try {
				setMap(identifier, read.parse(value));
			} catch (ParseException e) {
				System.out.println("Error encountered when parsing data: " + e.getMessage());
			}			
		}	
	}
	
	public Date getPickUpTime() {
		return (Date) getMap().get(pickUpTimeIdentifier);
	}
	
	public String getVehicleType() {
		return (String) getMap().get(vehicleTypeIdentifier);
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
		return (String) getMap().get(pickUpLocationIdentifier);
	}
	
	public String getDropOffLocation() {
		return (String) getMap().get(dropOffLocationIdentifier);
	}
	
	public String getTagValue() {
		return (String) getMap().get(tagIdentifier);
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
}
