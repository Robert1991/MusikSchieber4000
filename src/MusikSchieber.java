import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;


import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JProgressBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;


public class MusikSchieber implements ActionListener, ListSelectionListener,ItemListener{
	
	//Konstanten für verwendete Farben
	private final Color LIGHTRED= new Color(255,179,194);
	private final Color LIGHTYELLOW= new Color(255,255,194);
		
	//***Frame-Variablen***
	
	//Frame
	public static JFrame frmMusikschieber = new JFrame();
	
	//Menu Bar
	private JMenuBar menuBar;
	private JMenu bibliothek;
	private JMenuItem bibliothekSetzen;	
	
	//Panel für ITunesBibliothek
	private JPanel panel_ITunesBibliothek;
	private JLabel label_ITunesBibliothek;
	private JTextField textField_ITunesBibliothek;
	
	//Panel für Ordner öffnen
	private JPanel panel_OrdnerWahl;
	private JLabel label_OrdnerWahl;
	private JTextField textField_OrdnerPfad;
	JButton btnOrdnerOeffnen;
	public static JProgressBar progressBar_OrdnerLesen;
	
	//Liste für eingelesene Dateien
	private JList list;
	private JScrollPane scrollPane;
	public static DefaultListModel listenModell;
		
	//Panel für Tag-Anzeige
	private JPanel panel_Tag;
	private JTextField textField_Interpret;
	private JTextField textField_Titel;
	private JTextField textField_Album;
	private JTextField textField_Track;
	private JTextField textField_Year;
	private JTextField textField_Genre;
	private JTextField textField_FilePath;
	private JTextField textField_GeleseneDateien;
	private ArrayList<JTextField> textFieldsTag = new ArrayList<JTextField>();
	JButton btnTagSpeichern;
	JButton btnMpEntfernen;
	
	//Start-Umgebung
	public static JProgressBar progressBarStart;
	public static JTextArea textArea_VerschobeneDateien;
	private JButton btnStart;
	private JScrollPane scrollPane_VerschobeneDateien;
	private JRadioButton radioButtonOriginaleEntfernen;
	private JRadioButton radioButtonOriginaleBehalten;
	
	//Panel für Playlist
	private JPanel panel_Playlist;
	private JButton buttonSpeichernUnter;
	private JLabel lblGespeichertUnter;
	private JTextField textField_gespeichertUnter;
	private JLabel lblNameDerPlaylist;
	private JTextField textField_NameDerPlaylist;
	private String pathToPlaylist;
	@SuppressWarnings("unused")
	private Playlist playlist;
	
	//Label um Playlist, Tag und Ordnerwahl zu blocken
	private JLabel label_BlockPlayList;
	private JLabel label_BlockTag;
	private JLabel label_BlockOrdnerWahl;
	
	//***Logische Variablen***
	//Array in dem MP3-Objekte hinterlegt werden
	public static ArrayList<MP3Datei> geleseneMP3 = new ArrayList<MP3Datei>();
	//Ausgewählter Index in Liste
	private int selectedIndex;
	//Array mit ausgewählten Indizies in Liste
	private int[] selectedindicies;
	//String-Array zur temporären Speicherung des Tags des in der Liste ausgewählten Objekts
	private String[] tag;
	//Zweidimensionales Array zur temporären Speicherung der Tags, wenn mehrere Einträge ausgewählt sind
	private String[][] stringFeldTags;
	//Objekt der ITunesLibrary
	private ITunesLibrary library;
	//Boolean- Original MP3-Dateien behalten
	private boolean originaleBehalten = true;
	//Boolean ob Playlist angelegt wurde
	private boolean playlistAngelegt = false;
	//Boolean ob bereits exisiterende Playlist erweitert werden soll
	private boolean playlistErgaenzen = false;
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MusikSchieber window = new MusikSchieber();
					window.frmMusikschieber.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MusikSchieber() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {	
		//***Menubar für ITunes Bibliothek Operationen***
		menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 633, 20);
		bibliothek = new JMenu("Bibliothek");
		bibliothekSetzen = new JMenuItem("ITunes Bibliothek setzen");
		bibliothekSetzen.addActionListener(this);
		menuBar.add(bibliothek);
		bibliothek.add(bibliothekSetzen);
		frmMusikschieber.getContentPane().add(menuBar);
		
		//***Initialisieren des Hauptframes***
		frmMusikschieber.getContentPane().setLocation(-12, -369);
		frmMusikschieber.setTitle("MusikSchieber 4000");
		frmMusikschieber.setBounds(100, 100, 651, 755);
		frmMusikschieber.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMusikschieber.getContentPane().setLayout(null);
		frmMusikschieber.setLocationRelativeTo(null);
		
		//***Transparentes Label um PlayList-Konfiguration geblockt aussehen zu lassen***
		label_BlockPlayList = new JLabel("");
		label_BlockPlayList.setBounds(10, 442, 609, 73);
		label_BlockPlayList.setBackground(new Color(255,255,255,50));
		label_BlockPlayList.setOpaque(true);
		frmMusikschieber.getContentPane().add(label_BlockPlayList);

		//***Transparentes Label um Tag-Konfiguration geblockt aussehen zu lassen***
		label_BlockTag = new JLabel("");
		label_BlockTag.setBounds(10, 285, 609, 150);
		label_BlockTag.setBackground(new Color(255,255,255,50));
		label_BlockTag.setOpaque(true);
		frmMusikschieber.getContentPane().add(label_BlockTag);
		
		//***Transparentes Label um Ordnerwahl geblockt aussehen zu lassen***
		label_BlockOrdnerWahl = new JLabel("");
		label_BlockOrdnerWahl.setBounds(10, 68, 609, 42);
		label_BlockOrdnerWahl.setBackground(new Color(255,255,255,50));
		label_BlockOrdnerWahl.setOpaque(true);
		frmMusikschieber.getContentPane().add(label_BlockOrdnerWahl);
				
		//Aufbau des Panels für ITunesBibliothek
		panel_ITunesBibliothek = new JPanel();
		panel_ITunesBibliothek.setLayout(null);
		panel_ITunesBibliothek.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_ITunesBibliothek.setBounds(10, 25, 609, 35);
		frmMusikschieber.getContentPane().add(panel_ITunesBibliothek);
		
		//Label für ITunesBibliothek
		label_ITunesBibliothek = new JLabel("ITunes-Bibliothek:");
		label_ITunesBibliothek.setBounds(12, 10, 107, 16);
		panel_ITunesBibliothek.add(label_ITunesBibliothek);
		
