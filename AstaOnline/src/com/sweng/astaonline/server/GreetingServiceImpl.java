package com.sweng.astaonline.server;

import com.sweng.astaonline.client.GreetingService;
import com.sweng.astaonline.shared.Categoria;
import com.sweng.astaonline.shared.Domanda;
import com.sweng.astaonline.shared.Offerta;
import com.sweng.astaonline.shared.Oggetto;
import com.sweng.astaonline.shared.Risposta;
import com.sweng.astaonline.shared.Utente;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import javax.servlet.ServletContext;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	//Metodo per la creazione/estrazione del DB Utenti
	private DB getDB() {
		ServletContext context = this.getServletContext();
		synchronized (context) {
			DB databaseUtenti = (DB) context.getAttribute("databaseUtenti");
			if (databaseUtenti == null) {
				databaseUtenti = DBMaker.newFileDB(new File("databaseUtenti")).closeOnJvmShutdown().make();
				context.setAttribute("databaseUtenti", databaseUtenti);

				//Inserimento dell'utente Amministratore con username "admin" e password "admin"
				Utente u = new Utente();
				Utente myAdmin = u.new Admin("admin", "admin");
				HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
				mappaUtenti.put("admin", myAdmin);
				databaseUtenti.commit();
			}
			return databaseUtenti;
		}
	}

	//Metodo per la creazione/estrazione del DB Oggetti
	private DB getDBOggetti() {
		ServletContext context = this.getServletContext();
		synchronized (context) {
			DB databaseOggetti = (DB) context.getAttribute("databaseOggetti");
			if (databaseOggetti == null) {
				databaseOggetti = DBMaker.newFileDB(new File("databaseOggetti")).closeOnJvmShutdown().make();
				context.setAttribute("databaseOggetti", databaseOggetti);
			}
			return databaseOggetti;
		}
	}

	//Metodo per la creazione/estrazione del DB Categoria
	private DB getDBCategoria() {
		ServletContext context = this.getServletContext();
		synchronized (context) {
			DB databaseCategoria = (DB) context.getAttribute("databaseCategoria");
			if (databaseCategoria == null) {
				databaseCategoria = DBMaker.newFileDB(new File("databaseCategoria")).closeOnJvmShutdown().make();
				context.setAttribute("databaseCategoria", databaseCategoria);
				HTreeMap<String, Categoria> mappaCategoria = databaseCategoria.getHashMap("databaseCategoria");
				ArrayList<String> sottoCategoria = new ArrayList<String>();

				//Inserimento delle categorie predefinite
				Categoria abbigliamento = new Categoria("Abbigliamento", sottoCategoria);
				Categoria giardinaggio = new Categoria("Giardinaggio", sottoCategoria);
				Categoria elettronica = new Categoria("Elettronica", sottoCategoria);
				Categoria casa = new Categoria("Casa", sottoCategoria);
				Categoria sport = new Categoria("Sport", sottoCategoria);
				mappaCategoria.put("Abbigliamento", abbigliamento);
				mappaCategoria.put("Giardinaggio", giardinaggio);
				mappaCategoria.put("Elettronica", elettronica);
				mappaCategoria.put("Casa", casa);
				mappaCategoria.put("Sport", sport);
				databaseCategoria.commit();
			}
			return databaseCategoria;
		}
	}

	//Metodo per la creazione/estrazione del DB Offerte
	public DB getDBOfferta() {
		ServletContext context = this.getServletContext();
		synchronized (context) {
			DB dbOfferta = (DB) context.getAttribute("databaseOfferta");
			if (dbOfferta == null) {
				dbOfferta = DBMaker.newFileDB(new File("databaseOfferta")).closeOnJvmShutdown().make();
				context.setAttribute("databaseOfferta", dbOfferta);
			}
			return dbOfferta;
		}
	}

	//Metodo per la creazione/estrazione del DB Domande
	public DB getDBDomanda() {
		ServletContext context = this.getServletContext();
		synchronized (context) {
			DB dbDomanda = (DB) context.getAttribute("databaseDomanda");
			if (dbDomanda == null) {
				dbDomanda = DBMaker.newFileDB(new File("databaseDomanda")).closeOnJvmShutdown().make();
				context.setAttribute("databaseDomanda", dbDomanda);
			}
			return dbDomanda;
		}
	}


	//Metodo per la creazione/estrazione del DB Risposta
	public DB getDBRisposta() {
		ServletContext context = this.getServletContext();
		synchronized (context) {
			DB dbRisposta = (DB) context.getAttribute("databaseRisposta");
			if (dbRisposta == null) {
				dbRisposta = DBMaker.newFileDB(new File("databaseRisposta")).closeOnJvmShutdown().make();
				context.setAttribute("databaseRisposta", dbRisposta);
			}
			return dbRisposta;
		}

	}

	//Metodo per l'inserimento di un'offerta per un determinato oggetto, il quale id viene passato come parametro
	public boolean inserisciOfferta(String id, String usernameOfferente, double importo) throws IllegalArgumentException {
		DB dbOfferte = getDBOfferta();
		HTreeMap<String, Offerta> mappaOfferta = dbOfferte.getHashMap("databaseOfferta");

		//Creazione di un id alfanumerico randomico
		String idOfferta = UUID.randomUUID().toString();
		Set<String> chiave = mappaOfferta.keySet();
		Offerta offerta = new Offerta(usernameOfferente, id, importo);
		boolean check = true;
		if(!mappaOfferta.isEmpty()) {
			for(String i : chiave) {
				if(mappaOfferta.get(i).getIdOggetto().equals(id) && mappaOfferta.get(i).getImporto()>importo) {
					check = false;
				}
			}
		}
		if(check) mappaOfferta.put(idOfferta, offerta);
		dbOfferte.commit();
		return check;
	}


	//Metodo per l'estrazione dell'ultima offerta fatta per un determinato oggetto il quale id 
	//viene passato come parametro
	public double getUltimaOfferta(String id) throws IllegalArgumentException {
		DB dbOfferte = getDBOfferta();
		HTreeMap<String, Offerta> mappaOfferta = dbOfferte.getHashMap("databaseOfferta");
		Set<String> chiave = mappaOfferta.keySet();

		//Se non vi è nessuna offerta viene ritornato -1
		double importo = -1; 
		if(!mappaOfferta.isEmpty()) {
			for(String i : chiave) {
				if (mappaOfferta.get(i).getIdOggetto().equals(id) && mappaOfferta.get(i).getImporto()>importo){
					importo = mappaOfferta.get(i).getImporto();
				}
			}
		}
		return importo;
	}

	//Metodo per l'eliminazione di un oggetto (azione che può essere compiuta solo da un Amministratore)
	public void eliminaOggettoSelezionato(String id) throws IllegalArgumentException {
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		mappaOggetti.remove(id);

		//L'eliminazione dell'oggetto comporta anche l'eliminazione di tutte le relative offerte 
		//riguardanti quell'oggetto
		DB dbOfferta = getDBOfferta();
		HTreeMap<String, Offerta> mappaOfferta = dbOfferta.getHashMap("databaseOfferta");
		Set<String> chiave = mappaOfferta.keySet();
		for(String i: chiave) {
			if (mappaOfferta.get(i).getIdOggetto().trim().equals(id.trim())) {
				mappaOfferta.remove(i);

			}
		}
	}


	//Metodo per l'eliminazione di un offerta (azione che può essere compiuta solo da un Amministratore)
	public Boolean eliminaOffertaSelezionato(String idOggetto, double importo) throws IllegalArgumentException {
		boolean eliminato = false;
		DB dbOfferta = getDBOfferta();
		HTreeMap<String, Offerta> mappaOfferta = dbOfferta.getHashMap("databaseOfferta");
		Set<String> chiave = mappaOfferta.keySet();
		for(String i: chiave) {
			if (mappaOfferta.get(i).getIdOggetto().trim().equals(idOggetto.trim()) && mappaOfferta.get(i).getImporto()==importo) {
				mappaOfferta.remove(i);
				eliminato = true;
			}
		}
		return eliminato;
	}
	
	public String[] getAllSottocategorie2(String categoria) throws IllegalArgumentException {
		DB dbCategoria = getDBCategoria();
		HTreeMap<String, Categoria> mappaCategoria = dbCategoria.getHashMap("databaseCategoria");
		ArrayList<String> sottoCategoria = new ArrayList<String>();
		Set<String> chiaveCategoria = mappaCategoria.keySet();

		for(String i : chiaveCategoria) {
			if(mappaCategoria.get(i).getCategoria().equals(categoria.trim())) {
				sottoCategoria.addAll(mappaCategoria.get(i).getSottoCategoria());
			}
		}

		for(int k = 0; k < sottoCategoria.size(); k++) {
			for(String x : chiaveCategoria) {
				if(mappaCategoria.get(x).getCategoria().equals(sottoCategoria.get(k).trim())) {
					sottoCategoria.addAll(mappaCategoria.get(x).getSottoCategoria());
				}
			}

		}
		
		String[] array = new String[sottoCategoria.size()];
		array = sottoCategoria.toArray(array);
		
		return array;
	}
	
	//Ottengo tutte le sottocategorie della categoria passata come input
	public ArrayList<String> getAllSottocategorie (String categoria) throws IllegalArgumentException {
		DB dbCategoria = getDBCategoria();
		HTreeMap<String, Categoria> mappaCategoria = dbCategoria.getHashMap("databaseCategoria");
		ArrayList<String> sottoCategoria = new ArrayList<String>();
		Set<String> chiaveCategoria = mappaCategoria.keySet();

		for(String i : chiaveCategoria) {
			if(mappaCategoria.get(i).getCategoria().equals(categoria.trim())) {
				sottoCategoria.addAll(mappaCategoria.get(i).getSottoCategoria());
			}
		}

		for(int k = 0; k < sottoCategoria.size(); k++) {
			for(String x : chiaveCategoria) {
				if(mappaCategoria.get(x).getCategoria().equals(sottoCategoria.get(k).trim())) {
					sottoCategoria.addAll(mappaCategoria.get(x).getSottoCategoria());
				}
			}

		}
		
		return sottoCategoria;
	}

	//Metodo per filtrare gli oggetti visualizzati in HomePage in base alla categoria selezionata
	public ArrayList<Oggetto> filtraPerCategoria(String categoria) throws IllegalArgumentException {
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		Set<String> chiave = mappaOggetti.keySet();
		ArrayList<Oggetto> arrayFiltrato = new ArrayList<Oggetto>();
		//int j = 0;

		ArrayList<String> sottoCategoria = getAllSottocategorie(categoria);
		
		
		//Iterazione di tutti gli oggetti nel DBOggetti
		for(String i: chiave) {
			Oggetto o = new Oggetto(mappaOggetti.get(i).getUsername(), mappaOggetti.get(i).getId(), 
					mappaOggetti.get(i).getNome(), mappaOggetti.get(i).getDescrizione(), mappaOggetti.get(i).getPrezzo(), 
					mappaOggetti.get(i).getScadenza(), mappaOggetti.get(i).getCategoria(), mappaOggetti.get(i).getImgUrl());
			
			
			//Se l'oggetto i-esimo appartiene alla categoria passata in input allora aggiungilo all'arraylist
			if(o.getCategoria().trim().equals(categoria.trim())) {
				arrayFiltrato.add(o);	
			}
			for(int h = 0; h < sottoCategoria.size(); h++) {
				if(o.getCategoria().trim().equals(sottoCategoria.get(h).trim())) {
					arrayFiltrato.add(o);
				}
			}
			
		}

		//Ordinamento degli oggetti in base alla data di scadenza (dal più recente al più lontano)
		//tramite algoritmo di BubbleSort
		Date date= null;
		Date date2 = null;

		Oggetto tmp;
		int n = arrayFiltrato.size();

		for(int i = 0; i < n; i++) {
			for(int k = 1; k < (n-i); k++) {
				date = arrayFiltrato.get(k-1).getScadenza();
				date2 = arrayFiltrato.get(k).getScadenza();
				if(date.after(date2)) {
					tmp = arrayFiltrato.get(k-1);
					arrayFiltrato.set(k-1, arrayFiltrato.get(k));
					arrayFiltrato.set(k, tmp);
				}
			}
		}
		//Rimozione dall'arraylist di tutti gli oggetti che hanno la scadenza dell'asta 
		//successiva alla data odierna
		Date oggi = new Date();
		for(int i = 0; i < arrayFiltrato.size(); i++) {
			if(arrayFiltrato.get(i).getScadenza().before(oggi)) {
				arrayFiltrato.remove(i);
			}
		}

		return arrayFiltrato;
	}


	//Metodo per l'inserimento di una categoria (azione che può essere compiuta solo da un Amministratore)
	public String inserisciSopraCategoria(String categoria) throws IllegalArgumentException {
		DB dbCategoria = getDBCategoria();
		HTreeMap<String, Categoria> mappaCategoria = dbCategoria.getHashMap("databaseCategoria");
		ArrayList<String> sottoCategoria = new ArrayList<String>();
		Set<String> chiave = mappaCategoria.keySet();
		String res = "";
		for(String i : chiave) {
			if(i.contentEquals(categoria)) {
				res = "Categoria gia' presente";
			}
		}
		Categoria c = new Categoria(categoria, sottoCategoria);
		mappaCategoria.put(categoria, c);
		dbCategoria.commit();
		res = "Nuova categoria inserita";
		return res;
	}

	//Metodo per l'inserimento di una sottocategoria passando la categoria padre come input
	//(azione che può essere compiuta solo da un Amministratore)
	public String inserisciSottoCategoria(String categoria, String sottocategoria) throws IllegalArgumentException {
		DB dbCategoria = getDBCategoria();
		HTreeMap<String, Categoria> mappaCategoria = dbCategoria.getHashMap("databaseCategoria");
		Set<String> chiave = mappaCategoria.keySet();
		ArrayList<String> figli = new ArrayList<String>();
		ArrayList<String> figli2 = new ArrayList<String>();
		String temp = null;
		String res = "Operazione fallita";
		for (String i : chiave) {

			if (mappaCategoria.get(i).getCategoria().trim().contentEquals(categoria.trim())) {

				if (mappaCategoria.get(i).getSottoCategoria().isEmpty()) {

					figli.add(sottocategoria.trim());
					mappaCategoria.remove(i);
					Categoria c = new Categoria(categoria.trim(), figli);
					mappaCategoria.put(categoria, c);
					Categoria c2 = new Categoria(sottocategoria.trim(), figli2);
					mappaCategoria.put(sottocategoria, c2);
					dbCategoria.commit();
					res = "Operazione riuscita";

				} else {
					figli.addAll(mappaCategoria.get(i).getSottoCategoria());

					//Ottieni la lista aggiornata dei figli della categoria padre
					for (int j = 0; j < figli.size(); j++) {
						temp = temp + " " + figli.get(j);
					}
					
					figli.add(sottocategoria.trim());
					mappaCategoria.remove(i);
					Categoria c = new Categoria(categoria.trim(), figli);
					mappaCategoria.put(categoria, c);
					Categoria c2 = new Categoria(sottocategoria.trim(), figli2);
					mappaCategoria.put(sottocategoria, c2);
					dbCategoria.commit();
					res = "Operazione riuscita";
				}
				break;
			}
		}
		return temp;
	}
	
	//Metodo per l'eliminazione di una categoria (azione permessa solo da un Amministratore)
	public boolean eliminaCategoria(String categoria) {
		DB dbCategoria = getDBCategoria();
		HTreeMap<String, Categoria> mappaCategoria = dbCategoria.getHashMap("databaseCategoria");
		Set<String> chiave = mappaCategoria.keySet();
		ArrayList<String> figli = new ArrayList<String>();
		ArrayList<String> nipoti = new ArrayList<String>();
		String padre = null;
		boolean res = false;
		
		//Cerca la categoria passata in input ed eliminala dal database, salvando le sottocategorie in un arraylist
		for(String i : chiave) {
			if(mappaCategoria.get(i).getCategoria().trim().contentEquals(categoria.trim())) {
				res = true;
				figli.addAll(mappaCategoria.get(i).getSottoCategoria());
				mappaCategoria.remove(i);
				dbCategoria.commit();
				break;
			}
		}
		
		boolean bool = false;
		//Cerchiamo tra tutte le sottocategorie se è presente la categoria passata in input
		//Se è presente, la i-esima categoria è la categoria "padre" della categoria passata in input
		//Quindi tra le sottocategorie della i-esima categoria, bisogna togliere la categoria passata in input
		for(String i : chiave) {
			nipoti.addAll(mappaCategoria.get(i).getSottoCategoria());
			for(int j = 0; j < nipoti.size(); j++) {
				if(nipoti.get(j).trim().contentEquals(categoria.trim())) {
					padre = mappaCategoria.get(i).getCategoria();
					for(int k = 0; k < nipoti.size(); k++) {
						if(nipoti.get(k).trim().contentEquals(categoria.trim())) {
							nipoti.remove(k);
						}
					}
					//Togliamo la categoria passata in input
					//Alla categoria padre della categoria passata in input, aggiungiamo i figli 
					//della categoria passata in input
					mappaCategoria.remove(i);
					nipoti.addAll(figli);
					Categoria c = new Categoria(padre, nipoti);
					mappaCategoria.put(padre, c);
					dbCategoria.commit();
					bool = true;
					break;
				}
			}
			if(bool) break;
		}
		
		//Eliminata la categoria, bisogna modificare la categoria degli oggetti appartenenti alla 
		//categoria appena eliminata. La nuova categoria dell'oggetto corrisponderà alla categoria padre 
		//della categoria eliminata
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		Set<String> chiaviOggetti = mappaOggetti.keySet();
		for(String i : chiaviOggetti) {
			if(mappaOggetti.get(i).getCategoria().trim().equals(categoria.trim())) {
				Oggetto o = new Oggetto(mappaOggetti.get(i).getUsername(), 
						mappaOggetti.get(i).getId(),
						mappaOggetti.get(i).getNome(),
						mappaOggetti.get(i).getDescrizione(),
						mappaOggetti.get(i).getPrezzo(),
						mappaOggetti.get(i).getScadenza(),
						padre,
						mappaOggetti.get(i).getImgUrl());
				mappaOggetti.remove(i);
				mappaOggetti.put(o.getId(), o);
				dbOggetti.commit();
			}
		}

		return res;
	}

	//Metodo per ottenere tutte le categorie nel DBCategorie
	public String getSopraCategorie() {
		DB dbCategoria = getDBCategoria();
		HTreeMap<String, Categoria> mappaCategoria = dbCategoria.getHashMap("databaseCategoria");
		Set<String> chiave = mappaCategoria.keySet();
		String res = "";
		for(String i : chiave) {
			res = res + ", " + i;
		}
		return res;
	}
	
	//Metodo per ottenere le sottocategorie della categoria passata in input
	public String getTutteCategorie(String categoria) {
		DB dbCategoria = getDBCategoria();
		HTreeMap<String, Categoria> mappaCategoria = dbCategoria.getHashMap("databaseCategoria");
		Set<String> chiave = mappaCategoria.keySet();
		ArrayList<String> sottocat = new ArrayList<String>();
		String res = " ";
		for (String i : chiave) {
			if (mappaCategoria.get(i).getCategoria().trim().contentEquals(categoria.trim())
					&& !mappaCategoria.get(i).getSottoCategoria().isEmpty()) {
				for (int j = 0; j < mappaCategoria.get(i).getSottoCategoria().size(); j++) {
					res = res + ", " + mappaCategoria.get(i).getSottoCategoria().get(j);
				}
			}
		}
		return res;
	}
	
	//Metodo per rinominare una categoria
	public void rinominaCat(String vecchioNome, String nuovoNome) throws IllegalArgumentException {
		DB dbCategoria = getDBCategoria();
		HTreeMap<String, Categoria> mappaCategoria = dbCategoria.getHashMap("databaseCategoria");
		Set<String> chiave = mappaCategoria.keySet();
		ArrayList<String> sottoCategoria = new ArrayList<String>();
		
		//Cerco la categoria a cui voglio cambiare nome, e salvo i suoi figli in un arraylist e in seguito 
		//elimino la categoria interessata
		for(String i : chiave) {
			if(mappaCategoria.get(i).getCategoria().trim().contentEquals(vecchioNome.trim())) {
				sottoCategoria.addAll(mappaCategoria.get(i).getSottoCategoria());
				mappaCategoria.remove(i);
				
				//Creo un nuovo oggetto categoria con i figli della categoria eliminata e lo aggiungo al DB
				Categoria c = new Categoria(nuovoNome, sottoCategoria);
				mappaCategoria.put(nuovoNome, c);
				dbCategoria.commit();
			}
		}
		
		//Rinominare la categoria degli oggetti che appartengono alla categoria a cui è appena 
		//stato cambiato nome
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		Set<String> chiaviOggetti = mappaOggetti.keySet();
		for(String i : chiaviOggetti) {
			if(mappaOggetti.get(i).getCategoria().trim().equals(vecchioNome.trim())) {
				Oggetto o = new Oggetto(mappaOggetti.get(i).getUsername(), 
						mappaOggetti.get(i).getId(),
						mappaOggetti.get(i).getNome(),
						mappaOggetti.get(i).getDescrizione(),
						mappaOggetti.get(i).getPrezzo(),
						mappaOggetti.get(i).getScadenza(),
						nuovoNome,
						mappaOggetti.get(i).getImgUrl());
				mappaOggetti.remove(i);
				mappaOggetti.put(o.getId(), o);
				dbOggetti.commit();
			}
		}
	}


	//Metodo per ottenere la lista delle offerte 
	public ArrayList<Offerta> getOfferta() throws IllegalArgumentException {
		DB dbOffeta = getDBOfferta();
		HTreeMap<String, Offerta> mappaOfferta = dbOffeta.getHashMap("databaseOfferta");
		Set<String> chiave = mappaOfferta.keySet();
		ArrayList<Offerta> arrayOfferte = new ArrayList<Offerta>();
		if(!mappaOfferta.isEmpty()) {
			for (String i : chiave) {
				Offerta o = new Offerta (mappaOfferta.get(i).getUsername(), mappaOfferta.get(i).getIdOggetto(), mappaOfferta.get(i).getImporto());
				arrayOfferte.add(o);
			} 
		}

		return arrayOfferte;
	}

	//Metodo per ottenere la lista delle offerte con il nome dell' oggetto
	public ArrayList<Offerta> getOffertaConNomeOggetto() throws IllegalArgumentException {
		DB dbOffeta = getDBOfferta();
		HTreeMap<String, Offerta> mappaOfferta = dbOffeta.getHashMap("databaseOfferta");
		Set<String> chiave = mappaOfferta.keySet();
		ArrayList<Offerta> arrayOfferte = new ArrayList<Offerta>();
		if(!mappaOfferta.isEmpty()) {
			for (String i : chiave) {
				DB dbOggetti = getDBOggetti();
				HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");

				Offerta o = new Offerta (mappaOfferta.get(i).getUsername(), mappaOfferta.get(i).getIdOggetto(), mappaOfferta.get(i).getImporto(), mappaOggetti.get(mappaOfferta.get(i).getIdOggetto()).getNome());
				arrayOfferte.add(o);
			} 
		}

		return arrayOfferte;
	}

	//Metodo per ottenere la lista delle domande con il nome dell' oggetto
	public ArrayList<Domanda> getDomandaConNomeOggetto() throws IllegalArgumentException {
		DB dbDomanda = getDBDomanda();
		HTreeMap<String, Domanda> mappaDomanda = dbDomanda.getHashMap("databaseDomanda");
		Set<String> chiave = mappaDomanda.keySet();
		ArrayList<Domanda> arrayDomanda = new ArrayList<Domanda>();
		if(!mappaDomanda.isEmpty()) {
			for (String i : chiave) {
				DB dbOggetti = getDBOggetti();
				HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");

				Domanda d = new Domanda (mappaDomanda.get(i).getIdDomanda(), mappaDomanda.get(i).getUserDomanda(), mappaDomanda.get(i).getDomanda(), mappaDomanda.get(i).getIdOggetto(), mappaOggetti.get(mappaDomanda.get(i).getIdOggetto()).getNome());
				arrayDomanda.add(d);
			} 
		}

		return arrayDomanda;
	}
	
	//Metodo per eliminare una domanda in base all'id passato come input
	public void eliminaDomanda(String id) throws IllegalArgumentException {
		DB dbDomanda = getDBDomanda();
		HTreeMap<String, Domanda> mappaDomanda = dbDomanda.getHashMap("databaseDomanda");
		mappaDomanda.remove(id.trim());

		DB dbRisposta = getDBRisposta();
		HTreeMap<String, Risposta> mappaRisposta = dbRisposta.getHashMap("databaseRisposta");
		Set<String> chiave = mappaRisposta.keySet();
		if(!mappaRisposta.isEmpty()) {
			for(String i : chiave) {
				if (mappaRisposta.get(i).getIdDomanda().trim().equals(id.trim())) {
					mappaRisposta.remove(i);
				}
			}
		}
	}

	//Metodo per salvare un utente che vuole effettuare la registrazione al sito
	public String sendUsername(String nomeUtente, String cognomeUtente, String usernameUtente, String telefonoUtente,
			String passwordUtente, String emailUtente, String cfUtente, String indirizzoUtente, String sessoUtente,
			String dataUtente, String luogoUtente) throws IllegalArgumentException {

		DB databaseUtenti = getDB();
		HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
		Set<String> chiave = mappaUtenti.keySet();

		String myResult = null;

		Utente utente = new Utente(nomeUtente, cognomeUtente, usernameUtente, telefonoUtente, passwordUtente,
				emailUtente, cfUtente, indirizzoUtente, sessoUtente, dataUtente, luogoUtente);

		boolean disp = true;
		for (String i : chiave) {
			if (mappaUtenti.get(i).getUsername().contentEquals(usernameUtente)) {
				myResult = "Errore, username non disponibile";
				disp = false;
				break;
			}
		}
		if (disp == true) {
			mappaUtenti.put(usernameUtente, utente);
			databaseUtenti.commit();
			myResult = "Inserimento di " + usernameUtente + "  avvenuto con successo";
		}
		return myResult;
	}

	//Metodo per mettere in vendita un oggetto
	public boolean vendiOggetti(String username, String nome, String descrizione, double prezzo, Date dataScadenza,
			String categoria, String img) throws IllegalArgumentException {
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		
		//Stringa alfanumerica randomica per la creazione di un id univoco
		String id = UUID.randomUUID().toString();
		Oggetto oggetto = new Oggetto(username, id, nome, descrizione, prezzo, dataScadenza, categoria, img);
		mappaOggetti.put(id, oggetto);
		dbOggetti.commit();
		return true;
	}

	//Metodo per ottenere la lista di tutti gli oggetti ancora in asta
	public ArrayList<Oggetto> getOggettiS2C() throws IllegalArgumentException {
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		Set<String> chiave = mappaOggetti.keySet();
		Oggetto[] array = new Oggetto[chiave.size()];
		ArrayList<Oggetto> arraylist = new ArrayList<Oggetto>();
		int j = 0;

		for(String i: chiave) {
			Oggetto o = new Oggetto(mappaOggetti.get(i).getUsername(), mappaOggetti.get(i).getId(), 
					mappaOggetti.get(i).getNome(), mappaOggetti.get(i).getDescrizione(), mappaOggetti.get(i).getPrezzo(), 
					mappaOggetti.get(i).getScadenza(), mappaOggetti.get(i).getCategoria(), mappaOggetti.get(i).getImgUrl());
			array[j] = o;
			j++;
		}
		
		//Ordinamento degli oggetti in base alla scadenza tramite BubbleSort
		Date date= null;
		Date date2 = null;

		Oggetto tmp;
		int n = array.length;
		
		for(int i = 0; i < n; i++) {
			for(int k = 1; k < (n-i); k++) {
				date = array[k-1].getScadenza();
				date2 = array[k].getScadenza();
				if(date.after(date2)) {
					tmp = array[k-1];
					array[k-1] = array[k];
					array[k] = tmp;
				}
			}
		}
		//Rimozione dall'arraylist degli oggetti che non sono più in asta
		Collections.addAll(arraylist, array);
		Date oggi = new Date();
		for(int i = 0; i < arraylist.size(); i++) {
			if(arraylist.get(i).getScadenza().before(oggi)) {
				arraylist.remove(i);
			}
		}

		return arraylist;
	}
	
	//Metodo per ottenere tutte le informazioni riguardanti un ogggetto il cui id 
	//viene passato in input
	public String[] getInfoOggetto(String id) throws IllegalArgumentException {
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		return mappaOggetti.get(id).getOggetto();
	}
	
	//Metodo per ottenere il nome di un ogggetto il cui id 
	//viene passato in input
	public String getNomeOggetto(String id) throws IllegalArgumentException {
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		return mappaOggetti.get(id.trim()).getNome();
	}
	
	//Metodo per effettuare il login 
	public boolean login(String username, String password) throws IllegalArgumentException {
		DB databaseUtenti = getDB();
		HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
		Set<String> chiave = mappaUtenti.keySet();
		boolean logged = false;
		for (String i : chiave) {

			if (mappaUtenti.get(i).getUsername().contentEquals(username)) {
				if (mappaUtenti.get(i).getPassword().contentEquals(password)) {
					logged = true;
				}
				break;
			}
		}
		return logged;
	}
	
	//Metodo per ottenere gli oggetti venduti da un utente passando lo username come input
	public ArrayList<Oggetto> getOggettiVenduti(String venditore) throws IllegalArgumentException {
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		Set<String> chiave = mappaOggetti.keySet();
		Oggetto[] array = new Oggetto[chiave.size()];
		ArrayList<Oggetto> arrayOggettiVenduti = new ArrayList<Oggetto>();
		int j = 0;

		for(String i: chiave) {
			Oggetto o = new Oggetto(mappaOggetti.get(i).getUsername(), mappaOggetti.get(i).getId(), 
					mappaOggetti.get(i).getNome(), mappaOggetti.get(i).getDescrizione(), mappaOggetti.get(i).getPrezzo(), 
					mappaOggetti.get(i).getScadenza(), mappaOggetti.get(i).getCategoria(), mappaOggetti.get(i).getImgUrl());
			if(o.getUsername().trim().equals(venditore.trim())) {
				arrayOggettiVenduti.add(o);
			}
		}
		
		Date date= null;
		Date date2 = null;

		Oggetto tmp;
		int n = arrayOggettiVenduti.size();

		for(int i = 0; i < n; i++) {
			for(int k = 1; k < (n-i); k++) {
				date = arrayOggettiVenduti.get(k-1).getScadenza();
				date2 = arrayOggettiVenduti.get(k).getScadenza();

				if(date.after(date2)) {
					tmp = arrayOggettiVenduti.get(k-1);
					arrayOggettiVenduti.set(k-1, arrayOggettiVenduti.get(k));
					arrayOggettiVenduti.set(k, tmp);
				}
			}
		}

		return arrayOggettiVenduti;
	}
	
	
	//Metodo per ottenere lo stato dell'asta di un oggetto messo in vendita
	public String getStatoOggettoVeduto(String id) throws IllegalArgumentException {
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");

		DB dbOfferte = getDBOfferta();
		HTreeMap<String, Offerta> mappaOfferta = dbOfferte.getHashMap("databaseOfferta");
		Set<String> chiave = mappaOfferta.keySet();
		double importo = -1;
		String vincitore = null;

		Date oggi = new Date();
		if(mappaOggetti.get(id).getScadenza().before(oggi)) {
			
			//Asta conclusa
			if(!mappaOfferta.isEmpty()) {
				for(String i : chiave) {
					if (mappaOfferta.get(i).getIdOggetto().equals(id) && mappaOfferta.get(i).getImporto()>importo){
						importo = mappaOfferta.get(i).getImporto();
						vincitore = mappaOfferta.get(i).getUsername();
					}
				}
			}
			if (importo==-1) {
				return "Asta conclusa e oggetto non aggiudicato";
			} else {
				return "Asta conclusa e oggetto aggiudicato da "+ vincitore + " a " + importo;
			}
		} else {
			
			//Asta aperta
			if(!mappaOfferta.isEmpty()) {
				for(String i : chiave) {
					if (mappaOfferta.get(i).getIdOggetto().equals(id) && mappaOfferta.get(i).getImporto()>importo){
						importo = mappaOfferta.get(i).getImporto();
						vincitore = mappaOfferta.get(i).getUsername();
					}
				}
			}
			if (importo==-1) {
				return "Asta in corso ";
			} else {
				return "Asta in corso - offerta attuale: " + vincitore + " " + importo;
			}
		}
	}
	
	//Metodo per ottenere gli oggetti a cui è stata fatta un offerta da un utente il cui username
	//viene passato in input
	public ArrayList<Oggetto> getOggettiAcquistati(String username) throws IllegalArgumentException {
		DB dbOfferte = getDBOfferta();
		HTreeMap<String, Offerta> mappaOfferta = dbOfferte.getHashMap("databaseOfferta");
		Set<String> chiave = mappaOfferta.keySet();
		ArrayList<String> arrayIdOggetti = new ArrayList<String>();
		Boolean check= true;
		
		//Evita l'inserimento multiplo di un oggetto all'interno dell'arraylist se l'utente ha fatto più offerte
		//per il medesimo oggetto
		for(String i : chiave) {
			if (mappaOfferta.get(i).getUsername().trim().equals(username.trim())){
				check = true;
				for (int j=0; j<arrayIdOggetti.size(); j++) {
					if (arrayIdOggetti.get(j).trim().equals(mappaOfferta.get(i).getIdOggetto().trim())) {
						check= false;
					}
				}
				if (check == true) {
					arrayIdOggetti.add(mappaOfferta.get(i).getIdOggetto().trim());
				}
			}
		}
		
		//estrazione degli oggetti a cui è stata fatta un'offerta
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		ArrayList<Oggetto> arrayOggettiAcquistati = new ArrayList<Oggetto>();
		for (int i=0; i<arrayIdOggetti.size(); i++) {
			Oggetto o = new Oggetto(mappaOggetti.get(arrayIdOggetti.get(i).trim()).getUsername(), mappaOggetti.get(arrayIdOggetti.get(i).trim()).getId(), 
					mappaOggetti.get(arrayIdOggetti.get(i).trim()).getNome(), mappaOggetti.get(arrayIdOggetti.get(i).trim()).getDescrizione(), mappaOggetti.get(arrayIdOggetti.get(i).trim()).getPrezzo(), 
					mappaOggetti.get(arrayIdOggetti.get(i).trim()).getScadenza(), mappaOggetti.get(arrayIdOggetti.get(i).trim()).getCategoria(), mappaOggetti.get(arrayIdOggetti.get(i).trim()).getImgUrl());
			arrayOggettiAcquistati.add(o);
		}

		return arrayOggettiAcquistati;
	}

	//Metodo per verificare l'aggiudicarsi dell'oggetto da parte dell'utente passato in input
	public String getStatoOggettoAcquistato(String id, String loggedUser) throws IllegalArgumentException {
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");

		DB dbOfferte = getDBOfferta();
		HTreeMap<String, Offerta> mappaOfferta = dbOfferte.getHashMap("databaseOfferta");
		Set<String> chiave = mappaOfferta.keySet();
		double importo = -1;
		String vincitore = null;

		Date oggi = new Date();
		if(mappaOggetti.get(id).getScadenza().before(oggi)) {
			//Asta conclusa
			if(!mappaOfferta.isEmpty()) {
				for(String i : chiave) {
					if (mappaOfferta.get(i).getIdOggetto().equals(id) && mappaOfferta.get(i).getImporto()>importo){
						importo = mappaOfferta.get(i).getImporto();
						vincitore = mappaOfferta.get(i).getUsername();
					}
				}
			}
			if (vincitore.trim().equals(loggedUser.trim())) {
				return "Asta conclusa e oggetto aggiudicato a " + importo;
			} else {
				return "Asta conclusa e oggetto non aggiudicato";
			}
		} else {
			//Asta aperta
			if(!mappaOfferta.isEmpty()) {
				for(String i : chiave) {
					if (mappaOfferta.get(i).getIdOggetto().equals(id) && mappaOfferta.get(i).getImporto()>importo){
						importo = mappaOfferta.get(i).getImporto();
						vincitore = mappaOfferta.get(i).getUsername();
					}
				}
			}
			if (vincitore.trim().equals(loggedUser.trim())) {
				return "Asta in corso e offerta migliore: " + importo;
			} else {
				return "Asta in corso e offerta superata da: " + vincitore + " con " + importo;
			}
		}
	}


	//Metodo per ottenere lo stato dell'oggetto 
	public String getStatoOggetto(String id) throws IllegalArgumentException {
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		Date oggi = new Date();
		if(mappaOggetti.get(id.trim()).getScadenza().before(oggi)) {
			DB dbOfferte = getDBOfferta();
			HTreeMap<String, Offerta> mappaOfferta = dbOfferte.getHashMap("databaseOfferta");
			Set<String> chiave = mappaOfferta.keySet();
			double importo = -1; 
			String vincitore=null;
			if(!mappaOfferta.isEmpty()) {
				//asta conclusa
				for(String i : chiave) {
					if (mappaOfferta.get(i).getIdOggetto().equals(id) && mappaOfferta.get(i).getImporto()>importo){
						importo = mappaOfferta.get(i).getImporto();
						vincitore = mappaOfferta.get(i).getUsername();
					}
				}

				if (importo!=-1) {
					return "Asta aggiudicata da " + vincitore + " a " + importo;
				}
			}
		} else {
			return "Asta in corso";
		}
		return "Asta conclusa con nessuna offerta";
	}


	//Metodo per inviare una domanda riguardante un oggetto al venditore di tale oggetto
	public void inviaDomanda(String userDomanda, String domanda, String idOggetto) throws IllegalArgumentException {
		DB dbDomanda = getDBDomanda();
		HTreeMap<String, Domanda> mappaDomanda = dbDomanda.getHashMap("databaseDomanda");
		String idDomanda = UUID.randomUUID().toString();
		Domanda question = new Domanda(idDomanda, userDomanda, domanda, idOggetto);
		mappaDomanda.put(idDomanda, question);
		dbDomanda.commit();
	}

	//Metodo per rispondere a una domanda riguardante un oggetto 
	public void inviaRisposta(String idDomanda, String risposta, String idUser) throws IllegalArgumentException {
		DB dbRisposta = getDBRisposta();
		HTreeMap<String, Risposta> mappaRisposta = dbRisposta.getHashMap("databaseRisposta");
		String idRisposta = UUID.randomUUID().toString();
		Risposta r = new Risposta(idDomanda, risposta, idUser);
		mappaRisposta.put(idDomanda, r);
		dbRisposta.commit();
	}
	
	//Metodo per estrarre la risposta data la domanda passata in input
	public Risposta getRisposta (String idDomanda) {
		DB dbRisposta = getDBRisposta();
		HTreeMap<String, Risposta> mappaRisposta = dbRisposta.getHashMap("databaseRisposta");
		Set<String> chiave = mappaRisposta.keySet();
		if(!mappaRisposta.isEmpty()) {
			for(String i : chiave) {
				if (mappaRisposta.get(i).getIdDomanda().trim().equals(idDomanda.trim())) {
					return mappaRisposta.get(i);
				}
			}
		}
		return null;
	}

	//Metodo l'estrazione delle risposte 
	public ArrayList<Risposta> getTutteRisposte () {
		DB dbRisposta = getDBRisposta();
		HTreeMap<String, Risposta> mappaRisposta = dbRisposta.getHashMap("databaseRisposta");
		Set<String> chiaveRisposta = mappaRisposta.keySet();

		DB dbDomanda = getDBDomanda();
		HTreeMap<String, Domanda> mappaDomanda = dbDomanda.getHashMap("databaseDomanda");

		DB dbUtente = getDB();
		HTreeMap<String, Utente> mappaUtente = dbUtente.getHashMap("databaseUtenti");

		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");

		ArrayList<Risposta> arraylist = new ArrayList<Risposta>();
		if(!mappaRisposta.isEmpty()) {
			for(String i : chiaveRisposta) {
				Risposta r = new Risposta(mappaDomanda.get(mappaRisposta.get(i).getIdDomanda().trim()).getDomanda(), mappaRisposta.get(i).getRisposta(), mappaUtente.get(mappaRisposta.get(i).getIdUser().trim()).getUsername(), mappaOggetti.get(mappaDomanda.get(mappaRisposta.get(i).getIdDomanda().trim()).getIdOggetto().trim()).getNome(), mappaRisposta.get(i).getIdDomanda());
				arraylist.add(r);
			}
		}
		return arraylist;
	}

	//Metodo per l'eliminazione della risposta (azione permessa solo ad un amministratore)
	public void eliminaRisposta(String idDomanda, String risposta, String user) {
		DB dbRisposta = getDBRisposta();
		HTreeMap<String, Risposta> mappaRisposta = dbRisposta.getHashMap("databaseRisposta");
		Set<String> chiaveRisposta = mappaRisposta.keySet();
		for (String i : chiaveRisposta) {
			if (mappaRisposta.get(i).getIdDomanda().trim().equals(idDomanda.trim()) && mappaRisposta.get(i).getRisposta().trim().equals(risposta.trim()) && mappaRisposta.get(i).getIdUser().trim().equals(user.trim()) ) {
				mappaRisposta.remove(i);
			}
		}
	}
	
	//Metodo per ottenere tutte le domande per un determinato oggetto (passato in input)
	public ArrayList<Domanda> getDomande(String idOggetto) throws IllegalArgumentException {
		DB dbDomanda = getDBDomanda();
		HTreeMap<String, Domanda> mappaDomanda = dbDomanda.getHashMap("databaseDomanda");
		Set<String> chiave = mappaDomanda.keySet();
		ArrayList<Domanda> arraylist = new ArrayList<Domanda>();
		if(!mappaDomanda.isEmpty()) {
			for(String i : chiave) {
				if(mappaDomanda.get(i).getIdOggetto().trim().equals(idOggetto.trim())) {
					Domanda question = new Domanda (mappaDomanda.get(i).getIdDomanda().trim(), mappaDomanda.get(i).getUserDomanda().trim(), mappaDomanda.get(i).getDomanda().trim(), mappaDomanda.get(i).getIdOggetto().trim());
					arraylist.add(question);
				}
			}
		}
		return arraylist;
	}

	//Metodo per ottenere il venditore di un oggetto
	public String getVenditore (String idOggetto) throws IllegalArgumentException {
		DB dbOggetti = getDBOggetti();
		HTreeMap<String, Oggetto> mappaOggetti = dbOggetti.getHashMap("databaseOggetti");
		return mappaOggetti.get(idOggetto.trim()).getUsername().trim();
	}


	//Metodo per ottenere il nome utente dato lo username
	public String nomeUtente(String username) throws IllegalArgumentException {
		DB databaseUtenti = getDB();
		HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
		Set<String> chiave = mappaUtenti.keySet();
		String nomeUser = null;
		for (String i : chiave) {
			if (mappaUtenti.get(i).getUsername().contentEquals(username)) {
				nomeUser = mappaUtenti.get(i).getNome();
			}
		}
		return nomeUser;
	}
	
	//Metodo per ottenere il cognome utente dato lo username
	public String cognomeUtente(String username) throws IllegalArgumentException {
		DB databaseUtenti = getDB();
		HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
		Set<String> chiave = mappaUtenti.keySet();
		String cognomeUser = null;
		for (String i : chiave) {
			if (mappaUtenti.get(i).getUsername().contentEquals(username)) {
				cognomeUser = mappaUtenti.get(i).getCognome();
			}
		}
		return cognomeUser;
	}

	//Metodo per ottenere il telefono utente dato lo username
	public String telefonoUtente(String username) throws IllegalArgumentException {
		DB databaseUtenti = getDB();
		HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
		Set<String> chiave = mappaUtenti.keySet();
		String telefonoUser = null;
		for (String i : chiave) {
			if (mappaUtenti.get(i).getUsername().contentEquals(username)) {
				telefonoUser = mappaUtenti.get(i).getTelefono();
			}
		}
		return telefonoUser;
	}

	//Metodo per ottenere l'email utente dato lo username
	public String emailUtente(String username) throws IllegalArgumentException {
		DB databaseUtenti = getDB();
		HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
		Set<String> chiave = mappaUtenti.keySet();
		String emailUser = null;
		for (String i : chiave) {
			if (mappaUtenti.get(i).getUsername().contentEquals(username)) {
				emailUser = mappaUtenti.get(i).getEmail();
			}
		}
		return emailUser;
	}

	//Metodo per ottenere il cf utente dato lo username
	public String cfUtente(String username) throws IllegalArgumentException {
		DB databaseUtenti = getDB();
		HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
		Set<String> chiave = mappaUtenti.keySet();
		String cfUser = null;
		for (String i : chiave) {
			if (mappaUtenti.get(i).getUsername().contentEquals(username)) {
				cfUser = mappaUtenti.get(i).getCodiceFiscale();
			}
		}
		return cfUser;
	}

	//Metodo per ottenere l'indirizzo utente dato lo username
	public String indirizzoUtente(String username) throws IllegalArgumentException {
		DB databaseUtenti = getDB();
		HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
		Set<String> chiave = mappaUtenti.keySet();
		String indirizzoUser = null;
		for (String i : chiave) {
			if (mappaUtenti.get(i).getUsername().contentEquals(username)) {
				indirizzoUser = mappaUtenti.get(i).getIndirizzo();
			}
		}
		return indirizzoUser;
	}

	//Metodo per ottenere il sesso utente dato lo username
	public String sessoUtente(String username) throws IllegalArgumentException {
		DB databaseUtenti = getDB();
		HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
		Set<String> chiave = mappaUtenti.keySet();
		String sessoUser = null;
		for (String i : chiave) {
			if (mappaUtenti.get(i).getUsername().contentEquals(username)) {
				sessoUser = mappaUtenti.get(i).getSesso();
			}
		}
		return sessoUser;
	}

	//Metodo per ottenere la data di nascita dell' utente dato lo username
	public String dataUtente(String username) throws IllegalArgumentException {
		DB databaseUtenti = getDB();
		HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
		Set<String> chiave = mappaUtenti.keySet();
		String dataUser = null;
		for (String i : chiave) {
			if (mappaUtenti.get(i).getUsername().contentEquals(username)) {
				dataUser = mappaUtenti.get(i).getData();
			}
		}
		return dataUser;
	}

	//Metodo per ottenere il luogo di nascita dell' utente dato lo username
	public String luogoUtente(String username) throws IllegalArgumentException {
		DB databaseUtenti = getDB();
		HTreeMap<String, Utente> mappaUtenti = databaseUtenti.getHashMap("databaseUtenti");
		Set<String> chiave = mappaUtenti.keySet();
		String luogoUser = null;
		for (String i : chiave) {
			if (mappaUtenti.get(i).getUsername().contentEquals(username)) {
				luogoUser = mappaUtenti.get(i).getLuogo();
			}
		}
		return luogoUser;
	}

}
