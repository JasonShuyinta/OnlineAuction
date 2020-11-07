package com.sweng.astaonline.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sweng.astaonline.shared.Oggetto;

//Classe EntryPoint dell'applicazione
public class AstaOnline implements EntryPoint {

	String loggedUsername = null;
	LoginUtente lu;

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	public void onModuleLoad() {
		
		//Costruzione della homepage aggiungendo la navbar
		//La navbar è differente a seconda del loggedUsername
		//Se il loggedUsername è null, verra mostrata la navbar che vedrà il visitatore
		//se il loggedUsername è "admin" verrà mostrata la navbar dell'amministratore
		//se il loggedUsername è lo username di un utente registrato verrà mostrata la sua navbar
		RootPanel.get().add(new Navbar(loggedUsername));
		
		//History Token che viene istanziato all'avvio dell'applicazione
		History.newItem("home");
		
		//Metodo per estrarre e recuperare tutti gli oggetti attualmente in asta
		getAllKeys();
		
		//Gestione della pagina da visualizzare in base all'history token
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();

				if (historyToken.equals("registrazione")) {
					apriRegistrazione();
				}
				else if (historyToken.equals("profilo")) {
					apriProfilo();
				}
				else if (historyToken.equals("home")) {
					homePage();
				}
				else if (historyToken.equals("login")) {
					lu = new LoginUtente();
					loggedUsername = null;
					RootPanel.get().clear();
					RootPanel.get().add(new Navbar(loggedUsername));
					RootPanel.get().add(lu);
				}
				else if (historyToken.equals("vendita")) {
					apriVendita(loggedUsername);
				}
				else if (historyToken.equals("successLogin")) {
					loggedUsername = lu.getLoggedUsername();
					RootPanel.get().clear();
					RootPanel.get().add(new Navbar(loggedUsername));
				}

				else if (historyToken.equals("adminCategoria")) {
					amministrazioneCategoria();
				}
				else if (historyToken.equals("categorie")) {
					
				}
				
				//Se l'history token non è nessuno dei precedenti, significa che siamo nella pagina 
				//di visualizzazione di un oggetto, dove l'url corrisponderà all'id dell'oggetto
				else {
					RootPanel.get().clear();
					RootPanel.get().add(new Navbar(loggedUsername));
					String idOggetto = historyToken;
					greetingService.getInfoOggetto(idOggetto, new AsyncCallback<String[]>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Si è verificato un errore");
							
						}

						@Override
						public void onSuccess(String[] result) {
							visualizzaOggetto(result, loggedUsername);
							
						}
						
						
					});
				}

			}
		});
	}
	
	//Metodo per la costruzione della pagina per la lettura dei dettagli di un oggetto
	//tramite la classe MostraOggetto
	public void visualizzaOggetto(String[] result, String usernamelogged) {
		loggedUsername = usernamelogged;
		RootPanel.get().clear();
		RootPanel.get().add(new Navbar(loggedUsername));
		MostraOggetto mo = new MostraOggetto(loggedUsername, result[0],result[1],result[2],result[3],result[4],result[5],result[6],result[7]);
		RootPanel.get().add(mo);
	}
	
	//Metodo per la costruzione della pagina accessibile solo da un amministratore
	//con tutte le operazione a quest'ultimo concesse
	public void amministrazioneCategoria() {
		RootPanel.get().clear();
		RootPanel.get().add(new Navbar("admin"));
		RootPanel.get().add(new Amministratore());
	}

	//Costruzione della homePage passando l'attuale user loggato
	public void homePage() {
		loggedUsername = lu.getLoggedUsername();
		RootPanel.get().clear();
		RootPanel.get().add(new Navbar(loggedUsername));
		getAllKeys();
	}
	
	//Costruisce la pagina di registrazione con il form per l'inserimento dei dati 
	public void apriRegistrazione() {
		RootPanel.get().clear();
		RootPanel.get().add(new Navbar(loggedUsername));
		Form form = new Form();
		RootPanel.get().add(form);
	}

	//Apre la pagina di profilo di un utente
	public void apriProfilo() {
		RootPanel.get().clear();
		VisualizzaProfilo profilo = new VisualizzaProfilo(loggedUsername);
		RootPanel.get().add(profilo);
	}
	
	//Filtra gli oggetti in base alla categoria scelta dal dropdown
	public void getOggettiFiltrati(String categoria, String username) {
		loggedUsername = username;
		RootPanel.get().add(new Navbar(loggedUsername));
		FiltraggioSottoCategoria fsc = new FiltraggioSottoCategoria(categoria, username);
		RootPanel.get().add(fsc);
		greetingService.filtraPerCategoria(categoria, new AsyncCallback<ArrayList<Oggetto>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore estrazione oggetti");
			}
			
			@Override
			public void onSuccess(ArrayList<Oggetto> result) {
				for(int i = 0; i < result.size(); i++) {
					Prodotti p = new Prodotti(result.get(i).getUsername(), 
							result.get(i).getId(), result.get(i).getNome(), result.get(i).getDescrizione(), 
							result.get(i).getPrezzo(), result.get(i).getScadenza(), result.get(i).getCategoria(),
							result.get(i).getImgUrl());
					RootPanel.get().add(p);
					
				}
				
			}
	
		});
	}
	
	//Mostra tutti gli oggetti attualmente in asta ordinati in base alla scadenza 
	//richiamando per ogni oggetto presente la classe Prodotti.java 
	public void getAllKeys() {
		greetingService.getOggettiS2C(new AsyncCallback<ArrayList<Oggetto>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore estrazione oggetti");
				
			}
			
			@Override
			public void onSuccess(ArrayList<Oggetto> result) {
				for(int i = 0; i < result.size(); i++) {
					Prodotti p = new Prodotti(result.get(i).getUsername(), 
							result.get(i).getId(), result.get(i).getNome(), result.get(i).getDescrizione(), 
							result.get(i).getPrezzo(), result.get(i).getScadenza(), result.get(i).getCategoria(),
							result.get(i).getImgUrl());
					RootPanel.get().add(p);
					
				}
				
			}
	
		});
	}
	
	//Costruisce la pagina per la messa in vendita all'asta di un oggetto
	public void apriVendita(String loggedUsername) {
		RootPanel.get().clear();
		RootPanel.get().add(new Navbar(loggedUsername));
		Vendita v = new Vendita(loggedUsername);
		RootPanel.get().add(v);
	}

}
