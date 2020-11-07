package com.sweng.astaonline.shared;

import java.io.Serializable;
import java.util.Date;

public class Oggetto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String username, id, nome, descrizione, categoria, img;
	private double prezzo;
	private Date dataScadenza;
	
	public Oggetto() {}
	
	public Oggetto(String username, String id, String nome, 
			String descrizione, double prezzo, Date dataScadenza,
			String categoria, String img) {
		this.username = username;
		this.id = id;
		this.nome = nome;
		this.descrizione = descrizione;
		this.prezzo = prezzo;
		this.dataScadenza = dataScadenza;
		this.categoria = categoria;
		this.img = img;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	public String[] getOggetto() {
		String[] array = new String[8];
		array[0] = username;
		array[1] = id;
		array[2] = nome;
		array[3] = descrizione;
		array[4] = String.valueOf(prezzo);
		array[5] = String.valueOf(dataScadenza);
		array[6] = categoria;
		array[7] = img;
		return array;
	}
	public String getUsername() {
		return username;
	}
	public String getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public double getPrezzo() {
		return prezzo;
	}
	public String getCategoria() {
		return categoria;
	}
	public String getImgUrl() {
		return img;
	}
	public Date getScadenza() {
		return dataScadenza;
	}
	
	
	
}
