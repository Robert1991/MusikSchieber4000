package AudioFiles;

import java.io.File;
import java.io.IOException;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.*;


public class MP3Datei {
	private String artist = "unknown";
	private String album = "unknown";
	private String title = "unknown";
	private String tracknumber = "0";
	private String genre = "unknown";
	private String year = "";
	private MP3File audioFile;
	private ID3v1Tag id3v1Tag;
	private AbstractID3v2Tag id3v2Tag;
	
	public MP3Datei(File file){

			try {
				audioFile = (MP3File)AudioFileIO.read(file);
			} catch (CannotReadException | IOException | TagException
					| ReadOnlyFileException | InvalidAudioFrameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(audioFile.hasID3v1Tag() && audioFile.hasID3v2Tag()){
				id3v1Tag = (ID3v1Tag) audioFile.getID3v1Tag(); 
				id3v2Tag = (AbstractID3v2Tag) audioFile.getID3v2Tag(); 
				
				this.artist = id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST);
				this.album = id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM);
				this.title = id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_TITLE);
				this.tracknumber = id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_TRACK);
				this.year = id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_YEAR);
				this.genre = id3v2Tag.getFirst(ID3v24Frames.FRAME_ID_GENRE);
				
				
				if(this.artist.isEmpty()){
					this.artist = id3v1Tag.getFirstArtist();	
				}
				
				if(this.album.isEmpty()){
					this.album = id3v1Tag.getFirstAlbum();
				}
				
				if(this.title.isEmpty()){
					this.title = id3v1Tag.getFirstTitle();
				}
				
				if(this.tracknumber.isEmpty()){
					this.tracknumber = id3v1Tag.getFirstTrack();
				}
				
				if(this.year.isEmpty()){
					this.year = id3v1Tag.getFirstYear();
				}
				
				if(this.genre.isEmpty()){
					this.genre = id3v1Tag.getFirstGenre();
				}
				
				
			}else if(audioFile.hasID3v2Tag()){
				
			}else{
				id3v1Tag = new ID3v11Tag();
			}
			
			
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
		Tag tag = audioFile.getTag();
		try {
			tag.setField(FieldKey.ARTIST, this.artist);
			tag.setField(FieldKey.ALBUM, this.album);
			tag.setField(FieldKey.TITLE, this.title);
			tag.setField(FieldKey.TRACK, this.tracknumber);
			tag.setField(FieldKey.YEAR, this.year);
			tag.setField(FieldKey.GENRE, this.genre);
			audioFile.setTag(tag);
			audioFile.commit();
		} catch (KeyNotFoundException | FieldDataInvalidException | CannotWriteException e) {
			e.printStackTrace();
		}
	}
}
