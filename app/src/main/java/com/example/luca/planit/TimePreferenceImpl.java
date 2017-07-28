package com.example.luca.planit;

public class TimePreferenceImpl implements TimePreference {
	private final String time;
	private final int numPreferences;
	
	
	public TimePreferenceImpl(String time, int numPreferences) {
		super();
		this.time = time;
		this.numPreferences = numPreferences;
	}

	@Override
	public String getTime() {
		return this.time;
	}

	@Override
	public int getNumPreferences() {
		return this.numPreferences;
	}

	@Override
	public String toString() {
		return "TimePreferenceImpl [time=" + time + ", numPreferences=" + numPreferences + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TimePreferenceImpl that = (TimePreferenceImpl) o;

		return time != null ? time.equals(that.time) : that.time == null;

	}

	@Override
	public int hashCode() {
		return time != null ? time.hashCode() : 0;
	}
}
