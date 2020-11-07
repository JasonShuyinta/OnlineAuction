package com.sweng.astaonline.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import com.github.gwtbootstrap.client.ui.*;



public class FiltraggioSottoCategoria extends Composite {

	private static FiltraggioSottoCategoriaUiBinder uiBinder = GWT.create(FiltraggioSottoCategoriaUiBinder.class);

	interface FiltraggioSottoCategoriaUiBinder extends UiBinder<Widget, FiltraggioSottoCategoria> {
	}

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);


	@UiField DivElement container;
	String finalLoggedUsername = null;
	public FiltraggioSottoCategoria() {}

	public FiltraggioSottoCategoria(String categoria, String loggedUsername) {
		initWidget(uiBinder.createAndBindUi(this));
		finalLoggedUsername = loggedUsername;
		greetingService.getAllSottocategorie2(categoria, new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore");

			}

			@Override
			public void onSuccess(String[] result) {
				for (int i=0; i<result.length; i++) {
					final NavLink sottoCat = new NavLink();
					sottoCat.setText(result[i]);
					sottoCat.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							History.newItem("categorie");
							RootPanel.get().clear();
							AstaOnline a = new AstaOnline();
							a.getOggettiFiltrati(sottoCat.getText().trim(), finalLoggedUsername.trim());
						}
						
					});
					RootPanel.get().add(sottoCat);
				}

			}

		});

	}

}
