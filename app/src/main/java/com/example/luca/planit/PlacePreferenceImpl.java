package com.example.luca.planit;

public class PlacePreferenceImpl implements PlacePreference {
	private final Place place;
	private final int numPreferences;
	
	
	public PlacePreferenceImpl(Place place, int numPreferences) {
		super();
		this.place = place;
		this.numPreferences = numPreferences;
	}


	public Place getPlace() {
		return place;
	}


	public int getNumPreferences() {
		return numPreferences;
	}


	@Override
	public String toString() {
		return "PlacePreferenceImpl [place=" + place + ", numPreferences=" + numPreferences + "]";
	}
	
	
	
}
