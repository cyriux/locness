package fr.arolla.locness.billplusv2;

import static fr.arolla.locness.billplusv2.OptionCode.CCARD;
import static fr.arolla.locness.billplusv2.OptionCode.MULT;
import static fr.arolla.locness.billplusv2.OptionCode.REPO;

import java.util.Date;
import java.util.EnumSet;

public class BillPlusMain {

	public static void main(String[] args) {
		final AllContracts contracts = new AllContracts();
		contracts.addUserContract(123L, new Contract("John Doe", null, ContractCode.BASI, null));
		contracts.addUserContract(456L, new Contract("Paul Smith", null, ContractCode.LEV2, null));
		contracts
				.addUserContract(789L, new Contract("Rihanna", null, ContractCode.LEV1, EnumSet.of(MULT, REPO, CCARD)));
		contracts.addUserContract(1123L, new Contract("Britney", null, ContractCode.FLEXI_L, null));
		contracts.addUserContract(1456L, new Contract("Lana", null, ContractCode.BIZ1, EnumSet.of(REPO)));

		contracts.doBilling(new Date(), new BillingService("2"), new UserConsumption(26, 70));
	}

}
