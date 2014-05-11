package fr.arolla.locness.billplusv3;

import org.junit.Test;

public class PricingPlanTest {

	@Test
	public void pricing_report_for_all_plans() {
		for (PricingPlan plan : PricingPlan.values()) {
			System.out.println(plan.pricingReport() + "\n");
		}
	}

}
