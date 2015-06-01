import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class MultipleDeleteAnswer extends JDialog {
	private JLabel lblSollenDieDateien;
	private JScrollPane scrollPane;
	private JList list;
	private JButton btnNewButton;
	private JButton btnOk;
	private static boolean abbruch = true;

	/**
	 * Create the dialog.
	 */
	public MultipleDeleteAnswer(ArrayList<MP3Datei> dateien) {
		setBounds(100, 100, 450, 287);
		getContentPane().setLayout(null);
		setModal(true);
		lblSollenDieDateien = new JLabel("Sollen die Dateien wirklich aus der Liste entfernt werden?");
		lblSollenDieDateien.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSollenDieDateien.setBounds(23, 13, 373, 16);
		getContentPane().add(lblSollenDieDateien);
		
		
		DefaultListModel listenModell = new DefaultListModel(); 
		list = new JList(listenModell);
		
		scrollPane = new JScrollPane(list);
		scrollPane.setBounds(47, 42, 336, 137);
		getContentPane().add(scrollPane);
		
		for(int i = 0; i < dateien.size(); i++){
			listenModell.addElement(dateien.get(i).getFileName());
		}
		
		btnNewButton = new JButton("Abbrechen");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnNewButton.setBounds(119, 198, 97, 25);
		getContentPane().add(btnNewButton);
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abbruch = false;
				dispose();
			}
		});
		btnOk.setBounds(228, 198, 97, 25);
		getContentPane().add(btnOk);
		
		setLocationRelativeTo(getParent());
		setVisible(true);
	}
	
	public boolean getAbbruch(){
		return abbruch;
	}
}
