package model;

import java.util.ArrayList;

import persistence.DBManager;

public class Utente {
	private String nome;
	private String cognome;
	private String email;
	private String password;
	private ArrayList<Video> video_preferiti;
	private ArrayList<Video> video_recenti;
	private ArrayList<Esito> storico;
	private Boolean amministratore;
	private double media;

	public Utente() {
		this.video_preferiti = new ArrayList<Video>();
		this.storico = new ArrayList<Esito>();
		this.video_recenti = new ArrayList<Video>();
		media = 0;
	}
	
	public Utente(String nome, String cognome) {
		this.nome = nome;
		this.cognome = cognome;
		this.video_preferiti = new ArrayList<Video>();
		this.storico = new ArrayList<Esito>();
		this.video_recenti = new ArrayList<Video>();
	}
	
	public Utente(String nome, String cognome, String email, String password, String amministratore) {
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.password = password;
		this.video_preferiti = new ArrayList<Video>();
		this.storico = new ArrayList<Esito>();

		if(amministratore.equals("on"))
			this.setAmministratore(true);
		else
			this.setAmministratore(false);
		
		this.video_recenti = new ArrayList<Video>();
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getAmministratore() {
		return amministratore;
	}

	public void setAmministratore(Boolean amministratore) {
		this.amministratore = amministratore;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public ArrayList<Video> getPreferiti(){
		return video_preferiti;
	}
	
	public void aggiungiAiPreferiti(Video v) {
		if(!video_preferiti.contains(v))
			video_preferiti.add(v);
		else
			video_preferiti.remove(v);
	}

	public ArrayList<Esito> getStorico() {
		return storico;
	}


	public void setAmministratore(String amministratore) {
		if(amministratore.equals("on"))
			this.amministratore = true;
		else
			this.amministratore = false;
	}
	
	public ArrayList<Video> getRecenti() {
		return DBManager.getInstance().getCategoriaDAO().findByPrimaryKey("recenti", email);
	}

	public void aggiornaRecenti(Video videoChiesto) {
		DBManager.getInstance().getCategoriaDAO().save(videoChiesto);
	}

	public double getMedia() {
		return media;
	}

	public void setMedia(double media) {
		this.media = media;
	}

}
