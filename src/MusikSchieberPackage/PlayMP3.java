package MusikSchieberPackage;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Map;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;


public class PlayMP3 implements BasicPlayerListener{
	private BasicPlayer player;
	private BasicController playerController;
	long lengthOfMP3InMilliSeconds;
	long timePlayed = 0;
	long timeBeingDisplayed;
	long positionInSong = 0;
	long seek = 0;
	private long mp3SizeInBytes;
	private long positionInMP3;
	private int status;
	public final static int PLAYING = 0;
	public final static int PAUSED = 1;
	public final static int OPENED = 2;
	public final static int RESUMED = 3;
	public final static int STOPPED = 4;
	private final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
	private Time timeAlreadyPlayed = new Time(0);
	private Time timeLeft = new Time(0);

	
	private boolean disableProgress = false;
	
	public PlayMP3(){
		player = new BasicPlayer();
		player.addBasicPlayerListener(this);
		playerController = (BasicController)player;
	}
	
	public void open(String pathToMP3){
		File f = new File(pathToMP3);
		this.mp3SizeInBytes = f.length();
		try {
			playerController.open(f);
			PlayerGUI.slider.setMaximum((int) mp3SizeInBytes);
			PlayerGUI.slider.setMinimum(0);
		} catch (BasicPlayerException e) {
			e.printStackTrace();
			System.err.println("MP3 kann nicht geöffnet werden!");
		}
	}
	
	public void play(){
		try {
			if(status != PAUSED){
				playerController.play();
			}else{
				this.resume();
			}
			
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void pause(){
		try {
			System.out.println("Hier: " + player.getStatus());
			playerController.pause();
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void resume(){
		try {
			playerController.resume();
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		try {
			playerController.stop();
			PlayerGUI.slider.setValue(0);
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}
	
	public void seek(int position){
		try {
			System.out.println(player.getStatus());
			playerController.seek(position);
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}
	
	public void setGain(double volume){
		try {
			playerController.setGain(volume);
		} catch (BasicPlayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public long getMP3LengthInMilliSeconds(){
		return lengthOfMP3InMilliSeconds;
	}
	
	public long getTimeLeft(){
		return (lengthOfMP3InMilliSeconds-timePlayed);
	}
	
	public long getTimePlayed(){
		return timePlayed;
	}
	
	public long getDataLength(){
		return mp3SizeInBytes;
	}
	
	public int getStatus(){
		return status;
	}
	
	public void calculateTimePlayed(long currentByte){
		double position = (double) currentByte;
		double mp3SizeInBytesDouble = (double) mp3SizeInBytes;
		double portion =(double) (position/mp3SizeInBytesDouble);
		
		timePlayed = (int)(lengthOfMP3InMilliSeconds*portion);
	}
	
	public void disableProgress(boolean disabled){
		this.disableProgress = disabled;
	}
	
	
	@Override
	public void opened(Object arg0, Map arg1) {
		lengthOfMP3InMilliSeconds = (long) arg1.get("duration");
		lengthOfMP3InMilliSeconds = lengthOfMP3InMilliSeconds/(1000);
		System.out.println(arg1.toString());
	}

	@Override
	public void progress(int arg0, long arg1, byte[] arg2, Map arg3) {
		// TODO Auto-generated method stub
		calculateTimePlayed(arg0);
		
		timeAlreadyPlayed.setTime(getTimePlayed());
		timeLeft.setTime(getTimeLeft());
		
		if(!disableProgress){
			PlayerGUI.slider.setValue(arg0);
			PlayerGUI.lblActualTime.setText(sdf.format(timeAlreadyPlayed));
			PlayerGUI.lblTimeLeft.setText("-" + sdf.format(timeLeft));
		}
		 
	}

	@Override
	public void setController(BasicController arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateUpdated(BasicPlayerEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println((arg0.toString()));
		
		if((arg0.toString()).contains("PLAYING")){
			status = PLAYING;
		}else if((arg0.toString()).contains("PAUSED")){
			status = PAUSED;
		}else if((arg0.toString()).contains("OPENED")){
			status = OPENED;
		}else if((arg0.toString()).contains("RESUMED")){
			status = RESUMED;
		}else if((arg0.toString()).contains("STOPPED")){
			status = STOPPED;
			
			if(getTimeLeft() == 0){
				System.out.println("Lied zu Ende!");
				int selected = MusikSchieber.list.getSelectedIndex();
				if(selected < MusikSchieber.listenModell.getSize()){
					MusikSchieber.list.setSelectedIndex(selected + 1);
					PlayerGUI.btnPlayPause.setIcon(PlayerGUI.iconPauseDisabled);
					PlayerGUI.btnPlayPause.setRolloverIcon(PlayerGUI.iconPause);
					PlayerGUI.play.play();
					PlayerGUI.play.setGain(PlayerGUI.volume);
				}
			}	
		}
	}
}
