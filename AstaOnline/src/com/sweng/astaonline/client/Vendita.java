package com.sweng.astaonline.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

import com.github.gwtbootstrap.client.ui.*;
import com.github.gwtbootstrap.datetimepicker.client.ui.*;


public class Vendita extends Composite {

	private static VenditaUiBinder uiBinder = GWT.create(VenditaUiBinder.class);

	interface VenditaUiBinder extends UiBinder<Widget, Vendita> {
	}
	
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	@UiField TextBox nome, prezzo;
	@UiField TextArea descrizione;
	@UiField DateTimeBox dataScadenza;
	@UiField Button sellBtn;
	@UiField DropdownButton dropdownCategorie, dropdownImg;
	@UiField Image imageBox;
	String objUser = null;
	
	public Vendita() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public Vendita(String loggedUsername) {
		initWidget(uiBinder.createAndBindUi(this));
		
		objUser = loggedUsername;
		
		//creazione immagine per prodotti
		String[] imgs = {"asus","gucci","iphone","jordan","mjglove","pokemon","rolex","supreme"};
		
		for(int i = 0; i < imgs.length; i++) {
			NavLink nl = new NavLink();
			nl.setText(imgs[i]+".jpg");
			dropdownImg.add(nl);
			nl.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {					
					imageBox.setUrl("images/"+dropdownImg.getLastSelectedNavLink().getText().trim());
				}
				
			});
		}
		
		
		//metodo per ottenere le categorie disponibili da inserire dinamicamente nel dropdown
		greetingService.getSopraCategorie(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Couldn't load dropdown navlinks");
			}

			@Override
			public void onSuccess(String result) {
				String[] categories = result.split(",");
				for (int i = 0; i < categories.length; i++) {
					NavLink link = new NavLink();
					link.setText(categories[i]);
					dropdownCategorie.add(link);
				}
			}
		});
		
		sellBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				sendObj();
			}
		});
	}
	
	//metodo per inserire l'oggetto all'asta
	public void sendObj() {
		String nomeOggetto = getNome();
		String descrizioneOggetto = getDescrizione();
		double prezzoOggetto = getPrezzo();
		Date dataScadenza1 = getDataScadenza();
		String categoria = getCategoria();
		String imgUrl = getImage();
		greetingService.vendiOggetti(objUser, 
				nomeOggetto, descrizioneOggetto, prezzoOggetto, dataScadenza1, categoria, imgUrl,  new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Vendita oggetto non riuscita");						
					}

					@Override
					public void onSuccess(Boolean result) {
						Window.alert("Oggetto messo all'asta");
					}
		});
	}
	
	//metodi getters
	public String getNome() {
		return nome.getValue();
	}
	
	public String getDescrizione() {
		return descrizione.getValue();
	}
	public double getPrezzo() {
		return Double.parseDouble(prezzo.getValue());
	}
	
	public Date getDataScadenza() {
		return dataScadenza.getValue();
	}
	public String getCategoria() {
		return dropdownCategorie.getLastSelectedNavLink().getText();
	}
	public String getImage() {
		return dropdownImg.getLastSelectedNavLink().getText().trim();
	}
}
