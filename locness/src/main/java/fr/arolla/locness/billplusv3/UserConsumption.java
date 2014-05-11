package fr.arolla.locness.billplusv3;

import java.util.EnumSet;
import java.util.Set;

public class UserConsumption {

	private final int textCount;
	private final int callTime;
	private final Set<Option> options;

	public UserConsumption(int textCount, int callTime, Set<Option> options) {
		this.textCount = textCount;
		this.callTime = callTime;
		this.options = options == null ? EnumSet.noneOf(Option.class) : options;
	}

	public UserConsumption withOptions(Set<Option> options) {
		return new UserConsumption(this.textCount, this.callTime, options);
	}

	public int getTextCount() {
		return textCount;
	}

	public int getCallTime() {
		return callTime;
	}

	public boolean hasOptions(Option option) {
		return options.contains(option);
	}

}