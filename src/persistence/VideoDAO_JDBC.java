package persistence;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javafx.util.Pair;
import model.Categoria;
import model.Commenti;
import model.OpzioniRisposte;
import model.Video;



public class VideoDAO_JDBC implements VideoDAO{

	

	public final String query_findAll = "SELECT * FROM video v";

	public final String query_risposta_corretta = "SELECT * FROM video WHERE url=?";
	
	
	@Override
	public Video findByPrimaryKey(String matricola) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ArrayList<Video> findAll() {
		Connection connection = null;
		ArrayList<Video> lista_video = new ArrayList<Video>();
		try {
			connection = DBManager.getInstance().getConnection();
			Video video = null;
			PreparedStatement statement;
			
			statement = connection.prepareStatement(query_findAll);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				video = new Video();
				video.setId(result.getString("id"));				
				video.setUrl(result.getString("url"));
				video.setNome(result.getString("nome"));
				video.setDescrizione(result.getString("descrizione"));
				video.setDifficolta(result.getString("difficolta"));
				video.setVisualizzazioni(result.getInt("visualizzazioni"));
				
				video.setRisposte(new OpzioniRisposte(result.getString("rispostaCorretta"), result.getString("rispostaErrata"), null));
				
				video.setCategoria(new Categoria(result.getString("categoria")));
				
				video.setCommenti(DBManager.getInstance().getCommentiDAO().findByPrimaryKey(result.getString("url")));
				
				lista_video.add(video);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}	 finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return lista_video;
	}
	
	@Override
	public void save(Video video) {
		Connection connection = null;
		try {
			connection = DBManager.getInstance().getConnection();

			String insert = "insert into video(id, url, nome, descrizione, difficolta, visualizzazioni,rispostaCorretta, rispostaErrata, categoria) values (?,?,?,?,?,?,?,?,?)";

			PreparedStatement statement = connection.prepareStatement(insert);
			statement.setString(1, video.getId());
			statement.setString(2, video.getUrl());
			String nome;
			try {
				nome = new String(video.getNome().getBytes(), "UTF-8");
				statement.setString(3, nome);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			statement.setString(4, video.getDescrizione());
			statement.setString(5, video.getDifficolta());
			statement.setInt(6, 0);
			statement.setString(7, video.getRisposte().getOpzioneCorretta());
			statement.setString(8, video.getRisposte().getOpzioneErrata());

			statement.setString(9, video.getCategoria().get(0).getNome());
			
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
	}
	
	@Override
	public void update(Video video) {
		Connection connection = null;
		try {
			connection = DBManager.getInstance().getConnection();

			String insert = "UPDATE video SET nome=?, descrizione=?, difficolta=?, rispostacorretta=?, rispostaerrata=?, categoria=? WHERE url=?";
			PreparedStatement statement = connection.prepareStatement(insert);

			
			statement.setString(1, video.getNome());
			statement.setString(2, video.getDescrizione());
			statement.setString(3, video.getDifficolta());
			statement.setString(4, video.getRisposte().getOpzioneCorretta());
			statement.setString(5, video.getRisposte().getOpzioneErrata());
			statement.setString(6, video.getCategoria().get(0).getNome());
			statement.setString(7, video.getUrl());
			
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}

	}
	
	@Override
	public void delete(String url) {
		Connection connection = null;
		
		try {
			connection = DBManager.getInstance().getConnection();
			
			PreparedStatement statement;
			statement = connection.prepareStatement("DELETE FROM video WHERE url=?");
			statement.setString(1, url);

			statement.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());}
		}

	}

	public boolean esisteVideo(String urlNuovo) {
		Connection connection = null;
		boolean b=false;
		try {
			connection = DBManager.getInstance().getConnection();
			
			PreparedStatement statement;
			statement = connection.prepareStatement("SELECT nome FROM video WHERE url=?");
			statement.setString(1, urlNuovo);

			ResultSet result = statement.executeQuery();
			if(result.next()) {
				b= true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());}
		}

