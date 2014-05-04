package fr.arolla.locness.billplusv2;

public enum OptionCode {

	MULT("multicalls"), REPO("report"), SING("singlepayment"), CCARD("creditcard");

	private final String name;

	private OptionCode(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
