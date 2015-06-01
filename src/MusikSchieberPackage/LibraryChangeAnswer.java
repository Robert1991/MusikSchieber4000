package MusikSchieberPackage;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class LibraryChangeAnswer extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private boolean change = false;
	private JLabel lblNewLabel;
	private JButton btnNein;
	private JButton button;

	/**
	 * Create the dialog.
	 */
	public LibraryChangeAnswer(String path) {
		setBounds(100, 100, 414, 162);
		getContentPane().setLayout(null);
		setModal(true);
		contentPanel.setBounds(0, 0, 396, 134);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		lblNewLabel = new JLabel("Soll ITunes-Bibliothek wirklich geändert werden?");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(0, 10, 396, 19);
		contentPanel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBackground(SystemColor.control);
		textField.setBounds(48, 42, 303, 22);
		contentPanel.add(textField);
		textField.setColumns(10);
		textField.setEditable(false);
		textField.setText(path);
		
		btnNein = new JButton("Nein");
		btnNein.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				change = false;
				dispose();
			}
		});
		btnNein.setBounds(94, 77, 97, 25);
		contentPanel.add(btnNein);
		
		button = new JButton("Ja");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				change = true;
				dispose();
			}
		});
		button.setBounds(203, 77, 97, 25);
		contentPanel.add(button);
		
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(getParent());
		this.setVisible(true);
	}
	
	public boolean getChangeAnswer(){
		return this.change;
	}
}
