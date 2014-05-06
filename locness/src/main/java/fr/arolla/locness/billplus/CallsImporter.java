package fr.arolla.locness.billplus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @author YoungWolf
 */
public class CallsImporter {

	private UserActivityStore calls = BillPlusMain.getUserActivityStore();

	// file format is a bit weird: user Ids are in rows, and each event is
	// in a different column with an Excel-like column name ('A', 'B', 'AA',
	// 'AB'...) to indicate the day (it does not always start with A)
	// Import only events after the cutoff day

	public void importCallsFile(String fileName, int cutoffDay, boolean callOrText) {
		if (calls == null) {
			throw new IllegalStateException("UserActivityStore is not initialized");
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String header = br.readLine();
			// analyze header with dates
			StringTokenizer st = new StringTokenizer(header, ",");
			int ignoreUntil = -1;
			int i = 0;
			while (st.hasMoreElements()) {
				st.nextToken();// skip first
			}
			while (st.hasMoreElements()) {
				String token = st.nextToken();
				int day = convertExcelFormatToDecimal(token);
				if (ignoreUntil == -1 && day >= cutoffDay) {
					// ignore before cutoffDay
					ignoreUntil = i;
				}
				i++;
			}
			String line = br.readLine();

			while (line != null) {
				Long userId = null;
				StringTokenizer st2 = new StringTokenizer(line, ",");
				if (st2.hasMoreElements()) {
					String token = st2.nextToken();
					userId = Long.parseLong(token);
				}
				int totalCallTime = 0;
				i = 0;
				while (st2.hasMoreElements()) {
					String token = st2.nextToken();
					// in case of SMS
					int call = Integer.parseInt(token);
					if (i >= ignoreUntil) {
						totalCallTime += call;
					}
					i++;
				}
				if (callOrText) {
					calls.addCallTime(userId, totalCallTime);
				} else {
					calls.addTextCount(userId, totalCallTime);
				}

				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// LLOC=97, McCabe=33
	private static int convertExcelFormatToDecimal(String code) {
		if (code == "A") {
			return 0;
		}
		if (code == "B") {
			return 1;
		}
		if (code == "C") {
			return 2;
		}
		if (code == "D") {
			return 3;
		}
		if (code == "E") {
			return 4;
		}
		if (code == "F") {
			return 5;
		}
		if (code == "G") {
			return 6;
		}
		if (code == "H") {
			return 7;
		}
		if (code == "I") {
			return 8;
		}
		if (code == "J") {
			return 9;
		}
		if (code == "K") {
			return 10;
		}
		if (code == "L") {
			return 11;
		}
		if (code == "M") {
			return 12;
		}
		if (code == "N") {
			return 13;
		}
		if (code == "O") {
			return 14;
		}
		if (code == "P") {
			return 15;
		}
		if (code == "Q") {
			return 16;
		}
		if (code == "R") {
			return 17;
		}
		if (code == "S") {
			return 18;
		}
		if (code == "T") {
			return 19;
		}
		if (code == "U") {
			return 20;
		}
		if (code == "V") {
			return 21;
		}
		if (code == "W") {
			return 22;
		}
		if (code == "X") {
			return 23;
		}
		if (code == "Y") {
			return 24;
		}
		if (code == "Z") {
			return 25;
		}

		if (code == "AA") {
			return 26;
		}
		if (code == "AB") {
			return 27;
		}
		if (code == "AC") {
			return 28;
		}
		if (code == "AD") {
			return 29;
		}
		if (code == "AE") {
			return 30;
		}
		if (code == "AF") {
			return 31;
		}

		// no need to go further than 31 (max 31 days in month!!)
		return 0;
	}
	
}
