import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class PlayListOverrideAnswer extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private int answer = 0;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel1;

	/**
	 * Create the dialog.
	 */
	public PlayListOverrideAnswer(String playListName,boolean itunesPlayList) {
		setBounds(100, 100, 414, 207);
		getContentPane().setLayout(null);
		setModal(true);
		contentPanel.setBounds(0, 0, 396, 149);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			if(itunesPlayList){
				lblNewLabel = new JLabel("Playlist exisitiert bereits!");
				lblNewLabel.setHorizontalAlignment(JLabel.CENTER);
				lblNewLabel.setVerticalAlignment(JLabel.CENTER);
				lblNewLabel1 = new JLabel("Bitte Option wählen");
				lblNewLabel1.setFont(new Font("Tahoma", Font.PLAIN, 15));
				lblNewLabel1.setBounds(102, 40, 176, 35);
				lblNewLabel1.setHorizontalAlignment(JLabel.CENTER);
				lblNewLabel1.setVerticalAlignment(JLabel.CENTER);
				contentPanel.add(lblNewLabel1);
				lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
				lblNewLabel.setBounds(31, 0, 305, 50);
				contentPanel.add(lblNewLabel);
				
				JButton btnAbbruch = new JButton("Abbruch");
				btnAbbruch.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				
				btnAbbruch.setBounds(31, 119, 97, 25);
				contentPanel.add(btnAbbruch);
				
				JButton btnErgaenzen = new JButton("Ergänzen");
				btnErgaenzen.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						answer = 2;
						dispose();
					}
				});
				
				btnErgaenzen.setBounds(140, 119, 97, 25);
				contentPanel.add(btnErgaenzen);
				
				JButton btnUeberschreiben = new JButton("Überschreiben");
				btnUeberschreiben.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						answer = 1;
						dispose();
					}
				});
				
				btnUeberschreiben.setBounds(249, 119, 122, 25);
				contentPanel.add(btnUeberschreiben);
			}else{
				if(itunesPlayList){
					lblNewLabel = new JLabel("Playlist exisitiert bereits!");
					lblNewLabel.setHorizontalAlignment(JLabel.CENTER);
					lblNewLabel.setVerticalAlignment(JLabel.CENTER);
				}else{
					lblNewLabel = new JLabel("Es handelt sich nicht um eine ITunes Playlist!");
					lblNewLabel1 = new JLabel("Trotzdem überschreiben?");
					lblNewLabel1.setFont(new Font("Tahoma", Font.PLAIN, 15));
					lblNewLabel1.setBounds(112, 40, 176, 35);
					lblNewLabel1.setHorizontalAlignment(JLabel.CENTER);
					lblNewLabel1.setVerticalAlignment(JLabel.CENTER);
					contentPanel.add(lblNewLabel1);
					lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
					lblNewLabel.setBounds(43, 0, 305, 50);
					contentPanel.add(lblNewLabel);
					
					JButton btnOk = new JButton("Ja");
					btnOk.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							answer = 1;
							dispose();
						}
					});
					btnOk.setBounds(206, 119, 97, 25);
					contentPanel.add(btnOk);
					JButton btnAbbruch = new JButton("Nein");
					btnAbbruch.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});
					btnAbbruch.setBounds(97, 119, 97, 25);
					contentPanel.add(btnAbbruch);
				}
			}
			
			
			
			
			
			
			
			
			/*
			
			 	*/
			
			
		}
		{
			textField = new JTextField();
			textField.setBackground(SystemColor.control);
			textField.setBounds(43, 78, 320, 28);
			contentPanel.add(textField);
			textField.setColumns(10);
			textField.setEditable(false);
			textField.setText(playListName);
			
		}
		{
			
		}
		{
			
		}
		
		setLocationRelativeTo(getParent());
		setVisible(true);
	}
	
	public int getAnswer(){
		return answer;
	}

}
