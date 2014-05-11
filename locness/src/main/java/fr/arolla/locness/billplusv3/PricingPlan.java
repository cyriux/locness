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

		public Payment texts(Date paymentDate, int textCount) {
			return new Payment(paymentDate, currency, textRate * (textCount / 2));
		}

	},
	PREMIER(24.99, 120) {

	},
	VIP(44.99, 240) {

	};
	//LEVEL1(), LEVEL2(), FLEXI_L(), BIZ1();

	protected final Currency currency;
	protected final double monthlyFee;
	protected final int includedTime;
	protected final double textRate;
	protected final double overtimeRate;
	protected final EnumSet<Option> includedOptions;

	private PricingPlan(double monthlyFee, int includedTime) {
		this(Currency.getInstance("EUR"), monthlyFee, includedTime, 0.10, 0.35, null);
	}

	private PricingPlan(Currency currency, double monthlyFee, int includedTime, double textRate, double overtimeRate,
			EnumSet<Option> includedOptions) {
		this.currency = currency;
		this.monthlyFee = monthlyFee;
		this.includedTime = includedTime;
		this.textRate = textRate;
		this.overtimeRate = overtimeRate;
		this.includedOptions = includedOptions == null ? EnumSet.noneOf(Option.class) : includedOptions;
	}

	public PaymentSequence allPayments(Date paymentDate, UserConsumption consumption) {
		final Payment fee = fee(paymentDate);
		final Payment calls = calls(paymentDate, consumption.getCallTime());
		final Payment texts = texts(paymentDate, consumption.getTextCount());
		final PaymentSequence paymentSequence = new PaymentSequence(fee, calls, texts);

		final PaymentSequence optionsFees = optionsFees(paymentDate, consumption.getOptions());
		return paymentSequence.add(optionsFees);
	}

	public Payment texts(Date paymentDate, int textCount) {
		return new Payment(paymentDate, currency, textRate * textCount);
	}

	public Payment calls(Date paymentDate, int callTime) {
		final int overtime = Math.max(0, callTime - includedTime);
		return new Payment(paymentDate, currency, overtimeRate * overtime);
	}

	public Payment fee(Date paymentDate) {
		return new Payment(paymentDate, currency, monthlyFee);
	}

	public PaymentSequence optionsFees(Date paymentDate, Set<Option> options) {
		final List<Payment> payments = new ArrayList<Payment>();
		for (Option option : options) {
			payments.add(option.fee(paymentDate));
		}
		return new PaymentSequence(payments);
	}

}
