package fr.arolla.locness.billplusv2;

public class UserConsumption {
	
	private final int textCount;
	private final int callTime;

	public UserConsumption(int textCount, int callTime) {
		this.textCount = textCount;
		this.callTime = callTime;
	}

	public int getTextCount() {
		return textCount;
	}

	public int getCallTime() {
		return callTime;
	}

}