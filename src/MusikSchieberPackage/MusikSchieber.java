package MusikSchieberPackage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.SystemColor;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import java.util.Collections;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JRadioButton;


public class MusikSchieber implements ActionListener, ListSelectionListener,MouseListener,ItemListener,DocumentListener{
	
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
	private JMenuItem bibliothekUpdate;
	
	//Panel für ITunesBibliothek
	private JPanel panel_ITunesBibliothek;
	private JLabel label_ITunesBibliothek;
	private JTextField textField_ITunesBibliothek;
	
	//Panel für PlayerGui
	private PlayerGUI player;
	
	//Panel für Ordner öffnen
	private JPanel panel_OrdnerWahl;
	private JLabel label_OrdnerWahl;
	private JTextField textField_OrdnerPfad;
	JButton btnOrdnerOeffnen;
	public static JProgressBar progressBar_OrdnerLesen;
	
	//Liste für eingelesene Dateien
	public static JList list;
	private JScrollPane scrollPane;
	public static DefaultListModel listenModell;
		
	//Panel für Tag-Anzeige
	private JPanel panel_Tag;
	
	//Variablen für Autovervollständigung in Interpretenfeld
	private JTextField textField_Interpret;
	private DefaultComboBoxModel modelInterpret;
	private JComboBox comboboxInterpret;
	private JTextField textField_Titel;
	//Variablen für Autovervollständigung in Albumfeld
	private JTextField textField_Album;
	private DefaultComboBoxModel modelAlbum;
	private JComboBox comboboxAlbum;
	private JTextField textField_Track;
	private JTextField textField_Year;
	private JTextField textField_Genre;
	
	private DefaultComboBoxModel modelGenre;
	private JComboBox comboboxGenre;
	
	
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
	//ArrayLists mit Artist und Album aus Datenbank
	private ArrayList<String> artists; 
	private ArrayList<String> albums;
	private ArrayList<String> genreList;
	private JButton btnGoTo;
	private JLabel lblFileExists;
		
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
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
		bibliothekUpdate = new JMenuItem("ITunes Bibliothek aktualisieren");
		bibliothekSetzen.addActionListener(this);
		bibliothekUpdate.addActionListener(this);
		menuBar.add(bibliothek);
		bibliothek.add(bibliothekSetzen);
		bibliothek.add(bibliothekUpdate);
		frmMusikschieber.getContentPane().add(menuBar);
		
		//***Initialisieren des Hauptframes***
		frmMusikschieber.getContentPane().setLocation(-12, -369);
		frmMusikschieber.setTitle("MusikSchieber 4000");
		frmMusikschieber.setBounds(100, 100, 651, 829);
		frmMusikschieber.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMusikschieber.getContentPane().setLayout(null);
		frmMusikschieber.setLocationRelativeTo(null);
		
		//***Transparentes Label um PlayList-Konfiguration geblockt aussehen zu lassen***
		label_BlockPlayList = new JLabel("");
		label_BlockPlayList.setBounds(10, 517, 609, 73);
		label_BlockPlayList.setBackground(new Color(255,255,255,50));
		label_BlockPlayList.setOpaque(true);
		frmMusikschieber.getContentPane().add(label_BlockPlayList);

		//***Transparentes Label um Tag-Konfiguration geblockt aussehen zu lassen***
		label_BlockTag = new JLabel("");
		label_BlockTag.setBounds(10, 360, 609, 150);
		label_BlockTag.setBackground(new Color(255,255,255,50));
		label_BlockTag.setOpaque(true);
		frmMusikschieber.getContentPane().add(label_BlockTag);
		
		//***Transparentes Label um Ordnerwahl geblockt aussehen zu lassen***
		label_BlockOrdnerWahl = new JLabel("");
		label_BlockOrdnerWahl.setBounds(10, 143, 609, 42);
		label_BlockOrdnerWahl.setBackground(new Color(255,255,255,50));
		label_BlockOrdnerWahl.setOpaque(true);
		frmMusikschieber.getContentPane().add(label_BlockOrdnerWahl);
				
		//Aufbau des Panels für ITunesBibliothek
		panel_ITunesBibliothek = new JPanel();
		panel_ITunesBibliothek.setLayout(null);
		panel_ITunesBibliothek.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_ITunesBibliothek.setBounds(10, 95, 609, 35);
		frmMusikschieber.getContentPane().add(panel_ITunesBibliothek);
		
		//Aufbau des Panels für Player
		player = new PlayerGUI();
		JPanel Panel_player = player.getPanel();
		frmMusikschieber.getContentPane().add(Panel_player);
		
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
		panel_OrdnerWahl.setBounds(10, 143, 609, 42);
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
		scrollPane.setBounds(10, 190, 609, 137);
		scrollPane.setBackground(SystemColor.control); 			//ScrollPane soll grau sein, solange Ordnerwahl nicht stimmt
		frmMusikschieber.getContentPane().add(scrollPane);
		
