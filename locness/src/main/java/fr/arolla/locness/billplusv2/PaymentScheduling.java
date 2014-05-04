package fr.arolla.locness.billplusv2;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public enum PaymentScheduling {

	MONTH_5, ANNIVERSARY_DATE() {
		int dayOfMonth(Date startDate, Date now) {
			final Calendar cal = new GregorianCalendar();
			cal.setTime(startDate);
			return cal.get(Calendar.DAY_OF_MONTH);
		}
	},
	ANNIVERSARY_DATE_EOM() {
		int dayOfMonth(Date startDate, Date now) {
			int dayOfMonth = ANNIVERSARY_DATE.dayOfMonth(startDate, now);
			final Calendar cal = toCalendar(now);
			return dayOfMonth >= 28 ? lastDayOfMonth(cal, -2) : dayOfMonth;
		}

	},
	ANNIVERSARY_DATE_EOM_UK() {
		int dayOfMonth(Date startDate, Date now) {
			int dayOfMonth = ANNIVERSARY_DATE.dayOfMonth(startDate, now);
			final Calendar cal = toCalendar(now);
			return dayOfMonth >= 28 ? lastDayOfMonth(cal, -3) : dayOfMonth;
		}

	};

	private static final int lastDayOfMonth(final Calendar cal2, int shift) {
		// note: could use cal.getActualMaximum(Calendar.DAY_OF_MONTH) - 1
		cal2.add(Calendar.MONTH, 1);
		cal2.set(Calendar.DATE, 1);
		cal2.add(Calendar.DATE, shift);
		return cal2.get(Calendar.DATE);
	}

	public Date paymentDate(Date startDate, Date now) {
		final Calendar cal = toCalendar(now);
		int dayOfMonth = dayOfMonth(startDate, now);
		if (cal.get(Calendar.DAY_OF_MONTH) >= dayOfMonth) {
			cal.roll(Calendar.MONTH, +1);
		}
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		return cal.getTime();
	}

	private final static Calendar toCalendar(Date date) {
		final Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.get(Calendar.MONTH);
		return cal;
	}

	int dayOfMonth(Date startDate, Date now) {
		return 5;
	}

}
