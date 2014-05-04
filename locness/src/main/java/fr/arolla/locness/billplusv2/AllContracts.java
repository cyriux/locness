package fr.arolla.locness.billplusv2;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

	private final static Set<OptionCode> parseOptions(String options) {
		final Set<OptionCode> optionSet = EnumSet.noneOf(OptionCode.class);
		final String[] optionArray = options == null ? new String[0] : options.split(";");
		for (String token : optionArray) {
			optionSet.add(OptionCode.valueOf(token));
		}
		return optionSet;
	}

}
