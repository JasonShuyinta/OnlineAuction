package com.sweng.astaonline.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sweng.astaonline.shared.Domanda;
import com.sweng.astaonline.shared.Offerta;
import com.sweng.astaonline.shared.Oggetto;
import com.sweng.astaonline.shared.Risposta;
import com.sweng.astaonline.shared.Utente;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String sendUsername(String nomeUtente, String cognomeUtente,
			String usernameUtente, String telefonoUtente, 
			String passwordUtente, String emailUtente,
			String cfUtente, String indirizzoUtente,
			String sessoUtente, String dataUtente, String luogoUtente) throws IllegalArgumentException;
	boolean login(String username, String password) throws IllegalArgumentException;
	String nomeUtente(String username) throws IllegalArgumentException;
	String cognomeUtente(String username) throws IllegalArgumentException;
	String telefonoUtente(String username) throws IllegalArgumentException;
	String emailUtente(String username) throws IllegalArgumentException;
	String cfUtente(String username) throws IllegalArgumentException;
	String indirizzoUtente(String username) throws IllegalArgumentException;
	String sessoUtente(String username) throws IllegalArgumentException;
	String dataUtente(String username) throws IllegalArgumentException;
	String luogoUtente(String username) throws IllegalArgumentException;
	boolean vendiOggetti(String username, String nome,
			String descrizione, double prezzo, Date dataScadenza, String categoria, String img) throws IllegalArgumentException;
	String inserisciSottoCategoria(String categoria, String sottocategoria) throws IllegalArgumentException;
	String getTutteCategorie(String categoria) throws IllegalArgumentException;
	String inserisciSopraCategoria(String categoria) throws IllegalArgumentException;
	String getSopraCategorie() throws IllegalArgumentException;
	boolean eliminaCategoria(String categoria) throws IllegalArgumentException;
	ArrayList<Oggetto> getOggettiS2C() throws IllegalArgumentException;
	String[] getInfoOggetto(String id) throws IllegalArgumentException;
	boolean inserisciOfferta(String id, String usernameOfferente, double importo) throws IllegalArgumentException;
	double getUltimaOfferta(String id) throws IllegalArgumentException;
	ArrayList<Oggetto> filtraPerCategoria(String categoria) throws IllegalArgumentException;
	ArrayList<Oggetto> getOggettiVenduti(String venditore) throws IllegalArgumentException;
	String getStatoOggettoVeduto(String id) throws IllegalArgumentException;
	ArrayList<Oggetto> getOggettiAcquistati(String username) throws IllegalArgumentException;
	String getStatoOggettoAcquistato(String id, String loggedUser) throws IllegalArgumentException;
	void eliminaOggettoSelezionato(String id) throws IllegalArgumentException;
	String getStatoOggetto(String id) throws IllegalArgumentException;
	void inviaDomanda(String userDomanda, String domanda, String idOggetto) throws IllegalArgumentException;
	ArrayList<Domanda> getDomande(String idOggetto) throws IllegalArgumentException;
	String getVenditore (String idOggetto) throws IllegalArgumentException;
	ArrayList<Offerta> getOfferta() throws IllegalArgumentException;
	void inviaRisposta(String idDomanda, String risposta, String idUser) throws IllegalArgumentException;
	Risposta getRisposta (String idDomanda) throws IllegalArgumentException;
	Boolean eliminaOffertaSelezionato(String idOggetto, double importo) throws IllegalArgumentException;
	String getNomeOggetto(String id) throws IllegalArgumentException;
	ArrayList<Offerta> getOffertaConNomeOggetto() throws IllegalArgumentException;
	void rinominaCat(String vecchioNome, String nuovoNome) throws IllegalArgumentException;
	ArrayList<Domanda> getDomandaConNomeOggetto() throws IllegalArgumentException;
	void eliminaDomanda(String id) throws IllegalArgumentException;
	ArrayList<Risposta> getTutteRisposte() throws IllegalArgumentException;
	void eliminaRisposta(String idDomanda, String risposta, String user) throws IllegalArgumentException;
	ArrayList<String> getAllSottocategorie(String categoria) throws IllegalArgumentException;
	String[] getAllSottocategorie2(String categoria) throws IllegalArgumentException;
}
