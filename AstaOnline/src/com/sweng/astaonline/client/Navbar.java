package com.sweng.astaonline.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

import com.github.gwtbootstrap.client.ui.*;

public class Navbar extends Composite {

	private static NavbarUiBinder uiBinder = GWT.create(NavbarUiBinder.class);

	interface NavbarUiBinder extends UiBinder<HTMLPanel, Navbar> {
	}
	
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	@UiField
	NavLink registrazione, brand, myProfile, logoutAdmin, uiLogin, logout, sellObj, gestioneCategorie;
	@UiField
	Dropdown userDropdown, categoryDropdown;
	@UiField ResponsiveNavbar theNavbar;
	String loggedUsername;

	public Navbar() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public Navbar(String username) {
		initWidget(uiBinder.createAndBindUi(this));
		
		Hyperlink home = new Hyperlink("Online Auction", "home");
		brand.add(home);
		if (username != null) {
			// Carica la navbar contentete i link per poter accedere alle operazioni
			// esclusive dell'amministratore
			if (username == "admin") {
				Hyperlink cat = new Hyperlink("Amministrazione", "adminCategoria");
				Hyperlink logoutAmministratore = new Hyperlink("Log out", "login");
				Hyperlink sell = new Hyperlink("Vendi oggetto", "vendita");
				Hyperlink account = new Hyperlink("Il mio profilo", "profilo");
				gestioneCategorie.setStyleName("showNav");
				gestioneCategorie.add(cat);
				logout.add(logoutAmministratore);
				userDropdown.setStyleName("showNav");
				categoryDropdown.setStyleName("showNav");
				myProfile.add(account);
				sellObj.add(sell);
				
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
					 		categoryDropdown.add(link);
							link.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									History.newItem("categorie");
									RootPanel.get().clear();
									AstaOnline a = new AstaOnline();
									a.getOggettiFiltrati(categoryDropdown.getLastSelectedNavLink().getText().trim(), "admin");
								}
								
							});
						}
					}
				});
				
				
			} else {

				// Carica la navbar corrispondernte all'utente che ha eseguito il login
				// con successo
				Hyperlink account = new Hyperlink("Il mio profilo", "profilo");
				Hyperlink sell = new Hyperlink("Vendi oggetto", "vendita");
				Hyperlink logOutAccount = new Hyperlink("Logout", "login");
				userDropdown.setStyleName("showNav");
				categoryDropdown.setStyleName("showNav");
				registrazione.setStyleName("hideLinks");
				uiLogin.setStyleName("hideLinks");
				myProfile.add(account);
				sellObj.add(sell);
				logout.add(logOutAccount);
				loggedUsername = username;
				
				// Metodo che ottiene la lista di tutte le categorie presenti nel sito
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
							categoryDropdown.add(link);
							link.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									History.newItem("categorie");
									RootPanel.get().clear();
									AstaOnline a = new AstaOnline();
									a.getOggettiFiltrati(categoryDropdown.getLastSelectedNavLink().getText().trim(), loggedUsername);
								}
								
							});
						}
					}
				});
			}
		} else {
			
			// Carica il navbar corrispondente al visitatore (utente che non
			// ha eseguito il login)
			Hyperlink reg = new Hyperlink("Registrati", "registrazione");
			Hyperlink accedi = new Hyperlink("Login", "login");
			registrazione.add(reg);
			uiLogin.add(accedi);
		}
	}

}
