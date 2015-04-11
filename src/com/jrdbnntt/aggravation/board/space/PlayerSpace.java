package com.jrdbnntt.aggravation.board.space;

import java.awt.Color;

import com.jrdbnntt.aggravation.Util.ColorManager;
import com.jrdbnntt.aggravation.game.Player;

abstract public class PlayerSpace extends Space {
	private Player owner;
	protected int zone;
	protected Color 
		borderColor = Space.BORDER_COLOR_DEFAULT,
		fillColor = Space.FILL_COLOR_DEFAULT;
	
	public PlayerSpace(int zone, int id, Player p) {
		super(id);
		
		Color base = p.getColor();
		
		this.zone = zone;
		this.owner = p;
		this.borderColor = base;
//		this.fillColor = ColorManager.lighten(base);
	}
	
	public Player getOwner() { return this.owner; }
	public int getZone() { return this.zone; }
	
	@Override
	public String getLabel() {
		return "" + this.getType().name().charAt(0) + this.zone + ":" + this.getId();
	}

}
