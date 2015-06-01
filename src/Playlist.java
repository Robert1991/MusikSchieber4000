import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Playlist {
	private String pathToPlayList;
	private String nameOfPlayList;
	private File playlist;
	private boolean exists = false;
	private boolean erweitern = false;
	private static String[] HEADER = {"Titelname","\t","Interpret","\t","Komponist","\t","Album","\t","Werk","\t","Genre","\t","Dauer","\t","CD-Nummer","\t","Titelnummer","\t","Zähler","\t","Jahr","\t","Geändert","\t","Hinzugefügt","\t","Datenrate","\t","Abtastrate","\t","Lautstärkeanpassung","\t","Art","\t","Equalizer","\t","Kommentar","\t","Wiedergaben","\t","Zuletzt gespielt","\t","Übersprungen","\t","Zuletzt übersprungen","\t","Meine Wertung","\t","Ort","\n"};
	private final int TITELNAME = 0;
	private final int INTERPRET = 2;
	private final int ALBUM = 6;
	private final int GENRE = 10;
	private final int TITEL_NUMMER = 16;
	private final int ZAEHLER = 18;
	private final int JAHR = 20;
	private final int HINZUGEFUEGT = 24;
	private final int KOMMENTAR = 36;
	private final int WIEDERGABEN = 38;
	private final int ORT = 48;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	
	
	public Playlist(String path, boolean add){
		this.pathToPlayList = path;
		this.erweitern = add;
		
		playlist = new File(this.pathToPlayList);
		
		if(playlist.exists() && !add){
			playlist.delete();
		}
		
		try{
			playlist.createNewFile();
		}catch (Exception e){
			System.err.println("Error creating " + playlist.toString()); 
		}
		
		if (playlist.isFile() && playlist.canWrite() && playlist.canRead()){
			System.out.println("Playlist erzeugt");
			if(!erweitern){
				this.initializePlaylist();
			}
		}else{
			System.err.println("Fehler beim Erzeugen der PlayList");
		}
			
	}
	
	public String getPathToPlaylist(){
		return this.pathToPlayList;
	}
	
	public String getNameOfPlaylist(){
		return this.nameOfPlayList;
	}
	
	
	public void initializePlaylist(){
		try{
			FileWriter writerInitialize = new FileWriter(playlist);
			
			for(int i = 0; i < HEADER.length ;i++){
				writerInitialize.write(HEADER[i]);
			}
			
			writerInitialize.flush();
			writerInitialize.close();
		} catch (IOException e) {
		      e.printStackTrace();
	    }
		
		System.out.println("Playlist initialisiert");
	}
	
	public void writeEntry(MP3Datei mp3){
		String[] Entry = {"","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\t","","\n"};
		Timestamp time = new Timestamp(System.currentTimeMillis());
		
		
		Entry[TITELNAME]= mp3.getTitle();
		Entry[INTERPRET]= mp3.getArtist();
		Entry[ALBUM]= mp3.getAlbumTitle();
		Entry[GENRE]= mp3.getGenre();
		Entry[TITEL_NUMMER] = mp3.getTrackNumber();
		Entry[ZAEHLER]="0";
		Entry[JAHR]= mp3.getYear();
		Entry[HINZUGEFUEGT]= sdf.format(time);
		Entry[KOMMENTAR]= "Robert rockt!";
		Entry[WIEDERGABEN] = "0";
		Entry[ORT]= mp3.getUpdatedFilePath() + "\\" + mp3.getUpdatedFileName();
		
		
		try{
			
			FileWriter writerEntry = new FileWriter(playlist,true);
			
			for(int i = 0; i < HEADER.length ;i++){
				writerEntry.write(Entry[i]);
			}
			
			writerEntry.flush();
			writerEntry.close();
			
			
		} catch (IOException e) {
		      e.printStackTrace();
	    }
		
		System.out.println("Eintrag geschrieben");
	}
	
	public void delete(){
		this.playlist.delete();
	}
	
	public boolean exists(){
		return exists;
	}
	
	public static String getHeader(){
		String header = new String();
		
		for(int i = 0; i < HEADER.length; i++){
			header += HEADER[i];
		}
		
		return header;
	}
	
}
