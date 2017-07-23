package com.example.luca.planit;

public class PreferenceImpl implements Preference {
	private final String date;
	private final int numPreferences;
	
	public PreferenceImpl(String date, int numPreferences) {
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
		return "PreferenceImpl [date=" + date + ", numPreferences=" + numPreferences + "]";
	}
	
	
}
