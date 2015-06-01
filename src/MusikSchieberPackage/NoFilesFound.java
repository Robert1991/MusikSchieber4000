package MusikSchieberPackage;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class NoFilesFound extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblEsKonntenKeine;
	private JTextField textField;
	private JButton btnOk;

	/**
	 * Create the dialog.
	 */
	public NoFilesFound(String ordnerPfad) {
		setBounds(100, 100, 450, 166);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 432, 136);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		lblEsKonntenKeine = new JLabel("Es konnten keine MP3-Dateien im Ordner gefunden werden!");
		lblEsKonntenKeine.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblEsKonntenKeine.setHorizontalAlignment(SwingConstants.CENTER);
		lblEsKonntenKeine.setBounds(12, 0, 408, 50);
		contentPanel.add(lblEsKonntenKeine);
		
		textField = new JTextField();
		textField.setBounds(22, 42, 398, 22);
		textField.setText(ordnerPfad);
		textField.setEditable(false);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnOk.setBounds(163, 77, 97, 25);
		contentPanel.add(btnOk);
		setLocationRelativeTo(getParent());
		setVisible(true);
	}
}
