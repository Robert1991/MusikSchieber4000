package MusikSchieberPackage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;



public class ListRenderer extends JLabel implements ListCellRenderer {
	  protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

	  public Component getListCellRendererComponent(JList list, Object value, int index,
	      boolean isSelected, boolean cellHasFocus) {
	    Color theForeground = null;
	    String theText = null;

	    JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
	        isSelected, cellHasFocus);

	    if (value instanceof Object[]) {
	      Object values[] = (Object[]) value;
	      theText = (String) values[0];
	      theForeground = (Color) values[1];
	    } 
	    else {
	      theForeground = list.getForeground();
	      theText = "";
	    }
	    if (!isSelected) {
	      renderer.setForeground(theForeground);
	    }
	    
	    renderer.setText(theText);
	    return renderer;
	  }
}