		//TextField für Pfad zu ITunesBibliothek
		textField_ITunesBibliothek = new JTextField();
		textField_ITunesBibliothek.setEditable(false);
		textField_ITunesBibliothek.setColumns(10);
		textField_ITunesBibliothek.setBackground(SystemColor.menu);
		textField_ITunesBibliothek.setBounds(121, 6, 476, 22);
		panel_ITunesBibliothek.add(textField_ITunesBibliothek);
		
		//***Aufbau des Panels zur Ordnerwahl***
		panel_OrdnerWahl = new JPanel();
		panel_OrdnerWahl.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_OrdnerWahl.setBounds(10, 68, 609, 42);
		panel_OrdnerWahl.setLayout(null);
		frmMusikschieber.getContentPane().add(panel_OrdnerWahl);
		
		//Label für Ordner
		label_OrdnerWahl = new JLabel("Ordner:");
		label_OrdnerWahl.setBounds(12, 13, 56, 16);
		panel_OrdnerWahl.add(label_OrdnerWahl);
		
		//TextField in dem OrdnerPfad erscheint
		textField_OrdnerPfad = new JTextField();
		textField_OrdnerPfad.setBounds(67, 10, 422, 22);
		textField_OrdnerPfad.setColumns(10);
		textField_OrdnerPfad.setEditable(false);
		textField_OrdnerPfad.setBackground(SystemColor.control);
		panel_OrdnerWahl.add(textField_OrdnerPfad);
		
		//Button für Ordner öffenen
		btnOrdnerOeffnen = new JButton("Öffnen");
		btnOrdnerOeffnen.addActionListener(this);
		btnOrdnerOeffnen.setBounds(500, 9, 97, 25);
		btnOrdnerOeffnen.setEnabled(false);
		panel_OrdnerWahl.add(btnOrdnerOeffnen);
		
		//***Aufbau des ScrollPanes, in dem geladene MP3 auftauchen***
		listenModell = new DefaultListModel(); 
		list = new JList(listenModell);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		//Renderer der Einträge färbt
		list.setCellRenderer(new ListRenderer());
		list.setBackground(SystemColor.control);
		
		//Listener für Listenoperationen
		list.addListSelectionListener(this);		//ScrollPane wird auf JFrame gelegt
		scrollPane = new JScrollPane(list);
		scrollPane.setBounds(10, 115, 609, 137);
		scrollPane.setBackground(SystemColor.control); 			//ScrollPane soll grau sein, solange Ordnerwahl nicht stimmt
		frmMusikschieber.getContentPane().add(scrollPane);
		
		//***Panel für Tag-Bearbeitung***
		panel_Tag = new JPanel();
		panel_Tag.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_Tag.setBounds(10, 285, 609, 150);
		frmMusikschieber.getContentPane().add(panel_Tag);
		panel_Tag.setLayout(null);
		
		//Label für Interpret
		JLabel lblInterpret = new JLabel("Interpret");
		lblInterpret.setBounds(12, 72, 50, 16);
		panel_Tag.add(lblInterpret);
		
		//Label für Titel
		JLabel lblTitel = new JLabel("Titel");
		lblTitel.setBounds(12, 96, 50, 16);
		panel_Tag.add(lblTitel);
		
		//Label für Album
		JLabel lblAlbum = new JLabel("Album");
		lblAlbum.setBounds(12, 121, 50, 16);
		panel_Tag.add(lblAlbum);
		
		//Label für Titelnummer
		JLabel lblTitelnummer = new JLabel("Titelnummer");
		lblTitelnummer.setBounds(202, 72, 79, 16);
		panel_Tag.add(lblTitelnummer);
		
		//Label für Jahr
		JLabel lblJahr = new JLabel("Jahr");
		lblJahr.setBounds(202, 96, 79, 16);
		panel_Tag.add(lblJahr);
		
		//Label für Genre
		JLabel lblGenre = new JLabel("Genre");
		lblGenre.setBounds(203, 121, 79, 16);
		panel_Tag.add(lblGenre);
		
		//Label für Anzahl gelesener MP3
		JLabel labelAnzahlDateienGelesen = new JLabel("Anzahl gelesener Dateien: ");
		labelAnzahlDateienGelesen.setFont(new Font("Tahoma", Font.PLAIN, 13));
		labelAnzahlDateienGelesen.setBounds(12, 9, 154, 18);
		panel_Tag.add(labelAnzahlDateienGelesen);
		
		//Label für DateiPfad
		JLabel lblTagInformationenZu = new JLabel("Tag Informationen zu :");
		lblTagInformationenZu.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTagInformationenZu.setBounds(12, 41, 173, 18);
		panel_Tag.add(lblTagInformationenZu);
		
		//TextField für Dateipfad gelesener Datei
		textField_FilePath = new JTextField();
		textField_FilePath.setBackground(SystemColor.control); // TextField Grau, so lange bis valide Ordnerwahl getroffen wurde
		textField_FilePath.setEditable(false);
		textField_FilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_FilePath.setBounds(180, 39, 417, 22);
		panel_Tag.add(textField_FilePath);
		textField_FilePath.setColumns(10);
		
		//TextField für Anzahl gelesener Dateien
		textField_GeleseneDateien = new JTextField();
		textField_GeleseneDateien.setEditable(false);
		textField_GeleseneDateien.setHorizontalAlignment(SwingConstants.CENTER);
		textField_GeleseneDateien.setBackground(SystemColor.control);
		textField_GeleseneDateien.setBounds(167, 8, 39, 22);
		panel_Tag.add(textField_GeleseneDateien);
		textField_GeleseneDateien.setColumns(10);
		
		//TextField für Interpet
		textField_Interpret = new JTextField();
		textField_Interpret.setBounds(74, 69, 116, 22);
		textField_Interpret.setColumns(10);
		textField_Interpret.setEditable(false); // TextField Grau und nicht editierbar, so lange bis valide Ordnerwahl getroffen wurde
		textField_Interpret.setBackground(SystemColor.control);
		panel_Tag.add(textField_Interpret);
		
		//TextField für Titel
		textField_Titel = new JTextField();
		textField_Titel.setColumns(10);
		textField_Titel.setBounds(74, 93, 116, 22);
		textField_Titel.setEditable(false); // TextField Grau und nicht editierbar, so lange bis valide Ordnerwahl getroffen wurde
		textField_Titel.setBackground(SystemColor.control);
		panel_Tag.add(textField_Titel);
		
		//TextField für Album
		textField_Album = new JTextField();
		textField_Album.setColumns(10);
		textField_Album.setBounds(74, 118, 116, 22);
		textField_Album.setEditable(false); // TextField Grau und nicht editierbar, so lange bis valide Ordnerwahl getroffen wurde
		textField_Album.setBackground(SystemColor.control);
		panel_Tag.add(textField_Album);
		
