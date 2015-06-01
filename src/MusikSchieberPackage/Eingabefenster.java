package MusikSchieberPackage;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.net.HttpURLConnection;
import java.net.SocketException;

import javax.json.Json; 
import javax.json.JsonArray; 
import javax.json.JsonObject; 
import javax.json.JsonReader; 
import javax.json.JsonStructure; 
import javax.json.JsonValue; 
import javax.json.JsonValue.ValueType; 





import javax.json.stream.JsonParser;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import AudioFiles.MP4Datei;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;

import java.net.URL;


public class Eingabefenster
{  
   public static void main(String[] args) throws SocketException, InterruptedException {
	   
	   
	   //MusicBrainzQuery query = new MusicBrainzQuery("C:\\MusikTestOrdner\\Jay - Z - Lost One.mp3");
	   
	   File datei = new File("C:\\MusikTestOrdner\\Compuphonic - Sunset (feat. Marques Toliver).mp3");
	   File datei1 = new File("C:\\MusikTestOrdner\\09 Bescheid.m4a");
	   File datei2 = new File("C:\\MusikTestOrdner\\08 3 Lil Puntos.m4a");
	   PlayMP3 play = new PlayMP3();
	   //play.open("C:\\MusikTestOrdner\\01 Beginner - Das Boot.m4a");
	   
	   AudioFiles.MP3Datei beginner = new AudioFiles.MP3Datei(datei);
	   
	   /*
	   Socket socket;
	   
	   try {
		socket = new Socket( "freedb.musicbrainz.org", 80 );
		System.out.println( socket.getKeepAlive() );           // false
		System.out.println( socket.getLocalAddress() );        // /192.168.2.138
		System.out.println( socket.getLocalPort() );           // 1456
		System.out.println( socket.getLocalSocketAddress() );  // /192.168.2.138:1202
		System.out.println( socket.getOOBInline() );           // false
		System.out.println( socket.getPort() );                // 80
		System.out.println( socket.getRemoteSocketAddress() ); // www.tutego.com/82.96.100.30:80
		System.out.println( socket.getReuseAddress() );        // false
		System.out.println( socket.getReceiveBufferSize() );   // 8192
		System.out.println( socket.getSendBufferSize() );      // 8192
		System.out.println( socket.getSoLinger() );            // –1
		System.out.println( socket.getTcpNoDelay() );          // false
		System.out.println( socket.getTrafficClass() );        // 0}
		*/
		
		
		/*
		SocketAddress addr = new InetSocketAddress( "freedb.musicbrainz.org", 80 );
		socket.connect( addr, 100 );
		
		
	   
	   
		URL url = new URL( "http://musicbrainz.org/ws/2/release/?query=country:unknown" );

		System.out.println( url.getProtocol() );  // http
		System.out.println( url.getHost() );      // www.tutego.com
		System.out.println( url.getPort() );      // 80
		System.out.println( url.getFile() );      // /java/faq.html?key=val
		System.out.println( url.getPath() );      // /java/faq.html
		System.out.println( url.getQuery() );     // key=val
		System.out.println( url.getRef() );       // Lang
		
		InputStream is = null;
		*/
	   /*
	   try {
		
		
	   	URL url = new URL( "http://api.acoustid.org/v2/lookup?format=xml&client=5aiX6y4m&meta=recordings+releasegroups+compress&duration=641&fingerprint=AQABz0qUkZK4oOfhL-CPc4e5C_wW2H2QH9uDL4cvoT8UNQ-eHtsE8cceeFJx-LiiHT-aPzhxoc-Opj_eI5d2hOFyMJRzfDk-QSsu7fBxqZDMHcfxPfDIoPWxv9C1o3yg44d_3Df2GJaUQeeR-cb2HfaPNsdxHj2PJnpwPMN3aPcEMzd-_MeB_Ej4D_CLP8ghHjkJv_jh_UDuQ8xnILwunPg6hF2R8HgzvLhxHVYP_ziJX0eKPnIE1UePMByDJyg7wz_6yELsB8n4oDmDa0Gv40hf6D3CE3_wH6HFaxCPUD9-hNeF5MfWEP3SCGym4-SxnXiGs0mRjEXD6fgl4LmKWrSChzzC33ge9PB3otyJMk-IVC6R8MTNwD9qKQ_CC8kPv4THzEGZS8GPI3x0iGVUxC1hRSizC5VzoamYDi-uR7iKPhGSI82PkiWeB_eHijvsaIWfBCWH5AjjCfVxZ1TQ3CvCTclGnEMfHbnZFA8pjD6KXwd__Cn-Y8e_I9cq6CR-4S9KLXqQcsxxoWh3eMxiHI6TIzyPv0M43YHz4yte-Cv-4D16Hv9F9C9SPUdyGtZRHV-OHEeeGD--BKcjVLOK_NCDXMfx44dzHEiOZ0Z44Rf6DH5R3uiPj4d_PKolJNyRJzyu4_CTD2WOvzjKH9GPb4cUP1Av9EuQd8fGCFee4JlRHi18xQh96NLxkCgfWFKOH6WGeoe4I3za4c5hTscTPEZTES1x8kE-9MQPjT8a8gh5fPgQZtqCFj9MDvp6fDx6NCd07bjx7MLR9AhtnFnQ70GjOcV0opmm4zpY3SOa7HiwdTtyHa6NC4e-HN-OfC5-OP_gLe2QDxfUCz_0w9l65HiPAz9-IaGOUA7-4MZ5CWFOlIfe4yUa6AiZGxf6w0fFxsjTOdC6Itbh4mGD63iPH9-RFy909XAMj7mC5_BvlDyO6kGTZKJxHUd4NDwuZUffw_5RMsde5CWkJAgXnDReNEaP6DTOQ65yaD88HoeX8fge-DSeHo9Qa8cTHc80I-_RoHxx_UHeBxrJw62Q34Kd7MEfpCcu6BLeB1ePw6OO4sOF_sHhmB504WWDZiEu8sKPpkcfCT9xfej0o0lr4T5yNJeOvjmu40w-TDmqHXmYgfFhFy_M7tD1o0cO_B2ms2j-ACEEQgQgAIwzTgAGmBIKIImNQAABwgQATAlhDGCCEIGIIM4BaBgwQBogEBIOESEIA8ARI5xAhxEFmAGAMCKAURKQQpQzRAAkCCBQEAKkQYIYIQQxCixCDADCABMAE0gpJIgyxhEDiCKCCIGAEIgJIQByAhFgGACCACMRQEyBAoxQiHiCBCFOECQFAIgAABR2QAgFjCDMA0AUMIoAIMChQghChASGEGeYEAIAIhgBSErnJPPEGWYAMgw05AhiiGHiBBBGGSCQcQgwRYJwhDDhgCSCSSEIQYwILoyAjAIigBFEUQK8gAYAQ5BCAAjkjCCAEEMZAUQAZQCjCCkpCgFMCCiIcVIAZZgilAQAiSHQECOcQAQIc4QClAHAjDDGkAGAMUoBgyhihgEChFCAAWEIEYwIJYwViAAlHCBIGEIEAEIQAoBwwgwiEBAEEEOoEwBY4wRwxAhBgAcKAESIQAwwIowRFhoBhAE" );
		//conn.setRequestProperty("User-Agent","MusikSchieber4000/0.9 ( rwaddell91@googlemail.com )");
		
		HttpURLConnection connection =
		    (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/xml");
		connection.setUseCaches(false);
		
		InputStream xml = connection.getInputStream();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		org.w3c.dom.Document doc = db.parse(xml);
		
		 if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
		    System.out.println(connection.getResponseMessage());
		 }
		 else {
			 
			 
			 System.out.println (doc.getDocumentElement().getNodeName());
			 
			 NodeList artists = doc.getElementsByTagName("artists");
	         int totalartists = artists.getLength();
	         System.out.println("Arists number " + totalartists);
			 
	         Node firstartist = artists.item(1);
	         Element firstartistelement = (Element) firstartist;
	         NodeList firstartistnode = firstartistelement.getElementsByTagName("id");
	         Element firstartistElement = (Element)firstartistnode.item(0);
	         NodeList textFNList = firstartistElement.getChildNodes();
	         
	         String artistID = (String)textFNList.item(0).getNodeValue().trim();
	         
	         System.out.println("First Name : " + 
                     ((Node)textFNList.item(0)).getNodeValue().trim());
	         
	         
	         url = new URL( "http://musicbrainz.org/ws/2/artist?query=arid:"+artistID);
	         connection = (HttpURLConnection) url.openConnection();
	     	 connection.setRequestMethod("GET");
	     	 connection.setRequestProperty("Accept", "application/xml");
	     	 connection.setRequestProperty("Musikschieber4000/0.9","( rwaddell91@googlemail.com )");
	     	 connection.setUseCaches(false);
	     	 
	     	 xml = connection.getInputStream();
	     	 doc = db.parse(xml);
	         
	     	 
	     	doc.getDocumentElement ().normalize();
			System.out.println (doc.getDocumentElement().getNodeName());
	     	 
	     	 
			/* 
			  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()) );
			  
			  String inputJsonString = reader.readLine();
			  
			  reader.close(); 
			  
			  JsonParserFactory factory=JsonParserFactory.getInstance();
			  JSONParser parser= factory.newJsonParser();
			  Map jsonData=parser.parseJson(inputJsonString);
			  
			  System.out.println(jsonData.get("artist"));
			  
			  ArrayList results =  (ArrayList) jsonData.get("results");
			  
			  System.out.println(results.get(0));
			  System.out.println(results.get(1));
			  
		  }
	    
	   } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   } catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 */
   }   
   
