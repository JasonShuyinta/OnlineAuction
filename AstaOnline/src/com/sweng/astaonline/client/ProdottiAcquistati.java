package com.sweng.astaonline.client;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.astaonline.client.GreetingService;
import com.sweng.astaonline.client.GreetingServiceAsync;

public class ProdottiAcquistati extends Composite {

	private static ProdottiAcquitatiUiBinder uiBinder = GWT.create(ProdottiAcquitatiUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	@UiField DivElement container;

	interface ProdottiAcquitatiUiBinder extends UiBinder<Widget, ProdottiAcquistati> {
	}

	public ProdottiAcquistati(String loggedUser, String username, String id, String nome, 
			String descrizione, double prezzo, Date scadenza, String categoria, 
			String img) {
		initWidget(uiBinder.createAndBindUi(this));
		
		// Definizione dei vari elementi che verranno stampati a video
		Element user = DOM.createLabel();
		Element nomeOggetto = DOM.createLabel();
		Element descrizioneOggetto = DOM.createCaption();
		Element prezzoOggetto = DOM.createLabel();
		Element dataScadenza = DOM.createLabel();
		Element categoriaOggetto = DOM.createLabel();
		Element tabella = DOM.createTable();
		Element riga = DOM.createTR();
		Element colonna = DOM.createTD();
		Element colonna2 = DOM.createTD();
		Element colonna3 = DOM.createTD();
		Element colonna4 = DOM.createTD();
		Element divImg = DOM.createDiv();
		Element hr = DOM.createDiv();
		final Element price = DOM.createLabel();
		Element bid = DOM.createDiv();
		Element contactSeller = DOM.createDiv();
		Element apriOggetto = DOM.createDiv();
		Element tmp = DOM.createDiv();
		
		// Creazione dei vari elementi che verranno stampati a video
		hr.setInnerHTML("<hr style='margin-top: 1rem; margin-bottom: 1rem; margin-right: 3rem; height:1px;border-width:0;color:gray;background-color:gray'>");
		tabella.addClassName("centerTable");
		colonna2.addClassName("colSize");
		divImg.addClassName("resizeDiv");
		divImg.setAttribute("id", "product");
		divImg.setInnerHTML("<img src='images/"+img+"' style='width: 100%;' >");
		user.setInnerHTML("<p style='margin-left: 2rem'><b>Venditore: </b>" + username + "</p>");
		nomeOggetto.setInnerHTML("<p style='margin-left: 2rem; font-size: 25px'><b>"+nome+"</b></p>");
		descrizioneOggetto.setInnerHTML("<p style='margin-left: 2rem;'><b>Descrizione: </b>"+descrizione+"</p>");
		prezzoOggetto.setInnerHTML("<p style='margin-left: 2rem'><b>Prezzo di partenza</b>: "+prezzo+"$</p>");
		dataScadenza.setInnerHTML("<p style='margin-left: 2rem'><b>Scadenza asta</b>: "+scadenza+"</p>");
		categoriaOggetto.setInnerHTML("<p style='margin-left: 2rem'><b>Categoria: </b>"+categoria+"</p>");
		
		// Metodo per ottenere lo stato di tutti gli oggetti messi in vedita da un determinato utente
		greetingService.getStatoOggettoAcquistato(id,loggedUser, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				price.setInnerHTML("<p style='margin-left: 3rem; margin-top: 3rem;'><b><span style='font-size: 18px;'>Stato: </span></b> Si � verificato un errore</p>");
			}
			@Override
			public void onSuccess(String result) {
				price.setInnerHTML("<p style='margin-left: 3rem; margin-top: 3rem;'><b><span style='font-size: 18px;'>Stato: </span></b>" + result +"</p>");					
			}
			
		});
		
		// Creazione del pulsante "mostra oggetto"
		Hyperlink hp = new Hyperlink("MostraOggetto",id);
		apriOggetto.setInnerHTML("<button style='margin-left: 3rem; margin-top: 3rem' class='btn btn-light'>"+hp+" </button>");
		
		// Esecuizine di appendChild in modo da rendere visibile tutti gli elementi
		// precedentemente creati
		container.appendChild(tabella);
		tabella.appendChild(riga);
		riga.appendChild(colonna);
		colonna.appendChild(divImg);
		riga.appendChild(colonna2);
		riga.appendChild(colonna3);
		riga.appendChild(colonna4);
		colonna2.appendChild(nomeOggetto);
		colonna2.appendChild(user);
		colonna2.appendChild(prezzoOggetto);
		colonna2.appendChild(dataScadenza);
		colonna2.appendChild(categoriaOggetto);
		colonna2.appendChild(descrizioneOggetto);
		colonna3.appendChild(price);
		colonna4.appendChild(bid);
		colonna4.appendChild(contactSeller);
		colonna4.appendChild(apriOggetto);
		colonna4.appendChild(tmp);
		container.appendChild(hr);
	}
}