		return b;
	}

	public boolean esisteNome(String nomeNuovo) {
		Connection connection = null;
		boolean b=false;
		
		try {
			connection = DBManager.getInstance().getConnection();
			
			PreparedStatement statement;
			statement = connection.prepareStatement("SELECT * FROM video WHERE nome=?");
			statement.setString(1, nomeNuovo);

			ResultSet result = statement.executeQuery();
			if(result.next()) {
				b= true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());}
		}

		return b;
	}

	@Override
	public boolean esisteNomeModifica(String modificaNome, String url) {
		Connection connection = null;
		boolean b=false;
		
		try {
			connection = DBManager.getInstance().getConnection();
			
			PreparedStatement statement;
			statement = connection.prepareStatement("SELECT * FROM video WHERE nome=? AND url!=?");
			statement.setString(1, modificaNome);
			statement.setString(2, url);

			ResultSet result = statement.executeQuery();
			if(result.next()) {
				b=true;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());}
		}

		return b;
	}
	
	@Override
	public String getRispostaCorretta(String url) {
		
		Connection connection = null;
		String risposta_corretta = null;
		try {
			connection = DBManager.getInstance().getConnection();
			PreparedStatement statement;
			
			statement = connection.prepareStatement(query_risposta_corretta);
			statement.setString(1, url);
			ResultSet result = statement.executeQuery();
			if(result.next()){
				risposta_corretta = result.getString("rispostaCorretta");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}	 finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}		
		return risposta_corretta;
	}
	
	public int updateVisualizzazioni(String url) {
		Connection connection = null;
		int visualizzazioni = 0;
		try {
			connection = DBManager.getInstance().getConnection();
			
			String numeroVisualizzazioni = "SELECT visualizzazioni FROM video WHERE url = ?";
			PreparedStatement statementNumeroVisualizzazioni = connection.prepareStatement(numeroVisualizzazioni);
			statementNumeroVisualizzazioni.setString(1, url);
			ResultSet resNumeroVisualizzazioni = statementNumeroVisualizzazioni.executeQuery();
			if(resNumeroVisualizzazioni.next()) {
				visualizzazioni = resNumeroVisualizzazioni.getInt(1);
			}
			
			String update = "UPDATE video SET visualizzazioni = ? WHERE url=?";
			PreparedStatement statement = connection.prepareStatement(update);
			statement.setInt(1, ++visualizzazioni );
			statement.setString(2, url);
			
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		return visualizzazioni;
	}

	@Override
	public int getVideoInseriti() {
		Connection connection = null;
		ArrayList<String> lista = new ArrayList<String>();

		try {
			connection = DBManager.getInstance().getConnection();
			String query = "select * from video";
			PreparedStatement statement = connection.prepareStatement(query);	
			ResultSet result = statement.executeQuery();
			while(result.next())
			{
				lista.add(result.getString("id"));
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		return lista.size();		
	}

	@Override
	public String getLinkVideoCorretto() {
		Connection connection = null;
		ArrayList<String> lista = new ArrayList<String>();

		try {
			connection = DBManager.getInstance().getConnection();
			String query = "select * from esiti where risultato=true";
			PreparedStatement statement = connection.prepareStatement(query);	
			ResultSet result = statement.executeQuery();
			while(result.next())
			{
				lista.add(result.getString("fk_video"));
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		if(lista.size()!=0)
		{	String massimo = lista.get(0);
			int max = 0;
			for(int i=0; i<lista.size(); i++)
			{
				int cont=0;
				for(int j=0; j<lista.size(); j++)
				{
					if(lista.get(i) == lista.get(j))
					{
						cont++;
					}
				}
				if(cont>max)
				{
					max=cont;
					massimo = lista.get(i);
				}
			}
			return massimo;
		}
		return "nessuno";
	}

	@Override
	public String getLinkVideoSbagliato() {
		Connection connection = null;
		ArrayList<String> lista = new ArrayList<String>();

		try {
			connection = DBManager.getInstance().getConnection();
			String query = "select * from esiti where risultato=false";
			PreparedStatement statement = connection.prepareStatement(query);	
			ResultSet result = statement.executeQuery();
			while(result.next())
			{
				lista.add(result.getString("fk_video"));
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		if(lista.size()!=0)
		{
		String massimo = lista.get(0);
		int max = 0;
		for(int i=0; i<lista.size(); i++)
		{
			int cont=0;
			for(int j=0; j<lista.size(); j++)
			{
				if(lista.get(i) == lista.get(j))
				{
					cont++;
				}
			}
			if(cont>max)
			{
				max=cont;
				massimo = lista.get(i);
			}
		}
		return massimo;
		}
		return "nessuno";
	}

	@Override
	public String getNomeVideoCorretto(String linkVideoCorretto) {
		Connection connection = null;
		ArrayList<String> lista = new ArrayList<String>();

		try {
			connection = DBManager.getInstance().getConnection();
			String query = "select * from video where url=?";
			PreparedStatement statement = connection.prepareStatement(query);	
			statement.setString(1, linkVideoCorretto);

			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				lista.add(result.getString("nome"));
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		if(lista.size()>0)
			return lista.get(0);
		return "nessuno";
	}

	@Override
	public String getNomeVideoSbagliato(String linkVideoSbagliato) {
		Connection connection = null;
		ArrayList<String> lista = new ArrayList<String>();

		try {
			connection = DBManager.getInstance().getConnection();
			String query = "select * from video where url=?";
			PreparedStatement statement = connection.prepareStatement(query);	
			statement.setString(1, linkVideoSbagliato);

			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				lista.add(result.getString("nome"));
			}
			
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		if(lista.size()>0)
			return lista.get(0);
		return "nessuno";
	}

	@Override
	public ArrayList<String> getVideoCategorie() {
		Connection connection = null;
		ArrayList<String> a=new ArrayList<String>();

		try {
			connection = DBManager.getInstance().getConnection();

			String insert = "select categoria from video";

			PreparedStatement statement = connection.prepareStatement(insert);
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				a.add((String) result.getString("categoria"));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return a;

	}

	@Override
	public ArrayList<String> getVideoDifficolta() {
		Connection connection = null;
		ArrayList<String> a=new ArrayList<String>();
		try {
			connection = DBManager.getInstance().getConnection();

			String insert = "select difficolta from video";

			PreparedStatement statement = connection.prepareStatement(insert);
			ResultSet result = statement.executeQuery();
			while(result.next()) {
				a.add((String) result.getString("difficolta"));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if((connection !=null) && !connection.isClosed())
			    {
			      connection.close(); 
			    }
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return a;

	}



	@Override
	public Video getVideo(String url) {
		Connection connection = null;
		Video video = new Video();
		try {
			connection = DBManager.getInstance().getConnection();
			PreparedStatement statement;
			
			statement = connection.prepareStatement("select * from video where url=?");
			statement.setString(1, url);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				video.setId(result.getString("id"));				
				video.setUrl(result.getString("url"));
				video.setNome(result.getString("nome"));
				video.setDescrizione(result.getString("descrizione"));
				video.setDifficolta(result.getString("difficolta"));
				video.setVisualizzazioni(result.getInt("visualizzazioni"));
				
				video.setRisposte(new OpzioniRisposte(result.getString("rispostaCorretta"), result.getString("rispostaErrata"), null));
				
				video.setCategoria(new Categoria(result.getString("categoria")));
				
				video.setCommenti(DBManager.getInstance().getCommentiDAO().findByPrimaryKey(result.getString("url")));
				
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}	 finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return video;
	}


	@Override
	public ArrayList<Video> getVideoHome() {
		Connection connection = null;
		ArrayList<Video> lista_video = new ArrayList<Video>();
		try {
			connection = DBManager.getInstance().getConnection();
			Video video = null;
			PreparedStatement statement;
			
			statement = connection.prepareStatement(query_findAll);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				video = new Video();
				video.setId(result.getString("id"));				
				video.setUrl(result.getString("url"));
				video.setNome(result.getString("nome"));
				video.setDifficolta(result.getString("difficolta"));
				
				lista_video.add(video);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}	 finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return lista_video;
	}

	


}
