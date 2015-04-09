/**
 * The toolbox containing information about the game and some actions players can preform.
 */

package com.jrdbnntt.aggravation.toolbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jrdbnntt.aggravation.game.Game;

@SuppressWarnings("serial")
public class ToolBox extends JPanel {
	private static final Color backgroundColor = Color.WHITE;
	
	private ArrayList<PlayerStatusView> pViews = new ArrayList<PlayerStatusView>();
	
	public ToolBox() {
		super(new GridBagLayout());
		
		
		this.setBackground(backgroundColor);
		setup();
		
	}
	
	/**
	 * Sets up layout and player views. Must be called after players have been defined.
	 */
	public void setup() {
		GridBagConstraints gbc = new GridBagConstraints();
		int curr = 0;
		
		//list out playerviews
		for(int i : Game.getCurrentInstance().getTurnOrder()) {
			PlayerStatusView psv = new PlayerStatusView(Game.getCurrentInstance().getPlayer(i));
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = curr++;
			gbc.gridy = 0;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			this.add(psv, gbc);
			this.pViews.add(psv);
		}
	}
	
	
	/**
	 * Updates all content with new data
	 */
	public void update() {
		
		
	}
}