		//TextField für Track
		textField_Track = new JTextField();
		textField_Track.setColumns(10);
		textField_Track.setBounds(293, 69, 116, 22);
		textField_Track.setEditable(false); // TextField Grau und nicht editierbar, so lange bis valide Ordnerwahl getroffen wurde
		textField_Track.setBackground(SystemColor.control);
		panel_Tag.add(textField_Track);
		
		//TextField für Jahr
		textField_Year = new JTextField();
		textField_Year.setColumns(10);
		textField_Year.setBounds(293, 93, 116, 22);
		textField_Year.setEditable(false); // TextField Grau und nicht editierbar, so lange bis valide Ordnerwahl getroffen wurde
		textField_Year.setBackground(SystemColor.control);
		panel_Tag.add(textField_Year);
		
		//TextField für Genre
		textField_Genre = new JTextField();
		textField_Genre.setColumns(10);
		textField_Genre.setBounds(293, 118, 116, 22);
		textField_Genre.setEditable(false); // TextField Grau und nicht editierbar, so lange bis valide Ordnerwahl getroffen wurde
		textField_Genre.setBackground(SystemColor.control);
		panel_Tag.add(textField_Genre);
		
		//Button für TagSpeichern
		btnTagSpeichern = new JButton("Tag speichern");
		btnTagSpeichern.addActionListener(this);
		btnTagSpeichern.setBounds(481, 117, 116, 25);
		btnTagSpeichern.setEnabled(false); //Solange deaktivieren bis valide Ordnerwahl getroffen wurde
		panel_Tag.add(btnTagSpeichern);
		
		//Button für Mp3 Entfernen
		btnMpEntfernen = new JButton("Mp3 entfernen");
		btnMpEntfernen.addActionListener(this);
		btnMpEntfernen.setBounds(481, 87, 116, 25);
		btnMpEntfernen.setEnabled(false); //Solange deaktivieren bis valide Ordnerwahl getroffen wurde
		panel_Tag.add(btnMpEntfernen);
		
		//***ProgressBar für Prozess Ordner lesen***
		//Wird in Thread gesteuert
		progressBar_OrdnerLesen = new JProgressBar();
		progressBar_OrdnerLesen.setBounds(10, 262, 609, 14);
		frmMusikschieber.getContentPane().add(progressBar_OrdnerLesen);
		
		//***Panel für Playlist anlegen**
		panel_Playlist = new JPanel();
		panel_Playlist.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_Playlist.setBounds(10, 442, 609, 73);
		frmMusikschieber.getContentPane().add(panel_Playlist);
		panel_Playlist.setLayout(null);
		
		//Label für Name der Playlist
		lblNameDerPlaylist = new JLabel("Name der Playlist:");
		lblNameDerPlaylist.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNameDerPlaylist.setBounds(12, 13, 125, 16);
		panel_Playlist.add(lblNameDerPlaylist);
		
		//Label für Playlist gespeicher unter
		lblGespeichertUnter = new JLabel("Gespeichert unter:");
		lblGespeichertUnter.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblGespeichertUnter.setBounds(12, 44, 125, 16);
		panel_Playlist.add(lblGespeichertUnter);
		
		//TextField für Namen der Playlist
		textField_NameDerPlaylist = new JTextField();
		textField_NameDerPlaylist.setBounds(142, 11, 301, 22);
		textField_NameDerPlaylist.setBackground(SystemColor.control); //Solange grau und nicht editierbar bis Tags korrekt gesetzt sind
		textField_NameDerPlaylist.setEditable(false);
		panel_Playlist.add(textField_NameDerPlaylist);
		textField_NameDerPlaylist.setColumns(10);
		
		//TextField für Playlist gespeichert unter
		textField_gespeichertUnter = new JTextField();
		textField_gespeichertUnter.setBackground(SystemColor.text);
		textField_gespeichertUnter.setEditable(false);
		textField_gespeichertUnter.setColumns(10);
		textField_gespeichertUnter.setBounds(142, 42, 301, 22);
		textField_gespeichertUnter.setBackground(SystemColor.control); //Solange grau bis Tags korrekt sind
		panel_Playlist.add(textField_gespeichertUnter);
		
		//Button für Playlist speichern unter
		buttonSpeichernUnter = new JButton("Speichern unter");
		buttonSpeichernUnter.addActionListener(this);
		buttonSpeichernUnter.setBounds(454, 10, 143, 25);
		buttonSpeichernUnter.setEnabled(false); //Solange deaktiviert, bis Tags korrekt sind
		panel_Playlist.add(buttonSpeichernUnter);
		
		//***Aufbau des Bereichs für die Start-Umgebung***
		//Button für Start der Verschiebung
		btnStart = new JButton("Start!");
		btnStart.setBounds(10, 528, 97, 25);
		btnStart.setEnabled(false); //Solange deaktiviert, bis Tags korrekt sind
		btnStart.addActionListener(this);
		frmMusikschieber.getContentPane().add(btnStart);
		
		//ProgressBar, die den Fortschritt des Verschiebens anzeigt
		progressBarStart = new JProgressBar();
		progressBarStart.setBounds(119, 528, 500, 25);
		frmMusikschieber.getContentPane().add(progressBarStart);
		
		//TextArea, die Pfade der verschobene Dateien anzeigt
		textArea_VerschobeneDateien = new JTextArea();
		textArea_VerschobeneDateien.setEnabled(false);
		textArea_VerschobeneDateien.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		textArea_VerschobeneDateien.setEditable(false);
		textArea_VerschobeneDateien.setBackground(SystemColor.control);
		//ScrollPane, in welches Text-Area hineingelegt wird
		scrollPane_VerschobeneDateien = new JScrollPane(textArea_VerschobeneDateien);
		scrollPane_VerschobeneDateien.setBounds(10, 594, 609, 101);
		frmMusikschieber.getContentPane().add(scrollPane_VerschobeneDateien);
		
		//Definieren der RadioButtons, Entscheidung: Sollen Originale behalten oder gelöscht werden?
		radioButtonOriginaleBehalten = new JRadioButton("Originale behalten");
		radioButtonOriginaleBehalten.setSelected(true);
		radioButtonOriginaleBehalten.setBounds(10, 560, 142, 25);
		radioButtonOriginaleBehalten.addItemListener(this);
		radioButtonOriginaleBehalten.setEnabled(false);
		frmMusikschieber.getContentPane().add(radioButtonOriginaleBehalten);
		
