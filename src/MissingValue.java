import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class MissingValue extends JDialog {
	private JLabel label;
	private JPanel panel;
	private JLabel lblNewLabel;
	private JButton btnOk;

	/**
	 * Create the dialog.
	 */
	public MissingValue(int gewaehlt) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 316, 121);
		getContentPane().setLayout(null);
		
		if(gewaehlt == 0){
			lblNewLabel = new JLabel("Unvollst\u00E4ndiger Eintrag!");
		}else if(gewaehlt == 1){
			lblNewLabel = new JLabel("Ungültiger numerischer Eintrag!");
		}else{	
			lblNewLabel = new JLabel("Bitte Eintrag w\u00E4hlen!");
		}
		
		lblNewLabel.setBounds(0, 0, 298, 51);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		getContentPane().add(lblNewLabel);
		
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
