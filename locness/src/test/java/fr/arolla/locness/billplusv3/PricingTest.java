package fr.arolla.locness.billplusv3;

import static fr.arolla.locness.billplusv3.AffineRule.inEuro;
import static fr.arolla.locness.billplusv3.Option.PHONE_CALLS;
import static fr.arolla.locness.billplusv3.Option.CREDITCARD;
import static fr.arolla.locness.billplusv3.Option.MULTICALLS;
import static fr.arolla.locness.billplusv3.Option.REPORTING;
import static fr.arolla.locness.billplusv3.Option.TEXT_MESSAGE;

import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

public class PricingTest {

	private static final Date paymentDate = new Date();
	private static final Currency eur = Currency.getInstance("EUR");

	@Test
	public void acceptance_test() {
		final Pricing p = Pricing
		/**/.of(PHONE_CALLS).are(inEuro().withFee(12.99).withRate(0.35).withIncluded(60))
		/**/.and(TEXT_MESSAGE).are(inEuro().withRate(0.10)).withExtra()
		/**/.and(MULTICALLS).are(inEuro().withRate(2.50))
		/**/.and(REPORTING).are(inEuro().withRate(2.00))
		/**/.and(CREDITCARD).are(inEuro().withRate(1.00));

		System.out.println(p.pricingReport());

		final UserConsumption consumption = new UserConsumption(27, 70, EnumSet.of(MULTICALLS, REPORTING, CREDITCARD));
		Assert.assertEquals(new PaymentSequence(new Payment(paymentDate, eur, 12.99 + 3.5 + 1.3 + 5.50)), p
				.allPayments(paymentDate, consumption).netted());
	}
}
