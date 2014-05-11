package fr.arolla.locness.billplusv3;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public enum PricingPlan {

	BASIC(12.99, 60) {

	},
	HOULAHOUP(32.99, 240) {

		public PaymentSequence allPayments(Date paymentDate, UserConsumption consumption) {
			final Payment calls = callsPricing.fee(paymentDate, consumption.getCallTime());
			final Payment texts = textPricing.fee(paymentDate, consumption.getTextCount() / 2);
			final PaymentSequence paymentSequence = new PaymentSequence(calls, texts);

			final PaymentSequence optionsFees = optionsFees(paymentDate, consumption.getOptions());
			return paymentSequence.add(optionsFees);
		}

	},
	PREMIER(24.99, 120) {

	},
	VIP(44.99, 240) {

	};
	//,LEVEL1(), LEVEL2(), FLEXI_L(), BIZ1();

	protected final AffinePricing callsPricing;
	protected final AffinePricing textPricing;

	protected final EnumSet<Option> includedOptions;

	private PricingPlan(double monthlyFee, int includedTime) {
		this(Currency.getInstance("EUR"), monthlyFee, includedTime, 0.10, 0.35, null);
	}

	private PricingPlan(Currency currency, double monthlyFee, int includedTime, double textRate, double overtimeRate,
			EnumSet<Option> includedOptions) {
		this.callsPricing = new AffinePricing(currency, includedTime, overtimeRate, monthlyFee);
		this.textPricing = new AffinePricing(currency, 0, textRate, 0);
		this.includedOptions = includedOptions == null ? EnumSet.noneOf(Option.class) : includedOptions;
	}

	public PaymentSequence allPayments(Date paymentDate, UserConsumption consumption) {
		final Payment calls = callsPricing.fee(paymentDate, consumption.getCallTime());
		final Payment texts = textPricing.fee(paymentDate, consumption.getTextCount());
		final PaymentSequence paymentSequence = new PaymentSequence(calls, texts);

		final PaymentSequence optionsFees = optionsFees(paymentDate, consumption.getOptions());
		return paymentSequence.add(optionsFees);
	}

	public PaymentSequence optionsFees(Date paymentDate, Set<Option> options) {
		final List<Payment> payments = new ArrayList<Payment>();
		for (Option option : options) {
			payments.add(option.fee(paymentDate));
		}
		return new PaymentSequence(payments);
	}

	public String pricingReport() {
		return name() + "\tcalls: " + callsPricing.pricingReport() + "\ttexts: " + textPricing.pricingReport();
	}

}

