package fr.arolla.locness.billplus;

import java.util.Date;

public class UserContract {

	private String name;
	private Date registrationDate;
	private String plan;
	private String options;
	private String payAsYouGoLevel;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getPayAsYouGoLevel() {
		return payAsYouGoLevel;
	}

	public void setPayAsYouGoLevel(String payAsYouGoLevel) {
		this.payAsYouGoLevel = payAsYouGoLevel;
	}

}
