
public class DateienVerschiebenThread implements Runnable{
	private boolean playListErgaenzen = false;
	private boolean originaleBehalten = false;
	private boolean stop = false;
	private ITunesLibrary library;
	private String pathToPlayList;
	private String pathToITunes;
	
	
	public DateienVerschiebenThread(boolean playListErgaenzen,boolean originaleBehalten,ITunesLibrary library,String pathToPlayList){
		this.playListErgaenzen = playListErgaenzen;
		this.originaleBehalten = originaleBehalten;
		this.library = library;
		this.pathToPlayList = pathToPlayList;
		this.pathToITunes = library.getLibrary();
	}
	
	public void run(){
		//ProgressBar konfigurieren
		MusikSchieber.progressBarStart.setStringPainted(true);
		MusikSchieber.progressBarStart.setMaximum(MusikSchieber.geleseneMP3.size());
		MusikSchieber.frmMusikschieber.getContentPane().add(MusikSchieber.progressBarStart);

		//PlayList anlegen
		Playlist playlist = new Playlist(pathToPlayList,playListErgaenzen);
		
		while(!stop){
			//Dateien Schieben und in Playlist schreiben
			for(int i = 0; i < MusikSchieber.geleseneMP3.size();i++){
				//Progressbar
				MusikSchieber.progressBarStart.setValue(i+1);
    			MusikSchieber.progressBarStart.paint(MusikSchieber.progressBarStart.getGraphics());
    			
    			//Ordner für MP3 anlegen
    			MusikSchieber.geleseneMP3.get(i).makeDir(pathToITunes);
    			//MP3Datei verschieben
    			MusikSchieber.geleseneMP3.get(i).replace(originaleBehalten);
    			//Textarea aktualisieren
    			MusikSchieber.textArea_VerschobeneDateien.paint(MusikSchieber.textArea_VerschobeneDateien.getGraphics());
    			MusikSchieber.textArea_VerschobeneDateien.append(MusikSchieber.geleseneMP3.get(i).getUpdatedFilePath() + "\\" + MusikSchieber.geleseneMP3.get(i).getUpdatedFileName() + "\n");
    			
    			//Playlisteintrag erzeugen
    			playlist.writeEntry(MusikSchieber.geleseneMP3.get(i));
			}
			
			MusikSchieber.progressBarStart.setStringPainted(false);
			MusikSchieber.progressBarStart.paint(MusikSchieber.progressBarStart.getGraphics());
			this.stop = true;
		}
	}
}
