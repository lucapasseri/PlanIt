package com.example.luca.planit;

public class PlaceImpl implements Place {
	private final String address;
	private final String province;
	private final String namePlace;
	private final String city;
	
	private PlaceImpl(String address, String province, String namePlace, String city) {
		super();
		this.address = address;
		this.province = province;
		this.namePlace = namePlace;
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public String getProvince() {
		return province;
	}

	public String getNamePlace() {
		return namePlace;
	}

	public String getCity() {
		return city;
	}
	
	
	public static class Builder {
		private  String address;
		private  String province;
		private  String namePlace;
		private  String city;
		
		public Builder setAddress(String address) {
			this.address = address;
			return this;
		}
		public Builder setProvince(String province) {
			this.province = province;
			return this;
		}
		public Builder setNamePlace(String namePlace) {
			this.namePlace = namePlace;
			return this;
		}
		public Builder setCity(String city) {
			this.city = city;
			return this;
		}
		
		public PlaceImpl build() {
			return new PlaceImpl((address),
					(province), (namePlace), (city));
		}
		
		
		

	}
}
