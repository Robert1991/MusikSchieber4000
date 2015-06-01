package MusikSchieberPackage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JDialog;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class Database{
	
	private String pathToITunesLibrary;
	private Connection connection  = null;
	private File ITunesLibrary;
	private UpDatingLibrary upDateDialog;
	
	public Database(){
		pathToITunesLibrary = MusikSchieber.readLibrary();
		ITunesLibrary = new File(pathToITunesLibrary);
	}
	
	public void connect(){
		try{
			Class.forName( "org.hsqldb.jdbcDriver" );
		}
		catch ( ClassNotFoundException e ){
			System.err.println( "Keine Treiber-Klasse!" );
		    return;
		}
		
		try{
		      connection = DriverManager.getConnection("jdbc:hsqldb:file:data/Library;shutdown=true", "sa", "" );
		      if(connection != null){
		    	  System.out.println("Database connected!");
		      }
		}
		catch ( SQLException e ){
			System.err.println("An error occured: Opening the connection!");
		    e.printStackTrace();
		}
		   
	}
	
	public void disconnect(){
		if ( connection != null ){
			try { 
				connection.close(); 
				System.out.println("Database disconnected!");
			} catch ( SQLException e ) {
				System.err.println("An error occured: Closing the connection!");
				e.printStackTrace(); 
			}
		}else{
			System.err.println("No Connection to close!");
		}
			
		   
	}
	
	public ArrayList<String> getArtists(){
		ArrayList<String> artistlist = new ArrayList();
		String temp = "";
		String artist;
		
		Statement stmt;
		try {
			stmt = connection.createStatement();
			java.sql.ResultSet rs = (java.sql.ResultSet) stmt.executeQuery("SELECT Artist FROM musicEntries");
			
			while(rs.next()){
				artist = rs.getString(1);
				
				if(!artist.equals(temp)){
					artistlist.add(artist);
				}
				
				temp = artist;
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return artistlist;
	}
	
	
	public ArrayList<String> getAlbums(){
		ArrayList<String> albumlist = new ArrayList();
		String temp = "";
		String artist;
		String album;
		
		Statement stmt;
		try {
			stmt = connection.createStatement();
			java.sql.ResultSet rs = (java.sql.ResultSet) stmt.executeQuery("SELECT Artist,Album FROM musicEntries");
			
			while(rs.next()){
				artist = rs.getString(1);
				album = rs.getString(2);
				
				if(!album.equals(temp)){
					albumlist.add(artist+ ":" + album);
				}
				
				temp = album;
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return albumlist;
	}
	
	
	public void insertValue(String artist, String album, String title, String filePath){
		artist = cleanString(artist);
		album = cleanString(album);
		title = cleanString(title);
		filePath = cleanString(filePath);
		double timeStamp = System.currentTimeMillis();
		
		try {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate( "INSERT INTO musicEntries " + "VALUES(" +timeStamp+ ",'"+ artist +"','"+ album +"','"+title+"','"+filePath+"')");
		} catch (SQLException e) {
			System.err.println("An error occured inserting:" + " " + artist + ";" + album + ";" + title + ";" + filePath);
			e.printStackTrace();
		}
	}
	
	public boolean searchValue(String artist, String title){
		int entrycount = 0;
		boolean vorhanden;
		
		try {
			Statement stmt = connection.createStatement();
			
			artist = cleanString(artist);
			title = cleanString(title);
			
			artist = artist.toUpperCase();
			title = title.toUpperCase();
			
			java.sql.ResultSet rs = (java.sql.ResultSet) stmt.executeQuery("SELECT * FROM musicEntries WHERE UPPER(Artist) LIKE '%" + artist + "%' AND UPPER(Title) LIKE('%"+ title + "%')");
			
			while ( rs.next() ){
				entrycount++;
				System.out.println(entrycount);
			}
			
			if(entrycount > 0){
				vorhanden = true;
			}else{
				vorhanden = false;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			vorhanden = false;
		}
		
		return vorhanden;
	}
	
	
	public String getFilepath(String artist, String title){
		String filepath = "";
		
		try {
			Statement stmt = connection.createStatement();
			
			artist = cleanString(artist);
			title = cleanString(title);
			
			artist = artist.toUpperCase();
			title = title.toUpperCase();
			
			java.sql.ResultSet rs = (java.sql.ResultSet) stmt.executeQuery("SELECT Filepath FROM musicEntries WHERE UPPER(Artist) LIKE '%" + artist + "%' AND UPPER(Title) LIKE('%"+ title + "%')");
			
			while ( rs.next() ){
				filepath = rs.getString(1);
			}

			
		} catch (SQLException e) {
			e.printStackTrace();
			filepath = "";
		}
		
		filepath = filepath.substring(0,1) + ":" + filepath.substring(1,filepath.length());
		System.out.println(filepath);
		
		
		return filepath;
	}
	
	
	
	
	public void upDateLibrary(boolean upDate){
		
		upDateDialog = new UpDatingLibrary();
		
		try {
			Statement stmt = connection.createStatement();
			String[] teile = pathToITunesLibrary.split(Pattern.quote("\\"));
			writeLibrary(ITunesLibrary,stmt,teile.length,upDate);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		upDateDialog.dispose();
	}
	
	public void clearDataBase(){
		try {
			Statement stmt = connection.createStatement();
			stmt.execute("DELETE FROM musicEntries");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void writeLibrary(File dir, Statement stmt,int depth,boolean upDate) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory() && (files[i].getName().length() == 1 || files[i].getName().equals("123"))) {
					writeLibrary(files[i],stmt,depth,upDate); 
				}
				else if(files[i].isDirectory()){
					writeLibrary(files[i],stmt,depth,upDate);
				}else{
					String artist;
					String album;
					String title;
					String filepath;
					double timeStamp;
					boolean newEntry = false;

					if(files[i].getAbsolutePath().contains(".mp3") || files[i].getAbsolutePath().contains(".m4a") || files[i].getAbsolutePath().contains(".wma") || files[i].getAbsolutePath().contains(".aac") || files[i].getAbsolutePath().contains(".ogg") || files[i].getAbsolutePath().contains(".flac") || files[i].getAbsolutePath().contains(".rm") ){
						String[] teile = files[i].getAbsolutePath().split(Pattern.quote("\\"));
						filepath = cleanString(files[i].getAbsolutePath());
						upDateDialog.setTextField(files[i].getName());
						
						if(upDate == true){
							try {
								java.sql.ResultSet rs;
								rs = (java.sql.ResultSet) stmt.executeQuery("SELECT * FROM musicEntries WHERE Filepath='"+filepath+"'");
								
								if(!rs.next()){
									System.out.println("neuer Eintrag!");
									newEntry = true;
								}
								
							}catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}		
						
						if(!upDate || upDate && newEntry){
							if((teile.length - depth)==4){
								artist = cleanString(teile[teile.length-3]);
								album = cleanString(teile[teile.length-2]);
							}else{
								artist = cleanString(teile[teile.length-4]);
								album = cleanString(teile[teile.length-3]);
							}
							
							
							if(files[i].getAbsolutePath().contains(".mp3")){
								try {
									title = MP3Datei.quickAccessTitle(files[i].getAbsolutePath());
									title = cleanString(title);
								} catch (UnsupportedTagException e) {
									title = cleanString(files[i].getName());
								} catch (InvalidDataException e) {
									title = cleanString(files[i].getName());
								} catch (IOException e) {
									title = cleanString(files[i].getName());
								}catch(NullPointerException e){
									title = cleanString(files[i].getName());
								}
							}else{
								System.out.println("no MP3");
								title = cleanString(files[i].getName());
							}
							
							/*
							if(title.length() > 100){
								title = title.substring(0,100);
							}
							*/
							
							System.out.println("Artist" + " " + artist);
							System.out.println("Album" + " " + album);
							System.out.println("Title" + " " + title);
							System.out.println("Filename" + " " + filepath);

							timeStamp = System.currentTimeMillis();
							
							try {
								
								if(upDate == true){
									java.sql.ResultSet rs = (java.sql.ResultSet) stmt.executeQuery("SELECT * FROM musicEntries WHERE Filepath='"+filepath+"'");
									
									//System.out.println(rs.next());
									
									if(!rs.next()){
										System.out.println("neu!");
										stmt.executeUpdate( "INSERT INTO musicEntries " +
										         "VALUES(" +timeStamp+ ",'"+ artist +"','"+ album +"','"+title+"','"+filepath+"')");
									}	
								}else{
									stmt.executeUpdate( "INSERT INTO musicEntries " +
									         "VALUES(" +timeStamp+ ",'"+ artist +"','"+ album +"','"+title+"','"+filepath+"')");
								}
								
								
								
							} catch (SQLException e) {
								e.printStackTrace();
							}
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}	
					}else{
						System.out.println("Keine Musikdatei!");
					}

				}
			  }			
						
			}
	}
   
   public String cleanString(String name){
		String unzulaessigeZeichen = "<>?\":|/*()'";		
		
		for(int i = 0;i < unzulaessigeZeichen.length(); i++){
			name = name.replace(unzulaessigeZeichen.substring(i,i+1),"");
		}

		return name;
	}
	
	
	
	
	
}
