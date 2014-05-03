package fr.arolla.locness.billplus;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class BillPlusMain {

	public static void main(String[] args) {
		PricePlans contracts = new PricePlans();
		UserContract contract = new UserContract();
		contract.setName("John Doe");
		contract.setPlan("BASI");
		contracts.addUserContract(123L, contract);
		
		UserContract contract2 = new UserContract();
		contract2.setName("Paul Smith");
		contract2.setPayAsYouGoLevel("LEV2");
		contracts.addUserContract(456L, contract2);

		billingService(contracts);
	}

	public static void billingService(PricePlans contracts) {
		BillingManager manager = new BillingManager();
		for (Iterator<Long> iterator = contracts.allUserIds().iterator(); iterator
				.hasNext();) {
			Long userId = (Long) iterator.next();
			UserContract userContract = contracts.findByUserid(userId);
			Map<Date, Double> fees = manager.toBill(
					userContract.getRegistrationDate(), userContract.getPlan(),
					26, userContract.getOptions(),
					userContract.getPayAsYouGoLevel(), 70);
			System.out.println(fees);
		}
	}

}
