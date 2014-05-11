package fr.arolla.locness.billplusv3;

import static fr.arolla.locness.billplusv3.AffineRule.inEuro;
import static fr.arolla.locness.billplusv3.Option.CREDITCARD;
import static fr.arolla.locness.billplusv3.Option.MULTICALLS;
import static fr.arolla.locness.billplusv3.Option.PHONE_CALLS;
import static fr.arolla.locness.billplusv3.Option.REPORTING;
import static fr.arolla.locness.billplusv3.Option.TEXT_MESSAGE;

import java.util.Date;

public class Pricing {

	private final Option option;
	private final AffineRule rule;
	private final int extra;

	private final Pricing next;

	public final static Pricing DEFAULT_PRICING = Pricing
	/**/.of(PHONE_CALLS).are(inEuro().withFee(0).withRate(0.35).withIncluded(60))
	/**/.and(TEXT_MESSAGE).are(inEuro().withRate(0.10))
	/**/.and(MULTICALLS).are(inEuro().withRate(2.50))
	/**/.and(REPORTING).are(inEuro().withRate(2.00))
	/**/.and(CREDITCARD).are(inEuro().withRate(1.00));

	public final static Pricing of(Option option) {
		return new Pricing(option, AffineRule.inEuro(), 1, null);
	}

	public Pricing(Option option, AffineRule rule, int extra, Pricing next) {
		this.option = option;
		this.rule = rule;
		this.extra = extra;

		this.next = next;
	}

	public final Pricing are(AffineRule affineRule) {
		return new Pricing(this.option, affineRule, this.extra, this.next);
	}

	public final Pricing and(Option option) {
		return of(option).append(this);
	}

	public Pricing withExtra() {
		return new Pricing(this.option, this.rule, 2, this.next);
	}

	public Pricing append(final Pricing other) {
		return new Pricing(this.option, this.rule, this.extra, other);
	}

	public PaymentSequence allPayments(Date paymentDate, UserConsumption consumption) {
		final Payment payment = rule.payment(paymentDate, extra(value(consumption)));
		final PaymentSequence payments = new PaymentSequence(payment);
		if (next != null) {
			return payments.add(next.allPayments(paymentDate, consumption));
		}
		return payments;
	}

	private double extra(double value) {
		return (int) value / extra;
	}

	private double value(UserConsumption consumption) {
		if (option == Option.PHONE_CALLS) {
			return consumption.getCallTime();
		}
		if (option == Option.TEXT_MESSAGE) {
			return consumption.getTextCount();
		}
		return consumption.hasOptions(option) ? 1 : 0;
	}

	public String pricingReport() {
		return (next != null ? next.pricingReport() + "\n" : "") + option + "\t" + rule.pricingReport();
	}

	@Override
	public String toString() {
		return "Pricing of " + option + " " + rule;
	}

}
