/**
 * The toolbox containing information about the game and some actions players can preform.
 */

package com.jrdbnntt.aggravation.toolbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ToolBox extends JPanel {
	private static final Color backgroundColor = Color.WHITE;
	
	public ToolBox() {
		super();
//		super(new BorderLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		
		this.setBackground(backgroundColor);
		this.add(new JButton("ToolBox East"), BorderLayout.EAST);
		this.add(new JButton("ToolBox West"), BorderLayout.WEST);
		
		
		//layout
//		gbc.gridheight = 5;
//		gbc.gridwidth = 5;
//		
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gbc.gridx = 0;
//		gbc.gridy = 0;
//		gbc.anchor = GridBagConstraints.NORTHEAST;
//		this.add(new JButton("ToolBox"), gbc);
//		
//		gbc.fill = GridBagConstraints.HORIZONTAL;
//		gbc.gridx = 0;
//		gbc.gridy = 1;
//		gbc.anchor = GridBagConstraints.NORTHEAST;
//		this.add(new JButton("ToolBox"), gbc);
		
	}
}
