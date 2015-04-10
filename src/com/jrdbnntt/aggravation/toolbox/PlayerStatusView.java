/**
 * A panel showing information about a specific player.
 */
package com.jrdbnntt.aggravation.toolbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import com.jrdbnntt.aggravation.Util.ColorManager;
import com.jrdbnntt.aggravation.Util.Log;
import com.jrdbnntt.aggravation.game.GameDisplay;
import com.jrdbnntt.aggravation.game.Player;

public class PlayerStatusView extends JPanel {
	private static final Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
	private static final String FORMAT =
			"<html><center><font color=%s>"
			+ "<b><u>%s</u></b><br>"
			+ "<br>"
			+ "%d at Base<br>"
			+ "%d at Home<br>"
			+ "%d Exposed<br>"
			+ "<br>"
			+ "%s"
			+ "</font></center></html>";
	
	private Player p;			//reference to player
	private JLabel name, baseMarbles, freeMarbles, homeMarbles, status;
	
	public PlayerStatusView(Player p) {
		super(new BorderLayout());
//		GridBagConstraints gbc = new GridBagConstraints();
		
		this.p = p;
		//create components
		name = new JLabel();
		name.setBorder(PlayerStatusView.padding);
		this.add(name, BorderLayout.CENTER);
		update();
		

	}
	
	/**
	 * Retrieves player data and updates display
	 */
	public void update() {
		Log.d("PSV", "\'"+p.getName()+"\' Update");
		
		name.setText(String.format(PlayerStatusView.FORMAT, 
				ColorManager.getHexString(this.p.getColor()),
				this.p.getName(),
				this.p.getBaseMarbles(),
				this.p.getHomeMarbles(),
				this.p.getFreeMarbles(),
				this.p.getStatusString()
				));
		
		if(this.p.getStatus() == Player.Status.CURRENT_PLAYER) {
			this.setBorder(BorderFactory.createLineBorder(p.getColor(), 3, true));
		} else {
			this.setBorder(BorderFactory.createEmptyBorder());
		}
			
		
	}
}
