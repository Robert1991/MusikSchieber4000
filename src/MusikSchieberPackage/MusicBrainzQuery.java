package MusikSchieberPackage;
import java.io.*;
import java.lang.Object;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MusicBrainzQuery {
	private final static String CHROMAPRINTADDRESS = "http://api.acoustid.org/v2/lookup?format=xml&meta=releaseids&client=5aiX6y4m&"; 
	private final static String MUSICBRAINZRECORDINGSQUERY = "http://musicbrainz.org/ws/2/release/?query=reid:";
	private final static String MUSICBRAINZARTISTQUERY = "http://musicbrainz.org/ws/2/artist/?query=arid:";
	private String acusticID = "";
	private String duration  = "";
	private URL chromaPrintURL;
	private URL musicBrainzRecordingURL;
	private URL musicBrainzArtistURL;
	private HttpURLConnection connectionChromaprint;
	private HttpURLConnection connectionMusicBrainzRecording;
	private HttpURLConnection connectionMusicBrainzArtist;
	private ArrayList<String> recordingIDS = new ArrayList<String>();
	private ArrayList<String> artistIDS = new ArrayList<String>();
	private ArrayList<String> titles = new ArrayList<String>();
	private ArrayList<String> albums = new ArrayList<String>();
	private ArrayList<MusicBrainzEntry> entry = new ArrayList<MusicBrainzEntry>();
	
	
	public MusicBrainzQuery(String pathToMP3){
		//(1) Schritt: Akustischer Fingerabdruck erzeugen
		
		processID(pathToMP3);
		
		//(2) Schritt: TrackID und ArtistID lesen
		
		chromaPrintQuery();
		
		//(3) Schritt: Metadaten aus Musicbrainz abrufen
		musicBrainzQuery();
	}
	
	
	private void processID(String pathToMP3){
		Process p;
		String query;
		String[] queryArray;
		
		try {
			p = Runtime.getRuntime().exec( "data/fpcalc.exe \"" + pathToMP3 + "\"");
			InputStream input = p.getInputStream();
			
			query = IOUtils.toString(input, "UTF-8");
			queryArray = query.split(Pattern.quote("\n"));

			duration = queryArray[1];
			acusticID = queryArray[2];
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	private void chromaPrintQuery(){
		try {
			duration = duration.substring(9, 12);
			acusticID = acusticID.substring(12, acusticID.length() - 1);
			chromaPrintURL = new URL(CHROMAPRINTADDRESS + "duration=" + duration + "&fingerprint=" + acusticID);
			connectionChromaprint = (HttpURLConnection) chromaPrintURL.openConnection();
			connectionChromaprint.setRequestMethod("GET");
			connectionChromaprint.setRequestProperty("Accept", "application/xml");
			connectionChromaprint.setUseCaches(false);
			
			
			 if (connectionChromaprint.getResponseCode() != HttpURLConnection.HTTP_OK) {
				    System.out.println(connectionChromaprint.getResponseMessage());
				    System.err.println(connectionChromaprint.getResponseCode());
			 }
			 else {
				try {
					InputStream xml = connectionChromaprint.getInputStream();
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					org.w3c.dom.Document doc = db.parse(xml);
					NodeList recordingids = doc.getElementsByTagName("release");
					
					
					for(int i = 0; i < recordingids.getLength(); i ++){
						Element recording = (Element) recordingids.item(i);
						NodeList recordingIDList = recording.getElementsByTagName("id");
						Element value = (Element) recordingIDList.item(0);
						recordingIDS.add(value.getFirstChild().getNodeValue());
					}
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			 }
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void musicBrainzQuery(){
		
		for(int i = 0; i < recordingIDS.size(); i++){
			entry.add(new MusicBrainzEntry());
			try {
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				musicBrainzRecordingURL = new URL(MUSICBRAINZRECORDINGSQUERY + recordingIDS.get(i));
				System.out.println(musicBrainzRecordingURL);
				connectionMusicBrainzRecording = (HttpURLConnection) musicBrainzRecordingURL.openConnection();
				connectionMusicBrainzRecording.setRequestMethod("GET");
				connectionMusicBrainzRecording.setRequestProperty("Accept", "application/xml");
				connectionMusicBrainzRecording.setRequestProperty("User-Agent","MusikSchieber4000/0.9 ( rwaddell91@googlemail.com )");
				connectionMusicBrainzRecording.setUseCaches(false);
				if (connectionMusicBrainzRecording.getResponseCode() != HttpURLConnection.HTTP_OK) {
				    System.out.println(connectionMusicBrainzRecording.getResponseMessage());
				    System.err.println(connectionMusicBrainzRecording.getResponseCode());
				}
				else {

				 try {
					String artist  = "";
					String title = "";
					String album  = "";
					String track  = "";
					String year  = "";
					String date = "";
					
					
					InputStream xmlRecording = connectionMusicBrainzRecording.getInputStream();
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					org.w3c.dom.Document doc = db.parse(xmlRecording);
					NodeList artistIDS = doc.getElementsByTagName("artist");
					
					for(int j = 0; j < artistIDS.getLength() ; j++){
						Element artistID = (Element) artistIDS.item(j);
						this.artistIDS.add(artistID.getAttribute("id"));
						artist = artistID.getAttribute("id");
					}
					
					NodeList recordings = doc.getElementsByTagName("recording");
					
					for(int rIT = 0; rIT < recordings.getLength(); rIT++){
						Element recordingElement = (Element)recordings.item(rIT);
						NodeList titlesList = recordingElement.getElementsByTagName("title");
						Element titleElement = (Element)titlesList.item(0);
						title =titleElement.getFirstChild().getNodeValue();
						System.out.println(title);
						entry.get(i).setTitle(titleElement.getFirstChild().getNodeValue());
					}
					
					NodeList releases = doc.getElementsByTagName("release");
					NodeList tracks = doc.getElementsByTagName("track");
					
					for(int reIT = 0; reIT < releases.getLength(); reIT++){
						Element releaseElement = (Element)releases.item(reIT);
						NodeList albumList = releaseElement.getElementsByTagName("title");
						NodeList dateList = releaseElement.getElementsByTagName("date");

						date = "";
						if(dateList.getLength()> 0){
							Element albumdateElement = (Element)dateList.item(0);
							date = albumdateElement.getFirstChild().getNodeValue().substring(0,4);
							year = date;
						}
						
						entry.get(i).setYear(year);
						
						Element albumtitleElement = (Element)albumList.item(0);
						Element trackElement = (Element)tracks.item(reIT);
						NodeList trackList = releaseElement.getElementsByTagName("track-count");
						Element trackNumberElement = (Element)trackList.item(0);
						
						album = albumtitleElement.getFirstChild().getNodeValue();
						entry.get(i).setAlbum(albumtitleElement.getFirstChild().getNodeValue());
						track = trackNumberElement.getFirstChild().getNodeValue();
						entry.get(i).setTrackNumber(trackNumberElement.getFirstChild().getNodeValue());
						albums.add(albumtitleElement.getFirstChild().getNodeValue() + ";" + trackNumberElement.getFirstChild().getNodeValue()+ ";" + date);
					}

					
					musicBrainzArtistURL = new URL(MUSICBRAINZARTISTQUERY + artist);
					connectionMusicBrainzArtist = (HttpURLConnection) musicBrainzArtistURL.openConnection();
					connectionMusicBrainzArtist.setRequestMethod("GET");
					connectionMusicBrainzArtist.setRequestProperty("Accept", "application/xml");
					connectionMusicBrainzArtist.setRequestProperty("User-Agent","MusikSchieber4000/0.9 ( rwaddell91@googlemail.com )");
					connectionMusicBrainzArtist.setUseCaches(false);
					
					
					if (connectionMusicBrainzArtist.getResponseCode() != HttpURLConnection.HTTP_OK) {
					    System.out.println(connectionMusicBrainzArtist.getResponseMessage());
					    System.err.println(connectionMusicBrainzArtist.getResponseCode());
					}
					else {
						 try {
							 InputStream xmlArtist = connectionMusicBrainzArtist.getInputStream();
							 dbf = DocumentBuilderFactory.newInstance();
							 db = dbf.newDocumentBuilder();
							 doc = db.parse(xmlArtist);
							 
							 NodeList artistList = doc.getElementsByTagName("artist");
							 Element artistElement = (Element) artistList.item(0);
							 NodeList artistNameList = artistElement.getElementsByTagName("name");
							 entry.get(i).setArtist(artistNameList.item(0).getFirstChild().getNodeValue());
							 
							 if(!entry.get(i).getArtist().contains("Various Artists")){
								 NodeList tagList = doc.getElementsByTagName("tag");
								 
								 for(int tagCount = 0; tagCount < tagList.getLength(); tagCount++){
									 Element tags = (Element)tagList.item(tagCount);
									 NodeList tagNames = tags.getElementsByTagName("name");
									 Element tagName = (Element)tagNames.item(0);
									 entry.get(i).addGenre(tagName.getFirstChild().getNodeValue());
								 }
								 
							 }
						 }catch (ParserConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SAXException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			
			} catch (ProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		for(int i = 0; i < entry.size(); i++){
			System.out.println("--------------------------------------------");
			System.out.println("Artist: " + entry.get(i).getArtist());
			System.out.println("Title: " + entry.get(i).getTitle());
			System.out.println("Album: " + entry.get(i).getAlbum());
			System.out.println("TrackNumber: " + entry.get(i).getTrackNumber());
			System.out.println("Year: " + entry.get(i).getYear());
			System.out.println("Genres: " + entry.get(i).getYear());
			for(int j = 0; j < entry.get(i).getGenres().size() ; j++){
				System.out.println(entry.get(i).getGenres().get(j));
			}
			
			System.out.println("--------------------------------------------");
		}
		
	}
	
	private ArrayList<String> removeDuplicatedEntries(ArrayList<String> arrayList) {
		HashSet<String> hashSet = new HashSet<String>(arrayList);
		arrayList.clear();
		arrayList.addAll(hashSet);
		   
		return arrayList;
	}
	
}
