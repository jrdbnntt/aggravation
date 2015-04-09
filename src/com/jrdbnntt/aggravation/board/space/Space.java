/**
 * A space to go on the board.
 * 
 * Similar to a component, but not exactly the same, since it is painted off another component.
 */
package com.jrdbnntt.aggravation.board.space;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;

import com.jrdbnntt.aggravation.board.Marble;

abstract public class Space {
	public static enum Type {
		HOME, BASE, CENTER, LOOP, CORNER, ENTRANCE
	}
	public static enum BorderStatus {
		BOLD, NORMAL, NONE
	}
	
	public static final Stroke
		BORDER_BOLD = new BasicStroke(6),
		BORDER_NORMAL = new BasicStroke(3);
	
	public static final Color 
		BORDER_COLOR_DEFAULT = Color.BLACK,
		FILL_COLOR_DEFAULT = new Color(117,117,117,100);
	
	
	private static double diameter = 50;
	public static Color COLOR = new Color(245, 245, 220);
	
	private Ellipse2D.Double circle = null;
	private Marble marble = null;
	protected BorderStatus borderStatus = Space.BorderStatus.NORMAL;
	
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
	
	abstract public Type getType();
	abstract public void paint(Graphics2D g2d);
	
	
	//statics
	public static double getDiameter() { return Space.diameter; }
	public static void setDiameter(double d) { Space.diameter = d; }
	
	
	//accessors
	public Ellipse2D.Double getShape() { return this.circle; }	
	public Marble getMarble() { return this.marble; }
	
	
	public void setMarble(Marble m) {
		this.marble = m;
	}
	public void clearMarble() {
		this.marble = null;
	}
	
	public boolean hasMarble() {
		return this.marble != null;
	}
}
