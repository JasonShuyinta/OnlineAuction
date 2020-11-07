package com.sweng.astaonline.shared;

import java.io.Serializable;

public class Domanda implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String idDomanda, userDomanda, domanda, idOggetto, nomeOggetto;
	
	public Domanda() {}
	
	public Domanda(String idDomanda, String userDomanda, String domanda, String idOggetto) {
		this.idDomanda = idDomanda;
		this.userDomanda = userDomanda;
		this.domanda = domanda;
		this.idOggetto = idOggetto;
	}
	
	public Domanda(String idDomanda, String userDomanda, String domanda, String idOggetto, String nomeOggetto) {
		this.idDomanda = idDomanda;
		this.userDomanda = userDomanda;
		this.domanda = domanda;
		this.idOggetto = idOggetto;
		this.nomeOggetto = nomeOggetto;
	}
	
	public String getNomeOggetto() {
		return nomeOggetto;
	}
	
	public String getIdOggetto() {
		return idOggetto;
	}
	public String getUserDomanda() {
		return userDomanda;
	}
	public String getDomanda() {
		return domanda;
	}
	public String getIdDomanda() {
		return idDomanda;
	}

}
