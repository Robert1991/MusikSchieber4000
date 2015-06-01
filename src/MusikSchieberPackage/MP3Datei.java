package MusikSchieberPackage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import org.farng.mp3.MP3File;

import com.mpatric.mp3agic.*;

public class MP3Datei implements MusicFile{
	
	private Mp3File mp3datei;
	private File datei;
	private String newFilename;
	private String artist = "unknown";
	private String album = "unknown";
	private String title = "unknown";
	private String tracknumber = "0";
	private String genre = "unknown";
	private String newFilePath;
	private String year = "";
	private int genrenumber;
	private final String[] GENRE_LIST = {"Blues","Classic Rock","Country","Dance","Disco","Funk","Grunge","Hip-Hop","Jazz","Metal","New Age","Oldies","Other","Pop","Rhtym and Blues","Rap","Reggae","Rock","Techno","Industrial","Alternative","Ska","Death Metal", "Pranks", "Soundtrack","Euro-Techno","Ambient","Trip-Hop","Vocal","Jazz & Funk","Fusion","Trance","Classical","Instrumental","Acid","House","Game","Sound Clip","Gospel","Noise","Alternative Rock","Bass","Soul","Punk","Space","Meditative","Instrumental Pop","Instrumental Rock","Ethnic","Gothic","Darkwave","Techno-Industrial","Electronic","Pop-Folk","Eurodance","Dream","Southern Rock","Comedy","Cult","Gangsta","Top 40","Christian Rap","Pop/Funk","Jungle","Native US", "Cabaret","New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock&Roll", "Hard Rock"};
	private final String NUMBERS = "0123456789"; 
	private boolean damaged;
	private boolean missingValues;
	private boolean ID3v2;
	private boolean duplicate = false;
	private String duplicateFilepath = "";
	
