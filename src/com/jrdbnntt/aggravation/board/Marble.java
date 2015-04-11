/**
 * A marble that moves around the board. Can be set to a color.
 */
package com.jrdbnntt.aggravation.board;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.jrdbnntt.aggravation.game.Player;

@SuppressWarnings("serial")
public class Marble {
	private static final int MARBLE_DIAMETER = 10;
	private Player owner;
	private Color marbleColor;
	
	public Marble(Player owner, Color marbleColor) {
		this.owner = owner;
		this.marbleColor = marbleColor;
	}
	
	public Player getOwner() { return this.owner; }
	public Color getColor() { return this.marbleColor; }
}
