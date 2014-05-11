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
	public void basic_plan_has_monthly_fee_on_payment_date() {
		assertEquals(eurPaymentOnPaymentDate(12.99), BASIC.fee(paymentDate));
	}

	@Test
	public void basic_plan_has_calls_time_included() {
		assertEquals(eurPaymentOnPaymentDate(0), BASIC.calls(paymentDate, 50));
	}

	@Test
	public void basic_plan_has_overtime_rate() {
		assertEquals(eurPaymentOnPaymentDate(0.35), BASIC.calls(paymentDate, 61));
	}

	@Test
	public void basic_plan_has_texts_rate() {
		assertEquals(eurPaymentOnPaymentDate(2.60), BASIC.texts(paymentDate, 26));
	}

	@Test
	public void premier_plan_has_monthly_fee_on_payment_date() {
		assertEquals(eurPaymentOnPaymentDate(24.99), PREMIER.fee(paymentDate));
	}

	@Test
	public void premier_plan_has_lot_of_included_time() {
		assertEquals(eurPaymentOnPaymentDate(0), PREMIER.calls(paymentDate, 120));
	}

	@Test
	public void premier_plan_has_texts_rate() {
		assertEquals(eurPaymentOnPaymentDate(2.60), PREMIER.texts(paymentDate, 26));
	}

	@Test
	public void vip_plan_has_monthly_fee_on_payment_date() {
		assertEquals(eurPaymentOnPaymentDate(44.99), VIP.fee(paymentDate));
	}

	@Test
	public void vip_plan_has_included_time() {
		assertEquals(eurPaymentOnPaymentDate(0), VIP.calls(paymentDate, 240));
	}

	@Test
	public void vip_plan_has_texts_rate() {
		assertEquals(eurPaymentOnPaymentDate(2.60), VIP.texts(paymentDate, 26));
	}

	@Test
	public void houlahoup_plan_has_monthly_fee_on_payment_date() {
		assertEquals(eurPaymentOnPaymentDate(32.99), HOULAHOUP.fee(paymentDate));
	}

	@Test
	public void houlahoup_plan_has_included_time() {
		assertEquals(eurPaymentOnPaymentDate(0), HOULAHOUP.calls(paymentDate, 240));
	}

	@Test
	public void houlahoup_plan_has_one_text_free_for_each_text() {
		assertEquals(eurPaymentOnPaymentDate(1.30), HOULAHOUP.texts(paymentDate, 27));
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
