package fr.arolla.locness.billplusv3;

import java.util.Date;

public class Contract {

	private final String name;

	private final Date registrationDate;
	private final PaymentScheduling scheduling;
	private final PricingPlan pricingPlan;

	public Contract(String name, Date registrationDate, PaymentScheduling scheduling, PricingPlan plan) {
		this.name = name;
		this.registrationDate = registrationDate;
		this.scheduling = scheduling;
		this.pricingPlan = plan;
	}

	public String getName() {
		return name;
	}

	public PaymentSequence allPayments(Date billDate, UserConsumption consumption) {
		final Date paymentDate = scheduling.paymentDate(registrationDate, billDate);
		return pricingPlan.allPayments(paymentDate, consumption);
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public PricingPlan getPricingPlan() {
		return pricingPlan;
	}

	@Override
	public String toString() {
		return "Contract of " + name + " " + pricingPlan + " from " + registrationDate + ")";
	}

}
