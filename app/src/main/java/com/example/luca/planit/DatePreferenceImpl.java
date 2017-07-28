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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DatePreferenceImpl that = (DatePreferenceImpl) o;

		return date != null ? date.equals(that.date) : that.date == null;

	}

	@Override
	public int hashCode() {
		return date != null ? date.hashCode() : 0;
	}
}
