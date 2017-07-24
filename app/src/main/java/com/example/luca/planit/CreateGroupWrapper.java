package com.example.luca.planit;

public class CreateGroupWrapper {
	private final String idUtente;
	private final String nomeGruppo;
	
	/**
	 * 
	 * @param idUtente
	 * @param nomeGruppo
	 */
	public CreateGroupWrapper(String idUtente, String nomeGruppo) {
		super();
		this.idUtente = idUtente;
		this.nomeGruppo = nomeGruppo;
	}


	public String getIdUtente() {
		return idUtente;
	}


	public String getNomeGruppo() {
		return nomeGruppo;
	}



}
