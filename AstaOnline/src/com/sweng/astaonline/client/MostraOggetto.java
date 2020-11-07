package com.sweng.astaonline.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.astaonline.shared.Domanda;
import com.sweng.astaonline.shared.FieldVerifier;
import com.sweng.astaonline.shared.Oggetto;
import java.util.ArrayList;
import com.github.gwtbootstrap.client.ui.*;

public class MostraOggetto extends Composite {

	private static MostraOggettoUiBinder uiBinder = GWT.create(MostraOggettoUiBinder.class);

	interface MostraOggettoUiBinder extends UiBinder<Widget, MostraOggetto> {
	}

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);


	@UiField DivElement container;
	@UiField Button offri, btnDomanda;
	@UiField TextBox importoOfferto;
	@UiField Image imgBoxBootstrap;
	@UiField Heading nomeOggetto;
	@UiField Paragraph venditoreOggetto, descrizioneOggetto, 
	prezzoIniziale, scadenzaAsta, categoriaOggetto, offertaAttuale,statoOggetto;
	@UiField TextAreaElement areaDomanda;
	String finalId = null;
	String finalLoggedUsername = null;
	String finalPrezzo = null;
	String[] datiInput = new String[8];
	String finalVenditore = null;


	public MostraOggetto() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public MostraOggetto(String loggedUsername, String username, String id, String nome, 
			String descrizione, String prezzo, String scadenza, String categoria, 
			String img) {
		initWidget(uiBinder.createAndBindUi(this));
		datiInput[0] = username;
		datiInput[1] = id;
		datiInput[2] = nome;
		datiInput[3] = descrizione;
		datiInput[4] = prezzo;
		datiInput[5] = scadenza;
		datiInput[6] = categoria;
		datiInput[7] = img;
		finalId = id;
		finalLoggedUsername = loggedUsername;
		finalPrezzo = prezzo;
		finalVenditore = username;
		imgBoxBootstrap.setUrl("images/"+img);
		nomeOggetto.setText(nome);
		venditoreOggetto.setText("Venditore: "+username);
		descrizioneOggetto.setText("Descrizione: "+descrizione);
		prezzoIniziale.setText("Prezzo iniziale: " +prezzo);
		scadenzaAsta.setText("Scadenza Asta: " +scadenza);
		categoriaOggetto.setText("Categoria:" + categoria);
		
		//Servizio che ottiene l'ultima offerta dell'oggetto preso in considerazione
		greetingService.getUltimaOfferta(finalId, new AsyncCallback<Double>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("ERRORE");
			}

			@Override
			public void onSuccess(Double result) {
				if (result != -1) {
					offertaAttuale.setText("Offerta attuale: " + result);
				} else {
					offertaAttuale.setText("Offerta attuale: nessuna offerta");
				}
				
			}
			
		});
		
		// Servizio che ottine lo stato dell'oggetto 
		greetingService.getStatoOggetto(id, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Si � verificato un errore durante il caricamento dello stato");
			}

			@Override
			public void onSuccess(String result) {
				statoOggetto.setText("Stato: " + result);	
				// Se l'asta non � in corso, disabilita il pulsante per proporre un'offerta
				if (!result.trim().equals("Asta in corso")) {
					offri.setEnabled(false);
				}
			}
		});
		

		// Se l'utente e' un visitatore (utente non loggatto), allora disabilita i pulsanti per
		// proprorre un'offerta e fare una domanda relativa all'oggetto in questione 
		if(loggedUsername == null) {
			offri.setEnabled(false);
			btnDomanda.setEnabled(false);
		} else {
			offri.addClickHandler(new ClickHandler() {
				// quando viene proposta un'offerta, viene controllato la validit� dell'importo 
				@Override
				public void onClick(ClickEvent event) {
					if(importoOfferto.getValue().trim().isEmpty() || 
							!FieldVerifier.isValidNumber(importoOfferto.getValue().trim())) {
						Window.alert("Importo non valido");
					} else if (Double.parseDouble(importoOfferto.getValue().trim()) <= Double.parseDouble(finalPrezzo)){
						Window.alert("Importo errato");
					} else {
						// richiama il servizio che salva la proposta di offerta nel database presente nel server
						greetingService.inserisciOfferta(finalId, finalLoggedUsername,
								Double.parseDouble(importoOfferto.getValue().trim()), new AsyncCallback<Boolean>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore");
							}

							@Override
							public void onSuccess(Boolean result) {
								if(result) {
									Window.alert("Offerta inserita");
									AstaOnline a = new AstaOnline();
									a.visualizzaOggetto(datiInput, finalLoggedUsername);
								} else Window.alert("Offerta non inserita");
							}
						});
					}
				}

			});
			
			// onClick che permatte di inviare una domanda 
			btnDomanda.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					
					if(areaDomanda.getValue().isEmpty()) {
						Window.alert("Inserisci una domanda!");
					} else {
						String quest = areaDomanda.getValue().trim();
						greetingService.inviaDomanda(finalLoggedUsername, quest, finalId, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Errore nell'invio della domanda");
							}

							@Override
							public void onSuccess(Void result) {
								Window.alert("Invio riuscito");
								AstaOnline a = new AstaOnline();
								a.visualizzaOggetto(datiInput, finalLoggedUsername);
							}
							
						});
					}
					
				}
				
			});
		}
		
		// metodo che ottine la lista di tutte le domande riguardanti l'oggetto mostrato
		greetingService.getDomande(finalId, new AsyncCallback<ArrayList<Domanda>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore nel recupero domande");
			}

			@Override
			public void onSuccess(ArrayList<Domanda> result) {
				for(int i=0 ; i < result.size(); i++) {
					MostraDomanda md = new MostraDomanda(result.get(i), finalLoggedUsername, datiInput);
					RootPanel.get().add(md);
				}
			}
			
		});
	}
}
