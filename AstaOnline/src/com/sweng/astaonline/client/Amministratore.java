package com.sweng.astaonline.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import java.util.ArrayList;
import java.util.Arrays;
import com.github.gwtbootstrap.client.ui.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.sweng.astaonline.shared.Domanda;
import com.sweng.astaonline.shared.Offerta;
import com.sweng.astaonline.shared.Oggetto;
import com.sweng.astaonline.shared.Risposta;
import com.google.gwt.user.client.ui.Widget;

public class Amministratore extends Composite {

	private static AmministratoreUiBinder uiBinder = GWT.create(AmministratoreUiBinder.class);

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	interface AmministratoreUiBinder extends UiBinder<Widget, Amministratore> {
	}

	@UiField
	DropdownButton categoryDropdown, category, deleteDropdown, eliminaOggetto, eliminaOfferta,rinominaCategoria, eliminaDomanda, eliminaRisposta;
	@UiField
	TextBox sottocategoria, categoriaPadre, nuovoNome;
	@UiField
	Button btnInsert, btnShow, btnInsertPadre, delete, btnEliminaOggetto, btnEliminaOfferta, btnRinomina, btnEliminaDomanda, btnEliminaRisposta;
	@UiField
	DivElement albero, msgEliminazione;
	String categoria = null;
	String sottoCat = null;
	String cat = null;
	String eliminaCategoria = null;
	ArrayList<Oggetto> oggettiEliminabili = new ArrayList<Oggetto>();


