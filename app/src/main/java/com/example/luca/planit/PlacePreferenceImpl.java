package com.example.luca.planit;

public class PlacePreferenceImpl implements PlacePreference {
	private final Place place;
	private final int numPreferences;
	private final String idPlace;

	public PlacePreferenceImpl(Place place, int numPreferences,String idPlace) {
		super();
		this.place = place;
		this.numPreferences = numPreferences;
		this.idPlace = idPlace;
	}


	public Place getPlace() {
		return place;
	}


	public int getNumPreferences() {
		return numPreferences;
	}




	@Override
	public String toString() {
		return "PlacePreferenceImpl [place=" + place + ", numPreferences=" + numPreferences + ", idPlace=" + idPlace
				+ "]";
	}


	@Override
	public String getidPlace() {
		return this.idPlace;
	}



}