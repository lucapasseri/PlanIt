package com.example.luca.planit;

import java.util.Objects;

public class EventInfoImpl implements EventInfo {
	private final String data;
	private final String address;
	private final String province;
	private final String namePlace;
	private final String city;




	public EventInfoImpl(String data, String address, String province, String namePlace, String city) {
		super();
		this.data = data;
		this.address = address;
		this.province = province;
		this.namePlace = namePlace;
		this.city = city;
	}




	public String getData() {
		return data;
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
	
	
	

	@Override
	public String toString() {
		return "EventInfoImpl [data=" + data + ", address=" + address + ", province=" + province + ", namePlace="
				+ namePlace + ", city=" + city + "]";
	}




	public static class Builder {
		private  String data;
		private  String address;
		private  String province;
		private  String namePlace;
		private  String city;
		public Builder setData(String data) {
			this.data = data;
			return this;
		}
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
		
		public EventInfo build() {
			return new EventInfoImpl(Objects.requireNonNull(data), Objects.requireNonNull(address),
					Objects.requireNonNull(province), Objects.requireNonNull(namePlace), Objects.requireNonNull(city));
		}
		
		
		

	}
}

