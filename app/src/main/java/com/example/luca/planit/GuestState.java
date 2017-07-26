package com.example.luca.planit;

public enum GuestState {
 NOT_CONFIRMED("0"),CONFIRMED("1"),DECLINED("2");
	private final String code;

	private GuestState(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	
}
