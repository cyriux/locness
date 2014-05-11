package fr.arolla.locness.billplusv3;

import static fr.arolla.locness.billplusv3.AffineRule.inEuro;
import static fr.arolla.locness.billplusv3.Option.CREDITCARD;
import static fr.arolla.locness.billplusv3.Option.MULTICALLS;
import static fr.arolla.locness.billplusv3.Option.PHONE_CALLS;
import static fr.arolla.locness.billplusv3.Option.REPORTING;
import static fr.arolla.locness.billplusv3.Option.TEXT_MESSAGE;

import java.util.Date;

public enum PricingPlan {

	BASIC(fixedPlan(12.99, 60)),
	/**/HOULAHOUP(fixedPlan(32.99, 60)) {
	// texts /2;
	},
	/**/PREMIER(fixedPlan(24.99, 60)),
	/**/VIP(fixedPlan(44.99, 60)),
	/**/LEVEL1(payAsYouGo(0, 0.50)),
	/**/LEVEL2(payAsYouGo(15, 0.25)),
	/**/FLEXI_L(fixedPlan(7.50, 15, 0.40)),
	/**/BIZ1(fixedPlan(16.99, 60, 0.35, REPORTING));

	private final Pricing pricing;

	private PricingPlan(Pricing pricing) {
		this.pricing = pricing;
	}

	private static Pricing fixedPlan(double fee, int time, double rate, Option freeOption) {
		return Pricing
		/**/.of(PHONE_CALLS).are(inEuro().withFee(fee).withRate(rate).withIncluded(time))
		/**/.and(TEXT_MESSAGE).are(inEuro().withRate(0.10))
		/**/.and(MULTICALLS).are(inEuro().withRate(freeOption == MULTICALLS ? 0 : 2.50))
		/**/.and(REPORTING).are(inEuro().withRate(freeOption == REPORTING ? 0 : 2.00))
		/**/.and(CREDITCARD).are(inEuro().withRate(freeOption == CREDITCARD ? 0 : 1.00));
	}

	private static Pricing fixedPlan(double fee, int time, double rate) {
		return fixedPlan(fee, time, rate, null);
	}

	private static Pricing fixedPlan(double fee, int time) {
		return fixedPlan(fee, time, 0.35, null);
	}

	private static Pricing payAsYouGo(double fee, double rate) {
		return fixedPlan(fee, 0, rate, null);
	}

	public PaymentSequence allPayments(Date paymentDate, UserConsumption consumption) {
		return pricing.allPayments(paymentDate, consumption);
	}

	public String pricingReport() {
		return name() + ":\n" + pricing.pricingReport();
	}

}
