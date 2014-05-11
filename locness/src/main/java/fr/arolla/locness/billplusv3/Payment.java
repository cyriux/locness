package fr.arolla.locness.billplusv3;

import java.util.Currency;
import java.util.Date;
import java.util.Iterator;

public final class Payment implements Comparable<Payment> {

	private final Date date;
	private final Currency currency;
	private final double amount;

	public Payment(Date date, String currencyCode, double amount) {
		this(date, Currency.getInstance(currencyCode), amount);
	}

	public Payment(Date date, Currency currency, double amount) {
		this.date = date;
		this.currency = currency;
		this.amount = amount;
		if (date == null || currency == null) {
			throw new IllegalArgumentException("date and currency must not be null");
		}
	}

	public Payment zero() {
		return new Payment(date, currency, 0);
	}

	public Date getDate() {
		return date;
	}

	public Currency getCurrency() {
		return currency;
	}

	public double getAmount() {
		return amount;
	}

	public Payment add(Payment other) {
		if (date.equals(other.date) && currency.equals(other.currency)) {
			return new Payment(date, currency, amount + other.amount);
		}
		throw new IllegalArgumentException("Cannot add payments on different date or currency");
	}

	public static Payment sum(Iterable<Payment> payments) {
		final Iterator<Payment> it = payments.iterator();
		if (!it.hasNext()) {
			throw new IllegalArgumentException("there must be at least one payment");
		}
		Payment total = it.next().zero();
		for (Payment other : payments) {
			total = total.add(other);
		}
		return total;
	}

	@Override
	public int hashCode() {
		return currency.hashCode() + date.hashCode() + (int) (Double.doubleToLongBits(amount));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Payment other = (Payment) obj;
		return currency == other.currency && date.equals(other.date)
				&& Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount);
	}

	public int compareTo(Payment other) {
		return date.compareTo(other.date);
	}

	@Override
	public String toString() {
		return "Payment at " + date + " " + currency + " " + amount;
	}

}
