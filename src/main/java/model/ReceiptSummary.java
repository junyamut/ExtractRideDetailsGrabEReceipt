package model;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.nodes.Element;

public class ReceiptSummary {
	private final String totalAmountIdentifier = "TOTAL";
	private final String paymentMethodIdentifier = "Payment Method";
	private final String rideFareIdentifier = "Ride Fare";
	private final String baseFareIdentifier = "Base Fare";
	private final String adjustmentMinFareIdentifier = "Adjustment for Min Fare";
	private final String distanceAmountIdentifier = "Distance";
	private final String timeAmountIdentifier = "Time";
	private final String surgeChargesIdentifier = "Surge Charges";
	private final String shareDiscountIdentifier = "Share Discount";
	private final String promotionAmountIdentifier = "Promotion";
	private String paymentMethodValue;
	private Double totalAmountValue;	
	private Double rideFareValue;
	private Double baseFareValue;
	private Double adjusmentMinFareValue;
	private Double distanceAmountValue;
	private Double timeAmountValue;
	private Double surgeChargesValue;
	private Double shareDiscountValue;
	private Double promotionAmountValue;
	private Element body;
	private Map<String, Object> receiptSummary = new HashMap<String, Object>();

	public ReceiptSummary(Element body) {
		 this.body = body;
		 paymentMethodValue = setValue(paymentMethodIdentifier);
		 totalAmountValue = setValueForAmount(totalAmountIdentifier);		 
		 try {
			 rideFareValue = setValueForAmount(rideFareIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + rideFareIdentifier + "] not found: " + e.getMessage());
		 }
		 try {
			 baseFareValue = setValueForAmount(baseFareIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + baseFareIdentifier + "] not found: " + e.getMessage());
		 }
		 try {
			 adjusmentMinFareValue = setValueForAmount(adjustmentMinFareIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + adjustmentMinFareIdentifier + "] not found: " + e.getMessage());
		 } 
		 try {
			 distanceAmountValue = setValueForAmount(distanceAmountIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + distanceAmountIdentifier + "] not found: " + e.getMessage());
		 } 
		 try {
			 timeAmountValue = setValueForAmount(timeAmountIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + timeAmountIdentifier + "] not found: " + e.getMessage());
		 }
		 try {
			 surgeChargesValue = setValueForAmount(surgeChargesIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + surgeChargesIdentifier + "] not found: " + e.getMessage());
		 }
		 try {
			 shareDiscountValue = setValueForAmount(shareDiscountIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + shareDiscountIdentifier + "] not found: " + e.getMessage());
		 }
		 try {
			 promotionAmountValue = setValueForAmount(promotionAmountIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + promotionAmountIdentifier + "] not found: " + e.getMessage());
		 }
	}
	
	private String setValue(String identifier) {
		String value = new String();
		value = body.select("tr:contains(" + identifier + ")").last().parent().select("td:contains(" + identifier + ")").select("span").first().text();
		setMap(identifier, value);
		return value;
	}
	
	private Double setValueForAmount(String identifier) {
		try {
			String value = new String();
			value = body.select("tr:contains(" + identifier + ")").last().parent().select("td:contains(" + identifier + ")").first().nextElementSibling().text();
			Double amount = Double.parseDouble(value.trim().replaceAll("[\\sP]", ""));
			setMap(identifier, amount);
			return amount;
		} catch (NumberFormatException e) {
			return (double) 0;
		}
	}
	
	public String getPaymentMethod() {
		return paymentMethodValue;
	}
	
	
	public Double getTotalAmount() {
		return totalAmountValue;
	}
	public Double getRideFare() {
		return rideFareValue;
	}
	
	public Double getBaseFare() {
		return baseFareValue;
	}
	
	public Double getAdjustmentMinFare() {
		return adjusmentMinFareValue;
	}
	
	public Double getDistanceAmount() {
		return distanceAmountValue;
	}
	
	public Double getTimeAmount() {
		return timeAmountValue;
	}
	
	public Double getSurgeCharges() {
		return surgeChargesValue;
	}
	
	public Double getShareDiscount() {
		return shareDiscountValue;
	}
	
	public Double getPromotionAmount() {
		return promotionAmountValue;
	}
	
	private void setMap(String key, Object value) {
		receiptSummary.put(key, value);
	}
	
	public Map<String, Object> getMap() {
		return receiptSummary;		
	}
}
