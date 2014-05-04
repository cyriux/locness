package fr.arolla.locness.billplusv2;

import static fr.arolla.locness.billplusv2.PaymentScheduling.ANNIVERSARY_DATE_EOM;
import static fr.arolla.locness.billplusv2.PaymentScheduling.MONTH_5;

import java.util.Currency;
import java.util.Date;
import java.util.Set;

public class BillingService {

	private final Config config;

	public BillingService() {
		config = new Config("billingconfig2");
	}

	public PaymentSequence toBill(Contract contract, Date billingDate, UserConsumption usage) {
		final Date registrationDate = contract.getRegistrationDate();
		final PaymentScheduling scheduling = registrationDate == null ? MONTH_5 : ANNIVERSARY_DATE_EOM;
		final Date paymentDate = scheduling.paymentDate(registrationDate, billingDate);

		// for each payment
		final double amount = billFee(contract, billingDate, usage) + billCalls(contract, billingDate, usage)
				+ billTexts(contract, billingDate, usage) + billOptions(contract, billingDate, usage);

		return new PaymentSequence(newPayment(paymentDate, amount, contract));
	}

	protected Payment newPayment(final Date paymentDate, final double amount, Contract contract) {
		final double commission = commission(contract);
		double rounded = Math.round((amount) * 100.) / 100. + commission;
		return new Payment(paymentDate, Currency.getInstance("EUR"), rounded);
	}

	protected double commission(Contract contract) {
		return contract.hasOption(OptionCode.CCARD) ? config.getCommission(OptionCode.CCARD.getName().toLowerCase(),
				0.0) : 0.;
	}

	protected double billFee(Contract contract, Date billingDate, UserConsumption usage) {
		return config.getFee(codeOf(contract), 0.);
	}

	protected double billCalls(Contract contract, Date billingDate, UserConsumption usage) {
		final double overtimeRate = config.getRate("overtime", 0.50);
		final double time = config.getTime(codeOf(contract), 0);
		final double rate = config.getRate(codeOf(contract), overtimeRate);
		final double billableTime = Math.max(usage.getCallTime() - time, 0);
		return billableTime * rate;
	}

	protected double billTexts(Contract contract, Date billingDate, UserConsumption usage) {
		final double textRate = config.getRate("text", 0.10);
		return ContractCode.HOUH == contract.getContractCode() ? usage.getTextCount() / 2 * textRate : usage
				.getTextCount() * textRate;
	}

	protected double billOptions(Contract contract, Date billingDate, UserConsumption usage) {
		double optionsBill = 0;
		final Set<OptionCode> builtInOptions = config.getOptionCodes(codeOf(contract));
		for (OptionCode option : contract.getOptions()) {
			if (!builtInOptions.contains(option)) {
				final String optionCode = option.getName().toLowerCase();
				optionsBill += config.getFee(optionCode, 0.0);
			}
		}
		return optionsBill;
	}

	protected static String codeOf(Contract contract) {
		return contract.getContractCode().getName().toLowerCase();
	}

}
