package fr.arolla.locness.billplus;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class BillPlusMain {

	public static void main(String[] args) {
		PricePlans contracts = new PricePlans();
		UserContract contract = new UserContract();
		contract.setName("Jonh Doe");
		contract.setPlan("BASI");
		contracts.addUserContract(123L, contract);

		billingService(contracts);
	}

	public static void billingService(PricePlans contracts) {
		BillingManager manager = new BillingManager();
		for (Iterator iterator = contracts.allUserIds().iterator(); iterator
				.hasNext();) {
			Long userId = (Long) iterator.next();
			UserContract userContract = contracts.findByUserid(userId);
			Map<Date, Double> fees = manager.fees(
					userContract.getRegistrationDate(), userContract.getPlan(),
					0, userContract.getOptions(), "", 125);
			System.out.println(fees);
		}
	}

}
