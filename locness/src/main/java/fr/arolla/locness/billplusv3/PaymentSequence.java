package fr.arolla.locness.billplusv3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PaymentSequence implements Iterable<Payment> {

	private final List<Payment> payments;

	public static final PaymentSequence EMPTY = new PaymentSequence();

	public PaymentSequence(Payment... payments) {
		this(Arrays.asList(payments));
	}

	public PaymentSequence(List<Payment> payments) {
		this.payments = payments;
		Collections.sort(payments);
	}

	public int size() {
		return payments.size();
	}

	public Iterator<Payment> iterator() {
		return payments.iterator();
	}

	public PaymentSequence add(PaymentSequence other) {
		final List<Payment> merged = new ArrayList<Payment>(this.payments);
		merged.addAll(other.payments);
		return new PaymentSequence(merged);
	}

	@Override
	public String toString() {
		return "PaymentSequence:" + payments;
	}
}