   public static void listDir(File dir, Statement stmt) {

		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory() && (files[i].getName().length() == 1 || files[i].getName().equals("123"))) {
					listDir(files[i],stmt); 
				}
				else if(files[i].isDirectory()){
					listDir(files[i],stmt);
				}else{
					if(files[i].getAbsolutePath().contains(".mp3")){
						System.out.println("---------------------------------------------------");
						MP3Datei datei = new MP3Datei(files[i].getAbsolutePath());
						String[] teile = files[i].getAbsolutePath().split(Pattern.quote("\\"));
						
						String artist = cleanString(teile[teile.length-3]);
						String album = cleanString(teile[teile.length-2]);
						String title = cleanString(datei.getTitle());
						String filepath = cleanString(files[i].getAbsolutePath());
						
						System.out.println("Artist" + " " + artist);
						System.out.println("Album" + " " + album);
						System.out.println("Title" + " " + title);
						System.out.println("Filename" + " " + filepath);
						
						double timeStamp = System.currentTimeMillis();
						try {
							stmt.executeUpdate( "INSERT INTO musicEntries " +
							         "VALUES(" +timeStamp+ ",'"+ artist +"','"+ album +"','"+title+"','"+filepath+"')");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("---------------------------------------------------");
					}
				}
			}
		}
	}
   
   public static String cleanString(String name){
		String unzulaessigeZeichen = "<>?\":|/*()'";		
		
		for(int i = 0;i < unzulaessigeZeichen.length(); i++){
			name = name.replace(unzulaessigeZeichen.substring(i,i+1),"");
		}

		return name;
	}
   
   
   
   private static void print(JsonValue value) { 
       JsonObject object = null; 
       if (value.getValueType() == ValueType.OBJECT) { 
           object = (JsonObject) value;
           
           if(object.getJsonObject("artists").getString("id")!=null)
           System.out.println(object.getJsonObject("artists").getString("id"));
           
           for (java.util.Map.Entry<String, JsonValue> set : object.entrySet()) { 
               if (set.getValue() instanceof JsonArray) { 
                   System.out.println("Array: "); 
                   print((JsonValue) set.getValue()); 
               } else { 
                   //System.out.println(set.getKey() + ": " + set.getValue());
               } 
           } 
       } else if (value.getValueType() == ValueType.ARRAY) { 
           JsonArray array = (JsonArray) value;
           
           for (JsonValue val : array) 
               print(val); 
       } 
   } 
   
   
}   	   
