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
	private static final Color BACKGROUND_COLOR = Color.BLACK;
	
	private ArrayList<PlayerStatusView> pViews = new ArrayList<PlayerStatusView>();
	private JLabel message = new JLabel("");
	private JButton bRoll = new JButton("Roll Die");
	
	public ToolBox() {
		super(new GridBagLayout());
		
		this.setBackground(BACKGROUND_COLOR);
	}
	
	/**
	 * Sets up layout and player views. Must be called after players have been defined.
	 */
	public void init() {
		GridBagConstraints gbc = new GridBagConstraints();
		int gx = 0;
		
		//Display action pane
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = gx;
		gbc.gridy = 0;
		gbc.weightx = .1;
		gbc.weighty = .2;
		this.add(message, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = gx++;
		gbc.gridy = 1;
		gbc.weightx = .1;
		gbc.weighty = .8;
		this.add(bRoll, gbc);
		
		//list out playerviews
		ArrayList<Integer> turnOrder = Game.getCurrentInstance().getTurnOrder();
		for(int i = 0; i < turnOrder.size(); ++i) {
			PlayerStatusView psv = new PlayerStatusView(Game.getCurrentInstance().getPlayer(turnOrder.get(i)));
			psv.setOpaque(false);
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridx = gx++;
			gbc.gridy = 0;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			gbc.gridheight = 2;
			this.add(psv, gbc);
			this.pViews.add(psv);
		}
	}
	
	
	/**
	 * Updates all content with new data
	 */
	public void update() {
		for(PlayerStatusView psv: this.pViews)
			psv.update();
		
	}
}
