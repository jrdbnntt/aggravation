/**
 * A panel showing information about a specific player.
 */
package com.jrdbnntt.aggravation.toolbox;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jrdbnntt.aggravation.game.GameDisplay;
import com.jrdbnntt.aggravation.game.Player;

public class PlayerStatusView extends JPanel {
	
	private Player p;			//reference to player
	private JLabel name, baseMarbles, freeMarbles, homeMarbles, status;
	
	public PlayerStatusView(Player p) {
		super(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		this.p = p;
		
		//create components
		name = new JLabel(p.getName());
		name.setForeground(p.getColor());
		name.setBackground(Color.BLACK);
		name.setOpaque(true);
		baseMarbles = new JLabel("# at Base");
		homeMarbles = new JLabel("# at Home");
		freeMarbles = new JLabel("# Exposed");
		status = new JLabel("[Status]");
		
		//add components
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = .2;
		this.add(this.name, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = .2;
		this.add(this.baseMarbles, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1.0;
		gbc.weighty = .2;
		this.add(this.homeMarbles, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 1.0;
		gbc.weighty = .2;
		this.add(this.freeMarbles, gbc);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 1.0;
		gbc.weighty = .2;
		this.add(this.status, gbc);
	}
	
	/**
	 * Retrieves player data and updates display
	 */
	public void update() {
		System.out.println("PSV: \'"+p.getName()+"\' Update");
		baseMarbles.setText(p.getBaseMarbles()+" at Base");
		homeMarbles.setText(p.getHomeMarbles()+" at Home");
		freeMarbles.setText(p.getFreeMarbles()+" Exposed");
		status.setText(p.getStatus());
	}
}
