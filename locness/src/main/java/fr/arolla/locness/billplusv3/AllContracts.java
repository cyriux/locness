package fr.arolla.locness.billplusv3;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AllContracts {

	private final Map<Long, Contract> contracts = new HashMap<Long, Contract>();

	public void addUserContract(Long userId, Contract contract) {
		contracts.put(userId, contract);
	}

	public void doBilling(Date billingDate, UserConsumption userConsumption) {
		for (Entry<Long, Contract> entry : contracts.entrySet()) {
			System.out.println(entry.getValue().allPayments(billingDate, userConsumption));
		}
	}

	@Override
	public String toString() {
		return "All Contracts with " + contracts.size() + " user contracts";
	}

}
