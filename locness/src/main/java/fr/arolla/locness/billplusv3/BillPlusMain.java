package fr.arolla.locness.billplusv3;

import static fr.arolla.locness.billplusv3.Option.CREDITCARD;
import static fr.arolla.locness.billplusv3.Option.MULTICALLS;
import static fr.arolla.locness.billplusv3.Option.REPORTING;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

public class BillPlusMain {

	public static void main(String[] args) {
		final AllContracts contracts = new AllContracts();
		final PaymentScheduling month5 = PaymentScheduling.MONTH_5;
		contracts.addUserContract(123L, new Contract("John Doe", null, month5, PricingPlan.BASIC, null));
		contracts.addUserContract(456L, new Contract("Paul Smith", null, month5, PricingPlan.LEVEL2, null));

		final Set<Option> options = EnumSet.of(MULTICALLS, REPORTING, CREDITCARD);
		contracts.addUserContract(789L, new Contract("Rihanna", null, month5, PricingPlan.LEVEL1, options));
		contracts.addUserContract(1123L, new Contract("Britney", null, month5, PricingPlan.FLEXI_L, null));
		contracts.addUserContract(1456L, new Contract("Lana", null, month5, PricingPlan.BIZ1, EnumSet.of(REPORTING)));

		contracts.doBilling(new Date(), new UserConsumption(26, 70, null));
	}

}
