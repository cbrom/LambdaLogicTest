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
	public void emptyBookingsTest() throws InconsistentCurrenciesException {
		List<Booking> bookings = new ArrayList<Booking>() {};
		evaluator.calculate(bookings, recipientPK);
	}
	
	@Test
	public void basicTest() throws InconsistentCurrenciesException, ParseException {
		List<Booking> bookings = new ArrayList<Booking>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		CurrencyAmount currencyAmount = new CurrencyAmount(TypeHelper.toBigDecimal("0.12"), "usd");
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		evaluator.calculate(bookings, recipientPK);
		
		assertNotNull(evaluator.getTotalAmount());
		assertNotNull(evaluator.getTotalOpenAmount());
		assertNotNull(evaluator.getTotalPaidAmount());
		
		assertEquals(evaluator.getTotalAmount(), currencyAmount);
	}
	
	@Test(expected = Exception.class) 
	public void inconsistentCurrencyTest() throws InconsistentCurrenciesException, ParseException {
		List<Booking> bookings = new ArrayList<Booking>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		inconsistentException = new InconsistentCurrenciesException(null, null);
		expectedException.expect(InconsistentCurrenciesException.class);
		
		CurrencyAmount currencyAmount = new CurrencyAmount(TypeHelper.toBigDecimal("0.12"), "usd");
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "eur", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		evaluator.calculate(bookings, recipientPK);
		assertEquals(evaluator.getTotalAmount(), currencyAmount);
	}
	
	@Test
	public void roundErrorsTest() throws InconsistentCurrenciesException, ParseException {
		List<Booking> bookings = new ArrayList<Booking>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		CurrencyAmount totalAmountCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("1.19"), "usd");
		CurrencyAmount totalOpenCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("0.29"), "usd");
		CurrencyAmount totalPaidCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("0.90"), "usd");
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		
		evaluator.calculate(bookings, recipientPK);
			
		assertEquals(evaluator.getTotalAmount(), totalAmountCurrency);
		assertEquals(evaluator.getTotalOpenAmount(), totalOpenCurrency);
		assertEquals(evaluator.getTotalPaidAmount(), totalPaidCurrency);
	}
	
	@Test
	public void differentInvoiceRecipientTest() throws InconsistentCurrenciesException, ParseException {
		List<Booking> bookings = new ArrayList<Booking>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		CurrencyAmount totalAmountCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("1.07"), "usd");
		CurrencyAmount totalOpenCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("0.26"), "usd");
		CurrencyAmount totalPaidCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("0.81"), "usd");
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 2L, null));
		
		evaluator.calculate(bookings, recipientPK);
			
		assertEquals(evaluator.getTotalAmount(), totalAmountCurrency);
		assertEquals(evaluator.getTotalOpenAmount(), totalOpenCurrency);
		assertEquals(evaluator.getTotalPaidAmount(), totalPaidCurrency);
	}
	
	@Test
	public void smallTaxRateTest() throws InconsistentCurrenciesException, ParseException {
		List<Booking> bookings = new ArrayList<Booking>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		CurrencyAmount totalAmountCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("100.19"), "usd");
		CurrencyAmount totalOpenCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("0.29"), "usd");
		CurrencyAmount totalPaidCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("0.90"), "usd");
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("10"), "usd", TypeHelper.toBigDecimal("0.19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("10"), "usd", TypeHelper.toBigDecimal("0.19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("10"), "usd", TypeHelper.toBigDecimal("0.19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("10"), "usd", TypeHelper.toBigDecimal("0.19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("10"), "usd", TypeHelper.toBigDecimal("0.19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("10"), "usd", TypeHelper.toBigDecimal("0.19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("10"), "usd", TypeHelper.toBigDecimal("0.19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("10"), "usd", TypeHelper.toBigDecimal("0.19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("10"), "usd", TypeHelper.toBigDecimal("0.19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("10"), "usd", TypeHelper.toBigDecimal("0.19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
		
		evaluator.calculate(bookings, recipientPK);
			
		assertEquals(evaluator.getTotalAmount(), totalAmountCurrency);
	}

}
