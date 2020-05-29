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


/**
 * Test cases for  {@link InvoiceRecipientBookingsCurrencyAmountsEvaluator} implementation
 * 
 * @author Cbrom
 * @version 1.0
 * @since 2019-05-28
 *
 */

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

	/**
	 * Test {@link InvoiceRecipientBookingsCurrencyAmountsEvaluator#calculate(List, Long)} method with an empty bookingList.
	 * @throws InconsistentCurrenciesException
	 */
	@Test
	public void emptyBookingsTest() throws InconsistentCurrenciesException {
		List<Booking> bookings = new ArrayList<Booking>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		evaluator.calculate(bookings, recipientPK);
	}
	
	/**
	 * Test {@link InvoiceRecipientBookingsCurrencyAmountsEvaluator#calculate(List, Long)} method with one booking. 	
	 * @throws InconsistentCurrenciesException
	 * @throws ParseException
	 */
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
		
		assertEquals(currencyAmount, evaluator.getTotalAmount());
	}
	
	/**
	 * Test with {@link InvoiceRecipientBookingsCurrencyAmountsEvaluator#calculate(List, Long)} method with bookingList with different currencies.
	 * @throws InconsistentCurrenciesException
	 * @throws ParseException
	 */
	@Test(expected = InconsistentCurrenciesException.class) 
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
		assertEquals(currencyAmount, evaluator.getTotalAmount());
	}
	
	/**
	 * Test {@link InvoiceRecipientBookingsCurrencyAmountsEvaluator#calculate(List, Long)} for summing up rounded values.
	 * @throws InconsistentCurrenciesException
	 * @throws ParseException
	 */
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
			
		assertEquals(totalAmountCurrency, evaluator.getTotalAmount());
		assertEquals(totalOpenCurrency, evaluator.getTotalOpenAmount());
		assertEquals(totalPaidCurrency, evaluator.getTotalPaidAmount());
	}
	
	/**
	 * Test {@link InvoiceRecipientBookingsCurrencyAmountsEvaluator#calculate(List, Long)} with different <code>InvoiceRecipientID</code> values.
	 * @throws InconsistentCurrenciesException
	 * @throws ParseException
	 */
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
		
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 2L, null));
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
			
		assertEquals(totalAmountCurrency, evaluator.getTotalAmount());
		assertEquals(totalOpenCurrency, evaluator.getTotalOpenAmount());
		assertEquals(totalPaidCurrency, evaluator.getTotalPaidAmount());
	}
	
	
	/**
	 * Test the effect of small tax value in {@link InvoiceRecipientBookingsCurrencyAmountsEvaluator#calculate(List, Long)} method.
	 * @throws InconsistentCurrenciesException
	 * @throws ParseException
	 */
	@Test
	public void smallTaxRateTest() throws InconsistentCurrenciesException, ParseException {
		List<Booking> bookings = new ArrayList<Booking>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		CurrencyAmount totalAmountCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("100.19"), "usd");
		
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
			
		assertEquals(totalAmountCurrency, evaluator.getTotalAmount());
	}
	
	@Test
	public void zeroPaidAndTotalAmountsTest() throws InconsistentCurrenciesException, ParseException {
		List<Booking> bookings = new ArrayList<Booking>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
		CurrencyAmount totalAmountCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("1.07"), "usd");
		CurrencyAmount totalOpenCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("0.26"), "usd");
		CurrencyAmount totalPaidCurrency = new CurrencyAmount(TypeHelper.toBigDecimal("0.81"), "usd");
		
		bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0"), "usd", TypeHelper.toBigDecimal("19"), false), null, null, null, TypeHelper.toBigDecimal("0"), null, null, null, 1L, null));
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
			
		assertEquals(totalAmountCurrency, evaluator.getTotalAmount());
		assertEquals(totalOpenCurrency, evaluator.getTotalOpenAmount());
		assertEquals(totalPaidCurrency, evaluator.getTotalPaidAmount());
	}

}
