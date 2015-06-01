import java.awt.*;

import javax.sound.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.management.modelmbean.ModelMBean;
import javax.swing.*;

import java.beans.PropertyChangeEvent; 
import java.beans.PropertyChangeListener; 
import java.io.*;
import java.util.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;



import java.net.URL;

import com.ldodds.musicbrainz.MusicBrainz;
import com.ldodds.musicbrainz.MusicBrainzImpl;
import com.mpatric.mp3agic.*;
import com.hp.hpl.jena.rdf.model.*;



public class Eingabefenster
{  
   
   public static void main(String[] args) throws SocketException {
	   
	   /*
	   Playlist playlist = new Playlist("C:\\MusikTestOrdner\\test1.txt",false);
	   
	   MP3Datei datei1 = new MP3Datei("C:\\MusikTestOrdner\\RANDY VALENTINE FT BLEMISH - KEEP ON TRODDING ON.mp3");
	   MP3Datei datei2 = new MP3Datei("C:\\MusikTestOrdner\\SIZZLA-HOTTEST GIRL.mp3");
	   MP3Datei datei3 = new MP3Datei("C:\\MusikTestOrdner\\Vybz Kartel - Poor People Land {The Message Riddim} JAN 2011 (Don Corleon Rec).mp3");
	   
	   datei1.makeDir("C:\\MusikTestOrdner\\ItunesTestBibliothek");
	   datei2.makeDir("C:\\MusikTestOrdner\\ItunesTestBibliothek");
	   datei3.makeDir("C:\\MusikTestOrdner\\ItunesTestBibliothek");
	   
	   datei1.replace(true);
	   datei2.replace(true);
	   datei3.replace(true);
	   
	   playlist.writeEntry(datei1);
	   playlist.writeEntry(datei2);
	   playlist.writeEntry(datei3);
	   */
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
		*/
		
	   
	   /*
		URL url = new URL( "http://musicbrainz.org/ws/2/release/?query=country:unknown" );

		System.out.println( url.getProtocol() );  // http
		System.out.println( url.getHost() );      // www.tutego.com
		System.out.println( url.getPort() );      // 80
		System.out.println( url.getFile() );      // /java/faq.html?key=val
		System.out.println( url.getPath() );      // /java/faq.html
		System.out.println( url.getQuery() );     // key=val
		System.out.println( url.getRef() );       // Lang
		
		InputStream is = null;
		
		 //url = new URL( "http://musicbrainz.org/ws/2/release/?query=country:unknown" );
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestProperty("User-Agent","MusikSchieber4000/0.9 ( rwaddell91@googlemail.com )");
		
		  conn.setUseCaches(false);
		  if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
		    System.out.println(conn.getResponseMessage());
		  }
		  else {
			  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()) );

			  for ( String line; (line = reader.readLine()) != null; )
			  {
				  System.out.println( line );
			  }
			  reader.close();
		  }
	     
	   } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
	   
		*/
	   }
  }   	   
	  
	   /*
	   Playlist playlist = new Playlist("C:\\MusikTestOrdner","test");
	   
	   MP3Datei datei2 = new MP3Datei("C:\\MusikTestOrdner\\04. Zion I - The Bay - www.torrentazos.com.mp3");
	   
	   System.out.println(datei2.getArtist());
	   //MP3Datei datei3 = new MP3Datei("C:\\MusikTestOrdner\\03. Zion I - Birds Eye View - www.torrentazos.com.mp3");
	   /*MP3Datei datei1 = new MP3Datei("C:\\MusikTestOrdner\\1 - 50Cent - Realest Song.mp3");
	   
	   
	   System.out.println("Album " + datei1.getAlbumTitle());
	   System.out.println("Artist " + datei1.getArtist());
	   
	   System.out.println("Genre " + datei1.getGenre());
	   System.out.println("TrackNr " + datei1.getTrackNumber());
	   System.out.println("Title " + datei1.getTitle());
	   System.out.println("Year " + datei1.getYear());
	   System.out.println("Filename " + datei1.getFileName());
	   System.out.println("Path " + datei1.getFilePath());
	   
	   datei1.setFilename();

	   
	   
	   datei2.setFilename();
	   String pfad2 = datei2.makeDir("C:\\MusikTestOrdner\\ItunesTestBibliothek");
	   datei2.replace(pfad2);

	   
	   datei3.setFilename();
	   
	   System.out.println(datei1.getUpdatedFileName());
	   System.out.println(datei2.getUpdatedFileName());
	   
	   String pfad1 = datei1.makeDir("C:\\MusikTestOrdner\\ItunesTestBibliothek");
	    */
	   //
	   
	   //String pfad3 = datei3.makeDir("C:\\MusikTestOrdner\\ItunesTestBibliothek");
	   
	   
	   //System.out.println(datei2.getUpdatedFilePath());
	   
	   //System.out.println(pfad2);
	   
	   //datei1.replace(pfad1);
	   //
	   /*
	   datei3.replace(pfad3);
	   playlist.writeEntry(datei2);
	   
	   */

	   