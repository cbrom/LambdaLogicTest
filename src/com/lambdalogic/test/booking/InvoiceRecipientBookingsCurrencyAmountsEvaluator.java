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

public class InvoiceRecipientBookingsCurrencyAmountsEvaluator implements IBookingsCurrencyAmountsEvaluator {
	
	private CurrencyAmount totalAmount;
	private CurrencyAmount totalPaidAmount;
	private CurrencyAmount totalOpenAmount;
	
	public void calculate(List<Booking> bookingList, Long invoiceRecipientID) throws InconsistentCurrenciesException {
		
		BigDecimal totalAmount = new BigDecimal(0);
		BigDecimal totalPaidAmount = new BigDecimal(0);
		BigDecimal totalOpenAmount = new BigDecimal(0);
		String currency = null;
		
		if (bookingList.size() > 0) {
			currency = bookingList.get(0).getCurrency();
		}
		
		//  initialize, check currency, check recipient id, throw error, rounding error
		for (Booking booking: bookingList) {
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
			
			if (booking.getInvoiceRecipientPK().equals(invoiceRecipientID)) {
				
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
		
		setTotalAmount(new CurrencyAmount(totalAmount.divide(Price.BD_100, 2, RoundingMode.HALF_UP), currency));
		setTotalOpenAmount(new CurrencyAmount(totalOpenAmount.divide(Price.BD_100, 2, RoundingMode.HALF_UP), currency));
		setTotalPaidAmount(new CurrencyAmount(totalPaidAmount.divide(Price.BD_100, 2, RoundingMode.HALF_UP), currency));
	}
	
	/**
	 * @return the totalAmount
	 */
	public CurrencyAmount getTotalAmount() {
		return totalAmount;
	}
	
	/**
	 * @param totalAmount the totalAmount to set
	 */
	public void setTotalAmount(CurrencyAmount totalAmount) {
		this.totalAmount = totalAmount;
	}
	/**
	 * @return the totalPiadAmount
	 */
	public CurrencyAmount getTotalPaidAmount() {
		return totalPaidAmount;
	}
	/**
	 * @param totalPiadAmount the totalPiadAmount to set
	 */
	public void setTotalPaidAmount(CurrencyAmount totalPiadAmount) {
		this.totalPaidAmount = totalPiadAmount;
	}
	/**
	 * @return the totalOpenAmount
	 */
	public CurrencyAmount getTotalOpenAmount() {
		return totalOpenAmount;
	}
	/**
	 * @param totalOpenAmount the totalOpenAmount to set
	 */
	public void setTotalOpenAmount(CurrencyAmount totalOpenAmount) {
		this.totalOpenAmount = totalOpenAmount;
	}
	
	public static void main(String[] args) {
		try {
			List<Booking> bookings = new ArrayList<Booking>() {};
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
