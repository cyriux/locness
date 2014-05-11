package fr.arolla.locness.billplusv3;

import static fr.arolla.locness.billplusv3.PricingPlan.BASIC;
import static fr.arolla.locness.billplusv3.PricingPlan.HOULAHOUP;
import static fr.arolla.locness.billplusv3.PricingPlan.PREMIER;
import static fr.arolla.locness.billplusv3.PricingPlan.VIP;
import static fr.arolla.locness.billplusv3.Option.CREDITCARD;
import static fr.arolla.locness.billplusv3.Option.MULTICALLS;
import static fr.arolla.locness.billplusv3.Option.REPORTING;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

public class PricingPlanTest {

	private static Date paymentDate = new Date();

	private static Payment eurPaymentOnPaymentDate(double amount) {
		return new Payment(paymentDate, "EUR", amount);
	}

	@Test
	public void pricing_by_contract() {
		assertEquals("VIP	calls: EUR 44.99	240	0.35	texts: EUR 0.0	0	0.1", VIP.pricingReport());
		assertEquals("PREMIER	calls: EUR 24.99	120	0.35	texts: EUR 0.0	0	0.1", PREMIER.pricingReport());
		assertEquals("BASIC	calls: EUR 12.99	60	0.35	texts: EUR 0.0	0	0.1", BASIC.pricingReport());
		assertEquals("HOULAHOUP	calls: EUR 32.99	240	0.35	texts: EUR 0.0	0	0.1", HOULAHOUP.pricingReport());
	}

	@Test
	public void option_multicalls() {
		assertEquals(eurPaymentOnPaymentDate(2.50), MULTICALLS.fee(paymentDate));
	}

	@Test
	public void option_reporting() {
		assertEquals(eurPaymentOnPaymentDate(2.00), REPORTING.fee(paymentDate));
	}

	@Test
	public void option_creditcard() {
		assertEquals(eurPaymentOnPaymentDate(1.00), CREDITCARD.fee(paymentDate));
	}

	@Test
	public void plan_with_no_included_option() {
		// assertEquals(new Payment(paymentDate, "EUR", 4.50),
		// BASIC.optionsFees(paymentDate, EnumSet.of(MULT, REPO, CCARD)));
	}

}
