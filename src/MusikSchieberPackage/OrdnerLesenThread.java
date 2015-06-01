package MusikSchieberPackage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class OrdnerLesenThread implements Runnable {

	private Boolean stop = false;
	private String ordnerPfad;
	private ArrayList<String> geleseneDaten = new ArrayList<String>();
	private int anzahlOther = 0;
	
	
	public OrdnerLesenThread(String ordnerPfad){ 
		this.ordnerPfad = ordnerPfad;
	}
	
	
    public void run(){
    	File dir = new File(ordnerPfad); 
    	String dateiPfad;
    	String artist;
    	String title;
    	String duplicatePath;
		MP3Datei mp3datei;
		boolean duplicate;
		System.out.print("Im Array:\n");

		Database database = new Database();
		database.connect();

    	ordnerAuswahlInGeleseneDaten(dir); 
    	
    	Collections.sort(geleseneDaten, String.CASE_INSENSITIVE_ORDER);
    	try{
    		
    	}catch(java.lang.NullPointerException e){
    		System.err.println("Im Ordner befinden sich keine MP3-Dateien");
    	}
    	
    	
    	MusikSchieber.progressBar_OrdnerLesen.setStringPainted(true);
		MusikSchieber.frmMusikschieber.getContentPane().add(MusikSchieber.progressBar_OrdnerLesen);
    	
        while(!stop){
    		MusikSchieber.progressBar_OrdnerLesen.setMaximum(geleseneDaten.size());
    		MusikSchieber.listenModell.clear();	
    		
    		for(int i = 0; i< geleseneDaten.size(); i++){
    			//Erzeugen eines neuen MP3-Objektes
    			MusikSchieber.progressBar_OrdnerLesen.setValue(i+1);
    			MusikSchieber.progressBar_OrdnerLesen.paint(MusikSchieber.progressBar_OrdnerLesen.getGraphics());
    			mp3datei = new MP3Datei(geleseneDaten.get(i));
    					
    			//Ist die Datei nicht beschädigt, so wird diese in das geleseneMP3-Array übernommen
    			if(mp3datei.damaged() == false){	
    				MusikSchieber.geleseneMP3.add(mp3datei);
    				
    				artist = mp3datei.getArtist();
    				title = mp3datei.getTitle();
    				
    				if(database.searchValue(artist,title) == true){
    					MusikSchieber.geleseneMP3.get(i).setDuplicate(true);
    					duplicatePath = database.getFilepath(artist, title);
    					MusikSchieber.geleseneMP3.get(i).setDuplicateFilepath(duplicatePath);
    				}
    				
    				
    				dateiPfad = MusikSchieber.geleseneMP3.get(i).getFilePath();
    				Object[] eintrag = {dateiPfad,MusikSchieber.setColorEintrag(i)};
    				MusikSchieber.listenModell.addElement(eintrag);
    			}else{
    				geleseneDaten.remove(i);
    				new DamagedFile(mp3datei);
    				i = i - 1;
    			}

    		}		
    		
    		database.disconnect();
    		
    		MusikSchieber.progressBar_OrdnerLesen.setStringPainted(false);
    		MusikSchieber.progressBar_OrdnerLesen.setValue(geleseneDaten.size());
    		
    		System.out.println("Anzahl von nicht MP3:" + anzahlOther);
    		System.out.println("Anzahl von nicht MP3:" + (anzahlOther + MusikSchieber.geleseneMP3.size()));
    		setStop(true);
        }
    }
    
    public Boolean getStop() {
        return stop;
    }

    public void setStop(Boolean stop) {
        this.stop = stop;
    }
    
    public void ordnerAuswahlInGeleseneDaten(File dir) {//
		//Dateien im Ordner werden in Array geschrieben
		File[] files = dir.listFiles();
			
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				//Handelt es sich um einen Ordner so wird diese Methode rekursiv auf den Ordner angewandt
				if (files[i].isDirectory()) {
					ordnerAuswahlInGeleseneDaten(files[i]);
				}
				else {
					//handelt es sich um eine MP3-Datei, so wird diese in das Array übernommen
					if(files[i].getAbsolutePath().contains(".mp3")){
						geleseneDaten.add(files[i].getAbsolutePath());
					}else{
						anzahlOther++;
						System.err.println("Andere: " + files[i].getAbsolutePath());
					}
					
				}
			}
		}
	}

}
