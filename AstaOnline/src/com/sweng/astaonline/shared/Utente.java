package com.sweng.astaonline.shared;

import java.io.Serializable;

public class Utente implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public String nome, cognome, username, telefono, password, email,codiceFiscale, indirizzo,sesso, data, luogo;
	
	public Utente() {}
	
	public Utente(String nome, String cognome, 
			String username, String telefono, 
			String password,  String email, 
			String codiceFiscale, String indirizzo, 
			String sesso, String data, String luogo ) {
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.telefono = telefono;
		this.password = password;
		this.email = email;
		this.codiceFiscale = codiceFiscale;
		this.indirizzo = indirizzo;
		this.sesso = sesso;
		this.data = data;
		this.luogo = luogo;
	}
	
	public Utente(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getIndirizzo() {
		return indirizzo;
	}
	
	public String getData() {
		return data;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	
	public String getSesso() {
		return sesso;
	}
	
	public String getLuogo() {
		return luogo;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	
	public class Admin extends Utente {
		public Admin( String username, String password) {
			this.username = username;
			this.password = password;

		}
	}

}