	public Amministratore() {
		initWidget(uiBinder.createAndBindUi(this));

		//Metodo per ottenere tutte le risposte per eventualmente eliminarle
		greetingService.getTutteRisposte(new AsyncCallback<ArrayList<Risposta>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Si è verificato un errore durante il caricamento delle risposte");

			}

			//Inserimento dinamico delle risposte nel Dropdown
			@Override
			public void onSuccess(ArrayList<Risposta> result) {
				for(int i = 0; i < result.size(); i++) {
					NavLink navRisposte = new NavLink();
					navRisposte.setText(result.get(i).getDomanda() + " _ " + result.get(i).getRisposta() + " _ " + result.get(i).getIdUser() + " _ " + result.get(i).getNomeOggetto() + " _ " + result.get(i).getIdDomanda());
					eliminaRisposta.add(navRisposte);
				}

			}

		});

		//Button per eliminazione di una risposta selezionata dal dropdown
		btnEliminaRisposta.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String rispostaDaEliminare = eliminaRisposta.getLastSelectedNavLink().getText().trim();
				String[] parts = rispostaDaEliminare.split("_");
				greetingService.eliminaRisposta(parts[4], parts[1], parts[2], new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Si è verificato un errore durante l'eliminazione della domanda");
					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("Risposta eliminata con successo");
					}

				});


			}

		});

		//Metodo per ottenere tutte le domande con i relativi dati da inserire nel dropdown dinamicamente
		greetingService.getDomandaConNomeOggetto(new AsyncCallback<ArrayList<Domanda>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Si è verificato un errore durante il caricamento delle domande");

			}

			@Override
			public void onSuccess(ArrayList<Domanda> result) {
				for(int i = 0; i < result.size(); i++) {
					NavLink navDomanda = new NavLink();
					navDomanda.setText(result.get(i).getIdDomanda() + " _ " + result.get(i).getDomanda() + " _ " + result.get(i).getUserDomanda());
					eliminaDomanda.add(navDomanda);			
				}

			}

		});

		//Button per l'eliminazione della domanda
		btnEliminaDomanda.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String offertaDaEliminare = eliminaDomanda.getLastSelectedNavLink().getText().trim();
				String[] parts = offertaDaEliminare.split("_");
				greetingService.eliminaDomanda(parts[0], new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Si è verifica un errore durante l'eliminazione");

					}

					@Override
					public void onSuccess(Void result) {
						Window.alert("Domanda eliminata");

					}

				});
			}

		});

		//Metodo per ottenere tutte le offerte con annesso il nome dell'oggetto per cui è stata
		//fatta l'offerta e aggiungerle dinamicamente al dropdown
		greetingService.getOffertaConNomeOggetto(new AsyncCallback<ArrayList<Offerta>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore nel caricamento delle offerte");

			}

			@Override
			public void onSuccess(ArrayList<Offerta> result) {
				for(int i = 0; i < result.size(); i++) {
					NavLink nomeofferta = new NavLink();
					nomeofferta.setText(result.get(i).getIdOggetto() + " _ " + result.get(i).getImporto() + " _ " + result.get(i).getUsername() + " _ " + result.get(i).getNomeOggetto());
					eliminaOfferta.add(nomeofferta);			
				}
			}

		});

		//Button per l'eliminazione dell'offerta una volta selezionata dal dropdown 
		btnEliminaOfferta.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) { 
				String offertaDaEliminare = eliminaOfferta.getLastSelectedNavLink().getText().trim();
				String[] parts = offertaDaEliminare.split("_");
				greetingService.eliminaOffertaSelezionato(parts[0], Double.parseDouble(parts[1]), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Si è verificato un errore durante l'eliminazione");

					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							Window.alert("Offerta eliminata");
						} else {
							Window.alert("Offerta non trovata");
						}

					}

				});
			}

		});

		//Metodo per ottenere la lista degli oggetti da aggiungere dinamicamente al dropdown 
		greetingService.getOggettiS2C(new AsyncCallback<ArrayList<Oggetto>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Non e' stato possibile caricare gli oggetti");

			}

			@Override
			public void onSuccess(ArrayList<Oggetto> result) {
				oggettiEliminabili = result;
				for(int i = 0; i < result.size(); i++) {
					NavLink nomeOggetti = new NavLink();
					nomeOggetti.setText(result.get(i).getNome());
					eliminaOggetto.add(nomeOggetti);			
				}

			}

		});

		//Button per l'eliminazione dell'oggetto dopo averlo selezionato dal dropdown 
		btnEliminaOggetto.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String oggettoDaEliminare = eliminaOggetto.getLastSelectedNavLink().getText().trim();
				String idOggettoDaEliminare = null;
				for(int i = 0; i < oggettiEliminabili.size(); i++) {
					if(oggettoDaEliminare.equals(oggettiEliminabili.get(i).getNome())) {
						idOggettoDaEliminare = oggettiEliminabili.get(i).getId();
						
						//Utilizzo il metodo eliminaOggettoSelezionato per eliminare l'oggetto dal Db
						//e per fare ciò gli passo in input l'id di tale oggetto
						greetingService.eliminaOggettoSelezionato(idOggettoDaEliminare, new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Non e' stato possibile eliminare l'oggetto");
							}
							
							//Una volta eliminato ricostruisco la HomePage con gli oggetti aggiornati
							@Override
							public void onSuccess(Void result) {
								RootPanel.get().clear();
								RootPanel.get().add(new Navbar("admin"));
								RootPanel.get().add(new Amministratore());
								Window.alert("Oggetto eliminato con successo");

							}

						});
					}
				}

			}

		});


		//Ottengo le categorie da inserire dinamicamente nel dropdown 
		greetingService.getSopraCategorie(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore nel caricamento delle categorie");
			}
			
			//Inserisco dinamicamente le categorie nei 4 dropdown che lo richiedono 
			@Override
			public void onSuccess(String result) {
				String[] categories = result.split(",");
				for (int i = 0; i < categories.length; i++) {
					NavLink link = new NavLink();
					link.setText(categories[i]);
					categoryDropdown.add(link);
				}

				for (int i = 0; i < categories.length; i++) {
					NavLink link = new NavLink();
					link.setText(categories[i]);
					category.add(link);
				}
				for (int i = 0; i < categories.length; i++) {
					NavLink link = new NavLink();
					link.setText(categories[i]);
					deleteDropdown.add(link);
				}
				for (int i = 0; i < categories.length; i++) {
					NavLink link = new NavLink();
					link.setText(categories[i]);
					rinominaCategoria.add(link);
				}

			}

		});

		//Button per l'eliminazione di una categoria dopo averlo selezionato dal dropdown 
		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				deleteDropdown.setText(deleteDropdown.getLastSelectedNavLink().getText());
				eliminaCategoria = deleteDropdown.getLastSelectedNavLink().getText().trim();
				
				//Le categorie predefinite non sono eliminabili
				String[] categoriePredefinite = { "Abbigliamento", "Giardinaggio", "Elettronica", "Casa", "Sport" };
				if (!Arrays.asList(categoriePredefinite).contains(eliminaCategoria)) {
					greetingService.eliminaCategoria(eliminaCategoria, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Errore durante l'eliminazione");
						}
						
						//Ricostruzione della homepage aggiornata con le categorie eventualmente eliminate
						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								AstaOnline a = new AstaOnline();
								a.amministrazioneCategoria();
								msgEliminazione.setInnerText("Categoria '" + eliminaCategoria + "' eliminata");
							}
						}
					});

				} else {
					Window.alert("Non puoi eliminare questa categoria!");
				}
			}

		});

		//Leggi metodo "inserimento"
		btnInsert.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				inserimento();
			}
		});
		
		//Leggi metodo "mostraSottoCategorie"
		btnShow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mostraSottoCategorie();
			}
		});

		//Leggi metodo "inserimentoPadre"
		btnInsertPadre.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				inserimentoPadre();
			}
		});

		//Button per la rinomina di una categoria scelta dal dropdown 
		btnRinomina.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String vecchioNome = rinominaCategoria.getLastSelectedNavLink().getText();
				
				//il nuovo nome della categoria non può essere vuoto
				if(!nuovoNome.getValue().isEmpty()) {
					String nuovoNomeCategoria = nuovoNome.getValue().trim(); 
					greetingService.rinominaCat(vecchioNome, nuovoNomeCategoria, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Non è stato possibile rinominare categoria");

						}
						
						//Ricostruisci la homePage una volta eseguita la rinomina 
						@Override
						public void onSuccess(Void result) {
							Window.alert("Rinomina riuscita");
							AstaOnline a = new AstaOnline();
							a.amministrazioneCategoria();
						}

					});
				} else Window.alert("Inserisci un nuovo nome per la categoria!");

			}

		});
	}

	//Inserimento di una categoria "padre", con sottocategorie null
	public void inserimentoPadre() {
		if(!categoriaPadre.getValue().isEmpty()) {
			String tmp = categoriaPadre.getValue();
			String categoria = tmp.substring(0, 1).toUpperCase() + tmp.substring(1);
			greetingService.inserisciSopraCategoria(categoria, new AsyncCallback<String>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore nell'inserimento della categoria");
				}
				
				//Ricostruzione della HomePage con la nuova categoria inserita 
				@Override
				public void onSuccess(String result) {
					Window.alert(result);
					AstaOnline a = new AstaOnline();
					a.amministrazioneCategoria();
				}

			});
		} else Window.alert("Inserisci un nome per la categoria!");
	}
	
	//Inserimento di una sottocategoria
	public void inserimento() {
		categoria = categoryDropdown.getLastSelectedNavLink().getText();
		if(!sottocategoria.getValue().isEmpty()) {
			String tmp = sottocategoria.getValue();
			sottoCat = tmp.substring(0, 1).toUpperCase() + tmp.substring(1);
			greetingService.inserisciSottoCategoria(categoria, sottoCat, new AsyncCallback<String>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Errore nell'inserimento della sottocategoria");

				}
				
				//Ricostruzione della homepage dopo l'inserimento della sottocategoria 
				@Override
				public void onSuccess(String result) {
					Window.alert("Sottocategoria inserita");
					AstaOnline a = new AstaOnline();
					a.amministrazioneCategoria();
				}

			});
		} else Window.alert("Inserisci il nome di una sottocategoria!");
	}
	
	//Metodo per mostrare tutte le sottocategorie di una categoria selezionata da un dropdown 
	public void mostraSottoCategorie() {
		cat = category.getLastSelectedNavLink().getText();
		category.setText(cat);
		greetingService.getTutteCategorie(cat, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Errore nel caricamento delle sottocategorie");

			}

			@Override
			public void onSuccess(String result) {
				String s = null;
				if (result != " ") {
					s = result.substring(2);
					String[] subCategories = s.split(",");
					String html = cat;
					for (int i = 0; i < subCategories.length; i++) {
						html = html + "<li style='margin-left: 2rem;'>" + subCategories[i] + "</li>";
					}
					albero.setInnerHTML("<ul>" + html + "</ul>");
				} else {
					albero.setInnerText(cat + " non ha sottocategorie ");
				}

			}

		});
	}

}
