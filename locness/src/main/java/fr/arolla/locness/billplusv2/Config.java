package fr.arolla.locness.billplusv2;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	final Properties prop = new Properties();

	public Config(String fileName) {
		InputStream input = null;
		try {
			input = new FileInputStream(fileName + ".properties");
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
	}

	public String getString(String code) {
		return prop.getProperty(code);
	}

	public double getDouble(String code, double defaultValue) {
		final String s = prop.getProperty(code);
		return s == null ? defaultValue : Double.parseDouble(s);
	}

	public double getFee(String code, double defaultValue) {
		return getDouble(code + ".fee", defaultValue);
	}

	public double getRate(String code, double defaultValue) {
		return getDouble(code + ".rate", defaultValue);
	}

	public double getTime(String code, double defaultValue) {
		return getDouble(code + ".time", defaultValue);
	}

	public double getCommission(String code, double defaultValue) {
		return getDouble(code + ".commission", defaultValue);
	}
}