		//***Panel für Tag-Bearbeitung***
		panel_Tag = new JPanel();
		panel_Tag.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_Tag.setBounds(10, 360, 609, 150);
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
		lblTagInformationenZu.setBounds(12, 37, 173, 18);
		panel_Tag.add(lblTagInformationenZu);
		
		//TextField für Dateipfad gelesener Datei
		textField_FilePath = new JTextField();
		textField_FilePath.setBackground(SystemColor.control); // TextField Grau, so lange bis valide Ordnerwahl getroffen wurde
		textField_FilePath.setEditable(false);
		textField_FilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_FilePath.setBounds(180, 36, 417, 22);
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
		
		
		//Abruf: Album und Artists aus Datenbank     
	    Database database = new Database();
	    database.connect();
	    artists = database.getArtists();
	    albums = database.getAlbums();
	    database.disconnect();
	    //***Lesen der MusikGenres***
	  	File genreFile = new File("data/genrelist.txt");
	  	genreList = new ArrayList<String>();
	  		
	  	if (genreFile.isFile() && genreFile.canRead()){
	  		System.out.println("Genres gelesen");
	  			
	  	}else{
	  		System.err.println("Fehler beim Lesen der Genres");
	  	}
	  		
	  	try {
	 		BufferedReader in = new BufferedReader(new FileReader(genreFile));
	  		String zeile = null;
	  		while ((zeile = in.readLine()) != null) {
	  			genreList.add(zeile);
	  		}	
	  		Collections.sort(genreList, String.CASE_INSENSITIVE_ORDER);
	  		in.close();
	  	} catch (IOException e) {
	  		e.printStackTrace();
	  	}
		
		
		//TextField für Interpet
		textField_Interpret = new JTextField();
		textField_Interpret.setBounds(74, 69, 116, 22);
		textField_Interpret.setColumns(10);
		textField_Interpret.setEditable(false); // TextField Grau und nicht editierbar, so lange bis valide Ordnerwahl getroffen wurde
		textField_Interpret.setBackground(SystemColor.control);
		modelInterpret = new DefaultComboBoxModel();
		comboboxInterpret = new JComboBox(modelInterpret){
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
        };
        
