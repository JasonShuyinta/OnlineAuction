package com.sweng.astaonline.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.github.gwtbootstrap.client.ui.*;

public class LoginUtente extends Composite {

	private static LoginUtenteUiBinder uiBinder = GWT.create(LoginUtenteUiBinder.class);

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	interface LoginUtenteUiBinder extends UiBinder<Widget, LoginUtente> {
	}

	@UiField
	Button loginBtn;
	@UiField
	TextBox username;
	@UiField
	PasswordTextBox password;
	boolean check;
	String globalUser = null;

	public LoginUtente() {
		
		initWidget(uiBinder.createAndBindUi(this));
		
		//Button per eseguire il login dell'utente
		loginBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				check = true;
				if (username.getValue().isEmpty()) {
					Window.alert("Inserisci lo username");
					check = false;
				}
				if (password.getValue().isEmpty()) {
					Window.alert("Inserisci la password");
					check = false;
				}

				if (check) {
					greetingService.login(username.getValue(), password.getValue(), new AsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
							if(result) {
							globalUser = username.getValue();
							History.newItem("home");
							} else {
							Window.alert("Wrong username or password"); 
							username.setText(""); 
							password.setText("");}
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore");
						}
					});
				}
			}
		});
	}
	
	//Metodo per ottenere lo username loggato
	//Verrà usato in altre classi per ottenere lo user in qualsiasi momento
	public String getLoggedUsername() {
		return globalUser;
	}
}