	public MP3Datei(String dateipfad){
		
		datei = new File(dateipfad);
		
		if(!datei.exists())
		{
			System.err.println("Datei: " + dateipfad + " - konnte nicht gefunden werden");
			damaged = true;
		}else if(datei.length() < 128){
			System.err.println("Datei beschädigt: " + dateipfad);
			damaged = true;
		} else {
			try{
				ID3v2 ID3v2Tag = new ID3v24Tag();
				ID3v1 ID3v1Tag = new ID3v24Tag();
				
				mp3datei = new Mp3File(dateipfad);
				if(mp3datei.hasId3v2Tag()){
					ID3v2Tag = mp3datei.getId3v2Tag();
					ID3v2 = true;
					this.artist = ID3v2Tag.getArtist();
					this.album = ID3v2Tag.getAlbum();
					this.title = ID3v2Tag.getTitle();
					this.tracknumber = ID3v2Tag.getTrack();
					this.genre = identifyGenre(this.getGenreNumber(ID3v2Tag,ID3v1Tag));
					this.year = ID3v2Tag.getYear();
				} 
				else if(mp3datei.hasId3v1Tag()){
					ID3v1Tag = mp3datei.getId3v1Tag();
					ID3v2 = false;
					this.artist = ID3v1Tag.getArtist();
					this.album = ID3v1Tag.getAlbum();
					this.title = ID3v1Tag.getTitle();
					this.tracknumber = ID3v1Tag.getTrack();
					this.genre = identifyGenre(this.getGenreNumber(ID3v2Tag,ID3v1Tag));
					this.year = ID3v1Tag.getYear();
				}
				
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
				
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
	
	public void setDuplicate(boolean duplicate){
		this.duplicate = duplicate;
	}
	
	
	public void setDuplicateFilepath(String filepath){
		duplicateFilepath = filepath;
	}
	
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setArtist(String artist){
		this.artist = artist;
	}
	
	public void setYear(String year){
		this.year = year;
		if(this.year.isEmpty() || this.year == null){
			this.year = "";
		}else{
			this.year = year;
		}
	}
	
	public void setTrackNumber(String number){
		this.tracknumber = number;
	}
	
	public void setGenre(String genre){
		this.genre = genre;
	}
	
	public void setAlbumTitle(String album){
		this.album = album;
	}
	
	public void setFilename(){ 
		String track = cleanString(this.getTrackNumber());
		String artist = cleanString(this.getArtist());
		String title = cleanString(this.getTitle());
		track = umlautebereinigen(track);
		artist = umlautebereinigen(artist);
		title = umlautebereinigen(title);
		
		
		if(!this.getTrackNumber().equals("0") && !this.getTrackNumber().equals("") && this.getTrackNumber() != null){
			newFilename = track + " - " + artist + " - " + title + ".mp3";
		}else{
			newFilename = artist + " - " + title + ".mp3";
		}
	}
	
	public String getFilePath(){
		return datei.getAbsolutePath();
	}
	
	public boolean damaged(){
		return this.damaged;
	}
	
	public boolean duplicate(){
		return this.duplicate;
	}
	
	public String getDuplicateFilepath(){
		return duplicateFilepath;
	}
	
	
	public String getTitle(){
		if(this.title != null && this.title.length() > 0){
			return this.title;
		}
		else{
			return "unknown";
		}	
	}
	
	public String getArtist(){
		if(this.artist != null && this.artist.length() > 0){
			return this.artist;
		}
		else{
			return "unknown";
		}
	}
	
	public String getYear(){
		if(this.year != null && !this.year.isEmpty()){
			return this.year;
		}
		else{
			return "";
		}
	}
	
	public String getTrackNumber(){
		if(this.tracknumber != null && this.tracknumber.length() > 0){
			return tracknumber;
		}
		else{
			return "0";
		}	
	}
	
	public int getGenreNumber(ID3v2 ID3v2Tag, ID3v1 ID3v1Tag){
		if(ID3v2){
			genrenumber = ID3v2Tag.getGenre();
		}else{
			genrenumber = ID3v1Tag.getGenre();
		}
		
		if(genrenumber < 80){
			return genrenumber;
		}
		else{
			return 80;
		}
	}
	
	public String getGenre(){
		if(this.genre == "" || this.genre == null){
			return "unknown";
		}else{
			return genre;
		}
	}
	
	public String getAlbumTitle(){
		if(this.album  != null && this.album.length() > 0){
			return this.album;	
		}else{
			return "unknown";
		}
	}
	
	public boolean missingNonCriticalValues(){
		try{
			if(this.genre.equals("unknown") || this.year.equals("") || this.tracknumber.equals("0") || this.genre.isEmpty() || this.year.isEmpty() || this.tracknumber.isEmpty()){
				System.out.println("Fall 1: " + "" + this.artist + " " + this.title);
				return true;
			}else{
				return false;
			}
		}catch(java.lang.NullPointerException e){
			System.out.println("Fall 2: " + "" + this.artist + " " + this.title);
			return true;
		}
		
		
	}
	
	
	public boolean missingCriticalValues(){
		try{
			if(this.artist.equals("unknown") || this.album.equals("unknown") || this.title.equals("unknown")){
				return true;
			}else{
				return false;
			}
		}catch(java.lang.NullPointerException e){
			return true;
		}
	}
	
	public String getFileName(){
		return datei.getName();
	}
	
	public String getUpdatedFileName(){
		return newFilename;
	}
	
	public String getUpdatedFilePath(){
		return this.newFilePath;
	}
	
	public static String quickAccessTitle(String filepath) throws UnsupportedTagException, InvalidDataException, IOException{
		String title;
		Mp3File mp3datei = new Mp3File(filepath);
		title = mp3datei.getId3v2Tag().getTitle();
		
		return title;
	}
	
	public void delete(){
		datei.delete();
	}
	
	public void saveTag(String filePath){
		
		try {
			Mp3File mp3File = new Mp3File(filePath);
			
			mp3File.getId3v2Tag().setArtist(this.artist);
			mp3File.getId3v2Tag().setTitle(this.title);
			mp3File.getId3v2Tag().setAlbum(this.album);
			mp3File.getId3v2Tag().setTrack(this.tracknumber);
			mp3File.getId3v2Tag().setYear(this.year);
			mp3File.getId3v2Tag().setGenre(this.genrenumber);
			mp3File.getId3v2Tag().setAlbumArtist(this.artist);
			mp3File.getId3v2Tag().setItunesComment("Robert rockt!");
			
			mp3File.removeCustomTag();
			
			RandomAccessFile file;
			
			file = new RandomAccessFile(filePath,"rw");
			file.write(mp3File.getId3v2Tag().toBytes());
			
			if(mp3File.hasId3v1Tag()){
				System.err.println("Has ID3v1!");
				mp3File.getId3v1Tag().setAlbum(this.album);
				mp3File.getId3v1Tag().setArtist(this.artist);
				mp3File.getId3v1Tag().setComment("Robert rockt!");
				mp3File.getId3v1Tag().setGenre(this.genrenumber);
				mp3File.getId3v1Tag().setTitle(this.title);
				mp3File.getId3v1Tag().setTrack(this.tracknumber);
				mp3File.getId3v1Tag().setYear(this.year);
				file.seek(file.length()-128);
				file.write(mp3File.getId3v1Tag().toBytes());
			}else{
				System.err.println("Has no ID3v1!");
				mp3File.getId3v1Tag().setAlbum(this.album);
				mp3File.getId3v1Tag().setArtist(this.artist);
				mp3File.getId3v1Tag().setComment("Robert rockt!");
				mp3File.getId3v1Tag().setGenre(this.genrenumber);
				mp3File.getId3v1Tag().setTitle(this.title);
				mp3File.getId3v1Tag().setTrack(this.tracknumber);
				mp3File.getId3v1Tag().setYear(this.year);
				file.seek(file.length()-128);
				file.write(mp3File.getId3v1Tag().toBytes());
			}
			
			file.close();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedTagException e){
			e.printStackTrace();
		}catch (InvalidDataException e){
			e.printStackTrace();
		}
	}
	
	
	
	public void replace(boolean keep){
		this.setFilename();
		
		File neueDatei = new File(newFilePath + "\\" + newFilename);
		copyMP3(datei,neueDatei);
		saveTag(neueDatei.getAbsolutePath());
		
		if(!keep){
			datei.delete();
		}
			
	}
	
	public String cleanString(String name){
		String unzulaessigeZeichen = "<>?\"\\:|/*()'°^´`_";
		
		for(int i = 0;i < unzulaessigeZeichen.length(); i++){
			name = name.replace(unzulaessigeZeichen.substring(i,i+1),"");
		}

		return name;
	}
	
	public String makeDir(String path){
		File ebene1;
		File ebene2;
		File ebene3;
		
		String artist = cleanString(this.artist);
		String album = cleanString(this.album);
		
		artist = umlautebereinigen(artist);
		album = umlautebereinigen(album);
		
		if(NUMBERS.contains(artist.substring(0,1))){
			ebene1 = new File(path + "\\" +  "123");
			ebene2 = new File(path + "\\" + "123" + "\\" + artist);
			ebene3 = new File(path + "\\" +  "123" + "\\" + artist + "\\" + album);
		}else{
			
			ebene1 = new File(path + "\\" +  artist.toUpperCase().charAt(0));
			ebene2 = new File(path + "\\" +  artist.toUpperCase().charAt(0) + "\\" + (artist.toUpperCase()).charAt(0) + artist.substring(1,artist.length()));
			ebene3 = new File(path + "\\" +  artist.toUpperCase().charAt(0) + "\\" + (artist.toUpperCase()).charAt(0) + artist.substring(1,artist.length()) + "\\"+ (album.toUpperCase()).charAt(0) + album.substring(1,album.length()));
		}
		
		if(ebene1.exists()== false){
			ebene1.mkdir();
		}
		
		if(ebene2.exists()== false){
			ebene2.mkdir();
		}
		
		if(ebene3.exists()== false){
			ebene3.mkdir();
		}
		
		this.newFilePath = ebene3.getAbsolutePath();
		
		return ebene3.getAbsolutePath();
	}
	
	public String identifyGenre(int genrenumber)
	{
		String genre = "unknown";
		
		if(genrenumber < 80){
			for(int i = 0; i < GENRE_LIST.length; i++){
				if(i == genrenumber){
					genre = GENRE_LIST[i];
				}
			}
		}else{
			genre = "unknown";
		}
		
		return genre;
	}
	
	public int getGenreInt(String genre)
	{
		boolean genreFound = false;
		int genreIdentity = 80;
		
		for(int i = 0; i < GENRE_LIST.length; i++){
			if(GENRE_LIST[i].toLowerCase() == genre){
				genreIdentity = i;
				genreFound = true;
			}
		}
		
		if(genreFound == false){
			genreIdentity = 80;
		}
		
		return genreIdentity;
	}
	
	public String[] tagToArray(){
		String[] array = new String[6];
		
		array[0] = this.artist;
		array[1] = this.title;
		array[2] = this.album;
		array[3] = this.tracknumber;
		array[4] = this.year;
		array[5] = this.genre;
		
		return array;
	}
	
	public void copyMP3(File original, File copy){
		FileChannel inChannel = null; 
        FileChannel outChannel = null; 
        try { 
            inChannel = new FileInputStream(original).getChannel(); 
            outChannel = new FileOutputStream(copy).getChannel(); 
            inChannel.transferTo(0, inChannel.size(), outChannel); 
        } catch (IOException e) { 
        	e.printStackTrace(); 
        } finally { 
        	try { 
        		if (inChannel != null) 
        			inChannel.close(); 
        		if (outChannel != null) 
        			outChannel.close(); 
        	}catch(IOException e) {} 
        } 
    }
	
	public static String umlautebereinigen(String name){
	
		String[] unzulaessigeZeichen = {"ä","ü","ö","Ä","Ö","Ü","è","é","É","È","Ç","ç","Ó","ó","Ò","ò","Ø","ø","Ú","ú","Ù","ù","æ","Æ","å","Å"};
		String[] zulaessig = {"ae","ue","oe","Ae","Oe","Üe","e","e","E","E","C","c","O","o","O","o","O","o","U","u","U","u","ae","Ae","a","A"};
		
		for(int i = 0;i < unzulaessigeZeichen.length; i++){
			
			name = name.replace(unzulaessigeZeichen[i],zulaessig[i]);
		}

		return name;
	}

}
