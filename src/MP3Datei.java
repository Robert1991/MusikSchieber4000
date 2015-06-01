import java.io.File;
import com.mpatric.mp3agic.*;

public class MP3Datei {
	
	private Mp3File mp3datei;
	private ID3v2 ID3v2Tag;
	private ID3v1 ID3v1Tag;
	private File datei;
	private String newFilename;
	private String artist;
	private String album;
	private String title;
	private String tracknumber = "";
	private String genre = "";
	private String newFilePath;
	private String year = "";
	private final String[] GENRE_LIST = {"Blues","Classic Rock","Country","Dance","Disco","Funk","Grunge","Hip-Hop","Jazz","Metal","New Age","Oldies","Other","Pop","Rhtym and Blues","Rap","Reggae","Rock","Techno","Industrial","Alternative","Ska","Death Metal", "Pranks", "Soundtrack","Euro-Techno","Ambient","Trip-Hop","Vocal","Jazz & Funk","Fusion","Trance","Classical","Instrumental","Acid","House","Game","Sound Clip","Gospel","Noise","Alternative Rock","Bass","Soul","Punk","Space","Meditative","Instrumental Pop","Instrumental Rock","Ethnic","Gothic","Darkwave","Techno-Industrial","Electronic","Pop-Folk","Eurodance","Dream","Southern Rock","Comedy","Cult","Gangsta","Top 40","Christian Rap","Pop/Funk","Jungle","Native US", "Cabaret","New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock&Roll", "Hard Rock"};
	private final String NUMBERS = "0123456789"; 
	private boolean damaged;
	private boolean missingValues;
	private boolean ID3v2;
	
	
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
				mp3datei = new Mp3File(dateipfad);
				if(mp3datei.hasId3v2Tag()){
					ID3v2Tag = mp3datei.getId3v2Tag();
					ID3v2 = true;
					System.out.println("ID3v2");
				} 
				else if(mp3datei.hasId3v1Tag()){
					ID3v1Tag = mp3datei.getId3v1Tag();
					ID3v2 = false;
					System.err.println("ID3v1");
				}else{
					ID3v2Tag = new ID3v24Tag();
					mp3datei.setId3v2Tag(ID3v2Tag);
					ID3v2 = true;
					System.err.println("None");
				}
				
				this.artist = cleanString(this.getArtist());
				this.album = cleanString(this.getAlbumTitle());
				this.title = cleanString(this.getTitle());
				this.tracknumber = cleanString(this.getTrackNumber());
				this.genre = identifyGenre(this.getGenreNumber());
				this.year = this.getYear();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
	
	public void setTitle(String title){
		if(ID3v2){
			ID3v2Tag.setTitle(title);
		}else{
			ID3v1Tag.setTitle(title);
		}
		
		this.title = cleanString(this.getTitle());
	}
	
	public void setArtist(String artist){
		if(ID3v2){
			ID3v2Tag.setArtist(artist);
		}else{
			ID3v1Tag.setArtist(artist);
		}
		
		this.artist = cleanString(this.getArtist());
	}
	
	public void setYear(String year){
		this.year = year;
		if(this.year.isEmpty() || this.year == null){
			if(ID3v2){
				ID3v2Tag.setYear("");
			}else{
				ID3v1Tag.setYear("");
			}
		}else{
			if(ID3v2){
				ID3v2Tag.setYear(year);
			}else{
				ID3v1Tag.setYear(year);
			}
		}
	}
	
	public void setTrackNumber(String number){
		tracknumber = cleanString(number);
		
		if(ID3v2){
			ID3v2Tag.setTrack(tracknumber);
		}else{
			ID3v1Tag.setTrack(tracknumber);
		}
		
	}
	
	public void setGenre(String genre){
		if(ID3v2){
			ID3v2Tag.setGenre(this.getGenreInt(genre));
		}else{
			ID3v1Tag.setGenre(this.getGenreInt(genre));
		}

		this.genre = genre;
	}
	
