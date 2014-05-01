package fr.arolla.locness.billplus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//anemic
public class PricePlans {

	private final Map<Long, UserContract> contracts = new HashMap<Long, UserContract>();

	public void addUserContract(Long userId, UserContract contract) {
		contracts.put(userId, contract);
	}

	public UserContract findByUserid(Long userId) {
		return contracts.get(userId);
	}

	public Set<Long> allUserIds() {
		return contracts.keySet();
	}

	@Override
	public String toString() {
		return "PricePlans with " + contracts.size() + " user contracts";
	}

	// int textCount, int callTime
}
