package MusikSchieberPackage;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;


public class StartMissingValues extends JDialog {
	private JPanel panel;
	private JLabel label_AnzeigeText;
	private JTextArea textArea;
	private JButton btnJa;
	private JButton btnNein;
	private boolean answer = false;
	private JScrollPane scrollPane;
	
	/**
	 * Create the dialog.
	 */
	public StartMissingValues() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		setModal(true);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 432, 255);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		label_AnzeigeText = new JLabel("<html><p>Es liegen immer noch unvollst\u00E4ndige Eintr\u00E4ge vor! </p><p \"style=text-align:center\">Trotzdem fortfahren?</p></html>");
		label_AnzeigeText.setFont(new Font("Tahoma", Font.PLAIN, 15));
		label_AnzeigeText.setHorizontalAlignment(SwingConstants.CENTER);
		label_AnzeigeText.setBounds(0, 5, 432, 38);
		panel.add(label_AnzeigeText);
		
		btnJa = new JButton("Ja");
		btnJa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				answer = true;
				dispose();
			}
		});
		btnJa.setBounds(227, 222, 97, 25);
		panel.add(btnJa);
		
		btnNein = new JButton("Nein");
		btnNein.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNein.setBounds(112, 222, 97, 25);
		panel.add(btnNein);
		
		textArea = new JTextArea();
		textArea.setBounds(12, 55, 408, 154);
		textArea.setEditable(false);
		for(int i = 0; i< MusikSchieber.geleseneMP3.size();i++){
			if(MusikSchieber.geleseneMP3.get(i).missingNonCriticalValues()){
				textArea.append(MusikSchieber.geleseneMP3.get(i).getFileName() + "\n");
			}
		}
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(12, 56, 408, 154);
		panel.add(scrollPane);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		setVisible(true);
	}
	
	public boolean getAnswer(){
		return this.answer;
	}
}
