package fr.arolla.locness.billplus;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BillingManager {

	// Pricing Plans
	public final static String PLAN_BASIC = "BASI";
	public final static String PLAN_PREMIER = "PREMI";
	public final static String PLAN_VIP = "VIP";
	// Pay as you go : null

	// Pay as you go rates
	public final static String PAYG_LEVEL1 = "LEV1";
	public final static String PAYG_LEVEL2 = "LEV2";

	// Text bundles
	public final static String TEXT_BUNDLE_25 = "TX25";
	public final static String TEXT_BUNDLE_50 = "TX50";

	// Options
	public final static String MULTI_CALLS = "MULT";
	public final static String REPORT = "REPO";
	public final static String SINGLE_PAYMENT = "SING";

	// boolean insurance

	// compute when and how to bill the customer in the next 30 days
	public Map<Date, Double> fees(Date registrationDate, String plan,
			int textCount, String options, String payAsYouGoLevel, int callTime) {
		Date today = new Date();

		final Calendar cal = new GregorianCalendar();
		cal.setTime(today);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.get(Calendar.MONTH);

		Date paymentDate = null;

		if (registrationDate == null) {// pay on 5th of month
			if (cal.get(Calendar.DAY_OF_MONTH) < 5) {
				cal.set(Calendar.DAY_OF_MONTH, 5);
				paymentDate = cal.getTime();
			} else {
				cal.roll(Calendar.MONTH, +1);
				cal.set(Calendar.DAY_OF_MONTH, 5);
				paymentDate = cal.getTime();
			}
		} else {// anniversary date
			final Calendar cal2 = new GregorianCalendar();
			cal2.setTime(registrationDate);

			int dayOfMonth = cal2.get(Calendar.DAY_OF_MONTH);
			if (dayOfMonth >= 28) {
				dayOfMonth = 28; // protect against End of month
			}
			if (cal.get(Calendar.DAY_OF_MONTH) < dayOfMonth) {
				cal.set(Calendar.YEAR, dayOfMonth);
			} else {
				cal.roll(Calendar.MONTH, +1);
				cal.set(Calendar.YEAR, dayOfMonth);
			}
			paymentDate = cal.getTime();
		}

		Double amount = pricingPlan(plan);
		if (amount == null) {
			amount = 0.;
		}
		final Map<Date, Double> map = new HashMap<Date, Double>();
		map.put(paymentDate, amount);
		return map;
	}

	private Double pricingPlan(String plan) {
		final Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("billingconfig.properties");
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (plan != null) {
			if (PLAN_BASIC.equals(plan)) {
				return Double.parseDouble(prop.getProperty("plan.basic.fee"));
			}
			if (PLAN_PREMIER.equals(plan)) {
				Double.parseDouble(prop.getProperty("plan.premier.fee"));
			}
			if (PLAN_VIP.equals(plan)) {
				Double.parseDouble(prop.getProperty("plan.vip.fee"));
			}
		}
		return null;
	}
}
