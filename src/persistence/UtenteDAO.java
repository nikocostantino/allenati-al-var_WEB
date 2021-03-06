package persistence;

import java.util.ArrayList;
import java.util.List;

import model.Utente;

public interface UtenteDAO {

	public void save(Utente utente);  // Create
	public Utente findByPrimaryKey(String id, String password);     // Retrieve
	public ArrayList<Utente> findAll();       
	public void update(Utente utente); //Update
	public void delete(Utente utente); //Delete	
	
	public boolean cercaPerEmail(String email);
	public String getNomePerEmail(String email);
	public String getPasswordPerEmail(String email);
	public void update(String emailVecchia, String nome, String cognome, String emailNuova);
	public void modificaPassword(String nuovaPassword);
	public void inserisciRichiesta(String email);
	public ArrayList<String> getRichieste();
	public void declinaAmministratore(String email);
	public void accettaAmministratore(String email);
	public int getProveEffettuate(String email);
	public int getProveSuperate(String email);
	public int getProveNonSuperate(String email);
	public double getMedia(String email);
	public int getVotoPiuFrequente(String email);
	public int getVotoMenoFrequente(String email);
	public int getProveSuperate();
	public int getProveNonSuperate();
	public double getVotoMedio();
	public List<Utente> getTuttiUtenti();
}
