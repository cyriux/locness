package fr.arolla.locness.billplusv2;

public enum ContractCode {

	BASI("Basic"), PREMI(""), HOUH("HoulaHoup"), VIP("VIP"), LEV1("Level1"), LEV2("Level2");

	private String name;

	// TEXT_BUNDLE_25("TX25"), TEXT_BUNDLE_50("TX50"),

	// MULTI_CALLS("MULT"), REPORT("REPO"), SINGLE_PAYMENT("SING"), CREDIT_CARD(
	// "CCARD");

	private ContractCode(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
