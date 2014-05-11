package fr.arolla.locness.billplusv3;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.apache.poi.hssf.util.CellReference;

/**
 * @author OldEnthusiast
 */
public class ImportUserConsumption {

	private final UserConsumptionTracking tracking;
	private final boolean callOrText;

	public ImportUserConsumption(UserConsumptionTracking tracking, boolean callOrText) {
		this.tracking = tracking;
		this.callOrText = callOrText;
	}

	// file format is a bit weird: user Ids are in rows, and each event is
	// in a different column with an Excel-like column name ('A', 'B', 'AA',
	// 'AB'...) to indicate the day (it does not always start with A)
	// Import only events after the cutoff day

	public void importCallsFile(String fileName, int cutoffDay) {
		try (Scanner scanner = new Scanner(new FileReader(fileName))) {
			parseFile(fileName, cutoffDay, scanner);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void parseFile(String fileName, int cutoffDay, Scanner scanner) {
		final int index = cutoffIndex(scanner.nextLine(), cutoffDay);
		while (scanner.hasNextLine()) {
			final String line = scanner.nextLine();
			parseLine(index, line);
		}
	}

	protected void parseLine(final int index, final String line) {
		int totalCallTime = 0;
		final String[] tokens = line.split(",");
		final Long userId = Long.parseLong(tokens[0]);
		for (int i = index; i < tokens.length; i++) {
			totalCallTime += Integer.parseInt(tokens[i]);
		}
		store(userId, totalCallTime);
	}

	protected void store(Long userId, int totalCallTime) {
		if (callOrText) {
			tracking.addCallTime(userId, totalCallTime);
		} else {
			tracking.addTextCount(userId, totalCallTime);
		}
	}

	private int cutoffIndex(String header, int cutoffDay) {
		final String[] tokens = header.split(",");
		for (int i = 1; i < tokens.length; i++) {
			final int day = CellReference.convertColStringToIndex(tokens[i]);
			if (day >= cutoffDay) {
				return i;
			}
		}
		return 0;
	}

}
