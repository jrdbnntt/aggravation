package com.jrdbnntt.aggravation.board.space;

import java.awt.Color;
import java.awt.geom.Path2D;

import com.jrdbnntt.aggravation.Util.ColorManager;
import com.jrdbnntt.aggravation.game.Player;

abstract public class PlayerSpace extends Space {
	protected Player owner;
	protected int zone;
	protected Color 
		borderColor = Space.BORDER_COLOR_DEFAULT,
		fillColor = Space.FILL_COLOR_DEFAULT;
	
	protected Path2D.Double box;
	
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
	
	@Override
	public void setLocation(double x, double y) {
		super.setLocation(x, y);
		
		//Prepare box (hexagon)
		box = new Path2D.Double();
		double r = this.getShape().getWidth()*.889;
		box.moveTo(x+r, y);
		for(int i = 0; i <= 6; ++i) {
			box.lineTo(x + r*Math.cos(i*2*Math.PI/6), y - r*Math.sin(i*2*Math.PI/6));
		}
	}
	
	public Path2D.Double getBox() { return this.box; }

}
