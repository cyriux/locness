package fr.arolla.locness.billplusv3;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

public class Contract {

	private final String name;

	private final Date registrationDate;
	private final PaymentScheduling scheduling;
	private final PricingPlan pricingPlan;
	
	private final Set<Option> options;

	public Contract(String name, Date registrationDate, PaymentScheduling scheduling, PricingPlan plan,
			Set<Option> options) {
		this.name = name;
		this.registrationDate = registrationDate;
		this.scheduling = scheduling;
		this.pricingPlan = plan;
		this.options = options == null ? EnumSet.noneOf(Option.class) : options;
	}

	public PaymentSequence allPayments(Date billDate, UserConsumption consumption) {
		final Date paymentDate = scheduling.paymentDate(registrationDate, billDate);
		return pricingPlan.allPayments(paymentDate, consumption.withOptions(options)).netted();
	}

	@Override
	public String toString() {
		return "Contract of " + name + " " + pricingPlan + " from " + registrationDate + ")";
	}

}
