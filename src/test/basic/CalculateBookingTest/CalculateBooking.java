package test.basic.CalculateBookingTest;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.lambdalogic.test.booking.InvoiceRecipientBookingsCurrencyAmountsEvaluator;
import com.lambdalogic.test.booking.exception.InconsistentCurrenciesException;
import com.lambdalogic.test.booking.model.Booking;
import com.lambdalogic.test.booking.model.CurrencyAmount;
import com.lambdalogic.test.booking.model.Price;
import com.lambdalogic.test.booking.util.TypeHelper;

public class CalculateBooking {
	InvoiceRecipientBookingsCurrencyAmountsEvaluator evaluator;
	Long recipientPK;
	InconsistentCurrenciesException inconsistentException;
	
	@Rule
    public ExpectedException expectedException;

	@Before
	public void setUp() throws Exception {
		evaluator = new InvoiceRecipientBookingsCurrencyAmountsEvaluator(); 
		recipientPK = 1L;
		
		expectedException = ExpectedException.none();
	}

	@Test
	public void emptyBookingsTest() {
		List<Booking> bookings = new ArrayList<Booking>() {};
		try {
			evaluator.calculate(bookings, recipientPK);
		} catch (InconsistentCurrenciesException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void basicTest() throws InconsistentCurrenciesException, ParseException {
		List<Booking> bookings = new ArrayList<Booking>() {};
		CurrencyAmount currencyAmount = new CurrencyAmount(TypeHelper.toBigDecimal("0.12"), "usd");
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		evaluator.calculate(bookings, recipientPK);
		assertEquals(evaluator.getTotalAmount(), currencyAmount);
	}
	
	@Test(expected = Exception.class) 
	public void inconsistentCurrencyTest() throws InconsistentCurrenciesException, ParseException {
		List<Booking> bookings = new ArrayList<Booking>() {};
		inconsistentException = new InconsistentCurrenciesException(null, null);
		expectedException.expect(InconsistentCurrenciesException.class);
		
		CurrencyAmount currencyAmount = new CurrencyAmount(TypeHelper.toBigDecimal("0.12"), "usd");
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "eur", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		evaluator.calculate(bookings, recipientPK);
		assertEquals(evaluator.getTotalAmount(), currencyAmount);
	}

}
