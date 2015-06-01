package MusikSchieberPackage;

import java.util.ArrayList;

public class MusicBrainzEntry {
	private String artist;
	private String title;
	private String album;
	private String tracknumber;
	private String year;
	private ArrayList<String> genres = new ArrayList<String>(); 
	
	public MusicBrainzEntry(){
		
	}
	
	public void setArtist(String artist){
		this.artist = artist;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setAlbum(String album){
		this.album = album;
	}
	
	public void setTrackNumber(String tracknumber){
		this.tracknumber = tracknumber;
	}
	
	public void setYear(String year){
		this.year = year;
	}
	
	public void addGenre(String genre){
		genres.add(genre);
	}
	
	public String getArtist(){
		return this.artist;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getAlbum(){
		return this.album;
	}
	
	public String getTrackNumber(){
		return this.tracknumber;
	}
	
	public String getYear(){
		return this.year;
	}
	
	public ArrayList<String> getGenres(){
		return this.genres;
	}
	
}
