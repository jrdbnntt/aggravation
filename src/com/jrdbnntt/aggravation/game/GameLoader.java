/**
 * Loads the game setup (was lagging without)
 */
package com.jrdbnntt.aggravation.game;

import java.awt.BorderLayout;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jrdbnntt.aggravation.Aggravation;
import com.jrdbnntt.aggravation.GameStyle;

public class GameLoader extends JPanel {
	
	Thread load;
	JLabel status;
	Aggravation parent;
	
	public GameLoader(Aggravation p) {
		super(new BorderLayout());
		
		parent = p;
		
		setOpaque(false);
		
		status = new JLabel("Loading...");
		status.setForeground(GameStyle.COLOR_FONT);
		status.setFont(GameStyle.FONT_TITLE);
		status.setHorizontalAlignment(JLabel.CENTER);
		status.setOpaque(false);
		this.add(status, BorderLayout.CENTER);
		
		load = new Thread() {
			@Override
			public void run() {
				Game.load();
				PlayerChooser pc = new PlayerChooser(parent);
				parent.startPlayerChooser(pc);
				status.setText("Load Complete! Launching game...");
				super.run();
			}
		};
		
	}
	
	public void load() {
		load.start();
	}
	
	
}
