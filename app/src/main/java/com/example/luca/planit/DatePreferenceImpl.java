package com.example.luca.planit;

public class DatePreferenceImpl implements DatePreference {
	private final String date;
	private final int numPreferences;
	
	public DatePreferenceImpl(String date, int numPreferences) {
		super();
		this.date = date;
		this.numPreferences = numPreferences;
	}

	public String getDate() {
		return date;
	}

	public int getNumPreferences() {
		return numPreferences;
	}

	@Override
	public String toString() {
		return "DatePreferenceImpl [date=" + date + ", numPreferences=" + numPreferences + "]";
	}
	
	
}
