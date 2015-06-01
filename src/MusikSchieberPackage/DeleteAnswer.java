package MusikSchieberPackage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class DeleteAnswer extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private static int abbruch = 0;
	private JLabel lblNewLabel;

	/**
	 * Create the dialog.
	 */
	public DeleteAnswer(String filename) {
		setBounds(100, 100, 414, 179);
		getContentPane().setLayout(null);
		setModal(true);
		contentPanel.setBounds(0, 0, 432, 220);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			
			if(filename == ""){
				lblNewLabel = new JLabel("\t               Keine Datei gewählt!");
			}else{
				lblNewLabel = new JLabel("Soll der Eintrag wirklich gel\u00F6scht werden ?");
			}
			
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblNewLabel.setBounds(66, 0, 272, 50);
			contentPanel.add(lblNewLabel);
		}
		{
			textField = new JTextField();
			textField.setBackground(SystemColor.control);
			textField.setBounds(33, 57, 320, 28);
			contentPanel.add(textField);
			textField.setColumns(10);
			textField.setEditable(false);
			textField.setText(filename);
		}
		{
			JButton btnOk = new JButton("OK");
			btnOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					abbruch = 1;
				}
			});
			btnOk.setBounds(206, 98, 97, 25);
			contentPanel.add(btnOk);
		}
		{
			JButton btnAbbruch = new JButton("Abbruch");
			btnAbbruch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
					abbruch = 2;
				}
			});
			btnAbbruch.setBounds(97, 98, 97, 25);
			contentPanel.add(btnAbbruch);
		}
		
		setLocationRelativeTo(getParent());
		setVisible(true);
	}
	
	public static int getAbbruch(){
		return abbruch;
	}
	
	

}
