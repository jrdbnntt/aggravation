package com.jrdbnntt.aggravation.board.space;

import java.awt.Color;

import com.jrdbnntt.aggravation.Util.ColorManager;
import com.jrdbnntt.aggravation.game.Player;

abstract public class PlayerSpace extends Space {
	private Player owner;
	protected Color 
		borderColor = Space.BORDER_COLOR_DEFAULT,
		fillColor = Space.FILL_COLOR_DEFAULT;
	
	public PlayerSpace(Player p) {
		Color base = p.getColor();
		
		this.owner = p;
		this.borderColor = base;
//		this.fillColor = ColorManager.lighten(base);
	}
	
	public Player getOwner() { return this.owner; }

}
