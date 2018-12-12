package model;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.jsoup.nodes.Element;

public class ReceiptSummary {
	public final String RECEIPT_SUMMARY_LABEL = "Receipt Summary";
	public final String TOTAL_FARE_LABEL = "TOTAL"; //	Total fare
	public static final List<String> RECEIPT_SUMMARY_SUBLABELS = Arrays.asList("Payment Method", "Ride Fare", "Base Fare", "Adjustment for Min Fare", "Distance", "Time", "Surge Charges", "Share Discount", "Promotion");
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
	private Row row;
	private Map<String, Object> receiptSummary = new HashMap<String, Object>();
	
	public ReceiptSummary() { }

	public ReceiptSummary(Element body) {
		 this.body = body;
		 setValueFromHtml(paymentMethodIdentifier);
		 setValueFromHtml(totalAmountIdentifier);
		 setValueFromHtml(rideFareIdentifier);
		 setValueFromHtml(baseFareIdentifier);
		 setValueFromHtml(adjustmentMinFareIdentifier);
		 setValueFromHtml(distanceAmountIdentifier);
		 setValueFromHtml(timeAmountIdentifier);
		 setValueFromHtml(surgeChargesIdentifier);
		 setValueFromHtml(shareDiscountIdentifier);
		 setValueFromHtml(promotionAmountIdentifier);
	}
	
	public ReceiptSummary(Row row) {
		this.row = row;
		setValueFromRow(paymentMethodIdentifier);
		setValueFromRow(totalAmountIdentifier);
		setValueFromRow(rideFareIdentifier);
		setValueFromRow(baseFareIdentifier);
		setValueFromRow(adjustmentMinFareIdentifier);
		setValueFromRow(distanceAmountIdentifier);
		setValueFromRow(timeAmountIdentifier);
		setValueFromRow(surgeChargesIdentifier);
		setValueFromRow(shareDiscountIdentifier);
		setValueFromRow(promotionAmountIdentifier);
	}
	
	private void setValueFromHtml(String identifier) {
		String value = new String();
		if (identifier.equals(paymentMethodIdentifier)) {
			value = body.select("tr:contains(" + identifier + ")").last().parent().select("td:contains(" + identifier + ")").select("span").first().text();
			setMap(identifier, value);
		} else {
			try {
				value = body.select("tr:contains(" + identifier + ")").last().parent().select("td:contains(" + identifier + ")").first().nextElementSibling().text();
				setMap(identifier, Double.parseDouble(value.trim().replaceAll("[\\sP]", "")));
			} catch (NullPointerException | NumberFormatException e) {
				System.err.println("[" + identifier + "] not found: " + e.getMessage());
				setMap(identifier, null);
			}			
		}
	}
	
	private void setValueFromRow(String identifier) {
		if (identifier.equals(totalAmountIdentifier)) {
			setMap(identifier, row.getCell(1).getNumericCellValue());
		} else {
			int index = RECEIPT_SUMMARY_SUBLABELS.indexOf(identifier) + BookingDetails.BOOKING_DETAILS_SUBLABELS.size() + 2;
			try {
				CellType cellType = row.getCell(index, MissingCellPolicy.RETURN_NULL_AND_BLANK).getCellTypeEnum();
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
			} catch (NullPointerException e) {
				System.err.println("Error encountered when reading data: " + e.getMessage());
				setMap(identifier, null);
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
	
	public String identifierAtIndex(int index) {
		return RECEIPT_SUMMARY_SUBLABELS.get(index);
	}
}
