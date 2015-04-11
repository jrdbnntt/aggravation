/**
 * The toolbox containing information about the game and some actions players can preform.
 */

package com.jrdbnntt.aggravation.toolbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import com.jrdbnntt.aggravation.GameStyle;
import com.jrdbnntt.aggravation.game.Game;

@SuppressWarnings("serial")
public class ToolBox extends JPanel {	
	private ArrayList<PlayerStatusView> pViews = new ArrayList<PlayerStatusView>();
	private JEditorPane log = new JEditorPane();
	private JButton bRoll = new JButton("Roll Die");
	
	public ToolBox() {
		super(new GridBagLayout());
		
		this.setBackground(GameStyle.COLOR_BACKGROUND);
	}
	
	/**
	 * Sets up layout and player views. Must be called after players have been defined.
	 */
	public void init() {
		GridBagConstraints gbc = new GridBagConstraints();
		JScrollPane sPane = new JScrollPane(log);
		int gx = 0;
		
		//setup log
		log.setEditable(false);
		log.setBackground(GameStyle.COLOR_BACKGROUND);
		log.setForeground(GameStyle.COLOR_FONT);
		log.setText("GAME START");
		
		//Display action pane
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = gx;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		sPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sPane.setBackground(GameStyle.COLOR_BACKGROUND);
		sPane.setBorder(BorderFactory.createTitledBorder(
				GameStyle.BORDER_BASIC,
				"Game Log",
				TitledBorder.DEFAULT_POSITION,
				TitledBorder.DEFAULT_JUSTIFICATION,
				GameStyle.FONT_TITLE,
				GameStyle.COLOR_FONT));
		
		this.add(sPane, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = gx++;
		gbc.gridy = 1;
		gbc.weightx = .2;
		gbc.weighty = .1;
		this.bRoll.setEnabled(false);
		this.bRoll.setOpaque(false);
		this.bRoll.setActionCommand(Game.AK_ROLL);
		this.add(bRoll, gbc);
		
		//list out playerviews
		ArrayList<Integer> turnOrder = Game.getCurrentInstance().getTurnOrder();
		for(int i = 0; i < turnOrder.size(); ++i) {
			PlayerStatusView psv = new PlayerStatusView(Game.getCurrentInstance().getPlayer(turnOrder.get(i)));
			psv.setOpaque(false);
			gbc.fill = GridBagConstraints.NONE;
			gbc.gridx = gx++;
			gbc.gridy = 0;
			gbc.weightx = .3;
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
	
	/**
	 * Sets the text of the message
	 * @param m
	 */
	public void addLogMessage(String m) {
		this.addLogMessage(m, true);
	}
	public void addLogMessage(String m, boolean includeDate) {
		SimpleDateFormat t = new SimpleDateFormat("hh:mm:ss");
		String str = log.getText() + "\n";
		if(includeDate) {
			str += "[" + t.format(new Date()) + "] ";
		}
		this.log.setText(str + m);
	}
	
	public JButton getRollButton() {
		return this.bRoll;
	}
}
