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

		UserContract contract3 = new UserContract();
		contract3.setName("Rihanna");
		contract3.setPayAsYouGoLevel("LEV1");
		contract3.setOptions("REPO;MULT;CCARD");
		contracts.addUserContract(789L, contract3);

		UserContract contract4 = new UserContract();
		contract4.setName("Britney");
		contract4.setPlan("FLXX");
		contracts.addUserContract(1123L, contract4);
		
		UserContract contract5 = new UserContract();
		contract5.setName("Lana");
		contract5.setPlan("BIZ1");
		contracts.addUserContract(1123L, contract5);

		billingService(contracts, "FR");
	}

	public static void billingService(PricePlans contracts, String country) {
		BillingManager manager = new BillingManager();
		for (Iterator<Long> iterator = contracts.allUserIds().iterator(); iterator.hasNext();) {
			Long userId = (Long) iterator.next();
			UserContract userContract = contracts.findByUserid(userId);
			Map<Date, Double> fees = manager.toBill(userContract.getRegistrationDate(), userContract.getPlan(), 26,
					userContract.getOptions(), userContract.getPayAsYouGoLevel(), 70, country);
			System.out.println(fees);
		}
	}

}
