package com.example.luca.planit;

public class InsertTaskWrapper {
	private final String username;
	private final String taskText;
	private final String id_evento;
	
	public InsertTaskWrapper(String username, String taskText, String id_evento) {
		super();
		this.username = username;
		this.taskText = taskText;
		this.id_evento = id_evento;
	}

	public String getUsername() {
		return username;
	}

	public String getTaskText() {
		return taskText;
	}

	public String getId_evento() {
		return id_evento;
	}
	
	
}
