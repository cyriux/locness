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
	public final static String PLAN_HOULAHOUP = "HOUH";
	public final static String PLAN_VIP = "VIP";
	// Pay as you go : null

	public final static String FLEXI_S = "FLXS";
	public final static String FLEXI_L = "FLXL";
	public final static String FLEXI_XL = "FLXX";

	public final static String BIZ1 = "BIZ1";
	public final static String BIZ2 = "BIZ2";
	public final static String BIZ3 = "BIZ3";

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
	private String country;

	// boolean insurance

	public Map<Date, Double> toBill(Date registrationDate, String plan, int textCount, String options,
			String payAsYouGoLevel, int callTime, String country) {
		System.out.println("Starting billing for plan: " + plan + " texts counts: " + textCount + " call time: "
				+ callTime);
		System.out.println("Country: " + country);
		this.country = country;
		Map<Date, Double> fees = new HashMap<Date, Double>();
		try {
			if (plan != null && plan.startsWith("FLX")) {
				fees = flexi(registrationDate, plan, textCount, options, payAsYouGoLevel, callTime);
			} else if (payAsYouGoLevel != null) {
				fees = payAsYouGo(registrationDate, payAsYouGoLevel, textCount, options, callTime);
			} else {
				fees = monthlyFee(registrationDate, plan, textCount, options, callTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (plan != null && plan.startsWith("BIZ")) {
				addMultiCallsOption(options, fees, true);
			} else {
				addMultiCallsOption(options, fees, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			fees = addCreditCardCommission(fees, options);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Done billing: " + fees);
		return fees;
	}

	private Map<Date, Double> flexi(Date registrationDate, String plan, int textCount, String options,
			String payAsYouGoLevel, int callTime) {
		final Properties prop = loadProperties();

		double amount = 0;
		double rate = 0;
		double fee = 0;
		int builtInTime = 0; // mn
		if (FLEXI_S.equals(plan)) {
			rate = Double.parseDouble(prop.getProperty("flexi.small.rate"));
			fee = Double.parseDouble(prop.getProperty("flexi.small.fee"));
			builtInTime = 15;
		}
		if (FLEXI_L.equals(plan)) {
			rate = Double.parseDouble(prop.getProperty("flexi.large.rate"));
			fee = Double.parseDouble(prop.getProperty("flexi.large.fee"));
			builtInTime = 30;
			if (country.equals("UK")) {
				builtInTime = 25;
			}
		}
		if (FLEXI_XL.equals(plan)) {
			rate = Double.parseDouble(prop.getProperty("flexi.extra.rate"));
			fee = Double.parseDouble(prop.getProperty("flexi.extra.fee"));
			builtInTime = 45;
			if (country.equals("UK")) {
				builtInTime = 40;
			}
		}
		int extraTime = callTime - builtInTime;
		if (extraTime < 0) {
			extraTime = 0;
		}
		if (country.equals("UK")) {
			amount = fee + extraTime * rate + textCount * 0.15;
		} else {
			amount = fee + extraTime * rate + textCount * 0.10;
		}
		Date paymentDate = getPaymentDate(registrationDate);

		final Map<Date, Double> map = new HashMap<Date, Double>();
		double total = Math.round((amount) * 100.) / 100.;
		map.put(paymentDate, total);
		return map;
	}

	private Map<Date, Double> addCreditCardCommission(Map<Date, Double> fees, String options) {
		if (options == null || options.length() == 0) {
			return fees;
		}
		boolean found = false;
		String[] optionArray = options.split(";");
		for (int i = 0; i < optionArray.length; i++) {
			if ("CCARD".equalsIgnoreCase(optionArray[i])) {
				found = true;
			}
		}
		if (found && fees.size() >= 1) {
			for (Date date : fees.keySet()) {
				Double amount = fees.get(date);
				final Properties prop = loadProperties();
				double creditCardCommission = Double.parseDouble(prop.getProperty("creditcardcommission"));
				fees.put(date, amount + creditCardCommission);
			}
		} else {
			return fees;
		}
		return fees;
	}

	private Map<Date, Double> payAsYouGo(Date registrationDate, String payAsYouGoLevel, int textCount, String options,
			int callTime) {
		final Properties prop = loadProperties();

		double amount = 0;
		double rate = 0;
		double fee = 0;
		if (PAYG_LEVEL1.equals(payAsYouGoLevel)) {
			rate = Double.parseDouble(prop.getProperty("paysasyougo.level1.rate"));
		}
		if (PAYG_LEVEL2.equals(payAsYouGoLevel)) {
			rate = Double.parseDouble(prop.getProperty("paysasyougo.level2.rate"));
			fee = Double.parseDouble(prop.getProperty("paysasyougo.level2.fee"));
		}
		if (country.equals("UK")) {
			amount = fee + callTime * rate + textCount * 0.15;
		} else {
			amount = fee + callTime * rate + textCount * 0.10;
		}
		Date paymentDate = getPaymentDate(registrationDate);

		final Map<Date, Double> map = new HashMap<Date, Double>();
		double total = Math.round((amount) * 100.) / 100.;
		map.put(paymentDate, total);
		return map;
	}

	/**
	 * if option MULTICALLS is selected, get the amount and add it to the next
	 * payment
	 */
	private void addMultiCallsOption(String options, Map<Date, Double> fees, boolean biz) {
		double optionalFee = 0;
		if (options == null || options.length() == 0) {
			return;
		}
		String[] optionArray = options.split(";");

		// multi-calls option
		final Properties prop = loadProperties();
		for (int i = 0; i < optionArray.length; i++) {
			if (MULTI_CALLS.equalsIgnoreCase(optionArray[i])) {
				optionalFee = Double.parseDouble(prop.getProperty("multicalls.fee"));
			}
		}
		// report option
		if (!biz) {
			for (int i = 0; i < optionArray.length; i++) {
				if (REPORT.equalsIgnoreCase(optionArray[i])) {
					optionalFee += Double.parseDouble(prop.getProperty("report.fee"));
				}
			}
		}

		// add the option fees to the regular fee payment
		Date date = null;
		if (fees.size() == 1) {
			date = fees.keySet().iterator().next();
			Double amount = fees.get(date);
			fees.put(date, amount + optionalFee);
		} else {
			// should never happen
			final Calendar cal = new GregorianCalendar();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.get(Calendar.MONTH);

			fees.put(date, optionalFee);
		}
	}

	public Map<Date, Double> monthlyFee(Date registrationDate, String plan, int textCount, String options, int callTime) {
		Date paymentDate = getPaymentDate(registrationDate);

		Double monthlyFee = pricingPlan(plan);
		if (monthlyFee == null) {
			monthlyFee = 0.;
		}

		Double overtimeAmount = null;
		if (PLAN_HOULAHOUP.equals(plan)) {
			overtimeAmount = overtimeAmount(plan, callTime) + (textCount / 2 * 0.10);
			// no houla houp for UK, never
		} else {
			if (country.equals("UK")) {
				overtimeAmount = overtimeAmount(plan, callTime) + textCount * 0.15;
			} else {
				overtimeAmount = overtimeAmount(plan, callTime) + textCount * 0.10;
			}
		}

		final Map<Date, Double> map = new HashMap<Date, Double>();
		double total = Math.round((monthlyFee + overtimeAmount) * 100.) / 100.;
		map.put(paymentDate, total);
		return map;
	}

	public Date getPaymentDate(Date registrationDate) {
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
				// protect against End of month
				dayOfMonth = lastDayOfMonth(cal2);
			}
			if (cal.get(Calendar.DAY_OF_MONTH) < dayOfMonth) {
				cal.set(Calendar.YEAR, dayOfMonth);
			} else {
				cal.roll(Calendar.MONTH, +1);
				cal.set(Calendar.YEAR, dayOfMonth);
			}
			paymentDate = cal.getTime();
		}
		return paymentDate;
	}

	public int lastDayOfMonth(final Calendar cal2) {
		// note: could use cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1
		cal2.add(Calendar.MONTH, 1);
		cal2.set(Calendar.DATE, 1);
		if (country.equals("UK")) {
			cal2.add(Calendar.DATE, -3);
		}else {			
			cal2.add(Calendar.DATE, -2);
		}
		return cal2.get(Calendar.DATE);
	}

	private Double pricingPlan(String plan) {
		final Properties prop = loadProperties();

		if (plan != null) {
			if (PLAN_BASIC.equals(plan)) {
				return Double.parseDouble(prop.getProperty("plan.basic.fee"));
			}
			if (PLAN_PREMIER.equals(plan)) {
				Double.parseDouble(prop.getProperty("plan.premier.fee"));
			}
			if (PLAN_HOULAHOUP.equals(plan)) {
				Double.parseDouble(prop.getProperty("plan.houlahoup.fee"));
			}
			if (PLAN_VIP.equals(plan)) {
				Double.parseDouble(prop.getProperty("plan.vip.fee"));
			}
			// BIZ
			if (BIZ1.equals(plan)) {
				return Double.parseDouble(prop.getProperty("plan.biz1.fee"));
			}
			if (BIZ2.equals(plan)) {
				Double.parseDouble(prop.getProperty("plan.biz2.fee"));
			}
			if (BIZ3.equals(plan)) {
				Double.parseDouble(prop.getProperty("plan.biz3.fee"));
			}
		}
		return null;
	}

	private Double overtimeAmount(String plan, int callTime) {
		final Properties prop = loadProperties();
		if (callTime == 0) {
			return 0.;
		}

		final double overtimeRate = Double.parseDouble(prop.getProperty("overtime.rate"));
		if (plan != null) {
			if (PLAN_BASIC.equals(plan)) {
				return overtimeAmount("plan.basic.time", callTime, prop, overtimeRate);
			}
			if (PLAN_PREMIER.equals(plan)) {
				return overtimeAmount("plan.premier.time", callTime, prop, overtimeRate);
			}
			if (PLAN_VIP.equals(plan)) {
				return overtimeAmount("plan.vip.time", callTime, prop, overtimeRate);
			}

			// BIZ
			if (BIZ1.equals(plan)) {
				return overtimeAmount("plan.biz1.time", callTime, prop, overtimeRate);
			}
			if (BIZ2.equals(plan)) {
				return overtimeAmount("plan.biz2.time", callTime, prop, overtimeRate);
			}
			if (BIZ3.equals(plan)) {
				return overtimeAmount("plan.biz3.time", callTime, prop, overtimeRate);
			}

		}
		return null;
	}

	public Double overtimeAmount(String planName, int callTime, final Properties prop, final double overtimeRate) {
		int overtime = callTime - Integer.parseInt(prop.getProperty(planName));
		if (overtime < 0) {
			return 0.;
		}
		return overtimeRate * overtime;
	}

	public Properties loadProperties() {
		final Properties prop = new Properties();
		InputStream input = null;
		try {
			if (country.equals("UK")) {
				input = new FileInputStream("billingconfig.uk.properties");
			} else {
				input = new FileInputStream("billingconfig.properties");
			}
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
		return prop;
	}

}
