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
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.tools.Tool;

import com.jrdbnntt.aggravation.GameStyle;
import com.jrdbnntt.aggravation.game.Game;

@SuppressWarnings("serial")
public class ToolBox extends JPanel {	
	private ArrayList<PlayerStatusView> pViews = new ArrayList<PlayerStatusView>();
	

	ArrayList<String> logData = new ArrayList<String>();
	private JPanel logBox;
	private JScrollPane logScroll;
	private JButton bRoll = new JButton("Roll Die");
	
	
	private static Dimension DIM_MAX = new Dimension (10000,100);
	private static Dimension DIM_LOG_MAX = new Dimension (100,100);
	
	public ToolBox() {
		super(new GridBagLayout());
		this.setBackground(GameStyle.COLOR_BACKGROUND);
		this.setMaximumSize(DIM_MAX);
	}
	
	/**
	 * Sets up layout and player views. Must be called after players have been defined.
	 */
	public void init() {
		
		//Create Log
		logBox = new JPanel(new BorderLayout());
		logScroll = new JScrollPane();
		logScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		logScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		logScroll.setBackground(GameStyle.COLOR_BACKGROUND);
		logScroll.setBorder(BorderFactory.createTitledBorder(
				GameStyle.BORDER_BASIC,
				"Game Log",
				TitledBorder.DEFAULT_POSITION,
				TitledBorder.DEFAULT_JUSTIFICATION,
				GameStyle.FONT_TITLE,
				GameStyle.COLOR_FONT));
		logScroll.setMaximumSize(ToolBox.DIM_LOG_MAX);
		
		addLogMessage("GAME START", false);
		
		logBox.add(logScroll, BorderLayout.CENTER);
		
		bRoll.setEnabled(false);
		bRoll.setOpaque(false);
		bRoll.setActionCommand(Game.AK_ROLL);
		logBox.add(bRoll, BorderLayout.SOUTH);
		
		
		
		
		// Add everything to this
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(logBox, gbc);

		ArrayList<Integer> turnOrder = Game.getCurrentInstance().getTurnOrder();
		for(int i = 0; i < turnOrder.size(); ++i) {
			PlayerStatusView psv = new PlayerStatusView(Game.getCurrentInstance().getPlayer(turnOrder.get(i)));
			psv.setOpaque(false);
			
			this.pViews.add(psv);
			
			gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.NONE;
			gbc.weightx = .2;
			gbc.weighty = 1.0;
			gbc.gridx = i+1;
			gbc.gridy = 0;
			this.add(psv, gbc);
		}
		
	}
		
	
	/**
	 * Updates all content with new data
	 */
	public void updateContent() {
		for(PlayerStatusView psv: this.pViews)
			psv.updateContent();
	}
	
	/**
	 * Sets the text of the message
	 * @param m
	 */
	public void addLogMessage(String m) {
		this.addLogMessage(m, true);
	}
	public void addLogMessage(String m, boolean includeDate) {
		String newLog = m;
		SimpleDateFormat t = new SimpleDateFormat("hh:mm:ss");
		if(includeDate) {
			newLog = "[" + t.format(new Date()) + "] " + newLog;
		}
		
		logData.add(newLog);
		JList list = new JList(logData.toArray());
		logScroll.setViewportView(list);
		logScroll.getVerticalScrollBar().setValue(logScroll.getVerticalScrollBar().getMaximum());
	}
	
	public JButton getRollButton() {
		return this.bRoll;
	}
	
	
	
}