        //Start-Einstellung
        setAdjusting(comboboxInterpret, false);
        for (String item : artists) {
            modelInterpret.addElement(item);
        }
		comboboxInterpret.setSelectedItem(null);
		comboboxInterpret.addActionListener(this);
		textField_Interpret.getDocument().addDocumentListener(this);
		textField_Interpret.setLayout(new BorderLayout());
		textField_Interpret.add(comboboxInterpret, BorderLayout.SOUTH);
		textField_Interpret.addKeyListener(new KeyAdapter()
			{
				public void keyPressed(KeyEvent e) {
	                setAdjusting(comboboxInterpret, true);
	                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
	                    if (comboboxInterpret.isPopupVisible()) {
	                        e.setKeyCode(KeyEvent.VK_ENTER);
	                    }
	                }
	                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
	                    e.setSource(comboboxInterpret);
	                    comboboxInterpret.dispatchEvent(e);
	                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	                        textField_Interpret.setText(comboboxInterpret.getSelectedItem().toString());
	                        comboboxInterpret.setPopupVisible(false);
	                    }
	                }
	                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	                	comboboxInterpret.setPopupVisible(false);
	                }
	                setAdjusting(comboboxInterpret, false);
	            }
				
		});
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
		
		modelAlbum = new DefaultComboBoxModel();
		comboboxAlbum = new JComboBox(modelAlbum){
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
        };
        
        //Start-Einstellung
        setAdjusting(comboboxInterpret, false);
        for (String item : albums) {
        	int stopzeichen = item.indexOf(":");
            modelAlbum.addElement(item.substring(stopzeichen+1, item.length()));
        }
		comboboxAlbum.setSelectedItem(null);
		comboboxAlbum.addActionListener(this);
		textField_Album.getDocument().addDocumentListener(this);
		textField_Album.setLayout(new BorderLayout());
		textField_Album.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e) {
                setAdjusting(comboboxAlbum, true);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (comboboxAlbum.isPopupVisible()) {
                        e.setKeyCode(KeyEvent.VK_ENTER);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    e.setSource(comboboxAlbum);
                    comboboxAlbum.dispatchEvent(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        textField_Album.setText(comboboxAlbum.getSelectedItem().toString());
                        comboboxAlbum.setPopupVisible(false);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                	comboboxAlbum.setPopupVisible(false);
                }
                setAdjusting(comboboxAlbum, false);
            }
	
		});
		
		textField_Album.addMouseListener(this);
		textField_Album.add(comboboxAlbum, BorderLayout.SOUTH);
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
		
		
		
		modelGenre = new DefaultComboBoxModel();
		comboboxGenre = new JComboBox(modelGenre){
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
        };
        
        //Start-Einstellung
        setAdjusting(comboboxGenre, false);
        for (String item : genreList) {
            modelGenre.addElement(item);
        }
        comboboxGenre.setSelectedItem(null);
        comboboxGenre.addActionListener(this);
		textField_Genre.getDocument().addDocumentListener(this);
		textField_Genre.setLayout(new BorderLayout());
		textField_Genre.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e) {
                setAdjusting(comboboxGenre, true);
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (comboboxGenre.isPopupVisible()) {
                        e.setKeyCode(KeyEvent.VK_ENTER);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    e.setSource(comboboxGenre);
                    comboboxGenre.dispatchEvent(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        textField_Genre.setText(comboboxGenre.getSelectedItem().toString());
                        comboboxGenre.setPopupVisible(false);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                	comboboxGenre.setPopupVisible(false);
                }
                setAdjusting(comboboxGenre, false);
            }
	
		});
		
		textField_Genre.addMouseListener(this);
		textField_Genre.add(comboboxGenre, BorderLayout.SOUTH);
		panel_Tag.add(textField_Genre);
		
		//Button für TagSpeichern
		btnTagSpeichern = new JButton("Tag speichern");
		btnTagSpeichern.addActionListener(this);
		btnTagSpeichern.setBounds(481, 120, 116, 25);
		btnTagSpeichern.setEnabled(false); //Solange deaktivieren bis valide Ordnerwahl getroffen wurde
		panel_Tag.add(btnTagSpeichern);
		
		//Button für Mp3 Entfernen
		btnMpEntfernen = new JButton("Mp3 entfernen");
		btnMpEntfernen.addActionListener(this);
		btnMpEntfernen.setBounds(481, 90, 116, 25);
		btnMpEntfernen.setEnabled(false); //Solange deaktivieren bis valide Ordnerwahl getroffen wurde
		panel_Tag.add(btnMpEntfernen);
		
		//Button für GoTo - Ordner bereits vorhandener Datei öffnen
		btnGoTo = new JButton("Go To");
		btnGoTo.setBounds(481, 60, 116, 25);
		btnGoTo.addActionListener(this);
		panel_Tag.add(btnGoTo);
		btnGoTo.setEnabled(false);
		btnGoTo.setVisible(false);
		
		//Label: Datei exisitiert bereits in Bibliothek
		lblFileExists = new JLabel("Datei befindet sich bereits in der Bibliothek");
		lblFileExists.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblFileExists.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFileExists.setBounds(225, 10, 372, 16);
		panel_Tag.add(lblFileExists);
		lblFileExists.setVisible(false);
		
		
		//***ProgressBar für Prozess Ordner lesen***
		//Wird in Thread gesteuert
		progressBar_OrdnerLesen = new JProgressBar();
		progressBar_OrdnerLesen.setBounds(10, 337, 609, 14);
		frmMusikschieber.getContentPane().add(progressBar_OrdnerLesen);
		
		//***Panel für Playlist anlegen**
		panel_Playlist = new JPanel();
		panel_Playlist.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_Playlist.setBounds(10, 517, 609, 73);
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
		btnStart.setBounds(10, 603, 97, 25);
		btnStart.setEnabled(false); //Solange deaktiviert, bis Tags korrekt sind
		btnStart.addActionListener(this);
		frmMusikschieber.getContentPane().add(btnStart);
		
		//ProgressBar, die den Fortschritt des Verschiebens anzeigt
		progressBarStart = new JProgressBar();
		progressBarStart.setBounds(119, 603, 500, 25);
		frmMusikschieber.getContentPane().add(progressBarStart);
		
		//TextArea, die Pfade der verschobene Dateien anzeigt
		textArea_VerschobeneDateien = new JTextArea();
		textArea_VerschobeneDateien.setEnabled(false);
		textArea_VerschobeneDateien.setBorder(new LineBorder(new Color(0, 0, 0), 0));
		textArea_VerschobeneDateien.setEditable(false);
		textArea_VerschobeneDateien.setBackground(SystemColor.control);
		//ScrollPane, in welches Text-Area hineingelegt wird
		scrollPane_VerschobeneDateien = new JScrollPane(textArea_VerschobeneDateien);
		scrollPane_VerschobeneDateien.setBounds(10, 669, 609, 101);
		frmMusikschieber.getContentPane().add(scrollPane_VerschobeneDateien);
		
		//Definieren der RadioButtons, Entscheidung: Sollen Originale behalten oder gelöscht werden?
		radioButtonOriginaleBehalten = new JRadioButton("Originale behalten");
		radioButtonOriginaleBehalten.setSelected(true);
		radioButtonOriginaleBehalten.setBounds(10, 635, 142, 25);
		radioButtonOriginaleBehalten.addItemListener(this);
		radioButtonOriginaleBehalten.setEnabled(false);
		frmMusikschieber.getContentPane().add(radioButtonOriginaleBehalten);
		
		radioButtonOriginaleEntfernen = new JRadioButton("Originale entfernen");
		radioButtonOriginaleEntfernen.setSelected(false);
		radioButtonOriginaleEntfernen.setEnabled(false);
		radioButtonOriginaleEntfernen.addItemListener(this);
		radioButtonOriginaleEntfernen.setBounds(156, 635, 142, 25);
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
						
						
						//Datenbank wird aktualisiert
						Runnable run = new Runnable(){
							@Override
							public void run() {
								Database database = new Database();
								database.connect();
								database.clearDataBase();
								database.upDateLibrary(false);
								database.disconnect();
							}
						};
						
						Thread thread = new Thread(run);
						thread.start();
						
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
		
		if(object.getSource() == bibliothekUpdate){
			//Datenbank wird aktualisiert
			Runnable run = new Runnable(){
				@Override
				public void run() {
					Database database = new Database();
					database.connect();
					database.upDateLibrary(true);
					database.disconnect();
				}
			};
			
			Thread thread = new Thread(run);
			thread.start();
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
			  	//Panel für Tag-Operationen und Player wird deaktiviert
			  	player.refreshPlayer(false);
			  	refreshTag(false);
				//Fall: Chooser geschlossen mit Auswahl
				if(!ordnerPfad.equals("Kein Pfad angegeben")){
					@SuppressWarnings("unused")
					//Dialog der anzeigt, dass keine MP3-Dateien sich im gewählte Ordner befinden 
					NoFilesFound noFilesFound = new NoFilesFound(ordnerPfad);
				}
		  //Es wurden MP3-Dateien im gewählten Ordner gefunden
		  }else{
			  	//Tag-Feld und Player werden freigeschalten
			  	player.refreshPlayer(true);	
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
    			   System.err.println(geleseneMP3.get(selectedIndex).getArtist());
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
       
       if(object.getSource() == btnGoTo){
    	   String path = geleseneMP3.get(selectedIndex).getDuplicateFilepath();
    	   
    	   String[] pathArray = path.split(Pattern.quote("\\"));
    	   
    	   path = "";
    	   
    	   for(int i = 0; i < pathArray.length - 1; i++){
    		   path += pathArray[i] + "\\";
    	   }
    	 
    	   try {	
				Runtime.getRuntime().exec("explorer.exe " + path);
			} catch (IOException e1) {
				e1.printStackTrace();
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
    			   //Feld für Name der Playlist richtig ausgefüllt, Pfad anlegen
    			   pathToPlaylist = pathToDir + "\\" + textField_NameDerPlaylist.getText() + ".txt";
    			   textField_gespeichertUnter.setText(pathToPlaylist);
					
    			   //Fall: TextDatei exisitiert bereits
    			   if((new File(pathToPlaylist)).exists() == true){
    				   	//Check ob es sich um eine ITunesBibliothek handelt
    				   	String zeile = new String();
    				   	String header = new String();
						
    				   	//Auslesen der ersten Zeile in Playlist
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
						
    				   	//Header und erste Zeile in PlayList vergleichen
    				   	//Fall: es handelt sich um PlayList
    				   	if(zeile.contains(header)){
    				   		//Soll die Playlist ergänzt oder überschrieben werden: Ja oder nein
    				   		PlayListOverrideAnswer answer = new PlayListOverrideAnswer(pathToPlaylist,true);
							
    				   		//Fall Ergänzen
    				   		if(answer.getAnswer() == 2){
    						   playlistErgaenzen = true;
    						   playlistAngelegt = true;
    						   refreshStart(true);
    						//Fall: Überschreiben   
    				   		}else if(answer.getAnswer() == 1){
    						   playlistAngelegt = true;
    						   playlistErgaenzen = false;
    						   refreshStart(true);
    						//Fall: Abbruch   
    				   		}else{
    						   textField_gespeichertUnter.setText("");
    						   textField_NameDerPlaylist.setText("");
    						   pathToPlaylist = "missing";
    						   playlistAngelegt = false;
    						   playlistErgaenzen = false;
    						   refreshStart(false);
    				   		}
    				   //Fall: Textdatei exisitiert, aber es handelt sich nicht um ItunesPlaylist	
    				   }else{
    					   	PlayListOverrideAnswer answer = new PlayListOverrideAnswer(pathToPlaylist,false);
							
    					   	//Fall: Textdatei soll überschrieben werden
    					   	if(answer.getAnswer() == 1){
    						   playlistErgaenzen = false;
    						   playlistAngelegt = true;
    						   refreshStart(true);
    						//Fall: Abbruch   
    					   	}else{
    						   playlistErgaenzen = false;
    						   playlistAngelegt = false;
    						   refreshStart(false);
								
    						   textField_gespeichertUnter.setText("");
    						   textField_NameDerPlaylist.setText("");
								
    						   pathToPlaylist = "missing";
    					   }
    				   }
    				//Fall: Textdatei existiert noch nicht, Textdatei soll angelegt werden   	
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
       
       if(object.getSource() == comboboxInterpret){
    	   if (!isAdjusting(comboboxInterpret)) {
               if (comboboxInterpret.getSelectedItem() != null) {
                   textField_Interpret.setText(comboboxInterpret.getSelectedItem().toString());
               }
           }
       }
       
       if(object.getSource() == comboboxAlbum){
    	   if (!isAdjusting(comboboxAlbum)) {
               if (comboboxAlbum.getSelectedItem() != null) {
                   textField_Album.setText(comboboxAlbum.getSelectedItem().toString());
               }
           }
       }
       
       if(object.getSource() == comboboxGenre){
    	   if (!isAdjusting(comboboxGenre)) {
               if (comboboxGenre.getSelectedItem() != null) {
                   textField_Genre.setText(comboboxGenre.getSelectedItem().toString());
               }
           }
       }
       
       
       if(object.getSource() == btnStart){
           //Boolean: Liegen noch "gelbe" Werte in Liste vor ?
    	   boolean missingNonCriticalValues = checkNonCriticalValues();
           //Fall: es liegen noch "gelbe" Werte vor
    	   if(missingNonCriticalValues){
        	   //Dialog: Soll trotzdem fortgefahren werden?
        	   StartMissingValues answer = new StartMissingValues();
        	   
        	   //Fall: Verschieben mit gelben Werten
        	   if(answer.getAnswer()){
        		   //Blocken des Tag, Player und PlaylistsFeldes
        		   player.refreshPlayer(false);
        		   refreshTag(false);
        		   refreshPlaylist(false);
        		   //Starten des Threads der Dateien verschiebt
        		   DateienVerschiebenThread thread = new DateienVerschiebenThread(playlistErgaenzen,originaleBehalten,library,pathToPlaylist);
        		   Thread th = new Thread(thread); 
        		   th.start(); 
        			
        		   //Thread und Hauptthread zusammenführen
        		   try{
        			   th.join();
        		   }catch(InterruptedException e1){
        			   e1.printStackTrace();
        		   }
        	   }
        	//Fall: Es liegen keine gelben Werte mehr vor   
           	}else{
           		//Blocken des Tag, Players und  PlaylistsFeldes
           		player.refreshPlayer(false);
           		refreshTag(false);
           		refreshPlaylist(false);
           		//Starten des Threads der Dateien verschiebt
           		DateienVerschiebenThread thread = new DateienVerschiebenThread(playlistErgaenzen,originaleBehalten,library,pathToPlaylist);
           		Thread th = new Thread(thread); 
           		th.start(); 
    			
           		//Thread und Hauptthread zusammenführen
           		try{
    			   th.join();
           		}catch(InterruptedException e1){
    			   e1.printStackTrace();
           		}
           }
       }  
	}
	
	/**
	 * ValueChangedListener für Liste in der MP3 angezeigt werden
	 * @param listSelectionEvent: SelectionEvent 
	 */
	public void valueChanged(ListSelectionEvent listSelectionEvent){
		boolean adjust = listSelectionEvent.getValueIsAdjusting();
		
		setAdjusting(comboboxInterpret,true);
		setAdjusting(comboboxAlbum,true);
		setAdjusting(comboboxGenre,true);
		
        if (!adjust) {
        	//Einzelner Eintrag ist markiert
        	selectedIndex = list.getSelectedIndex();
        	//Mehrere Einträge sind markiert
        	selectedindicies = list.getSelectedIndices();
          
        	//Fall: es wurde nur ein Eintrag markiert
        	if(selectedIndex < geleseneMP3.size() && selectedIndex >= 0 && selectedindicies.length == 1){
        		//Player lädt Lied
        		player.setTrack(geleseneMP3.get(selectedIndex));
        		
        		//Tag wird in Array gespeichert
        		tag = geleseneMP3.get(selectedIndex).tagToArray();
        		//Tag wird in Textfields übertragen
        		textFieldstoArray();
        	  
        		//Hier sind Titel und Tracknummer bearbeitbar
        		textFieldsTag.get(1).setEditable(true);
        		textFieldsTag.get(3).setEditable(true);
        	  
        		//Tag-Felder werden mit entsprechenden Faren gefüllt
        		textFieldsFill(tag);
        		//Pfad zur Mp3 wird angezeigt
        		textField_FilePath.setText(geleseneMP3.get(selectedIndex).getFilePath());
        		
        		//Datei liegt bereits vor
        		if(geleseneMP3.get(selectedIndex).duplicate()){
        			lblFileExists.setVisible(true);
        			btnGoTo.setVisible(true);
        			btnGoTo.setEnabled(true);
        		}else{
        			lblFileExists.setVisible(false);
        			btnGoTo.setVisible(false);
        		}
        		
        		//ComboBox soll nicht automatisch aufspringen
        		comboboxInterpret.setPopupVisible(false);
        		comboboxAlbum.setPopupVisible(false);
        		comboboxGenre.setPopupVisible(false);

        		setAdjusting(comboboxInterpret,false);
        		setAdjusting(comboboxAlbum,false);
        		setAdjusting(comboboxGenre,false);	
        	//Fall: es wurden mehrere Einträge markiert  
        	} else if(selectedIndex < geleseneMP3.size() && selectedIndex >= 0 && selectedindicies.length > 1){
        		setAdjusting(comboboxInterpret,true);
        		setAdjusting(comboboxAlbum,true);
        		setAdjusting(comboboxGenre,true);
        		
        		//Feld zur Speicherung der markierten Tags
        		stringFeldTags = new String[selectedindicies.length][tag.length + 1];
        		//Die Tags-TextFelder werden zurückgesetzt
        		tagFieldClear();
        	  
        		//Tags der markierten Einträge werden in Feld übergeben
        		for(int i = 0; i < selectedindicies.length; i++){
        			stringFeldTags[i][0] = "" + i +"";
        			tag = geleseneMP3.get(selectedindicies[i]).tagToArray();
        		  
        			for(int j = 1; j < tag.length + 1; j++){
        				stringFeldTags[i][j] = tag[j-1];
        			}
        		}
        	  
        		//TextFields für Tags werden gesetzt
        		textFieldstoArray();
        		multipleTagsToTextFields(stringFeldTags);
        		
        		//ComboBox soll nicht automatisch aufspringen
        		comboboxInterpret.setPopupVisible(false);
        		comboboxAlbum.setPopupVisible(false);
        		comboboxGenre.setPopupVisible(false);
        		
        		setAdjusting(comboboxInterpret,false);
        		setAdjusting(comboboxAlbum,false);
        		setAdjusting(comboboxGenre,false);
        	//Fall: Kein Eintrag gewählt  
        	}else{
        		list.setSelectedIndex(0);
        	}
        	  
        }
    }
	
	/**
	 * ItemListener für verwendete Radiobuttons
	 * @param ItemEvent: Radiobutton wird geändert
	 */
	public void itemStateChanged(ItemEvent e){
		//RadioButton: Originale Entfernen
		if(e.getSource() == radioButtonOriginaleEntfernen){
			if(radioButtonOriginaleEntfernen.isSelected()){
				originaleBehalten = false;
			}
		//RadioButton: Originale behalten
		}else if(e.getSource() == radioButtonOriginaleBehalten){
			if(radioButtonOriginaleBehalten.isSelected()){
				originaleBehalten = true;
			}
		}
	}
	
	
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		if(arg0.getDocument() == textField_Interpret.getDocument()){
			updateInterpret();
		}
		
		if(arg0.getDocument() == textField_Album.getDocument()){
			updateAlbum();
		}
		
		if(arg0.getDocument() == textField_Genre.getDocument()){
			updateGenre();
		}
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		if(arg0.getDocument() == textField_Interpret.getDocument()){
			updateInterpret();
		}
		
		if(arg0.getDocument() == textField_Album.getDocument()){
			updateAlbum();
		}
		
		if(arg0.getDocument() == textField_Genre.getDocument()){
			updateGenre();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		if(arg0.getDocument() == textField_Interpret.getDocument()){
			updateInterpret();
		}
		
		if(arg0.getDocument() == textField_Album.getDocument()){
			updateAlbum();
		}
		
		if(arg0.getDocument() == textField_Genre.getDocument()){
			updateGenre();
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getSource() == textField_Album){
			if(textField_Album.getText().length() == 0){
				
				setAdjusting(comboboxAlbum, true);
		        modelAlbum.removeAllElements();
		        String artist = textField_Interpret.getText();
		        
		        if (!artist.isEmpty()) {
		            for (String item : albums) {
		            	if(item.contains(artist)){
		            		String albumtitle = item.replace(artist + ":", "");
		            		modelAlbum.addElement(albumtitle);
		            	}
		            }
		        }
		        
		        if(modelAlbum.getSize() < 5){
		        	comboboxAlbum.setMaximumRowCount(modelAlbum.getSize());
		        }else{
		        	comboboxAlbum.setMaximumRowCount(5);
		        }
		        
		        comboboxAlbum.setPopupVisible(modelAlbum.getSize() > 0);
		        setAdjusting(comboboxAlbum, false);
			}
		}
		
		if(arg0.getSource() == textField_Genre){
			if(textField_Genre.getText().length() == 0 || textField_Genre.getText().contains("unknown")){
				setAdjusting(comboboxGenre, true);
		        modelGenre.removeAllElements();
		        
		        for (String item : genreList) {
		            modelGenre.addElement(item);
		        }
		    }
		        
		    if(modelGenre.getSize() < 10){
		        comboboxGenre.setMaximumRowCount(modelGenre.getSize());
		    }else{
		        comboboxGenre.setMaximumRowCount(10);
		    }
		        
		    comboboxGenre.setPopupVisible(modelGenre.getSize() > 0);
		    setAdjusting(comboboxGenre, false);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void updateGenre(){
		setAdjusting(comboboxGenre, true);
        modelGenre.removeAllElements();
        String input = textField_Genre.getText();
        
        if (!input.isEmpty()) {
            for (String item : genreList) {
                if (item.toLowerCase().startsWith(input.toLowerCase())) {
                    modelGenre.addElement(item);
                }
            }
        }
        if(modelGenre.getSize() < 10){
        	comboboxGenre.setMaximumRowCount(modelGenre.getSize());
        }else{
        	comboboxGenre.setMaximumRowCount(10);
        }
        
        comboboxGenre.setPopupVisible(modelGenre.getSize() > 0);
        
        setAdjusting(comboboxGenre, false);
	}
	
	private void updateAlbum() {
        setAdjusting(comboboxAlbum, true);
        modelAlbum.removeAllElements();
        String artist = textField_Interpret.getText();
        String album = textField_Album.getText();
        
        if (!artist.isEmpty() && !album.isEmpty()) {
        	
            for (String item : albums) {
            	if(item.contains(artist)){
            		String albumtitle = item.replace(artist + ":", "");
            		System.out.println(albumtitle);
            		if(albumtitle.toLowerCase().startsWith(album.toLowerCase())){
            			modelAlbum.addElement(albumtitle);
            		}
            	}
            }
        }
        
        if(modelAlbum.getSize() < 5){
        	comboboxAlbum.setMaximumRowCount(modelAlbum.getSize());
        }else{
        	comboboxAlbum.setMaximumRowCount(5);
        }
        
        comboboxAlbum.setPopupVisible(modelAlbum.getSize() > 0);
        setAdjusting(comboboxAlbum, false);
    }
	
	
	private void updateInterpret() {
        setAdjusting(comboboxInterpret, true);
        modelInterpret.removeAllElements();
        String input = textField_Interpret.getText();
        
        if (!input.isEmpty()) {
            for (String item : artists) {
                if (item.toLowerCase().startsWith(input.toLowerCase())) {
                    modelInterpret.addElement(item);
                }
            }
        }

        if(modelInterpret.getSize() < 5){
        	comboboxInterpret.setMaximumRowCount(modelInterpret.getSize());
        }else{
        	comboboxInterpret.setMaximumRowCount(5);
        }
        
        comboboxInterpret.setPopupVisible(modelInterpret.getSize() > 0);
        setAdjusting(comboboxInterpret, false);
    }
	
	
	
	/**
	 * Methode um Pfad zur Musikbibliothek aus Speicher zu lesen
	 * @param String: Pfad zur Musikbibliothek
	 */
	public static String readLibrary(){
		
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		String path;
		
		try {
			//Neuer FileInputStream auf Speicher
			fis = new FileInputStream("config/library.ser");
			//Fileinputstream wird ObjectInputStream übergeben
			ois = new ObjectInputStream(fis);
			//Gespeichertes Objekt wird gelesen
			Object obj = ois.readObject();
			
			//Handelt es sich um ein Objekt vom Typ ITunesLibrary?
			if (obj instanceof ITunesLibrary) {
				//Pfad wird ausgelesen und zurückgegeben
				ITunesLibrary so = (ITunesLibrary) obj;
				path= so.getLibrary();
				return path;
			//Fall: es handelt sich um ein anderes Objekt
			}else{
				return "";
			}
		}
		//Fehlerbehandlung...
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
	
	/**
	 * Die Anzeige zum gewählten Ordner wird entweder freigeschalten oder deaktiviert
	 * @param boolean: true - Freischalten; false - blockieren
	 */
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
	
	/**
	 * Die Anzeige zu den Taginformationen wird entweder freigeschalten oder deaktiviert
	 * @param boolean: true - Freischalten; false - blockieren
	 */
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
	
	/**
	 * Die Anzeige zu den Playlistinformationen wird entweder freigeschalten oder deaktiviert
	 * @param boolean: true - Freischalten; false - blockieren
	 */
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
	
	/**
	 * Die Anzeige zum Startbereich wird entweder freigeschalten oder deaktiviert
	 * @param boolean: true - Freischalten; false - blockieren
	 */
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
	
	
	/**
	 * Überprüfen, ob noch Mp3-Dateien mit unvollständigen Tags vorliegen (kritische Werte: Titel, Album,Artist)
	 * @return boolean: true - Es liegen noch unvollständige Tags vor; false - Es liegen keine unvollständigen Tags vor
	 */
	public boolean checkCriticalValues(){
		boolean missing = false;
		
		for(int i = 0; i < geleseneMP3.size();i++){
			if(geleseneMP3.get(i).missingCriticalValues() == true){
				missing = true;
			}
		}
		return missing;
	}
	
	/**
	 * Überprüfen, ob noch Mp3-Dateien mit unvollständigen Tags vorliegen (unkritische Werte: Genre, Titelnummer, Jahr)
	 * @return boolean: true - Es liegen noch unvollständige Tags vor; false - Es liegen keine unvollständigen Tags vor
	 */
	
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
		boolean green = false;
		
		if(geleseneMP3.get(index).duplicate()){
			green = true;
		}
		
		if(geleseneMP3.get(index).getArtist() == "unknown" || geleseneMP3.get(index).getTitle() == "unknown" || geleseneMP3.get(index).getAlbumTitle() == "unknown"){
			red = true;
		} 
		
		if((geleseneMP3.get(index).getTrackNumber().contains("0") && geleseneMP3.get(index).getTrackNumber().length() == 1) || geleseneMP3.get(index).getGenre().contains("unknown") || geleseneMP3.get(index).getGenre().isEmpty() || geleseneMP3.get(index).getTrackNumber().isEmpty()){
			yellow = true;
		}
		
		if(green){
			return Color.GREEN;
		}else{
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
		
		
	}
	
	/**
	 * Text-Fields werden Array zu einfacheren Bearbeitung übergeben
	 */
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
	
	
	/**
	 * TextFields werden mit Tags und Farben gefüllt
	 * @param String[]: Array mit Tags des ausgewählten Eintrages
	 */
	public void textFieldsFill(String[] tag){
		boolean missingEntryCritical = false;
		boolean missingEntryNonCritical = false;
		
		for(int i = 0; i <= 2 ; i++){
			textFieldsTag.get(i).setText(tag[i]);
			textFieldsTag.get(i).setBackground(Color.WHITE);
			
			if(tag[i] == null || tag[i] == "" || (tag[i].contains("unknown") && tag[i].length() == 7)){
				textFieldsTag.get(i).setBackground(LIGHTRED);
				missingEntryCritical = true;
			}
		}
		
		for(int i = 3; i <= 5 ; i++){
			textFieldsTag.get(i).setText(tag[i]);
			textFieldsTag.get(i).setBackground(Color.WHITE);
			
			if(tag[i] == null || tag[i].isEmpty() || tag[i].equals("0") || tag[i].contains("unknown")){
				textFieldsTag.get(i).setBackground(LIGHTYELLOW);
				missingEntryNonCritical=true;
			}
		}
		
		if(missingEntryCritical == true){
			textFieldsTag.get(6).setBackground(LIGHTRED);
		}else if(missingEntryNonCritical == true){
			textFieldsTag.get(6).setBackground(LIGHTYELLOW);
		}else{
			textFieldsTag.get(6).setBackground(Color.WHITE);
		}
		
	}
	
	/**
	 * Methode, die TextFields ausfüllt, wenn mehrere markiert sind
	 * @param String[][]: Array mit Tags der ausgewählten Einträge
	 */
	public void multipleTagsToTextFields(String[][] tagField){
		boolean allSame = true;
		
		for(int zaehler = 1; zaehler < tag.length + 1; zaehler++){
			String vergleich = tagField[0][zaehler];
			
			for(int i = 1; i < selectedindicies.length; i++){
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
	
	/**
	 * Methode die überprüft, ob der übergebene String einen numerischen Eintrag darstellt
	 * @param String: String der auf Nummern überprüft werden soll
	 * @return Boolean: Ja- es handelt sich um Nummer/Nein...
	 */
	@SuppressWarnings("unused")
	public boolean checkIfNumber(String s){
		try {
			if(s == "0" || s.isEmpty()){
			    return true;
		    }else{
		    	int zahl = Integer.parseInt(s);
		    	return true;
		    }
			 
		 }
		 catch(NumberFormatException e) {
		 	return false;
		 }
	}
	
	/**
	 * Methode zum Zurücksetzen aller TagFields
	 * Fall: Mehrere Dateien sind gewählt!
	 */
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
	
	/**
	 * Methode die Status der ComboBox auf Adjusting überprüft
	 * @param cpInput: Betrachtete Combobox
	 */
	public static boolean isAdjusting(JComboBox cbInput) {
        if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
            return (Boolean) cbInput.getClientProperty("is_adjusting");
        }
        return false;
    }
	
	/**
	 * Methode die Status der ComboBox auf Adjusting setzt
	 * @param cpInput: Betrachtete Combobox
	 * @param adjusting: Status
	 */
    public static void setAdjusting(JComboBox cbInput, boolean adjusting) {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }
}
