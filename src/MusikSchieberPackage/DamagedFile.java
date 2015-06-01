package MusikSchieberPackage;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;

import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;


public class DamagedFile extends JDialog {
	private JLabel lblBeschdigteDateiGefunden;
	private JTextField textField;
	private JButton btnNewButton;
	private JButton btnDelete;
	private JButton btnGoTo;


	/**
	 * Create the dialog.
	 */
	public DamagedFile(MP3Datei mp3Datei) {
		setBounds(100, 100, 450, 196);
		getContentPane().setLayout(null);
		
		lblBeschdigteDateiGefunden = new JLabel("Besch\u00E4digte Datei gefunden!");
		lblBeschdigteDateiGefunden.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBeschdigteDateiGefunden.setBounds(106, 0, 206, 50);
		getContentPane().add(lblBeschdigteDateiGefunden);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBackground(SystemColor.menu);
		textField.setBounds(12, 53, 408, 27);
		getContentPane().add(textField);
		textField.setColumns(10);
		textField.setText(mp3Datei.getFileName());
		
		btnNewButton = new JButton("Ok");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnNewButton.setBounds(55, 105, 97, 25);
		getContentPane().add(btnNewButton);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mp3Datei.delete();
				dispose();
			}
		});
		btnDelete.setBounds(164, 105, 97, 25);
		getContentPane().add(btnDelete);
		
		btnGoTo = new JButton("Go To");
		btnGoTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = mp3Datei.getFilePath();
				path = path.replace(mp3Datei.getFileName(), "");
				System.out.println(path);
				try {
					
					Runtime.getRuntime().exec("explorer.exe " + path);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}
		});
		btnGoTo.setBounds(273, 105, 97, 25);
		getContentPane().add(btnGoTo);
		
		setLocationRelativeTo(getParent());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
