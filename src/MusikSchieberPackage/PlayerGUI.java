package MusikSchieberPackage;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Shape;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JTextField;
import javax.swing.event.MouseInputListener;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class PlayerGUI extends JFrame implements MouseInputListener,MouseMotionListener,ActionListener{

	public static JSlider slider;
	private JPanel panel;
	public static PlayMP3 play;
	private static int position;
	public static JButton btnPlayPause;
	private JButton btnStop;
	public static final ImageIcon iconPlay = new ImageIcon("icons/Play.png");
	public static final ImageIcon iconPlayDisabled = new ImageIcon("icons/Play_Disabled.png");
	public static final ImageIcon iconPause = new ImageIcon("icons/Pause.png");
	public static final ImageIcon iconPauseDisabled = new ImageIcon("icons/Pause_Disabled.png");
	public static final ImageIcon iconStop = new ImageIcon("icons/Stop.png");
	public static final ImageIcon iconStopDisabled = new ImageIcon("icons/Stop_Disabled.png");
	private JLabel lblTrackName;
	private JLabel lblBackground;
	private JSlider slider_Volume;
	private JLabel lblVolume;
	public static JLabel lblActualTime;
	public static JLabel lblTimeLeft;
	public static double volume = 0.5;
	private final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
	private Time dragTimePlayed = new Time(0);
	private Time dragTimeLeft = new Time(0);	
	
	/**
	 * Create the frame.
	 */
	public PlayerGUI(){
		init();
	}
	
	public void init(){
		panel = new JPanel();
		panel.setBounds(0, 20, 655, 70);
		
		slider = new JSlider();
		slider.setBackground(new Color(245, 245, 245));
		slider.setBounds(177, 38, 300, 23);
		slider.addMouseListener(this);
		slider.addMouseMotionListener(this);
		panel.setLayout(null);
		panel.add(slider);
		
		btnPlayPause = new JButton("");
		btnPlayPause.addActionListener(this);
		btnPlayPause.setBounds(5, 5, 64,64);
		btnPlayPause.setBorder(null);
		btnPlayPause.setBorderPainted(false);
		btnPlayPause.setContentAreaFilled(false);
		btnPlayPause.setFocusPainted(false);
		btnPlayPause.setOpaque(false);
		btnPlayPause.setIcon(iconPlay);
		btnPlayPause.setIcon(iconPlayDisabled);
		btnPlayPause.setRolloverEnabled(true);
		btnPlayPause.setRolloverIcon(iconPlay);
		panel.add(btnPlayPause);
		
		btnStop = new JButton("");
		btnStop.addActionListener(this);
		btnStop.setBounds(60, 5, 64,64);
		btnStop.setBorder(null);
		btnStop.setBorderPainted(false);
		btnStop.setContentAreaFilled(false);
		btnStop.setFocusPainted(false);
		btnStop.setOpaque(false);
		btnStop.setIcon(iconStopDisabled);
		btnStop.setRolloverEnabled(true);
		btnStop.setRolloverIcon(iconStop);
		
		panel.add(btnStop);
		
		lblTrackName = new JLabel("");
		lblTrackName.setHorizontalAlignment(SwingConstants.CENTER);
		lblTrackName.setBounds(150, 12, 350, 16);
		panel.add(lblTrackName);
		
		lblActualTime = new JLabel("");
		lblActualTime.setHorizontalTextPosition(SwingConstants.CENTER);
		lblActualTime.setHorizontalAlignment(SwingConstants.CENTER);
		lblActualTime.setBounds(135, 38, 37, 23);
		panel.add(lblActualTime);
		
		lblTimeLeft = new JLabel("");
		lblTimeLeft.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTimeLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeLeft.setBounds(480, 38, 40, 23);
		panel.add(lblTimeLeft);
		
		lblBackground = new JLabel("");
		lblBackground.setOpaque(true);
		lblBackground.setBackground(new Color(245, 245, 245));
		lblBackground.setBounds(127, 0, 404, 68);
		panel.add(lblBackground);
		
		slider_Volume = new JSlider();
		slider_Volume.setBounds(543, 38, 87, 23);
		slider_Volume.setMaximum(100);
		slider_Volume.setMinimum(0);
		slider_Volume.addMouseListener(this);
		slider_Volume.addMouseMotionListener(this);
		panel.add(slider_Volume);
		
		lblVolume = new JLabel("Volume");
		lblVolume.setHorizontalAlignment(SwingConstants.CENTER);
		lblVolume.setBounds(557, 12, 56, 16);
		panel.add(lblVolume);
		play = new PlayMP3();
		refreshPlayer(false);
	}
	

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(arg0.getSource() == slider && (arg0.getX() > 0) && (arg0.getX() < 300)){
			System.out.println("Dragged");
			this.position = slider.getValue();
			play.disableProgress(true);
			double x = (double) arg0.getX();
			double portion =(double) (x/300);
			
			long timePlayed = (long)(portion*play.getMP3LengthInMilliSeconds());
			long timeLeft =play.getMP3LengthInMilliSeconds() - timePlayed;
			
			dragTimePlayed.setTime(timePlayed);
			dragTimeLeft.setTime(timeLeft);
			
			lblActualTime.setText(sdf.format(dragTimePlayed));
			lblTimeLeft.setText("-" + sdf.format(dragTimeLeft));
			
		}else if(arg0.getSource() == slider_Volume){
			System.out.println("Dragged");
			volume = (double) slider_Volume.getValue()/100;
			System.out.println(volume);
			play.setGain(volume);
		}
		
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
	public void mousePressed(MouseEvent e) {
		if(e.getSource() == slider){
			System.out.println("Pressed");
			double x = (double) e.getX();
			double portion =(double) (x/300);
			int position = (int) (play.getDataLength()*portion);
			PlayerGUI.position = position;
			play.disableProgress(true);
			
			
		}else if(e.getSource() == slider_Volume){
			System.out.println("Pressed");
			double x = (double) e.getX();
			double portion =(double) (x/87);
			volume = portion;
			slider_Volume.setValue((int)x);
			play.setGain(volume);
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(arg0.getSource() == slider){
			System.out.println("Released!");
			slider.setValue(PlayerGUI.position);
			play.seek(PlayerGUI.position);
			
			if(play.getStatus() != PlayMP3.PLAYING){
				play.resume();
			}
			play.setGain(volume);
			play.disableProgress(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnPlayPause){
			System.out.println(play.getStatus());
			if(play.getStatus() == PlayMP3.PLAYING || play.getStatus() == PlayMP3.RESUMED){
				btnPlayPause.setIcon(iconPlayDisabled);
				btnPlayPause.setRolloverIcon(iconPlay);
				play.setGain(volume);
				play.pause();
			}else if(play.getStatus() == PlayMP3.PAUSED){
				btnPlayPause.setIcon(iconPauseDisabled);
				btnPlayPause.setRolloverIcon(iconPause);
				play.setGain(volume);
				play.play();
			}else if(play.getStatus() == PlayMP3.OPENED || play.getStatus() == PlayMP3.STOPPED){
				btnPlayPause.setIcon(iconPauseDisabled);
				btnPlayPause.setRolloverIcon(iconPause);
				play.play();
				play.setGain(volume);
			}
		}else{
			this.stop();
		}
	}
	
	public JPanel getPanel(){
		return panel;
	}
	
	public void refreshPlayer(boolean setzen){
		if(setzen){
			btnPlayPause.setEnabled(true);
			btnStop.setEnabled(true);
			slider.setEnabled(true);
			slider_Volume.setEnabled(true);
		}else{
			btnPlayPause.setEnabled(false);
			btnStop.setEnabled(false);
			lblTrackName.setText("");
			lblActualTime.setText("");
			lblTimeLeft.setText("");
			slider.setValue(0);
			slider.setEnabled(false);
			slider_Volume.setEnabled(false);
		}
	}
	
	public void stop(){
		btnPlayPause.setIcon(iconPlayDisabled);
		btnPlayPause.setRolloverIcon(iconPlay);
		play.stop();
		lblActualTime.setText("");
		lblTimeLeft.setText("");
	}
	
	public void setTrack(MP3Datei mp3Datei){
		this.stop();
		String path = mp3Datei.getFilePath();
		play.open(path);
		lblTrackName.setText(mp3Datei.getFileName());
		
	}
}
