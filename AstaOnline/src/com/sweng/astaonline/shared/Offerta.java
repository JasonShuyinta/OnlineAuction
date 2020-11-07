package com.sweng.astaonline.shared;

import java.io.Serializable;

public class Offerta implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String username, idOggetto, nomeOggetto;
	private double importo;
	
	public Offerta() {}
	
	public Offerta(String username, String idOggetto, double importo) {
		this.username = username;
		this.idOggetto = idOggetto;
		this.importo = importo;
	}
	
	public Offerta(String username, String idOggetto, double importo, String nomeOggetto) {
		this.username = username;
		this.idOggetto = idOggetto;
		this.importo = importo;
		this.nomeOggetto = nomeOggetto;
	}
	
	public String getNomeOggetto() {
		return nomeOggetto;
	}
	
	public double getImporto() {
		return importo;
	}
	
	public String getIdOggetto() {
		return idOggetto;
	}
	
	public String getUsername() {
		return username;
	}
	
}
