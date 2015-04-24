/**
 * Contains game-related display objects
 */

package com.jrdbnntt.aggravation.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.jrdbnntt.aggravation.Aggravation;
import com.jrdbnntt.aggravation.GameStyle;
import com.jrdbnntt.aggravation.Util.Log;
import com.jrdbnntt.aggravation.board.Board;
import com.jrdbnntt.aggravation.toolbox.ToolBox;

public class GameDisplay extends JPanel {
	public static final double BOARD_WEIGHT_Y = .9;
	
	//Size constraints
	public static final Dimension
		MINIMUM_SIZE = new Dimension(
				700,
				(int)(700/GameDisplay.BOARD_WEIGHT_Y)
				),
		PREFERRED_SIZE = new Dimension(
				800,
				(int)(800/GameDisplay.BOARD_WEIGHT_Y)
				);
	
	
	private Board board = new Board();
	private ToolBox toolBox = new ToolBox();
	
	public GameDisplay() {
		super(new BorderLayout());
		
		this.setMinimumSize(GameDisplay.MINIMUM_SIZE);
		this.setPreferredSize(GameDisplay.PREFERRED_SIZE);
		this.setBackground(GameStyle.COLOR_BACKGROUND);
		
		this.add(board, BorderLayout.CENTER);
		this.add(toolBox, BorderLayout.SOUTH);
		init();
	}
	
	/**
	 * Refreshes display content
	 */
	public void refresh() {
		this.board.updateGeometry();
		this.toolBox.updateContent();
		repaint();
	}
	
	/**
	 * Initialize game display with starting positions/values
	 */
	public void init() {
		Log.v("GAME DISPLAY","Initializing...");
		this.board.init();
		this.toolBox.init();
		Log.v("GAME DISPLAY","Initialization complete.");
	}
	
	public Board getBoard() { return this.board; }
	public ToolBox getToolBox() { return this.toolBox; }
}
