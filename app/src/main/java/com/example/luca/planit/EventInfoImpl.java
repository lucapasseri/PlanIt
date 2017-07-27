package com.example.luca.planit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class EventInfoImpl implements EventInfo {

    public static final String EMPTY_FIELD = "null";

	private final String data;
	private final String address;
	private final String province;
	private final String namePlace;
	private final String city;
	private final Organizer organizer;
	private final String name_event;
	private final String time;
	private final String eventId;


	public EventInfoImpl(String data, String address, String province, String namePlace, String city, Organizer organizer, String name_event, String time, String eventId) {
		this.data = data;
		this.address = address;
		this.province = province;
		this.namePlace = namePlace;
		this.city = city;
		this.organizer = organizer;
		this.name_event = name_event;
		this.time = time;
		this.eventId = eventId;
	}

	public String getName_event() {
		return name_event;
	}




	public String getTime() {
		return time;
	}




	public String getDate(DateFormatType formatType) {
		if(data != null){
			switch (formatType) {

				case DD_MM_YYYY_BACKSLASH:
					SimpleDateFormat fromFormatter = new SimpleDateFormat(DateFormatType.YYYY_MM_DD_DASH.getFormat(), Locale.US);
					SimpleDateFormat toFormatter  = new SimpleDateFormat(DateFormatType.DD_MM_YYYY_BACKSLASH.getFormat(), Locale.US);

					String formattedDate = data;

					try {
						Date toFormatDate = fromFormatter.parse(formattedDate);
						formattedDate = toFormatter.format(toFormatDate);
					} catch (ParseException e) {
						e.printStackTrace();
					}

					return formattedDate;

				default:
					return data;
			}

		}else{
			return data;
		}

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
	public Organizer getOrganizer() {
		return this.organizer;
	}



	public String getNameEvent() {
		return name_event;
	}

	@Override
	public String getEventId() {
		return eventId;
	}

	@Override
	public String toString() {
		return "EventInfoImpl [data=" + data + ", address=" + address + ", province=" + province + ", namePlace="
				+ namePlace + ", city=" + city + ", organizer=" + organizer + ", name_event=" + name_event + ", time="
				+ time + "]";
	}








	public static class Builder {
		private  String data;
		private  String address;
		private  String province;
		private  String namePlace;
		private  String city;
		private  Organizer organizer;
		private  String name_event;
		private  String time;
		private  String eventId;

		public Builder setTime(String time) {
			this.time = time;
			return this;
		}
		
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
		
		public Builder setOrganizer(Organizer organizer) {
			this.organizer = organizer;
			return this;
		}
		
		public Builder setNameEvent(String name_event) {
			this.name_event = name_event;
			return this;
		}

		public Builder setEventId(String eventId) {
			this.eventId = eventId;
			return this;
		}
		
		public EventInfo build() {
			return new EventInfoImpl((data), (address),
					(province), (namePlace), (city),Objects.requireNonNull(organizer),Objects.requireNonNull(name_event),time,eventId);
		}
		

	}
}

