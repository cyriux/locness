package fr.arolla.locness.billplusv3;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class AffineRule {

	private final Currency currency;
	private final double included;
	private final double rate;
	private final double fee;

	public final static AffineRule inEuro() {
		return new AffineRule(Currency.getInstance("EUR"), 0, 0, 0);
	}

	public AffineRule(Currency currency, double included, double rate) {
		this(currency, included, rate, 0.);
	}

	public AffineRule(Currency currency, double included, double rate, double fee) {
		this.currency = currency;
		this.included = included;
		this.rate = rate;
		this.fee = fee;
	}

	public AffineRule withCurrency(Currency currency) {
		return new AffineRule(currency, included, rate, fee);
	}

	public AffineRule withFee(double fee) {
		return new AffineRule(currency, included, rate, fee);
	}

	public AffineRule withIncluded(double included) {
		return new AffineRule(currency, included, rate, fee);
	}

	public AffineRule withRate(double rate) {
		return new AffineRule(currency, included, rate, fee);
	}

	public Payment payment(Date paymentDate, double consumed) {
		return new Payment(paymentDate, currency, fee + rate * Math.max(0, consumed - included));
	}

	public String pricingReport() {
		final NumberFormat nf = DecimalFormat.getInstance(Locale.ENGLISH);
		DecimalFormat df = (DecimalFormat) nf;
		df.applyPattern("##0.00");
		return currency + " " + df.format(fee) + "\t" + df.format(included) + "\t" + df.format(rate);
	}
}