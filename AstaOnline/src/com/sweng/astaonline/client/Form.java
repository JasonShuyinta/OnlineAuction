package com.sweng.astaonline.client;

import com.github.gwtbootstrap.client.ui.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.github.gwtbootstrap.datepicker.client.ui.DateBox;
import com.sweng.astaonline.shared.FieldVerifier;

public class Form extends Composite {

	private static FormUiBinder uiBinder = GWT.create(FormUiBinder.class);
	
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	
	final Label errorLabel = new Label();


	interface FormUiBinder extends UiBinder<Widget, Form> {
	}

	@UiField
	TextBox nome, cognome, username, telefono, email, codiceFiscale, indirizzo, luogo;
	@UiField
	PasswordTextBox password;
	@UiField
	RadioButton m, f;
	@UiField Button sendBtn;
	@UiField DateBox data;
	
	public Form() {
		initWidget(uiBinder.createAndBindUi(this));
		RootPanel.get().add(errorLabel);
		
		//Al click del button "Invia" esegui il metodo sendToServer che contiene tutti i controlli
		sendBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			sendToServer();
			}
		});		
	}
	
	
	//Effettua tutti i controlli dei campi tramite classe FieldVerifier nel package Shared
	public void sendToServer() {
		errorLabel.setText("");
		String nomeUtente = getNome();
		if (!FieldVerifier.isValidName(nomeUtente)) {
			errorLabel.setText("Inserisci il tuo nome");
			return;
		}
		String cognomeUtente = getCognome();
		if (!FieldVerifier.isValidName(cognomeUtente)) {
			errorLabel.setText("Inserisci il tuo cognome");
			return;
		}
		String usernameUtente = getUsername();
		if (!FieldVerifier.isValidName(usernameUtente)) {
			errorLabel.setText("Inserisci il tuo username");
			return;
		}
		
		String telefonoUtente = getTelefono();
		if (!FieldVerifier.isValidNumber(telefonoUtente)) {
			errorLabel.setText("Non hai inserito un numero di telefono!");
			return;
		}
		String passwordUtente = getPassword();
		if (!FieldVerifier.isValidName(passwordUtente)) {
			errorLabel.setText("Inserisci la password");
			return;
		}
		String emailUtente = getEmail();
		if (!FieldVerifier.isValidName(emailUtente)) {
			errorLabel.setText("Inserisci la tua email");
			return;
		}
		String cfUtente = getCodiceFiscale();
		if (!FieldVerifier.isValidName(cfUtente)) {
			errorLabel.setText("Inserisci il tuo codice fiscale");
			return;
		}
		String indirizzoUtente = getIndirizzo();
		if (!FieldVerifier.isValidName(indirizzoUtente)) {
			errorLabel.setText("Inserisci il tuo indirizzo");
			return;
		}
		String sessoUtente = getSesso();

		String dataUtente = getData();

		String luogoUtente = getLuogo();

		//Una volta cliccato il button viene disabilitato
		sendBtn.setEnabled(false);
		//Richiama il metodo sendUsername per il salvataggio e la registrazione dell'utente
		//con i parametri passati in input
		greetingService.sendUsername(nomeUtente,cognomeUtente, usernameUtente, telefonoUtente, passwordUtente, emailUtente, cfUtente, indirizzoUtente, sessoUtente, dataUtente, luogoUtente, new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Non riuscito");
			}
			@Override
			public void onSuccess(String result) {
				Window.alert(result);
				//Viene istanziato l'history token "Home" (cfr AstaOnline.java)
				History.newItem("home");
			}
		});
	}

	public String getNome() {
		return nome.getValue();
	}

	public String getCognome() {
		return cognome.getValue();
	}

	public String getUsername() {
		return username.getValue();
	}

	public String getTelefono() {
		return telefono.getValue();
	}

	public String getPassword() {
		return password.getValue();
	}

	public String getEmail() {
		return email.getValue();
	}

	public String getCodiceFiscale() {
		return codiceFiscale.getValue();
	}

	public String getIndirizzo() {
		return indirizzo.getValue();
	}

	public String getSesso() {
		if(m.getValue()) 
			return "m";
		else 
			return "f";
	}

	public String getData() {
		return data.getValue().toString();
	}

	public String getLuogo() {
		return luogo.getValue();
	}

}
