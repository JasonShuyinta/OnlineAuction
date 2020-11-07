package com.sweng.astaonline.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Categoria implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String categoria;
	private ArrayList<String> sottoCategorie;
	
	
	public Categoria() {}
	
	public Categoria(String categoria, ArrayList<String> sottoCategorie) {
		this.categoria = categoria;
		this.sottoCategorie = sottoCategorie;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public ArrayList<String> getSottoCategoria() {
		return sottoCategorie;
	}
	
	public String ottieniSottoCategoria() {
		String s = null;
		for(int i = 0; i < sottoCategorie.size(); i++) {
			s = s + " " + sottoCategorie.get(i); 
		}
		return s;
	}
	
}
