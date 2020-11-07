package com.sweng.astaonline.client;

import com.github.gwtbootstrap.client.ui.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.astaonline.shared.Domanda;
import com.sweng.astaonline.shared.Risposta;


public class MostraDomanda extends Composite {

	private static MostraDomandaUiBinder uiBinder = GWT.create(MostraDomandaUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	interface MostraDomandaUiBinder extends UiBinder<Widget, MostraDomanda> {
	}
	
	@UiField DivElement container;
	@UiField ParagraphElement user, userQuestion, risposta;
	@UiField TextBox TextRisposta;
	@UiField Button btnRisposta;
	String finalUtenteLoggato= null;
	Domanda finalQuest;
	String[] finalDatiInput;
	
	public MostraDomanda(Domanda quest, String utenteLoggato, String[] datiInput) {
		initWidget(uiBinder.createAndBindUi(this));
		finalQuest = quest;
		finalDatiInput = datiInput;
		finalUtenteLoggato = utenteLoggato;
		user.setInnerText("User: " + quest.getUserDomanda());
		userQuestion.setInnerText("Domanda: "+ quest.getDomanda());
		
		//metodo per ottenere le risposte ad una domanda 
		greetingService.getRisposta(quest.getIdDomanda(), new AsyncCallback<Risposta>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore caricamento risposta");
			}

			@Override
			public void onSuccess(Risposta result) {
				risposta.setInnerText("Risposta: "+ result.getRisposta());
			}
			
		});
		
		//metodo per ottenere il venditore dell'oggetto passato in input
		greetingService.getVenditore(quest.getIdOggetto().trim(), new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore caricamento Uservenditore");
				
			}

			@Override
			public void onSuccess(String result) {
				if (!result.trim().equals(finalUtenteLoggato)) {
					btnRisposta.setEnabled(false);
				}
				
			}
			
		});
		
		//button per inviare una risposta
		btnRisposta.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(!TextRisposta.getValue().isEmpty()) {
				greetingService.inviaRisposta(finalQuest.getIdDomanda().trim(), TextRisposta.getValue().trim(),finalUtenteLoggato , new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Errore");
					}

					//aggiornamento della pagina dopo l'invio della risposta
					@Override
					public void onSuccess(Void result) {
						Window.alert("Risposta inviata");
						AstaOnline a = new AstaOnline();
						a.visualizzaOggetto(finalDatiInput, finalUtenteLoggato);
					}
					
				});
				
			} else Window.alert("Il campo risposta non può essere vuoto!");
				
			}
			
		});
		
		
	}

}
