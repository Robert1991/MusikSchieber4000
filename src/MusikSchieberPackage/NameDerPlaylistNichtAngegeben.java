package MusikSchieberPackage;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class NameDerPlaylistNichtAngegeben extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel label;
	private JButton btnOk;
	/**
	 * Create the dialog.
	 */
	public NameDerPlaylistNichtAngegeben() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 316, 121);
		getContentPane().setLayout(null);
		
		
		label = new JLabel("Bitte Namen der Playlist angeben!");
		label.setBounds(0, 0, 298, 51);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 15));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		
		getContentPane().add(label);
		
		btnOk = new JButton("OK");
		btnOk.setBounds(0, 51, 298, 25);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		getContentPane().add(btnOk);
		setLocationRelativeTo(getParent());
		setVisible(true);
	}
}
