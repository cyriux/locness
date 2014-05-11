package fr.arolla.locness.billplusv3;

import static org.junit.Assert.assertEquals;

import java.util.Currency;
import java.util.Date;

import org.junit.Test;

public class AffineRuleTest {

	private static Date paymentDate = new Date();

	private final static Currency currency = Currency.getInstance("EUR");
	
	private final static AffineRule pricing = new AffineRule(currency, 60, 0.45);
	private final static AffineRule pricingWithFee = new AffineRule(currency, 60, 0.45, 14.99);

	@Test
	public void nothing_billed_when_no_fee_nothing_consumed() {
		assertEquals(new Payment(paymentDate, currency, 0.), pricing.payment(paymentDate, 0));
	}

	@Test
	public void nothing_billed_when_below_included_quantity() {
		assertEquals(new Payment(paymentDate, currency, 0.), pricing.payment(paymentDate, 60));
	}

	@Test
	public void basic_plan_has_overtime_rate() {
		assertEquals(new Payment(paymentDate, currency, 0.45), pricing.payment(paymentDate, 61));
	}

	@Test
	public void fee_billed_when_nothing_consumed() {
		assertEquals(new Payment(paymentDate, currency, 14.99), pricingWithFee.payment(paymentDate, 0));
	}

	@Test
	public void fee_plus_rate_billed_when_overtime() {
		assertEquals(new Payment(paymentDate, currency, 14.99 + 10 * 0.45), pricingWithFee.payment(paymentDate, 70));
	}

}
