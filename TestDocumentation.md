| Test Case Name  | emptyBookingsTest |
|---|---|
|  Module to be tested | InvoiceRecipientBookingsCurrencyAmountsEvaluator.calculate()  |
|  Assumptions | None |
|  Test Data | Empty list of bookings  |
|  Test Steps | Create empty list of bookings <br/> Create target class object <br/> Cun calculate method |
|  Expected results | no error |
|  Result | pass |


| Test Case Name  |  basicTest |
|---|---|
|  Module to be tested | InvoiceRecipientBookingsCurrencyAmountsEvaluator.calculate()  |
|  Assumptions | None |
|  Test Data | List of bookings with one booking <br/> Net amount = ```0.10```, Paid amount = ```0.09```  |
|  Test Steps | Create empty list of bookings <br/> Create target class object <br/> Add one booking specified above <br/> Run calculate method |
|  Expected results | Total amount of ```0.12``` |
|  Result | pass |


| Test Case Name  |  inconsistentCurrencyTest |
|---|---|
|  Module to be tested | InvoiceRecipientBookingsCurrencyAmountsEvaluator.calculate()  |
|  Assumptions | None |
|  Test Data | List of bookings with two bookings <br/> Net amount = ```0.10```, Paid amount = ```0.09```, Currency = ```usd``` <br/> <br/> Net amount = ```0.10```, Paid amount = ```0.09```, Currency = ```eur```  |
|  Test Steps | Create empty list of bookings <br/> Create target class object <br/> Add two bookings specified above <br/> Run calculate method|
|  Expected results | InconsistentCurrenciesException |
|  Result | pass |


| Test Case Name  |  roundErrorsTest |
|---|---|
|  Module to be tested | InvoiceRecipientBookingsCurrencyAmountsEvaluator.calculate()  |
|  Assumptions | None |
|  Test Data | List of bookings with ten similar bookings <br/> Net amount = ```0.10```, Paid amount = ```0.09```  |
|  Test Steps | Create empty list of bookings <br/> Create target class object <br/> Add ten bookings specified above <br/> Run calculate method |
|  Expected results | getTotalAmount() = ```1.19``` <br/> getTotalOpenAmount = ```0.29``` <br/> getTotalPaidAmount() = ```0.90``` |
|  Result | pass |


| Test Case Name  |  differentInvoiceRecipientTest |
|---|---|
|  Module to be tested | InvoiceRecipientBookingsCurrencyAmountsEvaluator.calculate()  |
|  Assumptions | None |
|  Test Data | List of bookings with nine similar bookings <br/> Net amount = ```0.10```, Paid amount = ```0.09```, invoiceRecipientPK = ```1L``` <br/>  Add one booking <br/> Net amount = ```0.10```, Paid amount = ```0.09```, invoiceRecipientPK = ```2L``` |
|  Test Steps | Create empty list of bookings <br/> Create target class object <br/> Add ten bookings specified above <br/> Run calculate method with invoiceRecipientID = ```1L``` |
|  Expected results | getTotalAmount() = ```1.07``` <br/> getTotalOpenAmount = ```0.26``` <br/> getTotalPaidAmount() = ```0.81``` |
|  Result | pass |


| Test Case Name  |  smallTaxRateTest |
|---|---|
|  Module to be tested | InvoiceRecipientBookingsCurrencyAmountsEvaluator.calculate()  |
|  Assumptions | None |
|  Test Data |  List of bookings with ten similar bookings <br/> Net amount = ```10```, Paid amount = ```0.09```, taxRate=```0.19``` |
|  Test Steps | Create empty list of bookings <br/> Create target class object <br/> Add ten bookings specified above <br/> Run calculate method |
|  Expected results | getTotalAmount() = ```100.19``` |
|  Result | pass |


| Test Case Name  |  zeroPaidAndTotalAmountsTest |
|---|---|
|  Module to be tested | InvoiceRecipientBookingsCurrencyAmountsEvaluator.calculate()  |
|  Assumptions | None |
|  Test Data | List of bookings with nine similar bookings <br/> Net amount = ```0.10```, Paid amount = ```0.09``` <br/>  Add one booking <br/> Net amount = ```0```, Paid amount = ```0``` |
|  Test Steps | Create empty list of bookings <br/> Create target class object <br/> Add ten bookings specified above <br/> Run calculate method |
|  Expected results | etTotalAmount() = ```1.07``` <br/> getTotalOpenAmount = ```0.26``` <br/> getTotalPaidAmount() = ```0.81``` |
|  Result | pass |
