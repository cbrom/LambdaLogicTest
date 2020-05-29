# LambdaLogicTest
This is a java ```IRecipientBookingsCurrencyAmountsEvaluator``` interface implementation with 
```InvoiceRecipientBookingsCurrencyAmountsEvaluator``` for lambda logic short project test.

## Usage

```java
InvoiceRecipientBookingsCurrencyAmountsEvaluator evaluator = new InvoiceRecipientBookingsCurrencyAmountsEvaluator(); 
List<Booking> bookings = new ArrayList<Booking>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;};
bookings.add(new Booking(1L, new Price(TypeHelper.toBigDecimal("0.10"), "usd", TypeHelper.toBigDecimal("19"), false), 
null, null, null, TypeHelper.toBigDecimal("0.09"), null, null, null, 1L, null));
evaluator.calculate(bookings, recipientPK);

```

## Implementation Details
```InvoiceRecipientBookingsCurrencyAmountsEvaluator``` implements four methods of the parent interface.
- calculate
- getTotalAmount
- getPaidAmount
- getOpenAmount
The latter three methods are used to retrieve resultes calculated with the ```calculate``` method.

### Problem
The ```calculate``` method should add up the total amount, the paid amount and open amount of a list of Bookings
for the given invoice recipient ID satisfying the requirements and conditions provided below:
 - Implementation does not mix-up gross and net amounts
 - minimize rounding errors
 - ignore bookings that doesn't belong to the given invoice recipient PK
 - ignore bookings with both paid amount and total amount of 0
 - throw exception if any two relevant bookings have different currencies.
### Solution
The implementation assumes that:
 - ```totalAmount``` is the total gross amount of list of bookings.
 - ```totalPaidAmount``` is the total paid amount of list of bookings.
 - ```totalOpenAmount``` is the total open(unpaid) amount of list of bookings.

Given (from the classes) that main price, add price 1, add price 2 and paid amounts are rounded to two decimal points,
multiplying them by 100 would make the decimal points ```.00```. This multiplication helps us avoid adding up rounding errors
because multiplying two BigDecimal objects with the first one ```.00``` decimal points and ```.ab```(two significant figures) decimal
points will result in a BigDecimal of two significant figures. Therefore the original value of calcultions in gross, net, and 
tax amount would be preserved.

## License
[MIT](https://choosealicense.com/licenses/mit/)
