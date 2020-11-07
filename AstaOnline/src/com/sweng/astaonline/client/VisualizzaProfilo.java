package com.sweng.astaonline.client;

import java.util.ArrayList;

import com.github.gwtbootstrap.client.ui.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.astaonline.shared.Oggetto;
import com.sweng.astaonline.shared.Utente;

public class VisualizzaProfilo extends Composite {

	private static VisualizzaProfiloUiBinder uiBinder = GWT.create(VisualizzaProfiloUiBinder.class);

	interface VisualizzaProfiloUiBinder extends UiBinder<Widget, VisualizzaProfilo> {
	}
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	@UiField Paragraph nome, cognome, user, telefono, email, codiceFiscale, indirizzo, sesso, data, luogo;
	
	public VisualizzaProfilo() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public VisualizzaProfilo(String username) {
		initWidget(uiBinder.createAndBindUi(this));
		user.setText(username);
		RootPanel.get().add(new Navbar(username));
		//servizio che riceve il nome	
		greetingService.nomeUtente(username,new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("NO NOME");	
			}
			@Override
			public void onSuccess(String result) {
				nome.setText(result);
			}
		});
	
		//servizio che riceve il cognome	
		greetingService.cognomeUtente(username,new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("NON COGNOME");	
			}
			@Override
			public void onSuccess(String result) {
				cognome.setText(result);
			}
		});
		
		
		//servizio che riceve il telefono utente	
		greetingService.telefonoUtente(username,new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("NON TELEFONO");	
			}
			@Override
			public void onSuccess(String result) {
				telefono.setText(result);
			}
		});
		
		//servizio che riceve email utente	
		greetingService.emailUtente(username,new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("NON EMAIL");	
			}
			@Override
			public void onSuccess(String result) {
				email.setText(result);
			}
		});
		
		//servizio che riceve cf utente	
		greetingService.cfUtente(username,new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("NON CF");	
			}
			@Override
			public void onSuccess(String result) {
				codiceFiscale.setText(result);
			}
		});
		
		//servizio che riceve indirizzo utente	
		greetingService.indirizzoUtente(username,new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("NON INDIRIZZO");	
			}
			@Override
			public void onSuccess(String result) {
				indirizzo.setText(result);
			}
		});
		
		//servizio che riceve sesso utente	
		greetingService.sessoUtente(username,new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("NON SESSO");	
			}
			@Override
			public void onSuccess(String result) {
				sesso.setText(result);
			}
		});
		
		//servizio che riceve la data di nascita utente	
		greetingService.dataUtente(username,new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("NON DATA");	
			}
			@Override
			public void onSuccess(String result) {
				String dataRidotta = result.substring(3,10);
				String anno = result.substring(27);
				String dataEffettiva = dataRidotta + anno;
				data.setText(dataEffettiva);
			}
		});
		
		//servizio che riceve il luogo di nascita utente	
		greetingService.luogoUtente(username,new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("NON LUOGO");	
			}
			@Override
			public void onSuccess(String result) {
				luogo.setText(result);
			}
		});
		
		//metodo per caricare i prodotti messi all'asta
		greetingService.getOggettiVenduti(username, new AsyncCallback<ArrayList<Oggetto>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Si è verificato un errore durante il caricamento degli oggetti");
				
			}

			@Override
			public void onSuccess(ArrayList<Oggetto> result) {
				for (int i=0; i<result.size(); i++) {
					ProdottiVenduti p = new ProdottiVenduti(result.get(i).getUsername(), result.get(i).getId(), result.get(i).getNome(), result.get(i).getDescrizione(), result.get(i).getPrezzo(), result.get(i).getScadenza(), result.get(i).getCategoria(), result.get(i).getImgUrl());
					RootPanel.get().add(p);
				}
			}
			
		});
		final String nomeUtente = username;
		//metodo per caricare i prodotti per cui è stato fatto un'offerta
		greetingService.getOggettiAcquistati(nomeUtente, new AsyncCallback<ArrayList<Oggetto>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Si è verificato un errore durante il caricamento degli oggetti");
			}

			@Override
			public void onSuccess(ArrayList<Oggetto> result) {
				for (int i=0; i<result.size(); i++) {
					ProdottiAcquistati p = new ProdottiAcquistati(nomeUtente, result.get(i).getUsername(), result.get(i).getId(), result.get(i).getNome(), result.get(i).getDescrizione(), result.get(i).getPrezzo(), result.get(i).getScadenza(), result.get(i).getCategoria(), result.get(i).getImgUrl());
					RootPanel.get().add(p);
				}
				
			}
			
		});
		
		
	}

}
