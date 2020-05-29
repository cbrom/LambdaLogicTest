package com.lambdalogic.test.booking;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.lambdalogic.test.booking.exception.InconsistentCurrenciesException;
import com.lambdalogic.test.booking.model.Booking;
import com.lambdalogic.test.booking.model.CurrencyAmount;
import com.lambdalogic.test.booking.model.Price;
import com.lambdalogic.test.booking.util.TypeHelper;

/**
 * This program implements the {@link IBookingsCurrencyAmountsEvaluator} interface specified in this package
 * 
 * @author Cbrom
 * @version 1.0
 * @since 2019-05-28
 *
 */

public class InvoiceRecipientBookingsCurrencyAmountsEvaluator implements IBookingsCurrencyAmountsEvaluator {
	
	/**
	 * Total gross amount of the list of bookings. (set when {@link #calculate(List, Long)} is called) 
	 */
	protected CurrencyAmount totalAmount;
	
	/**
	 * Total paid amount of the list of bookings. (set when {@link #calculate(List, Long)} is called)
	 */
	protected CurrencyAmount totalPaidAmount;
	
	/**
	 * Total open(unpaid) amount of the list of bookings. (set when {@link #calculate(List, Long)} is called)
	 */
	protected CurrencyAmount totalOpenAmount;
	
	/**
	 * Add up the total amount(gross), paid amount, and open amount of the list of bookings and store them in the object instance.
	 * The main price, add1price, add2price and paid amount are all multiplied by 100 to avoid summing up rounding errors.
	 * BigDecimal values totalAmount, totalPaidAmount and total Open amount are always initialized to 0
	 * Currency is initialized to null and set with the first value of booking in <code>bookingList</code>
	 * <p></p>
	 * <b>Conditions</b>
	 * <p> -<code>invoiceRecipientID</code> matches ( {@link Booking#getInvoiceRecipientPK()} )</p>
	 * <p> - all bookings have the same ( {@link Booking#getCurrency()()} ) values</p>
	 * <p> - both ( {@link Booking#getTotalAmount()())} ) and ( {@link Booking#getPaidAmount()()())})'s values are not zero.</p>
	 * 
	 * @see IBookingsCurrencyAmountsEvaluator#calculate(List, Long)
	 */
	public void calculate(List<Booking> bookingList, Long invoiceRecipientID) throws InconsistentCurrenciesException {
		
		// initialize values to 0
		BigDecimal totalAmount = new BigDecimal(0);
		BigDecimal totalPaidAmount = new BigDecimal(0);
		BigDecimal totalOpenAmount = new BigDecimal(0);
		String currency = null;
		
		if (bookingList.size() > 0) {
			currency = bookingList.get(0).getCurrency();
		}
		
		for (Booking booking: bookingList) {
			multiplyB100(booking);
			
			if (booking.getInvoiceRecipientPK().equals(invoiceRecipientID)) {
				if  (
						!(booking.getTotalAmount().equals(Price.ZERO) && 
						!(booking.getPaidAmount().equals(Price.ZERO)))
				) {
					String currentCurrency = booking.getCurrency();
					

					if (currency.equals(currentCurrency)) {
						totalPaidAmount = totalPaidAmount.add(booking.getPaidAmount());
						totalAmount = totalAmount.add(booking.getTotalAmountGross());
						totalOpenAmount = totalOpenAmount.add(booking.getOpenAmount());
						
					} else {
						throw new InconsistentCurrenciesException(currency, currentCurrency);
					}
				}
				
				
			}
		}
		
		// convert back to their original values (divide by 100)
		setTotalAmount(new CurrencyAmount(totalAmount.divide(Price.BD_100, 2, RoundingMode.HALF_UP), currency));
		setTotalOpenAmount(new CurrencyAmount(totalOpenAmount.divide(Price.BD_100, 2, RoundingMode.HALF_UP), currency));
		setTotalPaidAmount(new CurrencyAmount(totalPaidAmount.divide(Price.BD_100, 2, RoundingMode.HALF_UP), currency));
	}
	
	/**
	 * Multiplies the main price, add1price, add2price and paid amount by 100.
	 * This avoids the summing up the rounding errors generated in each booking instance.
	 * @param booking - a {@link Booking} instance.
	 */
	public void multiplyB100(Booking booking) {
		Price mainPrice = new Price();
		mainPrice.copyFrom(booking.getMainPrice());
		mainPrice.setAmount(mainPrice.getAmount().multiply(Price.BD_100));
		booking.setMainPrice(mainPrice);
		
		Price price1 = new Price();
		price1.copyFrom(booking.getAdd1Price());
		price1.setAmount(price1.getAmount().multiply(Price.BD_100));
		booking.setAdd1Price(price1);
		
		Price price2 = new Price();
		price2.copyFrom(booking.getAdd2Price());
		price2.setAmount(price2.getAmount().multiply(Price.BD_100));
		booking.setAdd2Price(price2);
		
		booking.setPaidAmount(booking.getPaidAmount().multiply(Price.BD_100));
	}
	
	/**
	 * {@inheritDoc}
	 * @return - totalAmount
	 */
	public CurrencyAmount getTotalAmount() {
		return totalAmount;
	}
	
	/**
	 * Sets the <code>totalAmount</code> property.
	 * @param totalAmount - the totalAmount to set
	 */
	public void setTotalAmount(CurrencyAmount totalAmount) {
		this.totalAmount = totalAmount;
	}
	/**
	 * {@inheritDoc}
	 * @return - totalPaidAmount
	 */
	public CurrencyAmount getTotalPaidAmount() {
		return totalPaidAmount;
	}
	/**
	 * Sets the <code>toalPaidAmount</code> property.
	 * @param totalPiadAmount - the totalPiadAmount to set
	 */
	public void setTotalPaidAmount(CurrencyAmount totalPiadAmount) {
		this.totalPaidAmount = totalPiadAmount;
	}
	/**
	 * {@inheritDoc}
	 * @return - totalOpenAmount
	 */
	public CurrencyAmount getTotalOpenAmount() {
		return totalOpenAmount;
	}
	/**
	 * Sets the <code>totalOpenAmount</code> property.
	 * @param totalOpenAmount - the totalOpenAmount to set
	 */
	public void setTotalOpenAmount(CurrencyAmount totalOpenAmount) {
		this.totalOpenAmount = totalOpenAmount;
	}
	
	public static void main(String[] args) {
		try {
			List<Booking> bookings = new ArrayList<Booking>() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;};
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
			InvoiceRecipientBookingsCurrencyAmountsEvaluator ev = new InvoiceRecipientBookingsCurrencyAmountsEvaluator(); 
			try {
				ev.calculate(bookings, 1L);
				System.out.println(ev.getTotalAmount());
				System.out.println(ev.getTotalOpenAmount());
				System.out.println(ev.getTotalPaidAmount());
			} catch (InconsistentCurrenciesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
