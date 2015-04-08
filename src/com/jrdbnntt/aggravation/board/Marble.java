/**
 * A marble that moves around the board. Can be set to a color.
 */
package com.jrdbnntt.aggravation.board;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Marble {
	private static final int MARBLE_DIAMETER = 10;
	private int ownerPlayerNum;
	private Color marbleColor;
	
	public Marble(int ownerPlayerNum, Color marbleColor) {
		this.ownerPlayerNum = ownerPlayerNum;
		this.marbleColor = marbleColor;
	}
	
	public int getOwner() {
		return this.ownerPlayerNum;
	}
}
