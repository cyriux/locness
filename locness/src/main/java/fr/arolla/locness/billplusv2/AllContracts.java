package fr.arolla.locness.billplusv2;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AllContracts {

	private final Map<Long, Contract> contracts = new HashMap<Long, Contract>();

	public void addUserContract(Long userId, Contract contract) {
		contracts.put(userId, contract);
	}

	public void doBilling(Date billingDate, BillingService service, UserConsumption userConsumption) {
		for (Entry<Long, Contract> entry : contracts.entrySet()) {
			System.out.println(service.toBill(entry.getValue(), billingDate, userConsumption));
		}
	}

	@Override
	public String toString() {
		return "All Contracts with " + contracts.size() + " user contracts";
	}

}
