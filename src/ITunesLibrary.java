import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;



public class ITunesLibrary implements Serializable{
	private String pathToLibrary;
	
	public ITunesLibrary(){

	}
	
	/*
	public ITunesLibrary(String path){
		this.pathToLibrary = path;
	}
	*/
	
	public void setLibrary(String path){
		this.pathToLibrary = path;
	}
	
	public String getLibrary(){
		return this.pathToLibrary;
	}
	
	public void writeLibrary(){
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
		  fos = new FileOutputStream("config/library.ser");
		  oos = new ObjectOutputStream(fos);
		  
		  oos.writeObject(this);
		}
		catch (IOException e) {
		  e.printStackTrace();
		}
		finally {
		  if (oos != null) try { oos.close(); } catch (IOException e) {}
		  if (fos != null) try { fos.close(); } catch (IOException e) {}
		}
	}
}
