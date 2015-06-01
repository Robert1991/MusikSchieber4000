package MusikSchieberPackage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;

public class UpDatingLibrary extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JProgressBar progressBar;
	private JLabel lblMusikbibliothekWirdGeladen;
	private JTextField textField;
	
	/**
	 * Create the dialog.
	 */
	public UpDatingLibrary() {
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 142);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 432, 97);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setBounds(12, 77, 408, 14);
		contentPanel.add(progressBar);
		
		lblMusikbibliothekWirdGeladen = new JLabel("Musikbibliothek wird geladen");
		lblMusikbibliothekWirdGeladen.setHorizontalAlignment(SwingConstants.CENTER);
		lblMusikbibliothekWirdGeladen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblMusikbibliothekWirdGeladen.setBounds(12, 13, 408, 22);
		contentPanel.add(lblMusikbibliothekWirdGeladen);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(12, 42, 408, 22);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		setModal(true);
		setLocationRelativeTo(getParent());
		setVisible(true);
	}
	
	public void setTextField(String filename){
		this.textField.setText(filename);
	}
}
