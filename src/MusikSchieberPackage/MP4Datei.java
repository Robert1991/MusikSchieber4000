package MusikSchieberPackage;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;

public class MP4Datei {
	private String artist = "unknown";
	private String album = "unknown";
	private String title = "unknown";
	private String tracknumber = "0";
	private String genre = "unknown";
	private String year = "";
	private AudioFile audioFile;
	private Mp4Tag mp4tag;
	
	public MP4Datei(File file){
		try {
			audioFile = AudioFileIO.read(file);
			mp4tag = (Mp4Tag)audioFile.getTag(); 
			
			//Abfrage der Werte
			this.artist = mp4tag.getFirst(Mp4FieldKey.ARTIST);
			this.album = mp4tag.getFirst(Mp4FieldKey.ALBUM);
			this.title = mp4tag.getFirst(Mp4FieldKey.TITLE);
			this.tracknumber = mp4tag.getFirst(Mp4FieldKey.TRACK);
			
			if(tracknumber.contains("/")){
				tracknumber = tracknumber.substring(0, tracknumber.indexOf("/"));
			}
			
			if(!mp4tag.getFirst(Mp4FieldKey.GENRE).isEmpty()){
				this.genre = mp4tag.getFirst(Mp4FieldKey.GENRE);
			}
			else if(!mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM).isEmpty()){
				this.genre = mp4tag.getFirst(Mp4FieldKey.GENRE_CUSTOM);
			}else{
				this.genre = "unknown";
			}
			
			this.year = mp4tag.getFirst(Mp4FieldKey.DAY);
			
			
			//Setzen von Defaultwerten, falls Werte fehlen
			if(this.artist == null || this.artist.equals("")){
				this.artist = "unknown";
			}
			
			if(this.album == null || this.album.equals("")){
				this.album = "unknown";
			}
			
			if(this.title == null || this.title.equals("")){
				this.title = "unknown";
			}
			
			if(this.tracknumber == null || this.tracknumber.equals("")){
				this.tracknumber = "0";
			}
			
			if(this.genre == null || this.genre.equals("")){
				this.genre = "unknown";
			}
			
			if(this.year == null || this.year.equals("")){
				this.year = "";
			}

			System.out.println(artist);
			System.out.println(album);
			System.out.println(title);
			System.out.println(tracknumber);
			System.out.println(genre);
			System.out.println(year);
		} catch (CannotReadException | IOException | TagException
				| ReadOnlyFileException | InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setArtist(String artist){
		this.artist = artist;
	}
	
	public void setAlbum(String album){
		this.album = album;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setTrackNumber(String tracknumber){
		this.tracknumber = tracknumber;
	}
	
	public void setYear(String year){
		this.year = year;
	}
	
	public void setGenre(String genre){
		this.genre = genre;
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
	
	public String getYear(){
		return this.year;
	}
	
	public String getGenre(){
		return this.genre;
	}
	
	public String getTracknumber(){
		return this.tracknumber;
	}
	
	
	public void saveTag(){
		try {
			mp4tag.setField(Mp4FieldKey.ARTIST, this.artist);
			mp4tag.setField(Mp4FieldKey.ALBUM, this.album);
			mp4tag.setField(Mp4FieldKey.TITLE, this.title);
			mp4tag.setField(Mp4FieldKey.TRACK, this.tracknumber);
			mp4tag.deleteField(Mp4FieldKey.GENRE);
			mp4tag.setField(Mp4FieldKey.GENRE_CUSTOM, this.genre);
			mp4tag.setField(Mp4FieldKey.DAY, this.year);
			audioFile.setTag(mp4tag);
			audioFile.commit();
		} catch (KeyNotFoundException | FieldDataInvalidException | CannotWriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
