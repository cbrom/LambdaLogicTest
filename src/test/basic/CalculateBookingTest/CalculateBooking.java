package test.basic.CalculateBookingTest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.lambdalogic.test.booking.InvoiceRecipientBookingsCurrencyAmountsEvaluator;
import com.lambdalogic.test.booking.exception.InconsistentCurrenciesException;
import com.lambdalogic.test.booking.model.Booking;

public class CalculateBooking {
	InvoiceRecipientBookingsCurrencyAmountsEvaluator evaluator;
	Long recipientPK;

	@Before
	public void setUp() throws Exception {
		evaluator = new InvoiceRecipientBookingsCurrencyAmountsEvaluator(); 
		recipientPK = 1L;
	}

	@Test
	public void emptyBookingsTest() {
		List<Booking> bookings = new ArrayList<Booking>() {};
		try {
			evaluator.calculate(bookings, recipientPK);
		} catch (InconsistentCurrenciesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
