package model;
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
	private String pickUpTimeValue;
	private String vehicleTypeValue;
	private String issuedByDriverValue;
	private String issuedToValue;
	private String bookingCodeValue;
	private String pickUpLocationValue;
	private String dropOffLocationValue;
	private String tagValue;
	private Element body;
	private Map<String, Object> bookingDetails = new HashMap<String, Object>();

	public BookingDetails(Element body) { 
		this.body = body;
		vehicleTypeValue = setValue(vehicleTypeIdentifier);
		issuedByDriverValue = setValue(issuedByDriverIdentifier);
		issuedToValue = setValue(issuedToIdentifier);
		bookingCodeValue =  setValue(bookingCodeIdentifier);
		pickUpLocationValue = setValue(pickUpLocationIdentifier);
		dropOffLocationValue = setValue(dropOffLocationIdentifier);
		pickUpTimeValue = setValue(pickUpTimeIdentifier);
		tagValue = setValue(tagIdentifier);
	}
		
	private String setValue(String identifier) {
		String value = new String();
		if (!identifier.equals(pickUpTimeIdentifier)) {
			value = body.select("span:contains(" + identifier + ")").first().parent().select("span").last().text();
			setMap(identifier, value);
			return value;
		} else {
			value = body.select("tr:contains(" + identifier + ")").last().parent().select("td").last().select("span").first().text();
			setMap(identifier, value);
			return value;
		}	
	}
	
	public String getPickUpTime() {
		return pickUpTimeValue;
	}
	
	public String getVehicleType() {
		return vehicleTypeValue;
	}
	
	public String getIssuedByDriver() {
		return issuedByDriverValue;
	}
	
	public String getIssuedTo() {
		return issuedToValue;
	}
	
	public String getBookingCode() {
		return bookingCodeValue;
	}
	
	public String getPickUpLocation() {
		return pickUpLocationValue;
	}
	
	public String getDropOffLocation() {
		return dropOffLocationValue;
	}
	
	public String getTagValue() {
		return tagValue;
	}
	
	private String sanitizeIdentifier(String identifier) {
		return identifier.replaceAll("[:]", "");
	}
	
	private void setMap(String key, String value) {
		bookingDetails.put(sanitizeIdentifier(key), value);
	}
	
	public Map<String, Object> getMap() {
		return bookingDetails;		
	}
}
