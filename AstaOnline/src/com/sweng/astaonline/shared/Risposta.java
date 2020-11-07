package com.sweng.astaonline.shared;

import java.io.Serializable;


public class Risposta implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String idDomanda, risposta, idUser, domanda, nomeOggetto; 
	
	public Risposta() {}
	public Risposta(String idDomanda, String risposta, String idUser) {
		this.idDomanda = idDomanda;
		this.risposta = risposta;
		this.idUser = idUser;
	}
	
	public Risposta (String domanda, String risposta, String user, String nomeOggetto, String idDomanda) {
		this.domanda = domanda;
		this.risposta = risposta;
		this.idUser = user;
		this.nomeOggetto = nomeOggetto;
		this.idDomanda = idDomanda;
	}
	
	public String getIdDomanda() {
		return idDomanda;
	}
	public void setIdDomanda(String idDomanda) {
		this.idDomanda = idDomanda;
	}
	public String getRisposta() {
		return risposta;
	}
	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getDomanda() {
		return domanda;
	}
	public void setDomanda(String domanda) {
		this.domanda = domanda;
	}
	public String getNomeOggetto() {
		return nomeOggetto;
	}
	public void setNomeOggetto(String nomeOggetto) {
		this.nomeOggetto = nomeOggetto;
	}
}
