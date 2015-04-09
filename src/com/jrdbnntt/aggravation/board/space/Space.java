/**
 * A space to go on the board.
 */
package com.jrdbnntt.aggravation.board.space;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

import com.jrdbnntt.aggravation.board.Marble;

public abstract class Space {
	public static enum Type {
		HOME, BASE, CENTER, LOOP, CORNER, ENTRANCE
	}
	
	private static double diameter = 50;
	public static Color COLOR = new Color(245, 245, 220);
	
	private Ellipse2D circle = null;
	private Marble marble = null;
	
	public Space() {
		this.setLocation(0,0);
	}
	
	public void setLocation(double x, double y) {
		this.circle = new Ellipse2D.Double(
				x-Space.diameter/2,
				y-Space.diameter/2,
				Space.diameter, 
				Space.diameter);
	}
	
	public abstract Type getType();
	
	//statics
	public static double getDiameter() { return Space.diameter; }
	public static void setDiameter(double d) { Space.diameter = d; }
	
	
	//accessors
	public Ellipse2D getShape() { return this.circle; }	
	public Marble getMarble() { return this.marble; }
//	public Type getType() { return this.type; }
	
	
	public void setMarble(Marble m) {
		this.marble = m;
	}
	public boolean hasMarble() {
		return this.marble != null;
	}
}
