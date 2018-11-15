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
	private Element body;
	private Map<String, Object> receiptSummary = new HashMap<String, Object>();

	public ReceiptSummary(Element body) {
		 this.body = body;
		 setValue(paymentMethodIdentifier);
		 setValue(totalAmountIdentifier);		 
		 try {
			 setValue(rideFareIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + rideFareIdentifier + "] not found: " + e.getMessage());
		 }
		 try {
			 setValue(baseFareIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + baseFareIdentifier + "] not found: " + e.getMessage());
		 }
		 try {
			 setValue(adjustmentMinFareIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + adjustmentMinFareIdentifier + "] not found: " + e.getMessage());
		 } 
		 try {
			 setValue(distanceAmountIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + distanceAmountIdentifier + "] not found: " + e.getMessage());
		 } 
		 try {
			 setValue(timeAmountIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + timeAmountIdentifier + "] not found: " + e.getMessage());
		 }
		 try {
			 setValue(surgeChargesIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + surgeChargesIdentifier + "] not found: " + e.getMessage());
		 }
		 try {
			 setValue(shareDiscountIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + shareDiscountIdentifier + "] not found: " + e.getMessage());
		 }
		 try {
			 setValue(promotionAmountIdentifier);
		 } catch (NullPointerException e) {
			 System.out.println("[" + promotionAmountIdentifier + "] not found: " + e.getMessage());
		 }
	}
	
	private void setValue(String identifier) {
		String value = new String();
		if (identifier.equals(paymentMethodIdentifier)) {
			value = body.select("tr:contains(" + identifier + ")").last().parent().select("td:contains(" + identifier + ")").select("span").first().text();
			setMap(identifier, value);
		} else {
			value = body.select("tr:contains(" + identifier + ")").last().parent().select("td:contains(" + identifier + ")").first().nextElementSibling().text();
			try {
				setMap(identifier, Double.parseDouble(value.trim().replaceAll("[\\sP]", "")));
			} catch (NumberFormatException e) {
				System.out.println("Error encountered when parsing data: " + e.getMessage());
			}			
		}
	}
	
	public String getPaymentMethod() {
		return (String) getMap().get(paymentMethodIdentifier);
	}
		
	public Double getTotalAmount() {
		return (Double) getMap().get(totalAmountIdentifier);
	}
	public Double getRideFare() {
		return (Double) getMap().get(rideFareIdentifier);
	}
	
	public Double getBaseFare() {
		return (Double) getMap().get(baseFareIdentifier);
	}
	
	public Double getAdjustmentMinFare() {
		return (Double) getMap().get(adjustmentMinFareIdentifier);
	}
	
	public Double getDistanceAmount() {
		return (Double) getMap().get(distanceAmountIdentifier);
	}
	
	public Double getTimeAmount() {
		return (Double) getMap().get(timeAmountIdentifier);
	}
	
	public Double getSurgeCharges() {
		return (Double) getMap().get(surgeChargesIdentifier);
	}
	
	public Double getShareDiscount() {
		return (Double) getMap().get(shareDiscountIdentifier);
	}
	
	public Double getPromotionAmount() {
		return (Double) getMap().get(promotionAmountIdentifier);
	}
	
	private void setMap(String key, Object value) {
		receiptSummary.put(key, value);
	}
	
	public Map<String, Object> getMap() {
		return receiptSummary;		
	}
}
