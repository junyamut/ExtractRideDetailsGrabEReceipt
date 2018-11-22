package helper;

import java.util.Comparator;

import model.EReceipt;

public class SortByRideDate implements Comparator<EReceipt> {

	public SortByRideDate() { }

	@Override
	public int compare(EReceipt o1, EReceipt o2) {
		return o1.getBookingDetails().getPickUpTime().compareTo(o2.getBookingDetails().getPickUpTime());
	}

}
