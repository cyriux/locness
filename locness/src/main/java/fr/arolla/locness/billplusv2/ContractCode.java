package fr.arolla.locness.billplusv2;

public enum ContractCode {

	BASI("Basic"), PREMI(""), HOUH("HoulaHoup"), VIP("VIP"), LEV1("Level1"), LEV2("Level2"), FLEXI_S("Flexi.small"), FLEXI_L(
			"Flexi.large"), FLEXI_XL("Flexi.extra"), BIZ1("BizPro1"), BIZ2("BizPro2"), BIZ3("BizPro3");

	private String name;

	private ContractCode(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