		radioButtonOriginaleEntfernen = new JRadioButton("Originale entfernen");
		radioButtonOriginaleEntfernen.setSelected(false);
		radioButtonOriginaleEntfernen.setEnabled(false);
		radioButtonOriginaleEntfernen.addItemListener(this);
		radioButtonOriginaleEntfernen.setBounds(156, 560, 142, 25);
		frmMusikschieber.getContentPane().add(radioButtonOriginaleEntfernen);
		
		//Button-Group für Radio-Buttons
		ButtonGroup radiobuttons = new ButtonGroup();
		radiobuttons.add(radioButtonOriginaleBehalten);
		radiobuttons.add(radioButtonOriginaleEntfernen);
		
		
		//Überprüfen ob ITunesLibrary gesetzt wurde
		if(readLibrary() != null && readLibrary() != "" && !readLibrary().isEmpty()){
			System.out.println(readLibrary());
			library = new ITunesLibrary();
			library.setLibrary(readLibrary());
			library.writeLibrary();
			textField_ITunesBibliothek.setText(readLibrary());
			refreshOrdnerWahl(true);
		}else{
			refreshOrdnerWahl(false);
		}
	}
	
	
	/**
	 * ActionListener für verwendete Buttons
	 * @param object: ActionEvent des jeweils gedrückten Buttons
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public void actionPerformed(ActionEvent object) {
		
		//ActionListener für JMenu Eintrag BibliothekSetzen
		if(object.getSource() == bibliothekSetzen){
			//Der Pfad zu Itunes wird aus gespeicherter Datei gelesene
			String pfadZuItunes = readLibrary();
			//Für den Fall eines zweiten Einlesevorgangs wird Start-Fläche deaktiviert
			refreshStart(false);
			
			//Fall: Bibliothekspfad wurde schon gesetzt
			if(pfadZuItunes != null && pfadZuItunes != "" && !pfadZuItunes.isEmpty()){
				//Dialog Bibliothek wirklich geändert werden soll
				LibraryChangeAnswer change = new LibraryChangeAnswer(pfadZuItunes); 
				
				//Fall: Antwort für Ändern ist ja
				if(change.getChangeAnswer()){
					//FileChooser wird geöffnet, um PfadZuItunes zu wählen
					pfadZuItunes = oeffnen();
					//Fall: Fenster geschlossen
					if(!pfadZuItunes.equals("Kein Pfad angegeben"))
					{
						//ITunes-Bibliothek wird neu angelegt
						library = new ITunesLibrary();
						//Bibliothekspfad wird zu gewähltem Pfad gesetzt
						library.setLibrary(pfadZuItunes);
						//Bibliothekspfad wird gespeichert
						library.writeLibrary();
						//Bibliothekspfad wird in TextField geschrieben
						textField_ITunesBibliothek.setText(readLibrary());
						//Panel für die Wahl des einzuleseneden Ordners wird freigeschalten
						refreshOrdnerWahl(true);
					}
				}
			//Fall: ITunesbibliothek wurde noch nicht gesetzt	
			}else{
				//FileChooser zum wählen der ITunes-Bibliothekt
				pfadZuItunes = oeffnen();
				//Fall: Fenster geschlossen
				if(!pfadZuItunes.equals("Kein Pfad angegeben"))
				{
					//ITunes-Bibliothek wird neu angelegt
					library = new ITunesLibrary();
					//Bibliothekspfad wird zu gewähltem Pfad gesetzt
					library.setLibrary(pfadZuItunes);
					//Bibliothekspfad wird gespeichert
					library.writeLibrary();
					//Bibliothekspfad wird in TextField geschrieben
					textField_ITunesBibliothek.setText(readLibrary());
					//Panel für die Wahl des einzuleseneden Ordners wird freigeschalten
					refreshOrdnerWahl(true);
				}
			}	
		}
		
		//ActionListener für Button Ordner Öffnen
		if(object.getSource() == btnOrdnerOeffnen){
		  //Panel für Playlist und Start-Bereich werden deaktiviert
		  refreshPlaylist(false);
		  refreshStart(false);
		  //Angezeigte Liste wird zurückgesetzt
		  refreshList();
		  //Ordnerpfad wird über Chooser gewählt und in Text-Field gesetzt
		  String ordnerPfad = oeffnen();
		  textField_OrdnerPfad.setText(ordnerPfad);
		  
		  //Öffnen des Threads, der MP3-Dateien lädt
		  OrdnerLesenThread thread = new OrdnerLesenThread(ordnerPfad);
		  Thread th = new Thread(thread);
		  th.start();
			
		  //Gestarteter Thread und Hauptthread werden nach Ende des Einlesens zusammengeführt
		  try{
			  	th.join();
		  }catch(InterruptedException e1){
			  	e1.printStackTrace();
		  }
			
		  //Fall: es befinden sich keine Dateien im Ordner
		  if(geleseneMP3.size() == 0){
			  	//Panel für Tag-Operationen wird deaktiviert  
			  	refreshTag(false);
				//Fall: Chooser geschlossen mit Auswahl
				if(!ordnerPfad.equals("Kein Pfad angegeben")){
					@SuppressWarnings("unused")
					//Dialog der anzeigt, dass keine MP3-Dateien sich im gewählte Ordner befinden 
					NoFilesFound noFilesFound = new NoFilesFound(ordnerPfad);
				}
		  //Es wurden MP3-Dateien im gewählten Ordner gefunden
		  }else{
			  	//Tag-Feld wird freigeschalten
			  	refreshTag(true);
				//Anzahl gelesener Dateien wird im Text-Field angezeigt
				textField_GeleseneDateien.setText("" + geleseneMP3.size());
			
				//Sind alle Dateien vollständig annotiert, so wird Panel für Playlist freigeschalten
				if(!checkCriticalValues()){;
			  		System.out.println("PlayList freigegeben!");
			   		refreshPlaylist(true);
				}
		   }
       }
	   
	   //ActionListener für Button MP3-Entfernen
       if(object.getSource() == btnMpEntfernen){
    	   //Fall: Es wurde nur ein Eintrag ausgewählt 
    	   if(selectedIndex < geleseneMP3.size() && selectedIndex >= 0 && selectedindicies.length == 1){
    		   //Öffnen des Dialogs: Soll Eintrag wirklich gelöscht werden?
    		   DeleteAnswer delete = new DeleteAnswer(geleseneMP3.get(selectedIndex).getFileName());
				
    		   //Antwort des Dialogs ist Ja!
    		   if(delete.getAbbruch() == 1){
    			   //Eintrag wird aus ArrayList für gelesene MP3 entfernt
    			   geleseneMP3.remove(selectedIndex);
    			   //Eintrag wird aus Liste für angezeigte Dateien entfernt
				   listenModell.removeElementAt(selectedIndex);
				   //TextField für Anzahl gelesener Dateien wird aktualisiert
				   textField_GeleseneDateien.setText("" + geleseneMP3.size());
				   
				   //Sind alle Dateien vollständig annotiert, so wird Panel für Playlist freigeschalten
				   if(!checkCriticalValues()){;
				   		System.out.println("PlayList freigegeben!");
						refreshPlaylist(true);
				   }
				}
    		//Fall: Es wurden mehrere Einträge ausgewählt    
    	   }else if((selectedIndex < geleseneMP3.size() && selectedIndex >= 0 && selectedindicies.length >= 1)){
				
    		   //Temporäre ArrayList für zu löschende Dateien (Für Anzeige im Dialog)
    		   ArrayList<MP3Datei> dateien = new ArrayList<MP3Datei>();
    		   //Einträge die gelöscht werden sollen, werden in ArrayList übertragen				
    		   for(int i = 0; i < selectedindicies.length; i++){
    			   dateien.add(geleseneMP3.get(selectedindicies[i]));
    		   }
				
    		   //Ausgewählte Indizies werden in Int-Array - Übergeben
    		   int[] auswahl = selectedindicies;
				
    		   //Dialog zur Frage, ob Dateien wirklich gelöscht werden soll + Anzeige der Dateien
    		   MultipleDeleteAnswer answer = new MultipleDeleteAnswer(dateien);
				
    		   //Kein Abbruch! ---> Einträge entfernen
    		   if(answer.getAbbruch() == false){
    			   
    			   //Auswahl wird von hinten aus ArrayList und Liste gelöscht
    			   //Damit Listeneinträge und ArrayList übereinstimmen
    			   for(int i = auswahl.length - 1; i >= 0; i--){
    				   geleseneMP3.remove(auswahl[i]);
    				   listenModell.removeElementAt(auswahl[i]);
    			   }
					
    			   //TextField für gelesene Dateien wird aktualisiert
    			   textField_GeleseneDateien.setText("" + geleseneMP3.size());
				
    			   //Sind alle Dateien vollständig annotiert, so wird Panel für Playlist freigeschalten
    			   if(!checkCriticalValues()){;
    			   		System.out.println("PlayList freigegeben!");
						refreshPlaylist(true);
    			   }
				}
			}else{
				//Fall: Keine Datei zum Löschen gewählt
				DeleteAnswer delete = new DeleteAnswer("");
			}
       }
       
       //ActionListener für Button Tag-Speichern
       if(object.getSource() == btnTagSpeichern){
    	   //Fall: Es wurde nur eine Datei gewählt
    	   if(selectedIndex < geleseneMP3.size() && selectedIndex >= 0 && selectedindicies.length == 1){
    		   //Überprüfen, ob Artist, Album und Titel ausgefüllt wurden
    		   //Fall: Unvollständiger Eintrag
    		   if((textFieldsTag.get(0).getText().contains("unknown") && textFieldsTag.get(0).getText().length() == 7) || (textFieldsTag.get(1).getText().contains("unknown") && textFieldsTag.get(1).getText().length() == 7) || (textFieldsTag.get(2).getText().contains("unknown") && textFieldsTag.get(2).getText().length() == 7) || textFieldsTag.get(0).getText().isEmpty() || textFieldsTag.get(1).getText().isEmpty() || textFieldsTag.get(2).getText().isEmpty() || textFieldsTag.get(0).getText() == null || textFieldsTag.get(1).getText() == null || textFieldsTag.get(2).getText() == null){
    			   //Tag wird zurückgesetzt
    			   tag = geleseneMP3.get(selectedIndex).tagToArray();
    			   //TextFields werden zurückgesetzt
    			   textFieldstoArray();
    			   //Farbe wird zurückgesetzt
    			   textFieldsFill(tag);
    			   //Dialog: Unvollständiger Eintrag
    			   new MissingValue(0);
    			//Fall: Ungültiger numerischer Eintrag   
    		   }else if(!checkIfNumber(textFieldsTag.get(3).getText()) || !checkIfNumber(textFieldsTag.get(4).getText())){
    			   //Tag wird zurückgesetzt
    			   tag = geleseneMP3.get(selectedIndex).tagToArray();
    			   //TextFields werden zurückgesetzt
    			   textFieldstoArray();
    			   //Farbe wird zurückgesetzt
    			   textFieldsFill(tag);
    			   //Dialog: Ungültiger numerischer Eintrag
    			   new MissingValue(1);
    			//Alle Einträge koorekt
    		   }else{
    			   //Einträge werden im auswählten MP3-Objekt gespeichert
    			   geleseneMP3.get(selectedIndex).setArtist(textFieldsTag.get(0).getText());
    			   geleseneMP3.get(selectedIndex).setTitle(textFieldsTag.get(1).getText());
    			   geleseneMP3.get(selectedIndex).setAlbumTitle(textFieldsTag.get(2).getText());
    			   geleseneMP3.get(selectedIndex).setTrackNumber(textFieldsTag.get(3).getText());
    			   geleseneMP3.get(selectedIndex).setYear(textFieldsTag.get(4).getText());
    			   geleseneMP3.get(selectedIndex).setGenre(textFieldsTag.get(5).getText());
					
    			   //Tag wird zurückgesetzt
    			   tag = geleseneMP3.get(selectedIndex).tagToArray();
    			   //TextFields werden zurückgesetzt
    			   textFieldstoArray();
    			   //Farbe wird zurückgesetzt
    			   textFieldsFill(tag);
    			   //Farbe des Eintrags wird aktualisiert
    			   Object[] eintrag = {geleseneMP3.get(selectedIndex).getFilePath(),setColorEintrag(selectedIndex)};
    			   //Eintrag wird in aktualisierter Form in Liste hinzugefügt
    			   listenModell.setElementAt(eintrag, selectedIndex);
					
    			   //Sind alle Dateien vollständig annotiert, so wird Panel für Playlist freigeschalten
    			   if(!checkCriticalValues()){;
    			   		System.out.println("PlayList freigegeben!");
						refreshPlaylist(true);
    			   }
				}
    		 //Fall: Es wurde mehrere Dateien gewählt
			}else if(selectedIndex < geleseneMP3.size() && selectedIndex >= 0 && selectedindicies.length > 1){
				//Fall: ungültiger numierischer Eintrag (Titelnummer,Jahr)
				if(!checkIfNumber(textFieldsTag.get(3).getText()) || !checkIfNumber(textFieldsTag.get(4).getText())){
					//Dialog: Ungültiger numerischer Eintrag
					new MissingValue(1);
				//Fall: alle Einträge ok!	
				}else{
					for(int i = 0; i < selectedindicies.length; i++){					
						//Wenn Einträge gesetzt, dann schreibe
						if(!textFieldsTag.get(0).getText().isEmpty() && !textFieldsTag.get(0).getText().equals("unknown"))
							geleseneMP3.get(selectedindicies[i]).setArtist(textFieldsTag.get(0).getText());
						
						if(!textFieldsTag.get(2).getText().isEmpty() && !textFieldsTag.get(2).getText().equals("unknown"))
							geleseneMP3.get(selectedindicies[i]).setAlbumTitle(textFieldsTag.get(2).getText());
						
						if(!textFieldsTag.get(4).getText().isEmpty() && checkIfNumber(textFieldsTag.get(4).getText()))
							geleseneMP3.get(selectedindicies[i]).setYear(textFieldsTag.get(4).getText());
						
						if(!textFieldsTag.get(5).getText().isEmpty() && !textFieldsTag.get(5).getText().equals("unknown"))
							geleseneMP3.get(selectedindicies[i]).setGenre(textFieldsTag.get(5).getText());
						
						//Farbe des Eintrags wird aktualisiert
						Object[] eintrag = {geleseneMP3.get(selectedindicies[i]).getFilePath(),setColorEintrag(selectedindicies[i])};
						//Eintrag wird in aktualisierter Form in Liste hinzugefügt
						listenModell.setElementAt(eintrag, selectedindicies[i]);
					}
					
					//Sind alle Dateien vollständig annotiert, so wird Panel für Playlist freigeschalten
					if(!checkCriticalValues()){;
					System.out.println("PlayList freigegeben!");
						refreshPlaylist(true);
					}
				}
				
			}	
			else{
				//Fall: Keine Datei zum Speichern gewählt
				new MissingValue(2);
			}	
		}
       
       if (object.getSource() == buttonSpeichernUnter){
    	   textField_gespeichertUnter.setText("");
    	   String pathToDir = oeffnen();
    	   if(textField_NameDerPlaylist.getText().isEmpty()){
    		   new NameDerPlaylistNichtAngegeben();
    	   }else{		
    		   if(pathToDir.equals("Kein Pfad angegeben")){
    			   //Fall Fenster Abbruch
    			   textField_gespeichertUnter.setText("");
    			   refreshStart(false);
    		   }else{	
    			   //Liegt bereits eine Playlist vor, so lösche diese!
    			   pathToPlaylist = pathToDir + "\\" + textField_NameDerPlaylist.getText() + ".txt";
    			   textField_gespeichertUnter.setText(pathToPlaylist);
					
    			   if((new File(pathToPlaylist)).exists() == true){
    				   //Check ob es sich um eine ITunesBibliothek handelt
    				   String zeile = new String();
    				   String header = new String();
						
    				   try {
    					   BufferedReader in = new BufferedReader(new FileReader(pathToPlaylist));
    					   zeile = in.readLine();
    				   } catch (IOException e1) {
    					   e1.printStackTrace();
    				   }
						
    				   //Zeile um Tabs, Zeilenumbrüche und Leerzeichen bereinigen
    				   zeile = zeile.replaceAll("\t","");
    				   zeile = zeile.replaceAll(" ","");
    				   zeile = zeile.replaceAll("\n","");
						
    				   //Tue das gleiche mit dem Header
    				   header = Playlist.getHeader();
    				   header = header.replaceAll("\t","");
    				   header = header.replaceAll(" ","");
    				   header = header.replaceAll("\n","");
						
    				   if(zeile.contains(header)){
    					   //Ja oder nein
    					   PlayListOverrideAnswer answer = new PlayListOverrideAnswer(pathToPlaylist,true);
							
    					   if(answer.getAnswer() == 2){
    						   playlistErgaenzen = true;
    						   playlistAngelegt = true;
    						   refreshStart(true);
    					   }else if(answer.getAnswer() == 1){
    						   playlistAngelegt = true;
    						   playlistErgaenzen = false;
    						   refreshStart(true);
    					   }else{
    						   textField_gespeichertUnter.setText("");
    						   textField_NameDerPlaylist.setText("");
    						   pathToPlaylist = "missing";
    						   playlistAngelegt = false;
    						   playlistErgaenzen = false;
    						   refreshStart(false);
    					   }
    				   }else{
    					   PlayListOverrideAnswer answer = new PlayListOverrideAnswer(pathToPlaylist,false);
							
    					   if(answer.getAnswer() == 1){
    						   playlistErgaenzen = false;
    						   playlistAngelegt = true;
    						   refreshStart(true);
    					   }else{
    						   playlistErgaenzen = false;
    						   playlistAngelegt = false;
    						   refreshStart(false);
								
    						   textField_gespeichertUnter.setText("");
    						   textField_NameDerPlaylist.setText("");
								
    						   pathToPlaylist = "missing";
    					   }
    				   }
					}else{
						playlistAngelegt = true;
						refreshStart(true);
					}
					
					System.out.println("Playlist angelegt:" + " " + playlistAngelegt);
					System.out.println("Playlist ergaenzen:" + " " + playlistErgaenzen);
					System.out.println("Pfad:" + " " + pathToPlaylist);
					
				}	
			}
       }
       
       if(object.getSource() == btnStart){
           boolean missingNonCriticalValues = checkNonCriticalValues();
           if(missingNonCriticalValues){
        	   System.out.println("Gelbe Werte liegen vor");
        	   StartMissingValues answer = new StartMissingValues();
        	   
        	   if(answer.getAnswer()){
        		   System.out.println("Good To Go!");
        		   DateienVerschiebenThread thread = new DateienVerschiebenThread(playlistErgaenzen,originaleBehalten,library,pathToPlaylist);
        		   Thread th = new Thread(thread); //
        		   th.start(); //
        			
        		   try{
        			   th.join();
        		   }catch(InterruptedException e1){
        			   e1.printStackTrace();
        		   }
        	   }
        	   
           }else{
        	   System.out.println("Good To Go!");
    		   DateienVerschiebenThread thread = new DateienVerschiebenThread(playlistErgaenzen,originaleBehalten,library,pathToPlaylist);
    		   Thread th = new Thread(thread); 
    		   th.start(); 
    			
    		   try{
    			   th.join();
    		   }catch(InterruptedException e1){
    			   e1.printStackTrace();
    		   }

           }
       }  
	}
	
	public void valueChanged(ListSelectionEvent listSelectionEvent){
		boolean adjust = listSelectionEvent.getValueIsAdjusting();

        if (!adjust) {
          JList list = (JList) listSelectionEvent.getSource();
          selectedIndex = list.getSelectedIndex();
          selectedindicies = list.getSelectedIndices();
          
          
          if(selectedIndex < geleseneMP3.size() && selectedIndex >= 0 && selectedindicies.length == 1){
        	  tag = geleseneMP3.get(selectedIndex).tagToArray();
        	  textFieldstoArray();
        	  
        	  textFieldsTag.get(1).setEditable(true);
        	  textFieldsTag.get(3).setEditable(true);
        	  
        	  textFieldsFill(tag);
        	  textField_FilePath.setText(geleseneMP3.get(selectedIndex).getFilePath());
        	  
          } else if(selectedIndex < geleseneMP3.size() && selectedIndex >= 0 && selectedindicies.length > 1){
        	  stringFeldTags = new String[selectedindicies.length][tag.length + 1];
        	  tagFieldClear();
        	  
        	  
        	  for(int i = 0; i < selectedindicies.length; i++){
        		  stringFeldTags[i][0] = "" + i +"";
        		  tag = geleseneMP3.get(selectedindicies[i]).tagToArray();
        		  
        		  for(int j = 1; j < tag.length + 1; j++){
        			  stringFeldTags[i][j] = tag[j-1];
        		  }
        	  }
        	  
        	  textFieldstoArray();
        	  multipleTagsToTextFields(stringFeldTags);
        	  
          }else{
        	  list.setSelectedIndex(0);
          }
        	  
        }
    }
	
	public void itemStateChanged(ItemEvent e){
		if(e.getSource() == radioButtonOriginaleEntfernen){
			if(radioButtonOriginaleEntfernen.isSelected()){
				originaleBehalten = false;
			}
		}else if(e.getSource() == radioButtonOriginaleBehalten){
			if(radioButtonOriginaleBehalten.isSelected()){
				originaleBehalten = true;
			}
		}
		
		System.out.println("Entfernen? ist:" + originaleBehalten);
	}
	
	public String readLibrary(){
		ObjectInputStream ois = null;
		FileInputStream fis = null;
		String path;
		
		try {
		  fis = new FileInputStream("config/library.ser");
		  ois = new ObjectInputStream(fis);
		  Object obj = ois.readObject();
		  if (obj instanceof ITunesLibrary) {
			  ITunesLibrary so = (ITunesLibrary) obj;
			  path= so.getLibrary();
			  return path;
		  }else{
			  return "";
		  }
		}
		catch (java.io.EOFException e) {
			return "";
		}
		
		catch (IOException e) {
		  e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
		  e.printStackTrace();
		}
		finally {
		  if (ois != null) try { ois.close(); } catch (IOException e) {}
		  if (fis != null) try { fis.close(); } catch (IOException e) {}
		}
		return "";
	}
	
	
	/**
	 * Öffnen des Ordners aus dem Musik geladen werden soll
	 * @return: Rückgabe des Ordnerpfads (String)
	 */
	public String oeffnen() { 
		   final JFileChooser chooser = new JFileChooser("Verzeichnis wählen"); 
	       chooser.setDialogType(JFileChooser.OPEN_DIALOG); 
	       chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
	       final File file = new File("/home"); 

	       chooser.setCurrentDirectory(file); 

	       chooser.addPropertyChangeListener(new PropertyChangeListener() { 
	           public void propertyChange(PropertyChangeEvent e) { 
	               if (e.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) 
	                       || e.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)) { 
	                   final File f = (File) e.getNewValue(); 
	               } 
	           } 
	       }); 

	       chooser.setVisible(true); 
	       final int result = chooser.showOpenDialog(null); 

	       if (result == JFileChooser.APPROVE_OPTION) { 
	           File inputVerzFile = chooser.getSelectedFile(); 
	           String inputVerzStr = inputVerzFile.getPath();
	           return inputVerzStr; 
	       } 
	       chooser.setVisible(false);
	       return "Kein Pfad angegeben";
	}
	
	/**
	 * Angezeigte Liste, Array mit MP3-Objekten und Pfaden zu den MP3-Dateien werden gelöscht
	 * Anzeige wird aktualisiert
	 */
	public void refreshList(){
		textField_GeleseneDateien.setText("");
		geleseneMP3.clear();
		list.repaint();
		System.out.println("All Lists clear!");
	}
	
	public void refreshOrdnerWahl(boolean setzen){
		if(setzen){
			label_BlockOrdnerWahl.setOpaque(false);
			textField_OrdnerPfad.setBackground(Color.WHITE);
			btnOrdnerOeffnen.setEnabled(true);
			frmMusikschieber.repaint();
		}else{
			label_BlockOrdnerWahl.setOpaque(true);
			textField_OrdnerPfad.setBackground(SystemColor.control);
			btnOrdnerOeffnen.setEnabled(false);
			frmMusikschieber.repaint();
		}
	}
	
	
	public void refreshTag(boolean setzen){
		if(setzen){
			list.setBackground(Color.WHITE);
			label_BlockTag.setOpaque(false);
			textField_Interpret.setBackground(Color.WHITE);
			textField_Interpret.setEditable(true);
			textField_Titel.setBackground(Color.WHITE);
			textField_Titel.setEditable(true);
			textField_Album.setBackground(Color.WHITE);
			textField_Album.setEditable(true);
			textField_Track.setBackground(Color.WHITE);
			textField_Track.setEditable(true);
			textField_Year.setBackground(Color.WHITE);
			textField_Year.setEditable(true);
			textField_Genre.setBackground(Color.WHITE);
			textField_Genre.setEditable(true);
			btnTagSpeichern.setEnabled(true);
			btnMpEntfernen.setEnabled(true);
			textField_FilePath.setBackground(Color.WHITE);
			frmMusikschieber.repaint();
		}else{
			list.setBackground(SystemColor.control);
			label_BlockTag.setOpaque(true);
			textField_Interpret.setBackground(SystemColor.control);
			textField_Interpret.setEditable(false);
			textField_Titel.setBackground(SystemColor.control);
			textField_Titel.setEditable(false);
			textField_Album.setBackground(SystemColor.control);
			textField_Album.setEditable(false);
			textField_Track.setBackground(SystemColor.control);
			textField_Track.setEditable(false);
			textField_Year.setBackground(SystemColor.control);
			textField_Year.setEditable(false);
			textField_Genre.setBackground(SystemColor.control);
			textField_Genre.setEditable(false);
			btnTagSpeichern.setEnabled(false);
			btnMpEntfernen.setEnabled(false);
			textField_FilePath.setBackground(SystemColor.control);
			frmMusikschieber.repaint();
		}
	}
	
	public void refreshPlaylist(boolean setzen){
		if(setzen){
			textField_NameDerPlaylist.setBackground(Color.WHITE); //Solange grau und nicht editierbar bis Tags korrekt gesetzt sind
			textField_NameDerPlaylist.setEditable(true);
			textField_gespeichertUnter.setBackground(Color.WHITE);
			buttonSpeichernUnter.setEnabled(true);
			label_BlockPlayList.setOpaque(false);
			frmMusikschieber.repaint();
		}else{
			textField_NameDerPlaylist.setBackground(SystemColor.control); //Solange grau und nicht editierbar bis Tags korrekt gesetzt sind
			textField_NameDerPlaylist.setEditable(false);
			textField_NameDerPlaylist.setText("");
			textField_gespeichertUnter.setBackground(SystemColor.control);
			textField_gespeichertUnter.setText("");
			buttonSpeichernUnter.setEnabled(false);
			label_BlockPlayList.setOpaque(true);
			frmMusikschieber.repaint();
		}
	}
	
	public void refreshStart(boolean setzen){
		if(setzen){
			btnStart.setEnabled(true); 
			textArea_VerschobeneDateien.setBackground(Color.WHITE);
			textArea_VerschobeneDateien.setEnabled(true);
			radioButtonOriginaleBehalten.setEnabled(true);
			radioButtonOriginaleEntfernen.setEnabled(true);
		}else{
			btnStart.setEnabled(false);
			textArea_VerschobeneDateien.setBackground(SystemColor.control);
			textArea_VerschobeneDateien.setText("");
			radioButtonOriginaleBehalten.setEnabled(false);
			radioButtonOriginaleEntfernen.setEnabled(false);
			progressBarStart.setStringPainted(false);
			progressBarStart.setValue(0);
			frmMusikschieber.repaint();
		}
	}
	
	
	public boolean checkCriticalValues(){
		boolean missing = false;
		
		for(int i = 0; i < geleseneMP3.size();i++){
			if(geleseneMP3.get(i).missingCriticalValues() == true){
				missing = true;
			}
		}
		return missing;
	}
	
	public boolean checkNonCriticalValues(){
		boolean missing = false;
		
		for(int i = 0; i < geleseneMP3.size();i++){
			if(geleseneMP3.get(i).missingNonCriticalValues() == true){
				missing = true;
			}
		}
		return missing;
	}
	
	
	/**
	 * Setzen der entsprechenden Farben für Anzeige in Liste
	 * @param index: Für die momentan behandelte MP3-Datei
	 * @return Passende Farbe für Anzeige
	 */
	public static Color setColorEintrag(int index){
		boolean yellow = false;
		boolean red = false;
		
		if(geleseneMP3.get(index).getArtist() == "unknown" || geleseneMP3.get(index).getTitle() == "unknown" || geleseneMP3.get(index).getAlbumTitle() == "unknown"){
			red = true;
		} 
		
		if((geleseneMP3.get(index).getTrackNumber().contains("0") && geleseneMP3.get(index).getTrackNumber().length() == 1) || geleseneMP3.get(index).getGenre().contains("unknown") || geleseneMP3.get(index).getGenre().isEmpty() || geleseneMP3.get(index).getTrackNumber().isEmpty()){
			yellow = true;
		}
		
		if(red && yellow){
			return Color.RED;
		}else if(red && !yellow){
			return Color.RED;
		}else if(red == false && yellow == true){
			return new Color(255,191,0);
		}else{
			return Color.BLACK;
		}
	}
	
	public void textFieldstoArray(){
		textFieldsTag.clear();	
		textFieldsTag.add(textField_Interpret);
		textFieldsTag.add(textField_Titel);
		textFieldsTag.add(textField_Album);
		textFieldsTag.add(textField_Track);
		textFieldsTag.add(textField_Year);
		textFieldsTag.add(textField_Genre);
		textFieldsTag.add(textField_FilePath);
	}
	
	public void textFieldsFill(String[] tag){
		boolean missingEntry = false;
		boolean missingEntry1 = false;
		
		for(int i = 0; i <= 2 ; i++){
			textFieldsTag.get(i).setText(tag[i]);
			textFieldsTag.get(i).setBackground(Color.WHITE);
			
			if(tag[i] == null || tag[i] == "" || (tag[i].equals("unknown") && tag[i].length() == 7)){
				textFieldsTag.get(i).setBackground(LIGHTRED);
				missingEntry = true;
			}
		}
		
		for(int i = 3; i <= 5 ; i++){
			textFieldsTag.get(i).setText(tag[i]);
			textFieldsTag.get(i).setBackground(Color.WHITE);
			
			if(tag[i] == null || tag[i].isEmpty() || tag[i].equals("0") || tag[i].contains("unknown")){
				textFieldsTag.get(i).setBackground(LIGHTYELLOW);
				missingEntry1=true;
			}
		}
		
		if(missingEntry == true){
			textFieldsTag.get(6).setBackground(LIGHTRED);
		}else if(missingEntry1 == true){
			textFieldsTag.get(6).setBackground(LIGHTYELLOW);
		}else{
			textFieldsTag.get(6).setBackground(Color.WHITE);
		}
		
	}
	
	public void multipleTagsToTextFields(String[][] tagField){
		boolean allSame = true;
		
		for(int zaehler = 1; zaehler < tag.length + 1; zaehler++){
			String vergleich = tagField[0][zaehler];
			
			for(int i = 1; i < selectedindicies.length; i++){
				//System.out.println(tagField[0][zaehler] + "==" + tagField[i][zaehler] + "?");
				if(!tagField[i][zaehler].equals(vergleich)){
					allSame = false;
				}
			}
			
			if(allSame == true){
				textFieldsTag.get(zaehler - 1).setText(tagField[0][zaehler]);
			}else{
				textFieldsTag.get(zaehler - 1).setText("");
			}
			
			allSame=true;
		}
		
	}
	
	public boolean checkIfNumber(String s){
		try {
			if(s == "0" || s.isEmpty()){
			    return true;
		    }else{
		    	int number = Integer.parseInt(s);
		    	return true;
		    }
			 
		 }
		 catch(NumberFormatException e) {
		 	return false;
		 }
	}
	
	public void tagFieldClear(){
		textFieldsTag.get(0).setBackground(Color.WHITE);
		textFieldsTag.get(2).setBackground(Color.WHITE);
		textFieldsTag.get(4).setBackground(Color.WHITE);
		textFieldsTag.get(5).setBackground(Color.WHITE);
		textFieldsTag.get(6).setBackground(SystemColor.control);
		textFieldsTag.get(6).setText("");
		textFieldsTag.get(1).setEditable(false);
  	  	textFieldsTag.get(3).setEditable(false);
  	  	textFieldsTag.get(1).setBackground(SystemColor.control);
  	  	textFieldsTag.get(3).setBackground(SystemColor.control);
	}
}