	public void setAlbumTitle(String album){
		if(ID3v2){
			ID3v2Tag.setAlbum(album);
		}else{
			ID3v1Tag.setAlbum(album);
		}

		this.album = cleanString(this.getAlbumTitle());
	}
	
	public void setFilename(){ 
		if(!this.getTrackNumber().equals("0") && !this.getTrackNumber().equals("") && this.getTrackNumber() != null){
			newFilename = this.tracknumber + " - " + this.artist + " - " + this.title + ".mp3";
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
	
	public String getTitle(){
		String title;
		if(ID3v2){
			title = ID3v2Tag.getTitle();
		}else{
			title = ID3v1Tag.getTitle();
		}
		
		if(title != null && title.length() > 0){
			return title;
		}
		else{
			return "unknown";
		}
			
	}
	
	public String getArtist(){
		String artist;
		
		if(ID3v2){
			artist = ID3v2Tag.getArtist();
		}else{
			artist = ID3v1Tag.getArtist();
		}
		
		if(artist != null && artist.length() > 0){
			return artist;
		}
		else{
			return "unknown";
		}
	}
	
	public String getYear(){
		String year;
		
		if(ID3v2){
			year = ID3v2Tag.getYear();
		}else{
			year = ID3v1Tag.getYear();
		}
		
		if(year != null && !year.isEmpty()){
			return year;
		}
		else{
			return "";
		}
	}
	
	public String getTrackNumber(){
		String track;
		
		if(ID3v2){
			track = ID3v2Tag.getTrack();
		}else{
			track = ID3v1Tag.getTrack();
		}
		
		if(track != null && track.length() > 0){
			return track;
		}
		else{
			return "0";
		}	
	}
	
	public int getGenreNumber(){
		int genre;
		
		if(ID3v2){
			genre = ID3v2Tag.getGenre();
		}else{
			genre = ID3v1Tag.getGenre();
		}
		
		if(genre < 80){
			return genre;
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
		String album;
		
		if(ID3v2){
			album = ID3v2Tag.getAlbum();
		}else{
			album = ID3v1Tag.getAlbum();
		}
		
		
		if(album  != null && album.length() > 0){
			return album;	
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
	
	public void delete(){
		datei.delete();
	}
	
	public void replace(boolean keep){
		mp3datei.removeId3v2Tag();
		mp3datei.removeId3v1Tag();
		
		if(ID3v2){
			mp3datei.setId3v2Tag(ID3v2Tag);
		}else{
			mp3datei.setId3v1Tag(ID3v1Tag);
		}
		
		this.setFilename();
		
		try{
			mp3datei.save(newFilePath + "\\" + newFilename);
			if(!keep){
				this.delete();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
			
	}
	
	public String cleanString(String name){
		String unzulaessigeZeichen = "<>?\"\\:|/*";		
		
		for(int i = 0;i < unzulaessigeZeichen.length(); i++){
			name = name.replace(unzulaessigeZeichen.substring(i,i+1),"");
		}

		return name;
	}
	
	public String makeDir(String path){
		File ebene1;
		File ebene2;
		File ebene3;
		
		if(NUMBERS.contains(artist.substring(0,1))){
			ebene1 = new File(path + "\\" +  "123");
			ebene2 = new File(path + "\\" + "123" + "\\" + this.artist);
			ebene3 = new File(path + "\\" +  "123" + "\\" + this.artist + "\\" + this.album);
		}else{
			
			ebene1 = new File(path + "\\" +  this.artist.toUpperCase().charAt(0));
			ebene2 = new File(path + "\\" +  this.artist.toUpperCase().charAt(0) + "\\" + (this.artist.toUpperCase()).charAt(0) + this.artist.substring(1,artist.length()));
			ebene3 = new File(path + "\\" +  this.artist.toUpperCase().charAt(0) + "\\" + (this.artist.toUpperCase()).charAt(0) + this.artist.substring(1,artist.length()) + "\\"+ (this.album.toUpperCase()).charAt(0) + this.album.substring(1,album.length()));
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
		array[4] = this.getYear();
		array[5] = this.getGenre();
		
		return array;
	}
}
