package fr.arolla.locness.billplusv2;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

public class Contract {

	private final String name;

	private final Date registrationDate;
	private final ContractCode contractCode;
	private final Set<OptionCode> options;

	public Contract(String name, Date registrationDate, ContractCode contractCode, Set<OptionCode> options) {
		this.name = name;
		this.registrationDate = registrationDate;
		this.contractCode = contractCode;
		this.options = options == null ? EnumSet.noneOf(OptionCode.class) : options;
	}

	public String getName() {
		return name;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public ContractCode getContractCode() {
		return contractCode;
	}

	public boolean hasOption(OptionCode code) {
		return options.contains(code);
	}

	public Set<OptionCode> getOptions() {
		return options;
	}

	@Override
	public String toString() {
		return "Contract of " + name + " " + contractCode + " from " + registrationDate + " " + options + ")";
	}

}
