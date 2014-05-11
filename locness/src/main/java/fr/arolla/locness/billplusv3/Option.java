package fr.arolla.locness.billplusv3;

import java.util.Date;

public enum Option {
	MULTICALLS(2.50), REPORTING(2.), CREDITCARD(1.);

	private final double fee;

	private Option(double fee) {
		this.fee = fee;
	}

	public Payment fee(Date paymentDate) {
		return new Payment(paymentDate, "EUR", fee);
	}

}
