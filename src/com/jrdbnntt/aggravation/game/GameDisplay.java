/**
 * Contains game-related display objects
 */

package com.jrdbnntt.aggravation.game;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import com.jrdbnntt.aggravation.Aggravation;
import com.jrdbnntt.aggravation.board.Board;
import com.jrdbnntt.aggravation.toolbox.ToolBox;

public class GameDisplay extends JPanel {
	public static final double BOARD_WEIGHT_Y = .9;
	
	//Size constraints
	public static final Dimension
		MINIMUM_SIZE = new Dimension(
				500,
				(int)(500/GameDisplay.BOARD_WEIGHT_Y)
				),
		PREFERRED_SIZE = new Dimension(
				1000,
				(int)(1000/GameDisplay.BOARD_WEIGHT_Y)
				);
	
	
	private Board board = new Board();
	private ToolBox toolBox = new ToolBox();
	
	public GameDisplay() {
		super(new GridBagLayout());
		
		this.setMinimumSize(GameDisplay.MINIMUM_SIZE);
		this.setPreferredSize(GameDisplay.PREFERRED_SIZE);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = GameDisplay.BOARD_WEIGHT_Y;
		this.add(board, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1 - GameDisplay.BOARD_WEIGHT_Y;
		this.add(toolBox, gbc);
	}
	
	
	/**
	 * Refreshes display content
	 */
	public void refresh() {
		this.board.updateBoard();
		repaint();
	}
	
}
