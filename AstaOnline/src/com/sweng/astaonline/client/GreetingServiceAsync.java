package com.sweng.astaonline.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sweng.astaonline.shared.Domanda;
import com.sweng.astaonline.shared.Offerta;
import com.sweng.astaonline.shared.Oggetto;
import com.sweng.astaonline.shared.Risposta;
import com.sweng.astaonline.shared.Utente;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void sendUsername(String nomeUtente, String cognomeUtente, String usernameUtente, String telefonoUtente, String passwordUtente, String emailUtente, String cfUtente, String indirizzoUtente, String sessoUtente, String dataUtente, String luogoUtente, AsyncCallback<String> callback) throws IllegalArgumentException;
	void login(String username, String password, AsyncCallback<Boolean> callback) throws IllegalArgumentException; 
	void nomeUtente(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
	void cognomeUtente(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
	void telefonoUtente(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
	void emailUtente(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
	void cfUtente(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
	void indirizzoUtente(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
	void sessoUtente(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
	void dataUtente(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
	void luogoUtente(String username, AsyncCallback<String> callback) throws IllegalArgumentException;
	void vendiOggetti(String username, String nome, String descrizione, double prezzo, Date dataScadenza, String categoria, String img,  AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void inserisciSottoCategoria(String categoria, String sottocategoria, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getTutteCategorie(String categoria,AsyncCallback<String> callback )throws IllegalArgumentException;
	void inserisciSopraCategoria(String categoria, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getSopraCategorie(AsyncCallback<String> callback) throws IllegalArgumentException;
	void eliminaCategoria(String categoria, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void getOggettiS2C(AsyncCallback<ArrayList<Oggetto>> callback) throws IllegalArgumentException;
	void getInfoOggetto(String id, AsyncCallback<String[]> callback) throws IllegalArgumentException;
	void inserisciOfferta(String id, String usernameOfferente, double importo, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void getUltimaOfferta(String id, AsyncCallback<Double> callback) throws IllegalArgumentException;
	void filtraPerCategoria(String categoria,AsyncCallback<ArrayList<Oggetto>> callback ) throws IllegalArgumentException;
	void getOggettiVenduti(String venditore, AsyncCallback<ArrayList<Oggetto>> callback) throws IllegalArgumentException;
	void getStatoOggettoVeduto(String id, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getOggettiAcquistati(String username, AsyncCallback<ArrayList<Oggetto>> callback) throws IllegalArgumentException;
	void getStatoOggettoAcquistato(String id, String loggedUser, AsyncCallback<String> asyncCallback) throws IllegalArgumentException;
	void eliminaOggettoSelezionato(String id, AsyncCallback<Void> callback) throws IllegalArgumentException;
	void getStatoOggetto(String id, AsyncCallback<String> callback) throws IllegalArgumentException;
	void inviaDomanda(String userDomanda, String domanda, String idOggetto, AsyncCallback<Void> callback) throws IllegalArgumentException;
	void getDomande(String idOggetto, AsyncCallback<ArrayList<Domanda>> callback) throws IllegalArgumentException;
	void getVenditore (String idOggetto, AsyncCallback<String> asyncCallback) throws IllegalArgumentException;
	void getOfferta(AsyncCallback<ArrayList<Offerta>> asyncCallback)throws IllegalArgumentException;
	void inviaRisposta(String idDomanda, String risposta, String idUser, AsyncCallback<Void> callback) throws IllegalArgumentException;
	void getRisposta (String idDomanda, AsyncCallback<Risposta> callback) throws IllegalArgumentException;
	void eliminaOffertaSelezionato(String idOggetto, double importo, AsyncCallback<Boolean> callback) throws IllegalArgumentException;
	void getNomeOggetto(String id,AsyncCallback<String> callback) throws IllegalArgumentException;
	void getOffertaConNomeOggetto(AsyncCallback<ArrayList<Offerta>> asyncCallback) throws IllegalArgumentException;
	void rinominaCat(String vecchioNome, String nuovoNome, AsyncCallback<Void> callback) throws IllegalArgumentException;
	void getDomandaConNomeOggetto(AsyncCallback<ArrayList<Domanda>> asyncCallback) throws IllegalArgumentException;
	void eliminaDomanda(String id, AsyncCallback<Void> callback) throws IllegalArgumentException;
	void getTutteRisposte(AsyncCallback<ArrayList<Risposta>> asyncCallback) throws IllegalArgumentException;
	void eliminaRisposta(String idDomanda, String risposta, String user, AsyncCallback<Void> callback) throws IllegalArgumentException;
	void getAllSottocategorie(String categoria, AsyncCallback<ArrayList<String>> callback) throws IllegalArgumentException;
	void getAllSottocategorie2(String categoria,AsyncCallback<String[]> callback) throws IllegalArgumentException;
}
